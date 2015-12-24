package com.smartdatainc.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Response;
import com.facebook.Session;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.smartdatainc.app.ooVooSdkSampleShowApp;
import com.smartdatainc.dataobject.User;
import com.smartdatainc.helpers.FacebookSSO;
import com.smartdatainc.interfaces.FacebookTaskCompleted;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.LoginManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Constants.SocailNewtork;
import com.smartdatainc.utils.Utility;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by Anjani Kumar
 * The activity is used for handling the login screen actions 
 */
public class LoginActivity extends AppActivity implements ServiceRedirection, FacebookTaskCompleted,GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "LoginActivity";
    private String email;
    private String password;
    private EditText emailObj;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private ProgressDialog mProgressDialog;
    private SignInButton btnSignIn;
   // private TwitterAuthClient authClient;

    private EditText passwordObj;
    private Button btnLoginObj;
    private TextView btnSignUpObj;
    private Button linkedInLogin;
    private Button googlePlusLogin;
    private TextView textViewObj;
    private TextView mtextForgtPass;
    private Utility utilObj;
    private String message;
    private User userObj;
    private LoginManager loginManagerObj;
    private boolean mIntentInProgress;
    private LinearLayout fb_login_llObj;
   private TwitterLoginButton twitterLoginButton;
    FacebookSSO facebookssoObj;
    private boolean mSignInClicked;
    FacebookTaskCompleted onFacebookTaskCompletedObj;
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_TWITTER_SIGN_IN = 140;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        facebookssoObj = new FacebookSSO(this, this);
        facebookssoObj.createFacebookSession(savedInstanceState);
        SharedPreferences preferences = this.getSharedPreferences("user_info", 0);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // authClient = new TwitterAuthClient();
       // TwitterAuthConfig authConfig =  new TwitterAuthConfig("consumerKey", "consumerSecret");
        //Fabric.with(this, new Twitter(authConfig));
        //signIn();


        initData();
        bindControls();

    }
    private static final String getProfileUrl(String accessToken){

        return SocailNewtork.PROFILE_URL+"(id,first-name,last-name,email-address,picture-url)"
                +SocailNewtork.QUESTION_MARK
                +SocailNewtork.OAUTH_ACCESS_TOKEN_PARAM+SocailNewtork.EQUALS+accessToken+SocailNewtork.AMPERSAND+SocailNewtork.lINKED_IN_SERVICE_FORMAT;
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

        if(!isNetworkAvailable(this)) {
            utilObj.showAlertDialog(this,this.getResources().getString(R.string.network_service_message_title),this.getResources().getString(R.string.network_service_message), this.getResources().getString(R.string.Ok), null, Constants.ButtonTags.TAG_NETWORK_SERVICE_ENABLE, 0);
        }


        try {
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }

            // Session.getActiveSession().addCallback(statusCallback);
            facebookssoObj.addActiveSessionCallback();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    ooVooSdkSampleShowApp app(){
        return ((ooVooSdkSampleShowApp)getApplication()) ;
    }
    /**
     * Default activity life cycle method
     */
    @Override
    public void onStop() {
        super.onStop();

        try {

            // Session.getActiveSession().removeCallback(statusCallback);
            facebookssoObj.removeActiveSessionCallback();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * The method to handle the data when the orientation is changed
     *
     * @param outState contains Bundle data
     */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);

    }

    /**
     * Initializes the objects
     * @return none
     */
    @Override
    public void initData() {
        emailObj = (EditText) findViewById(R.id.email);
        passwordObj = (EditText) findViewById(R.id.password);
        btnLoginObj = (Button) findViewById(R.id.btnSignIn);
        btnSignUpObj = (TextView) findViewById(R.id.btnSignup);
        textViewObj = (TextView) findViewById(R.id.errorMessage);
        mtextForgtPass = (TextView) findViewById(R.id.forgotPassword);
        linkedInLogin = (Button) findViewById(R.id.login_with_linked_in);
        googlePlusLogin = (Button) findViewById(R.id.gsign_in_button);
        utilObj = new Utility(this);
        userObj = new User();
        loginManagerObj = new LoginManager(this, this);
        fb_login_llObj = (LinearLayout) findViewById(R.id.fb_login_ll);
      twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twlogin_button);

    }



    /**
     * Binds the UI controls
     * @return none
     */
    @Override
    public void bindControls() {

        //Login Button click
        btnLoginObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailObj.getText().toString();
                password = passwordObj.getText().toString();



                if(validatingRequired()) {
                    app().login(email, email);
                    if (app().isOnline()) {
                        Intent intentObj = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intentObj);
                        Toast.makeText(getApplicationContext(),"online",Toast.LENGTH_LONG).show();
                    }
                   /* utilObj.startLoader(LoginActivity.this, R.drawable.image_for_rotation);

                    //assigning the data to the user object
                    userObj.email = email;
                    userObj.password = password;
                    loginManagerObj.authenticateLogin(userObj);*/
                }

            }
        });
       googlePlusLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //SignUp
        btnSignUpObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intentObj);
            }
        });


       /* linkedInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginWithLinkedInIntent = new Intent(LoginActivity.this, LoginWithLinkedInActivity.class);
                startActivity(loginWithLinkedInIntent);
            }
        });*/


        //facebook login
        fb_login_llObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                facebookssoObj.startFacebookLogin(LoginActivity.this);
            }
        });
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(result.data, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        String email = result.data;
                        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
                        // Do something with the result, which provides the email address
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });

        //ForgotPassword
        mtextForgtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intentObj);
            }
        });


    }



    /**
     *  The method is used to validate the required fields
     *  @return boolean true if fields are validated else false     
     **/

    private boolean validatingRequired() {
        message = "";
        email = emailObj.getText().toString();
        password = passwordObj.getText().toString();

        //validate the content
        if(email.isEmpty()) {
            message = getResources().getString(R.string.EmailErrorMessage);
            utilObj.showError(this, message, textViewObj, emailObj);
        }
       /* else if(!utilObj.checkEmail(email)) {
            message = getResources().getString(R.string.invalid_email);
            utilObj.showError(this, message, textViewObj, emailObj);
        }*/
        else if(password.isEmpty()) {
            message = getResources().getString(R.string.PasswordErrorMessage);
            utilObj.showError(this, message, textViewObj, passwordObj);
        }

        if(message.equalsIgnoreCase("") || message == null) {
            return true;
        }
        else {
            return false;
        }

    }

    /**
     * The method handles the result from the Facebook
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else if(requestCode == RC_TWITTER_SIGN_IN){
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
    }



    /**
     * The interface method implemented in the java files
     *
     * @param taskID the id based on which the relevant action is performed
     * @return none
     */
    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopLoader();

        if(taskID == Constants.TaskID.LOGIN_TASK_ID) {
            //call the intent for the next activity
            // User userobj = (User)userObj;
            // int userid =  userobj.userID;
            Intent intentObj = new Intent(this, DashboardActivity.class);
            startActivity(intentObj);
        }
    }
    public void onSuccessRedirection(int taskID,String jsonMesseage) {

    }
    /**
     * The interface method implemented in the java files
     * @param errorMessage the error message to be displayed
     * @return none
     */
    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopLoader();
        utilObj.showError(this, errorMessage, textViewObj, null);

    }

    /**
     * The interface method implemented in the java files
     *
     * @param response is the response received from the Facebook
     */
    @Override
    public void onFacebookTaskCompleted(Response response) {
        //write your code here
        Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.authorizing));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("LoginActivity", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String usernameEmail = acct.getEmail()+"email: "+acct.getDisplayName();
            Log.d(TAG, "usernameEmail:" + usernameEmail);
            Toast.makeText(getApplicationContext(),usernameEmail, Toast.LENGTH_LONG).show();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }
    // [END handleSignInResult]

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        //updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
