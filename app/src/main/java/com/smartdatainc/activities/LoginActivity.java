package com.smartdatainc.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

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
    private ImageView googlePlusLogin;
    private TextView textViewObj;
    private TextView mtextForgtPass;
    private Utility utilObj;
    private String message;
    private User userObj;
    private LoginManager loginManagerObj;
    private boolean mIntentInProgress;
    private LinearLayout fb_login_llObj;
    private TwitterLoginButton twitterLoginButton;
    private String usertype;
    FacebookSSO facebookssoObj;
    private boolean mSignInClicked;
    FacebookTaskCompleted onFacebookTaskCompletedObj;
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_TWITTER_SIGN_IN = 140;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        setActionBar(Constants.APPHEADER);
        facebookssoObj = new FacebookSSO(this, this);
        facebookssoObj.createFacebookSession(savedInstanceState);
        SharedPreferences preferences = this.getSharedPreferences("user_info", 0);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        Bundle intentBundle =  getIntent().getBundleExtra("userbundle");
        //intentBundle.getString("usertype");
        usertype = intentBundle.getString("usertype");
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
 /*           OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
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
            }*/

            //Session.getActiveSession().addCallback(statusCallback);
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

    /*public void setActionBar(String title) {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.custom_action_bar, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);
        TextView saveText = (TextView) customView.findViewById(R.id.textViewSave);
        ImageView menuAddButton = (ImageView) customView.findViewById(R.id.addButton);
        ImageView home = (ImageView) customView.findViewById(R.id.home);
        title1.setText(title);
        // menuAddButton.setVisibility(View.VISIBLE);
        // saveText.setVisibility(View.GONE);
        //home.setOnTouchListener(this);
        //menuAddButton.setOnTouchListener(this);
        getSupportActionBar().setCustomView(customView);
    }*/
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
        mtextForgtPass = (TextView) findViewById(R.id.forgotPassword);linkedInLogin = (Button) findViewById(R.id.login_with_linked_in);
        googlePlusLogin = (ImageView) findViewById(R.id.gsign_in_button);

        utilObj = new Utility(this);
        userObj = new User();
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/robot_regular.ttf");
        btnSignUpObj.setTypeface(face);
        mtextForgtPass.setTypeface(face);
        loginManagerObj = new LoginManager(this, this);
        fb_login_llObj = (LinearLayout) findViewById(R.id.fb_login_ll);
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twlogin_button);
       /* twitterLoginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        twitterLoginButton.setBackgroundResource(R.drawable.tw);
        twitterLoginButton.setHeight(convertDpToPixel(60, LoginActivity.this));
        twitterLoginButton.setWidth(convertDpToPixel(60, LoginActivity.this));
        twitterLoginButton.setCompoundDrawablePadding(0);
        twitterLoginButton.setPadding(0, 0, 0, 0);
        twitterLoginButton.setText("");
        twitterLoginButton.setTextSize(18);*/

    }
    public static int convertDpToPixel(int dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = dp * (metrics.densityDpi / 160);
        return px;
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
                   /* if(usertype.equalsIgnoreCase("interpreter"))
                    {
                        Intent intentObj = new Intent(LoginActivity.this, InterpreterDashboardActivity.class);
                        startActivity(intentObj);
                    }
                    else{
                        Intent intentObj = new Intent(LoginActivity.this, CustomerDashBoardActivity.class);
                        startActivity(intentObj);}*/

                  /*  app().login(email, email);
                    if (app().isOnline()) {
                        Intent intentObj = new Intent(getApplicationContext(), InterpreterDashboardActivity.class);
                        startActivity(intentObj);
                        Toast.makeText(getApplicationContext(),"online",Toast.LENGTH_LONG).show();
                    }*/
//                    utilObj.startLoader(LoginActivity.this, R.drawable.image_for_rotation);

                    //assigning the data to the user object

                    // email = "togo-ibq@ice-breakrr.com";
                    // email = "obaidr@yopmail.com";
                    //  password = "Obaid@123";

                    //Csutomer
                    //UN : rakeshp@ice-breakrr.com

                    //PW : User@123

                    //UN / PW : togo-ibq@ice-breakrr.com/Int@123
                    email="togo-ibq@ice-breakrr.com";
                    password="Int@123";
                    userObj.email = email;
                    userObj.password = password;
                    // userObj.utype = "interpreter";
                    // Intent intentObj = new Intent(getApplicationContext(), InterpreterDashboardActivity.class);
                    // startActivity(intentObj);
                    login(email,password,"0");
                    //loginManagerObj.authenticateLogin(userObj);
                }

            }
        });
        googlePlusLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // utilObj.startLoader(LoginActivity.this, R.drawable.image_for_rotation);
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
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

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
                //utilObj.startLoader(LoginActivity.this, R.drawable.image_for_rotation);
                facebookssoObj.startFacebookLogin(LoginActivity.this);
            }
        });
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterAuthClient authClient = new TwitterAuthClient();
                //   utilObj.startLoader(LoginActivity.this, R.drawable.image_for_rotation);

                String username  = result.data.getUserName();

                if(username!= null && username.length()>0)
                {
                    email = username;
                    userObj.email = username;
                    login(username, "",  "1");
                    /*userObj.password = "";
                    loginManagerObj.authenticateLogin(userObj);*/
                }
                // Toast.makeText(getApplicationContext(),"Twitter user name:::"+ username, Toast.LENGTH_LONG).show();
              /*  authClient.requestEmail(result.data, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        String email = result.data;
                        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
                        // Do something with the result, which provides the email address
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                      //  Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                    }
                });*/
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
            utilObj.showToast(this, message, 0);
            //utilObj.showError(this, message, textViewObj, emailObj);
        }
       /* else if(!utilObj.checkEmail(email)) {
            message = getResources().getString(R.string.invalid_email);
            utilObj.showError(this, message, textViewObj, emailObj);
        }*/
        else if(password.isEmpty()) {
            message = getResources().getString(R.string.PasswordErrorMessage);
            utilObj.showToast(this, message, 0);
            //utilObj.showError(this, message, textViewObj, passwordObj);
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
            if(usertype.equalsIgnoreCase("interpreter"))
            {
                Intent intentObj = new Intent(this, InterpreterDashboardActivity.class);
                startActivity(intentObj);
            }
            else{
                Intent intentObj = new Intent(this, CustomerDashBoardActivity.class);
                startActivity(intentObj);}
        }
    }


    public void onSuccessRedirection(int taskID,String jsonMesseage) {
        if(utilObj!=null) {
            utilObj.stopLoader();
        }
        if(taskID == Constants.TaskID.LOGIN_TASK_ID) {
            //  Toast.makeText(getApplicationContext(),jsonMesseage,Toast.LENGTH_LONG).show();
            app().login(email, email);
            if (app().isOnline()) {
                /*Intent intentObj = new Intent(getApplicationContext(), InterpreterDashboardActivity.class);
                startActivity(intentObj);*/
                if(usertype.equalsIgnoreCase("interpreter"))
                {
                    Intent intentObj = new Intent(LoginActivity.this, InterpreterDashboardActivity.class);
                    startActivity(intentObj);
                }
                else{
                    Intent intentObj = new Intent(LoginActivity.this, CustomerDashBoardActivity.class);
                    startActivity(intentObj);
                }
                //  Toast.makeText(getApplicationContext(),"online",Toast.LENGTH_LONG).show();
            }
        }
    }
    /**
     * The interface method implemented in the java files
     * @param errorMessage the error message to be displayed
     * @return none
     */
    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopLoader();
        // utilObj.showError(this, errorMessage, textViewObj, null);
        utilObj.showToast(this, errorMessage, 0);

    }

    /**
     * The interface method implemented in the java files
     *
     * @param response is the response received from the Facebook
     */
    @Override
    public void onFacebookTaskCompleted(Response response) {
        String fbName="";
        String fbEmail="";
        try {
            JSONObject fbJsonObj = response.getGraphObject().getInnerJSONObject();
            fbName = fbJsonObj.get(Constants.FACEBOOK.NAME).toString();
            fbEmail = fbJsonObj.get(Constants.FACEBOOK.EMAIL).toString();
            login(fbEmail,"","1");
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        //write your code here
        //utilObj.startLoader(LoginActivity.this, R.drawable.image_for_rotation);
        //  userObj.email = email;
        // userObj.password = password;
        // loginManagerObj.authenticateLogin(userObj);
        Log.e("fbName", fbName);
        // Toast.makeText(this, "fbEmail::"+fbEmail, Toast.LENGTH_SHORT).show();
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
            // utilObj.startLoader(LoginActivity.this, R.drawable.image_for_rotation);
            GoogleSignInAccount acct = result.getSignInAccount();
            String usernameEmail = acct.getEmail()+"email: "+acct.getDisplayName();
            email = usernameEmail;
            userObj.email = usernameEmail;
            userObj.password = password;
            login(usernameEmail,"","1");
           /* loginManagerObj.authenticateLogin(userObj);*/
            Log.d(TAG, "Google usernameEmail:" + usernameEmail);
            // Toast.makeText(getApplicationContext(),"Google username"+usernameEmail, Toast.LENGTH_LONG).show();
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

    //**********For calling login webservice********

    public void login(String useremail,String userpass,String loginType)
    {
        utilObj.startLoader(LoginActivity.this, R.drawable.image_for_rotation);
        email = useremail;
        password = userpass;
        userObj.email = email;
        userObj.password = password;
        userObj.utype = usertype;
        userObj.username = useremail;
        userObj.logintype = loginType;
        loginManagerObj.authenticateLogin(userObj);
    }
}
