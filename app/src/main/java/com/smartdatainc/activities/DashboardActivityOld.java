package com.smartdatainc.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.smartdatainc.app.ooVooSdkSampleShowApp;
import com.smartdatainc.app.ooVooSdkSampleShowApp.CallNegotiationListener;
import com.smartdatainc.call.CNMessage;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.LocationUtility;
import com.smartdatainc.utils.Utility;

/**
 * Created by Anurag Sethi
 */
public class DashboardActivityOld extends Activity implements  CallNegotiationListener{

    private Utility utilObj;
    private Button btnLogoutObj;
    LocationUtility locationUtilityObj;
      private Button btnMapObj;
    private Button btnChoosePhotoObj;
    private Button btnSwipeRefreshObj;
    private Button btnGCMObj;
    private Button btnListViewObj;
    private ooVooSdkSampleShowApp	application	   = null;
    private AlertDialog 			callDialogBuilder = null;
    private BroadcastReceiver mRegistrationBroadcastReceiver = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        initData();
        bindControls();
        application = (ooVooSdkSampleShowApp) getApplication();
        application.setContext(this);
       // application.addOperationChangeListener(this);
        application.addCallNegotiationListener(this);
       /// ((MyApplication)getApplicationContext()).setAnalyticTracking(this, "dashboard");

    }

    /**
     * Default method of activity life cycle to handle the actions required once the activity starts
     * checks if the network is available or not
     * @return none
     */

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

         //checking location services status
        if(!locationUtilityObj.checkLocationServiceStatus()) {
            utilObj.showAlertDialog(this,this.getResources().getString(R.string.location_service_message_title),this.getResources().getString(R.string.location_service_message), this.getResources().getString(R.string.Ok), null, Constants.ButtonTags.TAG_LOCATION_SERVICE_ENABLE, 0);
        }
       
    }

    /**
     * Initializes the objects
     * @return none
     */
    private void initData() {
        utilObj = new Utility(this);
        btnLogoutObj = (Button) findViewById(R.id.btnLogout);
         locationUtilityObj = new LocationUtility(this);
      btnMapObj = (Button) findViewById(R.id.btnMap);
         btnChoosePhotoObj = (Button) findViewById(R.id.btnChoosePhoto);
         btnSwipeRefreshObj = (Button) findViewById(R.id.btnSwipeRefresh);
         btnGCMObj = (Button) findViewById(R.id.btnGCM);
         btnListViewObj = (Button) findViewById(R.id.btnListView);
    }

    /**
     * Binds the UI controls
     * @return none
     */
    private void bindControls() {

        //logout
        btnLogoutObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilObj.removeKeyFromSharedPreferences("Users", DashboardActivityOld.MODE_PRIVATE, "userID", true);
                Intent intentObj = new Intent(DashboardActivityOld.this, LoginActivity.class);
                startActivity(intentObj);
            }
        });
        
        //google map
        btnMapObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(DashboardActivityOld.this, MapsActivity.class);
                startActivity(intentObj);
            }
        });
        //choose photo
        btnChoosePhotoObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app().makeCall();
               /* Intent intentObj = new Intent(DashboardActivityOld.this, ChoosePhotoActivity.class);
                startActivity(intentObj);*/
            }
        });
        //Swipe Refresh
        btnSwipeRefreshObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(DashboardActivityOld.this, SwipeRefreshActivity.class);
                startActivity(intentObj);
            }
        });
        //GCM Notification
        btnGCMObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(DashboardActivityOld.this, GCMActivity.class);
                startActivity(intentObj);
            }
        });
        //ListView
        btnListViewObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(DashboardActivityOld.this, SwipeListActivity.class);
                startActivity(intentObj);
            }
        });

    }
    ooVooSdkSampleShowApp app(){
        return ((ooVooSdkSampleShowApp)getApplication()) ;
    }
    @Override
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

}
