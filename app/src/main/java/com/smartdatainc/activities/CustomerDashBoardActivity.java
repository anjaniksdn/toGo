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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.oovoo.core.Utils.LogSdk;
import com.oovoo.sdk.api.ooVooClient;
import com.smartdatainc.adapters.CustomerDraweAdapter;
import com.smartdatainc.app.ApplicationSettings;
import com.smartdatainc.app.ooVooSdkSampleShowApp;
import com.smartdatainc.app.ooVooSdkSampleShowApp.CallNegotiationListener;
import com.smartdatainc.app.ooVooSdkSampleShowApp.Operation;
import com.smartdatainc.app.ooVooSdkSampleShowApp.OperationChangeListener;
import com.smartdatainc.call.CNMessage;
import com.smartdatainc.dataobject.CallEndDetails;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.UserProfileManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.ui.SignalBar;
import com.smartdatainc.ui.fragments.AVChatLoginFragment;
import com.smartdatainc.ui.fragments.AVChatSessionFragment;
import com.smartdatainc.ui.fragments.BaseFragment;
import com.smartdatainc.ui.fragments.ChatDetailsFragment;
import com.smartdatainc.ui.fragments.CustomerDashboardFragment;
import com.smartdatainc.ui.fragments.InformationFragment;
import com.smartdatainc.ui.fragments.InterpretationFragment;
import com.smartdatainc.ui.fragments.PushNotificationFragment;
import com.smartdatainc.ui.fragments.ReautorizeFragment;
import com.smartdatainc.ui.fragments.SettingsFragment;
import com.smartdatainc.ui.fragments.UpdateCustomerProfileFragment;
import com.smartdatainc.ui.fragments.WaitingFragment;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

import java.util.ArrayList;
import java.util.Iterator;


public class CustomerDashBoardActivity extends AppActivity implements ServiceRedirection,OperationChangeListener, CallNegotiationListener {


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
    private CustomerDraweAdapter drawerCustomAdapter;
    private ListView mDrawerList = null;
    private DrawerLayout mDrawerLayout = null;
    private ArrayList<String> mPlanetTitles = null;
    private ActionBarDrawerToggle mDrawerToggle;
    private Utility utilObj;
    private String mCompletion;
    UserProfileManager userProfileManager;
    Context context;
   // public static ArrayList<String> toList = new ArrayList<String>();
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

        application.addOperationChangeListener(this);
        application.addCallNegotiationListener(this);
        context = this;

        userProfileManager = new UserProfileManager(context, this);
        createDrawer();


        if (savedInstanceState != null) {
            current_fragment = (BaseFragment) getFragmentManager().getFragment(savedInstanceState, STATE_FRAGMENT);
            showFragment(current_fragment);
        } else {
           /* Fragment newFragment = new CallNegotiationFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.host_activity, newFragment).commit();*/
            mCompletion = utilObj.readDataInSharedPreferences("Users", 0, "completion");
            //mCompletion =  "true";
            if (mCompletion != null) {
                if (mCompletion.equalsIgnoreCase("true")) {
                    Fragment newFragment = new CustomerDashboardFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.host_activity, newFragment).commit();
                } else {
                    Fragment fragment = new UpdateCustomerProfileFragment();

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

                // application.onMainActivityCreated();
            } catch (Exception e) {
                Log.e(TAG, "onCreate exception: ", e);
            }
        }
    }

    public void createDrawer() {
        mPlanetTitles = new ArrayList<String>();
        mPlanetTitles.add("DASHBOARD");
        mPlanetTitles.add("PROFILE");
        mPlanetTitles.add("ORDER INTERPRETATION");
        mPlanetTitles.add("CALL HISTORY");
        mPlanetTitles.add("PURCHASES");
        mPlanetTitles.add("FAVORITE INTERPRETER");
        mPlanetTitles.add("SETTINGS");


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(CustomerDashBoardActivity.this, mDrawerLayout,
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
        mDrawerList.setAdapter(new CustomerDraweAdapter(this, R.layout.custom_drawer_adpter, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    public void onSuccessRedirection(int taskID) {

    }

    @Override
    public void onSuccessRedirection(int taskID, String jsonData) {
        application.onEndOfCall();
        int count = getFragmentManager().getBackStackEntryCount();
        String name = getFragmentManager().getBackStackEntryAt(count - 2).getName();
        getFragmentManager().popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);


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

    public void setActionBar(String title) {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.custom_action_bar, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);
        ImageView home = (ImageView) customView.findViewById(R.id.home);

        title1.setText(title);

        getSupportActionBar().setCustomView(customView);

    }


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
                            // application.showErrorMessageBox(CustomerDashBoardActivity.this, getString(R.string.registering_message), getString(R.string.token_error_message));
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
        // application.logout();
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

        switch (item.getItemId()) {
            case R.id.menu_settings:
                // Toast.makeText(getApplication(), "Hi", Toast.LENGTH_LONG).show();
                Intent userTypeIntent = new Intent(CustomerDashBoardActivity.this,UserTypeActivity.class);
                userTypeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(userTypeIntent);
               // onBackPressed();
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

		/*if( item == null)
            return false;*/

		/*switch( item.getItemId())
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

		return super.onOptionsItemSelected( item);*/
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
                            // application.showErrorMessageBox(this, getString(R.string.join_session), state.getDescription());
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
                    /*current_fragment = AVChatSessionFragment.newInstance(mSignalStrengthMenuItem,
                            mSecureNetworkMenuItem, mInformationMenuItem);*/
                    current_fragment = new AVChatSessionFragment();
                    break;
                case Authorized:
                    //	current_fragment = LoginFragment.newInstance(mSettingsMenuItem);
                    //current_fragment = new CallNegotiationFragment();
                    String completion = utilObj.readDataInSharedPreferences("Users", 0, "completion");
                    if (completion != null) {
                        if (completion.equalsIgnoreCase("true")) {
                            current_fragment = new CustomerDashboardFragment();
                        } else {
                            current_fragment = new UpdateCustomerProfileFragment();
                        }
                    }
                    // current_fragment =  new CustomerDashboardFragment();
                    break;
            /*	case LoggedIn:
					if (checkPlayServices()) {
						// Start IntentService to register this application with GCM.
						Intent intent = new Intent(this, RegistrationIntentService.class);
						startService(intent);
					}
					current_fragment = OptionFragment.newInstance(mSettingsMenuItem);
					break;*/
                case AVChatDisconnected:/*
                    if (application.isCallNegotiation()) {
                        return;
                    } else {
                        current_fragment = AVChatLoginFragment.newInstance(mSettingsMenuItem);
                        break;
                    }*/

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
        //app().sendCNMessage(toList, type, completionHandler);
        if (cnMessage.getMessageType() == CNMessage.CNMessageType.AnswerAccept) {
            String accepteduserName = cnMessage.getDisplayName();
            if(InterpretationFragment.toList !=null) {
                Iterator<String> toListIterartor = InterpretationFragment.toList.iterator();
                ArrayList<String> discoonectuserlList = new ArrayList<String>();
                while (toListIterartor.hasNext()) {
                    String calledUserName = toListIterartor.next();
                    if (!calledUserName.equalsIgnoreCase(accepteduserName)) {
                        discoonectuserlList.add(calledUserName);
                    }
                }
                application.sendCNMessage(discoonectuserlList, CNMessage.CNMessageType.Cancel, null);
            }
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

           /* if (application.leave()) {*/
                String from = cnMessage.getFromId();
                String to = cnMessage.getToId();
                String callId = cnMessage.getConferenceId();
                long timeStamp = cnMessage.getTimeStamp();

                String userid = utilObj.readDataInSharedPreferences("Users", 0, "id");
                String poolId = utilObj.readDataInSharedPreferences("Call", 0, "poolId");
                String fromlanguageId = utilObj.readDataInSharedPreferences("Call", 0, "fromlanguageId");
                String tolanguageId = utilObj.readDataInSharedPreferences("Call", 0, "tolanguageId");
                String languagePrice = utilObj.readDataInSharedPreferences("Call", 0, "languagePrice");
                String starttimeStamp = utilObj.readDataInSharedPreferences("Call", 0, "starttimeStamp");
                String[] interpreterId = InterpretationFragment.toUserIdList.toArray(new String[InterpretationFragment.toUserIdList.size()]);

                CallEndDetails callEndDeatils = new CallEndDetails();
                callEndDeatils.poolId = poolId;
                callEndDeatils.userId = userid;
                callEndDeatils.callReceivedBy = from;
                callEndDeatils.tolanguage = tolanguageId;
                callEndDeatils.fromlanguage = fromlanguageId;
                callEndDeatils.cost = languagePrice;
                callEndDeatils.start_time = starttimeStamp;
                callEndDeatils.end_time = ""+timeStamp;
                callEndDeatils.interpreterId = interpreterId;
                /*int count = getFragmentManager().getBackStackEntryCount();
                String name = getFragmentManager().getBackStackEntryAt(count - 2).getName();
                getFragmentManager().popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);*/


            //    callDeatils.start_time = callId;



                userProfileManager.userCallDetailEnd(callEndDeatils);
                //String sharedPrefName, int mode, String key, String value
                // utilObj.saveDataInSharedPreferences("Call", getActivity().MODE_PRIVATE, "from",from);
                //  utilObj.saveDataInSharedPreferences("Call", getActivity().MODE_PRIVATE, "to",to);

           // }
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
                Fragment fragment = null;
                FragmentManager fragmentManager;
                if (position == 0) {
                    fragment = new CustomerDashboardFragment();
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.host_activity, fragment).commit();

                    mDrawerList.setItemChecked(position, true);
                    //setTitle(mPlanetTitles[position]);
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else if (position == 1)

                {
                    fragment = new UpdateCustomerProfileFragment();
                    // Insert the fragment by replacing any existing fragment
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.host_activity, fragment)
                            .commit();

                    // Highlight the selected item, update the title, and close the drawer
                    mDrawerList.setItemChecked(position, true);
                    //setTitle(mPlanetTitles[position]);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }else if(position ==2)
                {
                    fragment = new InterpretationFragment();
                    // Insert the fragment by replacing any existing fragment
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.host_activity, fragment)
                            .commit();

                    // Highlight the selected item, update the title, and close the drawer
                    mDrawerList.setItemChecked(position, true);
                    //setTitle(mPlanetTitles[position]);
                    mDrawerLayout.closeDrawer(mDrawerList);

                }
                else if(position ==3)
                {
                    fragment = new ChatDetailsFragment();
                    // Insert the fragment by replacing any existing fragment
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.host_activity, fragment)
                            .commit();

                    // Highlight the selected item, update the title, and close the drawer
                    mDrawerList.setItemChecked(position, true);
                    //setTitle(mPlanetTitles[position]);
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
        final Dialog dialog = new Dialog(CustomerDashBoardActivity.this);
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
                //dialog.dismiss();
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


}
