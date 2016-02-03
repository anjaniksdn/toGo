package com.smartdatainc.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartdatainc.dataobject.User;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.LoginManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.AlertDialogManager;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

/**
 * Created by ashutoshm on 14/9/15.
 */
public class ForgotPasswordActivity extends AppActivity implements ServiceRedirection {
    private String email;
    private Button btnForgotPasswordObj;
    private EditText emailObj;
    private TextView textViewObj;
    private Utility utilObj;
    private User userObj;
    private String message;
    private LoginManager loginManagerObj;
    private AlertDialogManager alertDialogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        setActionBar(Constants.APPHEADER);
        initData();
        bindControls();

    }

    /**
     * Initializes the objects
     *
     * @return none
     */
    @Override
    public void initData() {
        alertDialogManager = new AlertDialogManager();
        emailObj = (EditText) findViewById(R.id.email);
        btnForgotPasswordObj = (Button) findViewById(R.id.btnSubmit);
        textViewObj = (TextView) findViewById(R.id.errorMessage);

        utilObj = new Utility(this);
        userObj = new User();
        loginManagerObj = new LoginManager(this, this);


    }


    /**
     * Binds the UI controls
     *
     * @return none
     */
    @Override
    public void bindControls() {

        //Login Button click
        btnForgotPasswordObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailObj.getText().toString();


                if (validatingRequired()) {

                    utilObj.startLoader(ForgotPasswordActivity.this, R.drawable.image_for_rotation);

                    //assigning the data to the user object
                    userObj.email = email;
                    loginManagerObj.forgotPassword(userObj);
                }

            }
        });


    }


    /**
     * The method is used to validate the required fields
     *
     * @return boolean true if fields are validated else false
     */

    private boolean validatingRequired() {
        message = "";
        email = emailObj.getText().toString();


        //validate the content
        if (email.isEmpty()) {
            message = getResources().getString(R.string.EmailErrorMessage);

            utilObj.showToast(this, message, 0);
           // alertDialogManager.showAlertDialog(ForgotPasswordActivity.this, getResources().getString(R.string.forgot_password), message, false);
            //  utilObj.showError(this, message, textViewObj, emailObj);
        } else if (!utilObj.checkEmail(email)) {
            message = getResources().getString(R.string.invalid_email);
            utilObj.showToast(this, message, 0);
            //alertDialogManager.showAlertDialog(ForgotPasswordActivity.this, getResources().getString(R.string.forgot_password), message, false);
            //  utilObj.showError(this, message, textViewObj, emailObj);
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * The method handles the result from the Facebook
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


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
        if (taskID == Constants.TaskID.FORGOT_PASSWORD_TASK_ID) {
            textViewObj.setVisibility(View.GONE);
            // Toast.makeText(this, "Thanks, Please check your mail for new password.", Toast.LENGTH_SHORT).show();
            //alertDialogManager.showAlertDialog(ForgotPasswordActivity.this, getResources().getString(R.string.forgot_password), "Thanks, Please check your mail for new password.", false);
            confirmationDialog();
        }
        // Toast.makeText(this, errorMessage.toString(), Toast.LENGTH_SHORT).show();

    }

    /**
     * The interface method implemented in the java files
     *
     * @param errorMessage the error message to be displayed
     * @return none
     */
    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopLoader();
        // utilObj.showError(this, errorMessage, textViewObj, null);
        textViewObj.setVisibility(View.GONE);
        Toast.makeText(this, errorMessage.toString(), Toast.LENGTH_SHORT).show();
    }

    public void onSuccessRedirection(int taskID, Object parseString) {
        utilObj.stopLoader();
        if (taskID == Constants.TaskID.FORGOT_PASSWORD_TASK_ID) {

            textViewObj.setVisibility(View.GONE);
            //Toast.makeText(this, "Thanks, Please check your mail for new password.", Toast.LENGTH_SHORT).show();
            confirmationDialog();

        }
    }

    public void onSuccessRedirection(int taskID, String jsonMesseage) {

    }

    /**
     * confirmation dialog to check registered email.
     */
    private void confirmationDialog() {
        final Dialog dialog = new Dialog(ForgotPasswordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert_dialog);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        TextView confirmTextView = (TextView) dialog.findViewById(R.id.confirm_text_view);
        TextView cancelTextView = (TextView) dialog.findViewById(R.id.cancel_text_view);
        TextView titleTextView = (TextView) dialog.findViewById(R.id.title_text_view);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.message_text_view);

        titleTextView.setText(getResources().getString(R.string.forgot_password_title));
        messageTextView.setText(getResources().getString(R.string.forgot_password_message));
        EditText passwordEditText = (EditText) dialog.findViewById(R.id.password_edit_text);
        passwordEditText.setVisibility(View.GONE);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
