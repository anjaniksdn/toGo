package com.smartdatainc.activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.oovoo.core.Utils.LogSdk;
import com.oovoo.sdk.api.ooVooClient;
import com.smartdatainc.adapters.DrawerCustomAdapter;
import com.smartdatainc.app.ApplicationSettings;
import com.smartdatainc.app.ooVooSdkSampleShowApp;
import com.smartdatainc.app.ooVooSdkSampleShowApp.CallNegotiationListener;
import com.smartdatainc.app.ooVooSdkSampleShowApp.Operation;
import com.smartdatainc.app.ooVooSdkSampleShowApp.OperationChangeListener;
import com.smartdatainc.call.CNMessage;
import com.smartdatainc.dataobject.InterpreterCallStatus;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.LoginManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.ui.SignalBar;
import com.smartdatainc.ui.fragments.AVChatLoginFragment;
import com.smartdatainc.ui.fragments.AVChatSessionFragment;
import com.smartdatainc.ui.fragments.BaseFragment;
import com.smartdatainc.ui.fragments.ChatDetailsFragment;
import com.smartdatainc.ui.fragments.InformationFragment;
import com.smartdatainc.ui.fragments.InterpreterSettingFragment;
import com.smartdatainc.ui.fragments.InterprterDashBoardFragment;
import com.smartdatainc.ui.fragments.PushNotificationFragment;
import com.smartdatainc.ui.fragments.ReautorizeFragment;
import com.smartdatainc.ui.fragments.SettingsFragment;
import com.smartdatainc.ui.fragments.UpdateInterpreterProfile;
import com.smartdatainc.ui.fragments.WaitingFragment;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

import java.util.ArrayList;
import java.util.Iterator;


public class InterpreterDashboardActivity extends AppActivity implements ServiceRedirection,OperationChangeListener, CallNegotiationListener {


    private static final String TAG = InterpreterDashboardActivity.class.getSimpleName();
    private static final String STATE_FRAGMENT = "current_fragment";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BaseFragment current_fragment = null;
    private ooVooSdkSampleShowApp application = null;
    private MenuItem mSettingsMenuItem = null;
    private MenuItem mInformationMenuItem = null;
    private MenuItem mSignalStrengthMenuItem = null;
    private MenuItem mSecureNetworkMenuItem = null;
    private boolean mIsAlive = false;
    private boolean mNeedShowFragment = false;
    private AlertDialog callDialogBuilder = null;
    private BroadcastReceiver mRegistrationBroadcastReceiver = null;
    private DrawerCustomAdapter drawerCustomAdapter;
    private ListView mDrawerList = null;
    private DrawerLayout mDrawerLayout = null;
    private ArrayList<String> mPlanetTitles = null;
    private ActionBarDrawerToggle mDrawerToggle;
    private Utility utilObj;
    private String mCompletion;
    private CNMessage cnMessage;
    Context context;
    LoginManager loginManagerObj;
    public static ArrayList<String> toList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar(Constants.APPHEADER);
        ApplicationSettings settings = new ApplicationSettings(this);
        String loginname = settings.get(ApplicationSettings.Username);
        application = (ooVooSdkSampleShowApp) getApplication();
        application.setContext(this);
        utilObj = new Utility(this);
        //setRequestedOrientation(application.getDeviceDefaultOrientation());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.host_activity);
        context = this;
        application.addOperationChangeListener(this);
        application.addCallNegotiationListener(this);

        createDrawer();
         loginManagerObj = new LoginManager(this, this);

        if (savedInstanceState != null) {
            current_fragment = (BaseFragment) getFragmentManager().getFragment(savedInstanceState, STATE_FRAGMENT);
            showFragment(current_fragment);
        } else {
            /*Fragment newFragment = new CallNegotiationFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.host_activity, newFragment).commit();*/
            mCompletion = utilObj.readDataInSharedPreferences("Users", 0, "completion");
            if (mCompletion != null) {
                if (mCompletion.equalsIgnoreCase("true")) {
                    Fragment newFragment = new InterprterDashBoardFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.host_activity, newFragment).commit();
                } else {
                    Fragment fragment = new UpdateInterpreterProfile();
                    //Bundle args = new Bundle();
                    //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
                    //fragment.setArguments(args);

                    // Insert the fragment by replacing any existing fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.host_activity, fragment)
                            .commit();

                    // Highlight the selected item, update the title, and close the drawer
                    mDrawerList.setItemChecked(0, true);
                }
            }


            if (!ooVooClient.isDeviceSupported()) {
                return;
            }

            try {

                application.onMainActivityCreated();
            } catch (Exception e) {
                Log.e(TAG, "onCreate exception: ", e);
            }
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        try {
            getFragmentManager().putFragment(savedInstanceState, STATE_FRAGMENT, current_fragment);
        } catch( Exception e) {
            Log.e( TAG, "onSaveInstanceState exception: ", e);
        }
        super.onSaveInstanceState(savedInstanceState);
    }
    public void createDrawer() {
        mPlanetTitles = new ArrayList<String>();
        mPlanetTitles.add("DASHBOARD");
        mPlanetTitles.add("PROFILE");
        mPlanetTitles.add("CALL HISTORY");
        mPlanetTitles.add("FEEDBACK");
        mPlanetTitles.add("REVENUE");
        mPlanetTitles.add("SETTINGS");


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(InterpreterDashboardActivity.this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        //actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new DrawerCustomAdapter(this, R.layout.custom_drawer_adpter, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    public void onSuccessRedirection(int taskID) {

    }

    @Override
    public void onSuccessRedirection(int taskID, String jsonData) {

    }

    @Override
    public void onFailureRedirection(String errorMessage) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

	/*@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
		try {
			getFragmentManager().putFragment(savedInstanceState, STATE_FRAGMENT, current_fragment);
		} catch( Exception e) {
			Log.e(TAG, "onSaveInstanceState exception: ", e);
		}
		super.onSaveInstanceState(savedInstanceState);
	}*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        application.removeOperationChangeListener(this);
        application.removeCallNegotiationListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();


        try {
            if (mRegistrationBroadcastReceiver == null) {
                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        boolean sentToken = sharedPreferences.getBoolean(ApplicationSettings.SENT_TOKEN_TO_SERVER, false);
                        if (!sentToken) {
                            application.showErrorMessageBox(InterpreterDashboardActivity.this, getString(R.string.registering_message), getString(R.string.token_error_message));
                        }
                    }
                };
                LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(ApplicationSettings.REGISTRATION_COMPLETE));
            }
        } catch (Exception err) {
            Log.e(TAG, "onResume exception: with ", err);
        }


        mIsAlive = true;


        if (mNeedShowFragment) {
            showFragment(current_fragment);
            mNeedShowFragment = false;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mIsAlive = false;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        Object tag = v.getTag();
        if (tag != null && tag instanceof MenuList) {
            MenuList list = (MenuList) tag;
            list.fill(v, menu);
        }
    }

    public void finish() {
        if (current_fragment != null) {
            this.removeFragment(current_fragment);
            current_fragment = null;
        }
        application.logout();
        super.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        mSettingsMenuItem = menu.findItem(R.id.menu_settings);

        mInformationMenuItem = menu.findItem(R.id.menu_information);
        mInformationMenuItem.setVisible(false);

        mSignalStrengthMenuItem = menu.findItem(R.id.menu_signal_strenth);

        SignalBar signalBar = new SignalBar(this);

        mSignalStrengthMenuItem.setActionView(signalBar);
        mSignalStrengthMenuItem.setVisible(false);

        mSecureNetworkMenuItem = menu.findItem(R.id.menu_secure_network);

        mSecureNetworkMenuItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_secure_network);
        item.setEnabled(false);

        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...



		/*if( item == null)
            return false;
*/
        switch (item.getItemId()) {
            case R.id.menu_settings:
                //Toast.makeText(getApplication(),"Hi",Toast.LENGTH_LONG).show();

                Intent userTypeIntent = new Intent(InterpreterDashboardActivity.this,UserTypeActivity.class);
                userTypeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(userTypeIntent);
                onBackPressed();
            /*	SettingsFragment settings  = new SettingsFragment();
                settings.setBackFragment(current_fragment);

				mSettingsMenuItem.setVisible(false);

				addFragment(settings);

				current_fragment = settings;*/
                return true;

		/*	case R.id.menu_information:
                InformationFragment information  = new InformationFragment();
				information.setBackFragment(current_fragment);

				mSignalStrengthMenuItem.setVisible(false);
				mInformationMenuItem.setVisible(false);

				addFragment(information);

				current_fragment = information;
				return true;*/
        }
        return super.onOptionsItemSelected(item);
        //return super.onOptionsItemSelected( item);
    }

    @Override
    public void onOperationChange(Operation state) {
        try {
            switch (state) {
                case Error: {
                    switch (state.forOperation()) {
                        case Authorized:
                            current_fragment = ReautorizeFragment.newInstance(mSettingsMenuItem, state.getDescription());
                            break;
                    /*case LoggedIn:
                        //current_fragment = LoginFragment.newInstance(state.getDescription());
						current_fragment = new LoginFragment();
						break;*/
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
            /*	case AVChatRoom:
                    current_fragment = AVChatLoginFragment.newInstance(mSettingsMenuItem);
					break;*/
            /*	case AVChatCall:
                    current_fragment = CallNegotiationFragment.newInstance(mSettingsMenuItem);
					break;*/
                case PushNotification:
                    current_fragment = PushNotificationFragment.newInstance(mSettingsMenuItem);
                    break;
                case AVChatJoined:
                   /* current_fragment = AVChatSessionFragment.newInstance(mSignalStrengthMenuItem,
                            mSecureNetworkMenuItem, mInformationMenuItem);*/
                    current_fragment = new AVChatSessionFragment();
                    break;
                case Authorized:
                    //	current_fragment = LoginFragment.newInstance(mSettingsMenuItem);
                    //current_fragment = new CallNegotiationFragment();
                    String completion = utilObj.readDataInSharedPreferences("Users", 0, "completion");
                    if (completion != null) {
                        if (completion.equalsIgnoreCase("true")) {
                            current_fragment = new InterprterDashBoardFragment();
                        } else {
                            current_fragment = new UpdateInterpreterProfile();
                        }
                    }
                    break;
            /*	case LoggedIn:
					if (checkPlayServices()) {
						// Start IntentService to register this application with GCM.
						Intent intent = new Intent(this, RegistrationIntentService.class);
						startService(intent);
					}
					current_fragment = OptionFragment.newInstance(mSettingsMenuItem);
					break;*/
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
        if (!mIsAlive) {
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
        } catch (Exception err) {
            LogSdk.e(TAG, "showFragment " + err);
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
        } catch (Exception err) {
            LogSdk.e(TAG, "addFragment " + err);
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
        } catch (Exception err) {
            LogSdk.e(TAG, "removeFragment " + err);
        }
    }


    public static interface MenuList {
        public void fill(View view, ContextMenu menu);
    }

    @Override
    public void onBackPressed() {
        try {
            if (current_fragment != null) {

                if (/*(current_fragment instanceof WaitingFragment) ||*/ !current_fragment.onBackPressed()) {
                    return;
                }

                BaseFragment fragment = current_fragment.getBackFragment();
                if (fragment != null) {

                    if (current_fragment instanceof InformationFragment) {
                        mSignalStrengthMenuItem.setVisible(true);
                        mInformationMenuItem.setVisible(true);
                        removeFragment(fragment);
                    } else if (current_fragment instanceof SettingsFragment) {
                        mSettingsMenuItem.setVisible(true);
                        removeFragment(fragment);
                    } else {

                        showFragment(fragment);
                        System.gc();
                        Runtime.getRuntime().gc();
                    }
                    current_fragment = fragment;

                    return;
                }

            }
        } catch (Exception err) {
            Log.e(TAG, "");
        }
        super.onBackPressed();
    }

    @Override
    public void onMessageReceived(final CNMessage cnMessage) {
        if (application.getUniqueId().equals(cnMessage.getUniqueId())) {
            return;
        }
        // cnMessage= cnMessage;
        //app().sendCNMessage(toList, type, completionHandler);
        if (cnMessage.getMessageType() == CNMessage.CNMessageType.AnswerAccept) {
            String accepteduserName = cnMessage.getDisplayName();
            Iterator<String> toListIterartor =  toList.iterator();
            ArrayList<String> discoonectuserlList =  new ArrayList<String>();
            while(toListIterartor.hasNext())
            {
                String calledUserName = toListIterartor.next();
                if(!calledUserName.equalsIgnoreCase(accepteduserName)) {
                    discoonectuserlList.add(calledUserName);
                }
            }
            application.sendCNMessage(discoonectuserlList, CNMessage.CNMessageType.Cancel, null);
        }
        if (cnMessage.getMessageType() == CNMessage.CNMessageType.Calling) {

            if (application.isInConference()) {
                application.sendCNMessage(cnMessage.getFrom(), CNMessage.CNMessageType.Busy, null);
                return;
            }



        /*    NotificationCompat.Builder b = new NotificationCompat.Builder(application.getContext());

            Intent intent = new Intent(this, InterpreterDashboardActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            b.setContentIntent(pendingIntent);

            Intent actioncallIntent = new Intent(this, InterpreterDashboardActivity.class);
            PendingIntent actionPendingCallIntent = PendingIntent.getActivity(this, 0, actioncallIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            b.addAction(android.R.drawable.ic_btn_speak_now, "Answer", actionPendingCallIntent);

            Intent actionIntent = new Intent(this, InterpreterDashboardActivity.class);
            PendingIntent actionPendingIntent = PendingIntent.getActivity(this, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            b.addAction(android.R.drawable.ic_media_pause, "Decline", actionPendingIntent);

            b.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                            //.setTicker(property != null ? property : from)
                            //.setContentTitle(property != null ? property : from)
                    .setContentText("Calling")
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                            //.setContentIntent(contentIntent)
                    .setContentInfo("Info");


            NotificationManager notificationManager = (NotificationManager) application.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, b.build());*/

            callDialogBuilder = new AlertDialog.Builder(this).create();
            LayoutInflater inflater = getLayoutInflater();
            View incomingCallDialog = inflater.inflate(R.layout.incoming_call_dialog, null);
            incomingCallDialog.setAlpha(0.5f);
            callDialogBuilder.setView(incomingCallDialog);



            Button answerButton = (Button) incomingCallDialog.findViewById(R.id.answer_button);
            answerButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    application.setConferenceId(cnMessage.getConferenceId());
                    application.sendCNMessage(cnMessage.getFrom(), CNMessage.CNMessageType.AnswerAccept, null);
                    callDialogBuilder.hide();

                    application.join(application.getConferenceId(), true);
                }
            });

            Button declineButton = (Button) incomingCallDialog.findViewById(R.id.decline_button);
            declineButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    application.sendCNMessage(cnMessage.getFrom(), CNMessage.CNMessageType.AnswerDecline, null);
                    callDialogBuilder.hide();
                    InterpreterCallStatus interpreterCallStatus = new InterpreterCallStatus();
                    String userid = utilObj.readDataInSharedPreferences("Users", 0, "uid");
                    String email = utilObj.readDataInSharedPreferences("Users", 0, "email");
                    interpreterCallStatus.callstatus = 0;
                    interpreterCallStatus.id = userid;
                    interpreterCallStatus.emailid = email;


                    loginManagerObj.updateInterpreterCalls(interpreterCallStatus);



                }
            });

            callDialogBuilder.setCancelable(false);
            if(!callDialogBuilder.isShowing()) {
                callDialogBuilder.show();
            }
        } else if (cnMessage.getMessageType() == CNMessage.CNMessageType.Cancel) {
            callDialogBuilder.hide();
        } else if (cnMessage.getMessageType() == CNMessage.CNMessageType.EndCall) {
            application.onEndOfCall();
           // if (application.leave()) {
                int count = getFragmentManager().getBackStackEntryCount();
                String name = getFragmentManager().getBackStackEntryAt(count - 2).getName();
                getFragmentManager().popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
          //  }
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                LogSdk.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }


    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {

        if (mCompletion != null) {
            if (mCompletion.equalsIgnoreCase("true")) {
                // Create a new fragment and specify the planet to show based on position
                if (position == 0) {
                    Fragment newFragment = new InterprterDashBoardFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.host_activity, newFragment)
                            .commit();
                    // Highlight the selected item, update the title, and close the drawer
                    mDrawerList.setItemChecked(position, true);
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else if (position == 1)

                {
                    Fragment fragment = new UpdateInterpreterProfile();

                    // Insert the fragment by replacing any existing fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.host_activity, fragment)
                            .commit();
                    // Highlight the selected item, update the title, and close the drawer
                    mDrawerList.setItemChecked(position, true);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
                else if (position == 2)

                {
                    Fragment fragment = new ChatDetailsFragment();

                    // Insert the fragment by replacing any existing fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.host_activity, fragment)
                            .commit();
                    // Highlight the selected item, update the title, and close the drawer
                    mDrawerList.setItemChecked(position, true);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
                else if (position == 5)

                {
                    Fragment fragment = new InterpreterSettingFragment();

                    // Insert the fragment by replacing any existing fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.host_activity, fragment)
                            .commit();
                    // Highlight the selected item, update the title, and close the drawer
                    mDrawerList.setItemChecked(position, true);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }


            } else {
                confirmationDialog();
            }
        }
    }

    /**
     * confirmation dialog for complete the profile details
     */
    private void confirmationDialog() {
        final Dialog dialog = new Dialog(InterpreterDashboardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert_dialog);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        TextView confirmTextView = (TextView) dialog.findViewById(R.id.confirm_text_view);
        TextView cancelTextView = (TextView) dialog.findViewById(R.id.cancel_text_view);
        TextView titleTextView = (TextView) dialog.findViewById(R.id.title_text_view);
        titleTextView.setText(getResources().getString(R.string.complete_profile_title));
        TextView messageTextView = (TextView) dialog.findViewById(R.id.message_text_view);
        messageTextView.setText(getResources().getString(R.string.complete_profile_message));
        EditText passwordEditText = (EditText) dialog.findViewById(R.id.password_edit_text);
        passwordEditText.setVisibility(View.GONE);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mDrawerLayout.closeDrawer(mDrawerList);
                // dialog.dismiss();
                finish();
            }
        });
        confirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(mDrawerList);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void callingWindow()
    {
        callDialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View incomingCallDialog = inflater.inflate(R.layout.incoming_call_dialog, null);
        incomingCallDialog.setAlpha(0.5f);
        callDialogBuilder.setView(incomingCallDialog);



        Button answerButton = (Button) incomingCallDialog.findViewById(R.id.answer_button);
        answerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                application.setConferenceId(cnMessage.getConferenceId());
                application.sendCNMessage(cnMessage.getFrom(), CNMessage.CNMessageType.AnswerAccept, null);
                callDialogBuilder.hide();

                application.join(application.getConferenceId(), true);
            }
        });

        Button declineButton = (Button) incomingCallDialog.findViewById(R.id.decline_button);
        declineButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                application.sendCNMessage(cnMessage.getFrom(), CNMessage.CNMessageType.AnswerDecline, null);
                callDialogBuilder.hide();
            }
        });

        callDialogBuilder.setCancelable(false);
        callDialogBuilder.show();

    }


}
