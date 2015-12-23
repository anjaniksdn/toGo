package com.smartdatainc.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Response;
import com.facebook.Session;
import com.smartdatainc.activities.DashboardActivity;
import com.smartdatainc.activities.ForgotPasswordActivity;
import com.smartdatainc.activities.SignUp;
import com.smartdatainc.dataobject.User;
import com.smartdatainc.helpers.FacebookSSO;
import com.smartdatainc.interfaces.FacebookTaskCompleted;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.LoginManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;


public class LoginFragment extends BaseFragment implements ServiceRedirection, FacebookTaskCompleted {

	private String email;
	private String password;
	private EditText emailObj;



	private EditText passwordObj;
	private Button btnLoginObj;
	private Button btnSignUpObj;
	private TextView textViewObj;
	private TextView mtextForgtPass;
	private Utility utilObj;
	private String message;
	private User userObj;
	private LoginManager loginManagerObj;

	private LinearLayout fb_login_llObj;
	FacebookSSO facebookssoObj;
	FacebookTaskCompleted onFacebookTaskCompletedObj;




	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.login);

		facebookssoObj = new FacebookSSO(getActivity(), this);
		facebookssoObj.createFacebookSession(savedInstanceState);

		//initData();


	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.login, container, false);

		emailObj = (EditText) view.findViewById(R.id.email);
		passwordObj = (EditText) view.findViewById(R.id.password);
		btnLoginObj = (Button) view.findViewById(R.id.btnSignIn);
		btnSignUpObj = (Button) view.findViewById(R.id.btnSignup);
		textViewObj = (TextView) view.findViewById(R.id.errorMessage);
		mtextForgtPass = (TextView) view.findViewById(R.id.forgotPassword);
		utilObj = new Utility(getActivity());
		userObj = new User();
		loginManagerObj = new LoginManager(getActivity(), this);
		fb_login_llObj = (LinearLayout) view.findViewById(R.id.fb_login_ll);
		bindControls();
		return view;
	}

	/**
	 * Default method of activity life cycle to handle the actions required once the activity starts
	 * checks if the network is available or not
	 * @return none
	 */

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		if(!isNetworkAvailable(getActivity())) {
			utilObj.showAlertDialog(getActivity(),this.getResources().getString(R.string.network_service_message_title),this.getResources().getString(R.string.network_service_message), this.getResources().getString(R.string.Ok), null, Constants.ButtonTags.TAG_NETWORK_SERVICE_ENABLE, 0);
		}


		try {

			// Session.getActiveSession().addCallback(statusCallback);
			facebookssoObj.addActiveSessionCallback();

		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);

	}

	/**
	 * Initializes the objects
	 * @return none
	 */
/*	@Override
	public void initData() {
		emailObj = (EditText) findViewById(R.id.email);
		passwordObj = (EditText) findViewById(R.id.password);
		btnLoginObj = (Button) findViewById(R.id.btnSignIn);
		btnSignUpObj = (Button) findViewById(R.id.btnSignup);
		textViewObj = (TextView) findViewById(R.id.errorMessage);
		mtextForgtPass = (TextView) findViewById(R.id.forgotPassword);
		utilObj = new Utility(this);
		userObj = new User();
		loginManagerObj = new LoginManager(getActivity(), this);
		fb_login_llObj = (LinearLayout) findViewById(R.id.fb_login_ll);


	}*/

	/**
	 * Binds the UI controls
	 * @return none
	 */

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
						Intent intentObj = new Intent(getActivity().getApplicationContext(), DashboardActivity.class);
						startActivity(intentObj);
						Toast.makeText(getActivity(), "online", Toast.LENGTH_LONG).show();
					}
                   /* utilObj.startLoader(LoginActivity.this, R.drawable.image_for_rotation);

                    //assigning the data to the user object
                    userObj.email = email;
                    userObj.password = password;
                    loginManagerObj.authenticateLogin(userObj);*/
				}

			}
		});

		//SignUp
		btnSignUpObj.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentObj = new Intent(getActivity(), SignUp.class);
				startActivity(intentObj);
			}
		});


		//facebook login
		fb_login_llObj.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				facebookssoObj.startFacebookLogin(getActivity());
			}
		});


		//ForgotPassword
		mtextForgtPass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentObj = new Intent(getActivity(), ForgotPasswordActivity.class);
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
			utilObj.showError(getActivity(), message, textViewObj, emailObj);
		}
       /* else if(!utilObj.checkEmail(email)) {
            message = getResources().getString(R.string.invalid_email);
            utilObj.showError(this, message, textViewObj, emailObj);
        }*/
		else if(password.isEmpty()) {
			message = getResources().getString(R.string.PasswordErrorMessage);
			utilObj.showError(getActivity(), message, textViewObj, passwordObj);
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

		Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);

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
			Intent intentObj = new Intent(getActivity(), DashboardActivity.class);
			startActivity(intentObj);
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
		utilObj.showError(getActivity(), errorMessage, textViewObj, null);

	}

	/**
	 * The interface method implemented in the java files
	 *
	 * @param response is the response received from the Facebook
	 */
	@Override
	public void onFacebookTaskCompleted(Response response) {
		//write your code here
		Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
	}

	public boolean onBackPressed()
	{
		this.getActivity().finish();
		return false ;
	}
}
