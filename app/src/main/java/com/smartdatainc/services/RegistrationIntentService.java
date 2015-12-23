package com.smartdatainc.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.smartdatainc.dataobject.AppInstance;
import com.smartdatainc.dataobject.GcmNotification;
import com.smartdatainc.interfaces.CallBack;
import com.smartdatainc.managers.CommunicationManager;
import com.smartdatainc.managers.DataParser;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.ResponseCodes;
import com.smartdatainc.utils.Utility;

/**
 * Created by Anurag Sethi on 25-06-2015.
 */
public class RegistrationIntentService extends IntentService implements CallBack {

    private static final String TAG = "RegIntentService";
    private CommunicationManager communicationManagerObj;
    private Utility utilObj;
    private int taskID;
    private String jsonString;
    private GcmNotification gcmNotificationObj;
    private DataParser dataParserObj = new DataParser();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RegistrationIntentService() {
        super(TAG);
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                // [START get_token]
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(Constants.GCM.GCM_SENDER_ID,
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                // [END get_token]
                Log.i(TAG, "GCM Registration Token: " + token);

                // TODO: Implement this method to send any registration to your app's servers.
                sendRegistrationToServer(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                sharedPreferences.edit().putBoolean(Constants.GCM.SENT_TOKEN_TO_SERVER, true).apply();
                // [END register_for_gcm]
            }

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(Constants.GCM.SENT_TOKEN_TO_SERVER, false).apply();
        }

    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        communicationManagerObj = new CommunicationManager(getApplicationContext());
        utilObj = new Utility(getApplicationContext());
        taskID = Constants.TaskID.SEND_GCM_DEVICE_ID_TO_SERVER_TASK_ID;

        gcmNotificationObj = new GcmNotification();
        gcmNotificationObj.deviceID = token;

        jsonString = utilObj.convertObjectToJson(gcmNotificationObj);

        communicationManagerObj.CallWebService(getApplicationContext(), Constants.WebServices.WS_GCM_DEVICE_ID_TO_SERVER, RegistrationIntentService.this, jsonString , taskID);


    }

    /**
     * The interface method implemented in the java files
     *
     * @param data    the result returned by the web service
     * @param tasksID the ID to differential multiple webservice calls
     * @return none
     */
    @Override
    public void onResult(String data, int tasksID) {

        String errorMessage = "";
        if(taskID == Constants.TaskID.SEND_GCM_DEVICE_ID_TO_SERVER_TASK_ID) {
            AppInstance.logObj.printLog("data=" + data);
            if(data != null) {
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE_ID));
                switch(messageID) {
                    case ResponseCodes.Success:
                        AppInstance.logObj.printLog("DeviceID registered on server");
                        break;

                    case ResponseCodes.InvalidJsonFormat:
                        errorMessage = this.getResources().getString(R.string.invalid_json_format);
                        break;

                    default:
                        errorMessage = this.getResources().getString(R.string.invalid_message);
                        break;
                }

                if(!errorMessage.isEmpty()) {
                    AppInstance.logObj.printLog("DeviceID not registered on server");
                }
            }
        }
    }
}
