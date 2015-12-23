package com.smartdatainc.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.oovoo.core.Utils.LogSdk;
import com.smartdatainc.app.ApplicationSettings;
import com.smartdatainc.app.ooVooSdkSampleShowApp;
import com.smartdatainc.app.ooVooSdkSampleShowApp.CallNegotiationListener;
import com.smartdatainc.app.ooVooSdkSampleShowApp.Operation;
import com.smartdatainc.app.ooVooSdkSampleShowApp.OperationChangeListener;
import com.smartdatainc.call.CNMessage;
import com.smartdatainc.toGo.R;
import com.smartdatainc.ui.fragments.AVChatLoginFragment;
import com.smartdatainc.ui.fragments.AVChatSessionFragment;
import com.smartdatainc.ui.fragments.BaseFragment;
import com.smartdatainc.ui.fragments.CallNegotiationFragment;
import com.smartdatainc.ui.fragments.InformationFragment;
import com.smartdatainc.ui.fragments.SettingsFragment;
import com.smartdatainc.ui.fragments.WaitingFragment;



//Call manager Activity

public class ToGoActivity extends ActionBarActivity implements OperationChangeListener,CallNegotiationListener {

    private ooVooSdkSampleShowApp application	   = null;
    private AlertDialog callDialogBuilder = null;
    private static final String STATE_FRAGMENT 	= "current_fragment";
    private static final int 		PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BaseFragment current_fragment	= null;
    private BroadcastReceiver mRegistrationBroadcastReceiver = null;
    private MenuItem mSettingsMenuItem = null;
    private MenuItem mInformationMenuItem = null;
    private MenuItem mSignalStrengthMenuItem = null;
    private MenuItem mSecureNetworkMenuItem = null;
    private boolean					mIsAlive = false;
    private boolean					mNeedShowFragment = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_go);
        application = (ooVooSdkSampleShowApp) getApplication();
        application.setContext(this);
        application.addOperationChangeListener(this);
        application.addCallNegotiationListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_go, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();


        try {
            if(mRegistrationBroadcastReceiver == null)
            {
                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        boolean sentToken = sharedPreferences.getBoolean(ApplicationSettings.SENT_TOKEN_TO_SERVER, false);
                        if (!sentToken) {
                            application.showErrorMessageBox(ToGoActivity.this, getString(R.string.registering_message), getString(R.string.token_error_message));
                        }
                    }
                };
                LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(ApplicationSettings.REGISTRATION_COMPLETE));
            }
        }
        catch(Exception err){
            Log.e("ToGoActivity", "onResume exception: with ", err);
        }

        mIsAlive = true;

    }
    @Override
    public boolean onOptionsItemSelected( MenuItem item)
    {
        if( item == null)
            return false;

        switch( item.getItemId())
        {
            case R.id.menu_settings:
                SettingsFragment settings  = new SettingsFragment();
                settings.setBackFragment(current_fragment);

                mSettingsMenuItem.setVisible(false);

                addFragment(settings);

                current_fragment = settings;
                return true;

            case R.id.menu_information:
                InformationFragment information  = new InformationFragment();
                information.setBackFragment(current_fragment);

                mSignalStrengthMenuItem.setVisible(false);
                mInformationMenuItem.setVisible(false);

                addFragment(information);

                current_fragment = information;
                return true;
        }

        return super.onOptionsItemSelected( item);
    }

    @Override
    public void onOperationChange(Operation state) {
        try {
            switch (state) {
                case Error:
                {
                    switch (state.forOperation()) {
                        case Authorized:
                           // current_fragment = ReautorizeFragment.newInstance(mSettingsMenuItem, state.getDescription());
                            break;
                        case LoggedIn:
                          //  current_fragment = LoginFragment.newInstance(state.getDescription());
                            break;
                        case AVChatJoined:
                           application.showErrorMessageBox(this, getString(R.string.join_session), state.getDescription());
                          current_fragment = AVChatLoginFragment.newInstance(mSettingsMenuItem);
                            break;
                        default:
                            return;
                    }
                }
                break;
                case Processing:
                   current_fragment = WaitingFragment.newInstance(state.getDescription());
                    break;
                case AVChatRoom:
                    current_fragment = AVChatLoginFragment.newInstance(mSettingsMenuItem);
                    break;
                case AVChatCall:
                    current_fragment = CallNegotiationFragment.newInstance(mSettingsMenuItem);
                    break;
                case PushNotification:
                 //   current_fragment = PushNotificationFragment.newInstance(mSettingsMenuItem);
                    break;
                case AVChatJoined:
                    current_fragment = new AVChatSessionFragment();
                    break;
                case Authorized:
                   // current_fragment = LoginFragment.newInstance(mSettingsMenuItem);
                    break;
                case LoggedIn:
                    if (checkPlayServices()) {
                        // Start IntentService to register this application with GCM.
                      /*  Intent intent = new Intent(this, RegistrationIntentService.class);
                        startService(intent);*/
                    }
                   // current_fragment = OptionFragment.newInstance(mSettingsMenuItem);
                    break;
                case AVChatDisconnected:
                    if (application.isCallNegotiation()) {
                        return;
                    } else {
                        current_fragment = AVChatLoginFragment.newInstance(mSettingsMenuItem);
                        break;
                    }

                default:
                    return;
            }

            showFragment(current_fragment);
            System.gc();
            Runtime.getRuntime().gc();

        } catch (Exception err) {
            err.printStackTrace();
        }
    }
    private void showFragment(Fragment newFragment) {
        if(!mIsAlive){
            mNeedShowFragment = true;
            return;
        }

        try {
            if (newFragment != null) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.host_activity, newFragment);
                transaction.addToBackStack(newFragment.getClass().getSimpleName());
                transaction.commit();
            }
        }
        catch(Exception err){
            LogSdk.e("ToGo","showFragment " + err);
        }
    }@Override
    public void onMessageReceived(final CNMessage cnMessage)
    {
        if (application.getUniqueId().equals(cnMessage.getUniqueId())) {
            return;
        }

        if (cnMessage.getMessageType() == CNMessage.CNMessageType.Calling) {

            if (application.isInConference()) {
                application.sendCNMessage(cnMessage.getFrom(), CNMessage.CNMessageType.Busy, null);
                return;
            }

            callDialogBuilder = new AlertDialog.Builder(this).create();
            LayoutInflater inflater = getLayoutInflater();
            View incomingCallDialog = inflater.inflate(R.layout.incoming_call_dialog, null);
            incomingCallDialog.setAlpha(0.5f);
            callDialogBuilder.setView(incomingCallDialog);

            Button answerButton = (Button) incomingCallDialog.findViewById(R.id.answer_button);
            answerButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    application.setConferenceId(cnMessage.getConferenceId());
                    application.sendCNMessage(cnMessage.getFrom(), CNMessage.CNMessageType.AnswerAccept, null);
                    callDialogBuilder.hide();

                    application.join(application.getConferenceId(), true);
                }
            });

            Button declineButton = (Button) incomingCallDialog.findViewById(R.id.decline_button);
            declineButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    application.sendCNMessage(cnMessage.getFrom(), CNMessage.CNMessageType.AnswerDecline, null);
                    callDialogBuilder.hide();
                }
            });

            callDialogBuilder.setCancelable(false);
            callDialogBuilder.show();
        } else if (cnMessage.getMessageType() == CNMessage.CNMessageType.Cancel) {
            callDialogBuilder.hide();
        } else if (cnMessage.getMessageType() == CNMessage.CNMessageType.EndCall) {
            if (application.leave()) {
                int count = getFragmentManager().getBackStackEntryCount();
                String name = getFragmentManager().getBackStackEntryAt(count - 2).getName();
                getFragmentManager().popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }
    private void addFragment(Fragment newFragment) {

        try {
            if (newFragment != null) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.host_activity, newFragment);
                transaction.show(newFragment);
                transaction.hide(current_fragment);
                transaction.commit();
            }
        }
        catch(Exception err){
            LogSdk.e("ToGo", "addFragment " + err);
        }
    }

    private void removeFragment(Fragment fragment) {

        try {
            if (fragment != null) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(current_fragment);
                transaction.show(fragment);
                transaction.commit();
            }
        }
        catch(Exception err){
            LogSdk.e("ToGo","removeFragment " + err);
        }
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                LogSdk.i("ToGo", "This device is not supported.");
            }
            return false;
        }
        return true;
    }
}
