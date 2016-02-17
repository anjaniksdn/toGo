package com.smartdatainc.managers;

import android.content.Context;
import android.util.Log;

import com.smartdatainc.dataobject.ChangePassword;
import com.smartdatainc.dataobject.InterpreterCallStatus;
import com.smartdatainc.dataobject.User;
import com.smartdatainc.interfaces.CallBack;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

/**
 * Created by Anjani Kumar
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
        Log.v("",jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.LOGIN_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_USER_AUTHENTICATION, this, jsonString, tasksID);
    }
    public void updateInterpreterCalls(InterpreterCallStatus interpreterCallStatus) {
        String jsonString = utilObj.convertObjectToJson(interpreterCallStatus);
        Log.v("",jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.INTERPRETER_CALL_STATUS_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_UPDATEINTERPRETERCALLS, this, jsonString, tasksID);
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
    public void changePasswprd(ChangePassword changePassword) {
        String jsonString = utilObj.convertObjectToJson(changePassword);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.CHANGE_PASSWORD;
        commObj.CallWebService(this.context, Constants.WebServices.WS_CHANGEPASSWORD, this, jsonString, tasksID);
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
        Log.v("Json response:::",data);
        if(tasksID == Constants.TaskID.LOGIN_TASK_ID) {
            if(data != null) {

                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if(messageID == 200) {

                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    String token = dataParserObj.parseJsonString(data,5);
                    String completion = dataParserObj.parseJsonString(data,6);
                    String uid = dataParserObj.parseJsonString(data,7);
                    String id = dataParserObj.parseJsonString(data,8);
                    String email = dataParserObj.parseJsonString(data,9);
                    Log.v("token", token);
                    Log.v("completion",completion);
                   // AppInstance.userObj = (User) dataParserObj.parseDataForObject(jsonParsedData, "user");
                    utilObj.saveDataInSharedPreferences("Users", context.MODE_PRIVATE, "_token", "Bearer "+token);
                    utilObj.saveDataInSharedPreferences("Users", context.MODE_PRIVATE, "completion",completion);
                    utilObj.saveDataInSharedPreferences("Users", context.MODE_PRIVATE, "uid",uid);
                    utilObj.saveDataInSharedPreferences("Users", context.MODE_PRIVATE, "id",id);
                    utilObj.saveDataInSharedPreferences("Users", context.MODE_PRIVATE, "email",email);

                    serviceRedirectionObj.onSuccessRedirection(tasksID, completion);
                }
                else
                {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }
                    Log.v("Json response:",data);
              //  int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));

               //
             /*   switch(messageID) {

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
                }*/

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
                if(messageID == 200) {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, jsonParsedData);
                }
                else
                {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }
               /* int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));

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
                }*/

            }
            else {
               utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }

        }

        else if(tasksID == Constants.TaskID.SIGNUP_TASK_ID) {
            if(data != null) {
                Log.v("Json response:",data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if(messageID == 200) {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, jsonParsedData);
                }
                else
                {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            }
            else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        }   else if(tasksID == Constants.TaskID.CHANGE_PASSWORD){
            if(data != null) {
                Log.v("Json response:",data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if(messageID == 200) {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, jsonParsedData);
                }
                else
                {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            }


        }
        else if(tasksID == Constants.TaskID.INTERPRETER_CALL_STATUS_TASK_ID){
            if(data != null) {
                Log.v("Json response:",data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if(messageID == 200) {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, jsonParsedData);
                }
                else
                {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            }
            else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }

        }
        else {
            utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
        }

    }
}
