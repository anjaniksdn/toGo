package com.smartdatainc.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Session;
import com.smartdatainc.dataobject.User;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.LoginManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.PasswordValidator;
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
   // private RadioGroup selectusertype;
    private User userObj;
    private LoginManager loginManagerObj;
    PasswordValidator passwordValidator;
   // RadioButton radiointerpreter;
  //  RadioButton selectuser ;
    String usertype;
    Context contex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        setActionBar(Constants.APPHEADER);
        initData();
        //Show soft-keyboard:
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//hide keyboard :
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
     //   utilObj.hideVirtualKeyboard(SignUp.this);
        utilObj.hideKeyboard(SignUp.this);
        bindControls();
    }

    @Override
    public void initData() {
        emailObj = (EditText) findViewById(R.id.email);
        passwordObj = (EditText) findViewById(R.id.password);
        unameObj=(EditText)findViewById(R.id.uname);
      //  radiointerpreter = (RadioButton) findViewById(R.id.interpreterradio);
       // radiocustomer = (RadioButton) findViewById(R.id.userradio);
        //radiointerpreter.setChecked(true);
        //radiointerpreter.setOnCheckedChangeListener(this);
        //radiocustomer.setOnCheckedChangeListener(this);
        // lnameObj=(EditText)findViewById(R.id.lname);
        // dobObj=(EditText)findViewById(R.id.dob);
        cpasswordObj=(EditText)findViewById(R.id.cpassword);
       // selectusertype = (RadioGroup)findViewById(R.id.selectusertype);
        btnRegisterObj = (Button) findViewById(R.id.btnRegister);
        textViewObj = (TextView) findViewById(R.id.errorMessage);
        utilObj = new Utility(this);
        userObj = new User();
        loginManagerObj = new LoginManager(this, this);
        passwordValidator =  new PasswordValidator();
        Bundle intentBundle = getIntent().getBundleExtra("userbundle");
        //intentBundle.getString("usertype");
        usertype = intentBundle.getString("usertype");
        contex = this;

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
                    //int selectedId = selectusertype.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    //selectuser = (RadioButton) findViewById(selectedId);
                    //assigning the data to the user object
                    // userObj.firstName = fname;
                    // userObj.lastName = lname;
                    userObj.username = uname;
                    userObj.email= email;
                    userObj.password = password;
                    userObj.utype = usertype;
                 /*   if(selectuser.getText().toString().equalsIgnoreCase("Customer"))
                    {
                        userObj.utype = "user";
                    }
                    else
                    {
                        userObj.utype = "interpreter";
                    }*/
                   // userObj.utype = selectuser.getText().toString();
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
        uname = unameObj.getText().toString();
        boolean check =  utilObj.checkEmail(email);
        //validate the content
        if(email.isEmpty()) {
            message = getResources().getString(R.string.EmailErrorMessage);
            //utilObj.showError(this, message, textViewObj, emailObj);
            utilObj.showToast(this,message,0);
        }

        else if(!utilObj.checkEmail(email)) {
            message = getResources().getString(R.string.invalid_email);
            //utilObj.showError(this, message, textViewObj, emailObj);
            utilObj.showToast(this,message,0);
        }
        else if(uname.isEmpty()) {
            message = getResources().getString(R.string.usernameErrormesseage);
            //utilObj.showError(this, message, textViewObj, emailObj);
            utilObj.showToast(this,message,0);
        }
        else if(password.isEmpty()) {
            message = getResources().getString(R.string.PasswordErrorMessage);
            //utilObj.showError(this, message, textViewObj, passwordObj);
            utilObj.showToast(this,message,0);
        }
        else if(!password.isEmpty())
        {
            boolean validation = passwordValidator.validate(password);
            if(!validation)
            {
                message = getResources().getString(R.string.PasswordErrorMessage);
                //utilObj.showError(this, message, textViewObj, passwordObj);
                utilObj.showToast(this,message,0);
            }
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
        utilObj.showToast(this, errorMessage,0);
        //utilObj.showError(this, errorMessage, textViewObj, null);

    }
}
