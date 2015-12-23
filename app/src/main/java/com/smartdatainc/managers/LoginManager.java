package com.smartdatainc.managers;

import android.content.Context;

import com.smartdatainc.dataobject.User;
import com.smartdatainc.interfaces.CallBack;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.ResponseCodes;
import com.smartdatainc.utils.Utility;

/**
 * Created by Anurag Sethi
 * The class will handle all the implementations related to the login operations
 */
public class LoginManager implements CallBack{

    Context context;
    Utility utilObj;
    CommunicationManager commObj;
    ServiceRedirection serviceRedirectionObj;
    int tasksID;

    /**
     * Constructor
     * @param contextObj  The Context from where the method is called
     * @param successRedirectionListener The listener interface for receiving action events
     * @return none
     */
    public LoginManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }

    /**
     * Calls the Web Service of authenticateLogin
     * @param userObj  having the required data
     * @return none
     */
    public void authenticateLogin(User userObj) {
        String jsonString = utilObj.convertObjectToJson(userObj);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.LOGIN_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_USER_AUTHENTICATION, this, jsonString, tasksID);
    }

    /**
     * Calls the Web Service of authenticateLogin
     * @param userObj  having the required data
     * @return none
     */
    public void signUp(User userObj) {
        String jsonString = utilObj.convertObjectToJson(userObj);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.SIGNUP_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_USER_SIGNUP, this, jsonString, tasksID);
    }


    /**
     * Calls the Web Service of forgot password
     * @param userObj  having the required data
     * @return none
     */
    public void forgotPassword(User userObj) {
        String jsonString = utilObj.convertObjectToJson(userObj);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.FORGOT_PASSWORD_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_USER_FORGOTPASS, this, jsonString, tasksID);

    }

    /**
     * The interface method implemented in the java files
     *
     * @param data the result returned by the web service
     * @return none
     * @since 2014-08-28
     */
    @Override
    public void onResult(String data, int tasksID) {
        String errorMessage = "";  
        if(tasksID == Constants.TaskID.LOGIN_TASK_ID) {
            if(data != null) {

                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));

               //
                switch(messageID) {

                    case ResponseCodes.Success:
                        String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                        //AppInstance.userObj = (User) dataParserObj.parseDataForObject(jsonParsedData, "user");
                        //User userObj = (User) dataParserObj.parseDataForObject(jsonParsedData, "user");
                        //utilObj.saveDataInSharedPreferences("Users", context.MODE_PRIVATE, "_token", AppInstance.userObj._token);
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                        break;

                    case ResponseCodes.InvalidEmailPassword:
                        errorMessage =  dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);;
                        break;


                    case ResponseCodes.InvalidJsonFormat:
                        errorMessage = this.context.getResources().getString(R.string.invalid_json_format);
                        break;

                    case ResponseCodes.MissingEmail:
                        errorMessage = this.context.getResources().getString(R.string.missing_email);
                        break;

                    case ResponseCodes.MissingPassword:
                        errorMessage = this.context.getResources().getString(R.string.missing_password);
                        break;

                    case ResponseCodes.IncorrectEmailPassword:
                        errorMessage = this.context.getResources().getString(R.string.incorrect_email_password);
                        break;



                    case ResponseCodes.UnthorisedAccess:
                        errorMessage = this.context.getResources().getString(R.string.unauthorized_access);
                        break;

                    default:
                        errorMessage = context.getResources().getString(R.string.invalid_message);
                        break;
                }

                if(!errorMessage.isEmpty()) {
                   serviceRedirectionObj.onFailureRedirection(errorMessage);
                }

            }
            else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        }
        else if(tasksID == Constants.TaskID.FORGOT_PASSWORD_TASK_ID) {
            if(data != null) {

                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));

                switch(messageID) {

                    case ResponseCodes.Success:
                        String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                       // serviceRedirectionObj.onSuccessRedirection(tasksID,jsonParsedData);
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                        break;

                    case ResponseCodes.EmailNotFound:
                        errorMessage = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                        break;


                    case ResponseCodes.InvalidJsonFormat:
                        errorMessage = this.context.getResources().getString(R.string.invalid_json_format);
                        break;



                    case ResponseCodes.MissingEmail:
                        errorMessage = this.context.getResources().getString(R.string.missing_email);
                        break;

                    case ResponseCodes.MissingPassword:
                        errorMessage = this.context.getResources().getString(R.string.missing_password);
                        break;

                    case ResponseCodes.InvalidEmailPassword:
                        errorMessage = this.context.getResources().getString(R.string.invalid_email_password);
                        break;

                    case ResponseCodes.UnthorisedAccess:
                        errorMessage = this.context.getResources().getString(R.string.unauthorized_access);
                        break;

                    default:
                        errorMessage = context.getResources().getString(R.string.invalid_message);
                        break;

                }

                if(!errorMessage.isEmpty()) {
                    serviceRedirectionObj.onFailureRedirection(errorMessage);
                }

            }
            else {
               utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }

        }

        else if(tasksID == Constants.TaskID.SIGNUP_TASK_ID) {
            if(data != null) {

                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE_ID));

                //
                switch(messageID) {

                    case ResponseCodes.Success:
                        String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_RESULT);
                        //AppInstance.userObj = (User) dataParserObj.parseDataForObject(jsonParsedData, "user");
                        //utilObj.saveDataInSharedPreferences("Users", context.MODE_PRIVATE, "userID", Integer.toString(AppInstance.userObj.userID));
                        //User userObj = (User) dataParserObj.parseDataForObject(jsonParsedData, "user");
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                        break;

                    case ResponseCodes.InvalidJsonFormat:
                        errorMessage = this.context.getResources().getString(R.string.invalid_json_format);
                        break;

                    case ResponseCodes.MissingEmail:
                        errorMessage = this.context.getResources().getString(R.string.missing_email);
                        break;

                    case ResponseCodes.MissingPassword:
                        errorMessage = this.context.getResources().getString(R.string.missing_password);
                        break;

                    case ResponseCodes.IncorrectEmailPassword:
                        errorMessage = this.context.getResources().getString(R.string.incorrect_email_password);
                        break;

                    case ResponseCodes.InvalidEmailPassword:
                        errorMessage = this.context.getResources().getString(R.string.invalid_email_password);
                        break;

                    case ResponseCodes.UnthorisedAccess:
                        errorMessage = this.context.getResources().getString(R.string.unauthorized_access);
                        break;

                    default:
                        errorMessage = context.getResources().getString(R.string.invalid_message);
                        break;
                }

                if(!errorMessage.isEmpty()) {
                    serviceRedirectionObj.onFailureRedirection(errorMessage);
                }

            }
            else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        }

    }
}
