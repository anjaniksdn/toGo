package com.smartdatainc.helpers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.smartdatainc.interfaces.FacebookTaskCompleted;

import java.util.Arrays;

import sdei.support.lib.communication.HttpUtility;

/**
 * Created by Anurag Sethi on 12-03-2015.
 * The class handles the Facebook login integration using SSO
 */
public class FacebookSSO {

    Context contextObj;
    HttpUtility httpUtilityObj;
    String userName;
    FacebookTaskCompleted onFacebookTaskCompletedObj;

    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    /**
     * Constructor
     */

    public FacebookSSO(Context context, FacebookTaskCompleted onFacebookTaskCompletedObj) {
        contextObj = context;
        httpUtilityObj = new HttpUtility(contextObj);
        this.onFacebookTaskCompletedObj = onFacebookTaskCompletedObj;
   }

    /**
     * Creates the facebook session
     * @param savedInstanceState
     */
    public void createFacebookSession(Bundle savedInstanceState) {

        if(httpUtilityObj.isNetworkAvailable()) {
            Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

            Session session = Session.getActiveSession();

            if (session == null) {

                if (savedInstanceState != null) {
                    session = Session.restoreSession(contextObj, null, statusCallback, savedInstanceState);
                }
                if (session == null) {
                    session = new Session(contextObj);
                }
                Session.setActiveSession(session);
            } else {
                session.closeAndClearTokenInformation();
            }
        }

    }

    /**
     * Starts the facebook login
     * @param activity
     */
    public void startFacebookLogin(Activity activity) {

        if (httpUtilityObj.isNetworkAvailable()) {

            Session session = Session.getActiveSession();
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(activity).setCallback(statusCallback));
            }

            onClickLogin(activity);

        } else {

           Toast.makeText(contextObj, "Internet Not Available", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * The function checks the session state and open in case it is closed
     * @param activity
     */
    private void onClickLogin(Activity activity) {
        Session session = Session.getActiveSession();

        if (!session.isOpened() && !session.isClosed()) {
                    session.openForRead(new Session.OpenRequest(activity)
                            .setPermissions(Arrays.asList("email"))
                            .setCallback(statusCallback));
        } else {
            Session.openActiveSession(activity, true, statusCallback);
        }
    }


    /**
     * The callback function
     */
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            sendRequests(session);
        }
    }

    /**
     * The function manages the login request
     * @param session - facebook active session
     */
    private void sendRequests(final Session session) {

       /* GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Insert your code here
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();*/

        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                onFacebookTaskCompletedObj.onFacebookTaskCompleted(response);
                            }
                        }

                        if (response.getError() != null) {
                            System.out.println("Response=" + response);
                        }
                    }
                });
        Bundle fbrequestBundle =  new Bundle();
        fbrequestBundle.putString("fields","id,name,email");
        request.setParameters(fbrequestBundle);
        request.executeAsync();

    }

    /**
     * Removes the callback from the active session
     */
    public void removeActiveSessionCallback() {
        Session.getActiveSession().removeCallback(statusCallback);
    }

    /**
     * Adds the callback from the active session
     */
    public void addActiveSessionCallback() {

        Session.getActiveSession().addCallback(statusCallback);
    }

    /**
     * The function fetch the friend list for the logged in user
     */
    public void getFriendList() {
        Session session = Session.getActiveSession();

        /* make the API call */
        new Request(
                session,
                "/me/friends",
                null,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                        /* handle the result */
                        onFacebookTaskCompletedObj.onFacebookTaskCompleted(response);

                        if (response.getError() != null) {
                            System.out.println("Response=" + response);
                        }

                    }
                }
        ).executeAsync();

    }

    /**
     * Post the message on the facebook wall
     */
    public void postMessageOnTheWall() {

        Session session = Session.getActiveSession();
        session.getAccessToken();

        session.requestNewPublishPermissions(new Session.NewPermissionsRequest((Activity)contextObj, "publish_actions" ));

        Bundle params = new Bundle();
        params.putString("message", "this is a test message");

        //make the API call
        new Request(
                session,
                "/me/feed",
                params,
                HttpMethod.POST,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                        /* handle the result */
                        onFacebookTaskCompletedObj.onFacebookTaskCompleted(response);

                        if (response.getError() != null) {
                            System.out.println("Response11=" + response);
                    }

                    }
                }
        ).executeAsync();
    }
}
