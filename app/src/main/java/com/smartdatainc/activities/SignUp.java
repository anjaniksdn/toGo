package com.smartdatainc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Session;
import com.smartdatainc.dataobject.User;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.LoginManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

/**
 * Created by Anurag Sethi on 08-04-2015.
 * The activity will handle the SignUp screens actions 
 */
public class SignUp extends AppActivity implements ServiceRedirection {
    private String uname;
    private String lname;
    private String password;
    private String cpassword;
    private String email;
    private String dob;
    private EditText unameObj;
   // private EditText lnameObj;
    private EditText emailObj;
    //private EditText dobObj;
    private EditText passwordObj;
    private EditText cpasswordObj;
    private  TextView textViewObj;
    private Button btnRegisterObj;
    private Utility utilObj;
    private String message;
    private User userObj;
    private LoginManager loginManagerObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        initData();
        bindControls();
    }

    @Override
    public void initData() {
        emailObj = (EditText) findViewById(R.id.email);
        passwordObj = (EditText) findViewById(R.id.password);
        unameObj=(EditText)findViewById(R.id.uname);
       // lnameObj=(EditText)findViewById(R.id.lname);
       // dobObj=(EditText)findViewById(R.id.dob);
        cpasswordObj=(EditText)findViewById(R.id.cpassword);

        btnRegisterObj = (Button) findViewById(R.id.btnRegister);
        textViewObj = (TextView) findViewById(R.id.errorMessage);
        utilObj = new Utility(this);
        userObj = new User();
        loginManagerObj = new LoginManager(this, this);



    }
    @Override
    public void bindControls() {

        //Login Button click
        btnRegisterObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uname=unameObj.getText().toString();
               // lname=lnameObj.getText().toString();
                password=passwordObj.getText().toString();
                cpassword=cpasswordObj.getText().toString();
             //   dob=dobObj.getText().toString();
                email = emailObj.getText().toString();


                if (validatingRequired()) {

                    utilObj.startLoader(SignUp.this, R.drawable.image_for_rotation);

                    //assigning the data to the user object
                   // userObj.firstName = fname;
                   // userObj.lastName = lname;
                    userObj.username = uname;
                    userObj.email= email;
                    userObj.password = password;
                   // userObj.cpassword=cpassword;
                   // userObj.dob=dob;
                    loginManagerObj.signUp(userObj);
                }

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
        else if(!utilObj.checkEmail(email)) {
            message = getResources().getString(R.string.invalid_email);
            utilObj.showError(this, message, textViewObj, emailObj);
        }
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

    }


    @Override
    public void onSuccessRedirection(int taskID,String messeage) {
        utilObj.stopLoader();

        if(taskID == Constants.TaskID.SIGNUP_TASK_ID) {
            //call the intent for the next activity
            utilObj.showToast(this, messeage, 0);
            Intent intentObj = new Intent(this, LoginActivity.class);
            startActivity(intentObj);
        }

    }
    public void onSuccessRedirection(int taskID) {

    }
    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopLoader();
        utilObj.showError(this, errorMessage, textViewObj, null);

    }
}
