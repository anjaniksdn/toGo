package com.smartdatainc.managers;

import android.content.Context;
import android.util.Log;

import com.smartdatainc.dataobject.CallDeatils;
import com.smartdatainc.dataobject.CallEndDetails;
import com.smartdatainc.dataobject.CountryData;
import com.smartdatainc.dataobject.Customer;
import com.smartdatainc.dataobject.InterepreterdashBoard;
import com.smartdatainc.dataobject.Interpreter;
import com.smartdatainc.dataobject.InterprterDashBoardRequest;
import com.smartdatainc.dataobject.Upload;
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
public class UserProfileManager implements CallBack {

    Context context;
    Utility utilObj;

    CommunicationManager commObj;
    ServiceRedirection serviceRedirectionObj;
    int tasksID;

    /**
     * Constructor
     *
     * @param contextObj                 The Context from where the method is called
     * @param successRedirectionListener The listener interface for receiving action events
     * @return none
     */
    public UserProfileManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }

    /**
     * Calls the Web Service of authenticateLogin
     *
     * @param userObj having the required data
     * @return none
     */
    public void interpreterProfile(User userObj) {
        String jsonString = utilObj.convertObjectToJson(userObj);
        Log.v("interpreter request", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.USER_PROFILE_TASK_ID;
        commObj.CallGetWebService(this.context, Constants.WebServices.WS_USER_PROFILE, this, userObj.Authorization, tasksID);
    }
    public void interpreterDashBoard(InterprterDashBoardRequest interprterDashBoardRequest) {
        String jsonString = utilObj.convertObjectToJson(interprterDashBoardRequest);
        Log.v("interpreter dashboard", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_INTERPRETE_DASHBOARD_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GETDASHBOARDDATA, this, jsonString, tasksID);
    }
    public void chatDetails(InterprterDashBoardRequest interprterDashBoardRequest) {
        String jsonString = utilObj.convertObjectToJson(interprterDashBoardRequest);
        Log.v("interpreter dashboard", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_INTERPRETE_CHAT_HISTORY_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GETCHATDETAILS, this, jsonString, tasksID);
    }

    public void getCountryList(User userObj) {
        String jsonString = utilObj.convertObjectToJson(userObj);
        Log.v("interpreter request", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_COUNTRYLIST_TASK_ID;
        commObj.CallGetWebService(this.context, Constants.WebServices.WS_GETCOUNTRY, this, userObj.Authorization, tasksID);
    }

    public void getStateList(CountryData countryData) {

        String jsonString = utilObj.convertObjectToJson(countryData);
        Log.v("interpreter request", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_SATELIST_TASK_ID;
        String appendedurl = Constants.WebServices.WS_GETSTATE + "?country=" + countryData.country;
        commObj.CallGetWebService(this.context, appendedurl, this, countryData.Authorization, tasksID);
        //commObj.CallGetWebService(this.context, Constants.WebServices.WS_GETSTATE, this, countryData.Authorization, tasksID);
    }

    public void customerProfile(User userObj) {
        String jsonString = utilObj.convertObjectToJson(userObj);
        Log.v("customerProfile request", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.CUSTOMER_PROFILE_TASK_ID;
        commObj.CallGetWebService(this.context, Constants.WebServices.WS_CUSTOMER_PROFILE, this, userObj.Authorization, tasksID);
    }

    public void updateCustomerProfile(User userObj) {
        String jsonString = utilObj.convertObjectToJson(userObj);
        Log.v("customerProfile request", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.UPDATE_USERPROFILE_TASK_ID;
        commObj.CallGetWebService(this.context, Constants.WebServices.WS_UPDATE_USERPROFILE, this, userObj.Authorization, tasksID);
    }

    /**
     * Calls the Web Service of authenticateLogin
     *
     * @param interepreterdashBoard having the required data
     * @return none
     */
    public void updateAviblity(InterepreterdashBoard interepreterdashBoard) {
        String jsonString = utilObj.convertObjectToJson(interepreterdashBoard);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.UPDATE_AVAILAIBLITY_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_UPDATE_AGENT, this, jsonString, tasksID);
    }

    public void updateInterpreterProfile(Interpreter interpreterprofile) {
        String jsonString = utilObj.convertObjectToJson(interpreterprofile);
        Log.v("Interpreter profilejson", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.UPDATE_INTERPRETERPROFILE_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_UPDATE_INTERPRETERPROFILE, this, jsonString, tasksID);
    }

    public void updateCustomerProfile(Customer customerprofile) {
        String jsonString = utilObj.convertObjectToJson(customerprofile);
        Log.v("Customer profilejson", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.UPDATECUSTOMER_PROFILE_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_UPDATE_USERPROFILE, this, jsonString, tasksID);
    }

    public void updateProfileImage(Upload upload) {
        String jsonString = utilObj.convertObjectToJson(upload);
        Log.v("Interpreter imagejson", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.UPDATE_PROFILEIMAGE_TASK_ID;
        //commObj.CallWebService(this.context, Constants.WebServices.WS_UPDATE_PROFILEIMAGE, this, jsonString, tasksID);
        commObj.CallWebServiceFormdata(this.context, Constants.WebServices.WS_UPDATE_PROFILEIMAGE, this, jsonString, tasksID);
    }

    public void getLanguageList(User userObj) {
        String jsonString = utilObj.convertObjectToJson(userObj);
        Log.v("interpreter request", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_LANGUAGE_LIST_TASK_ID;
        commObj.CallGetWebService(this.context, Constants.WebServices.WS_GET_LANGUAGE_LIST, this, userObj.Authorization, tasksID);
    }
    public void getLanguageListForInterpretion(User userObj) {
        String jsonString = utilObj.convertObjectToJson(userObj);
        Log.v("interpreter request", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_LANGUAGE_LIST_INTERPRETETION_TASK_ID;
        commObj.CallGetWebService(this.context, Constants.WebServices.WS_GET_LANGUAGE_LIST, this, userObj.Authorization, tasksID);
    }

    /**
     * Calls the Web Service of forgot password
     *
     * @param userObj having the required data
     * @return none
     */
    public void forgotPassword(User userObj) {
        String jsonString = utilObj.convertObjectToJson(userObj);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.FORGOT_PASSWORD_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_USER_FORGOTPASS, this, jsonString, tasksID);

    }

    public void interpreterLanguageDetails(User userObj) {
        String jsonString = utilObj.convertObjectToJson(userObj);
        Log.v("interpreter request", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_INTERPRETATION_DETAILS_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_LANGUAGE_PRICE, this, jsonString, tasksID);
    }
    public void interpreterPoolDetails(User userObj) {
        String jsonString = utilObj.convertObjectToJson(userObj);
        Log.v("interpreter request", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_INTERPRETATION_POOL_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_LANGUAGE_POOL, this, jsonString, tasksID);
    }
    public void userCallDetail(CallDeatils callDeatils) {
        String jsonString = utilObj.convertObjectToJson(callDeatils);
        Log.v("interpreter request", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.SET_CALL_DETAILS_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_POST_CALL_DETAILS, this, jsonString, tasksID);
    }
    public void userCallCancel(CallDeatils callDeatils) {
        String jsonString = utilObj.convertObjectToJson(callDeatils);
        Log.v("interpreter request", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.SET_CALL_CANCEL_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_POST_CALL_DETAILS, this, jsonString, tasksID);
    }
    public void userCallDetailEnd(CallEndDetails callEndDeatils) {
        String jsonString = utilObj.convertObjectToJson(callEndDeatils);
        Log.v("interpreter request", jsonString);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.SET_CALL_CDR_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_POST_CALL_CDR, this, jsonString, tasksID);
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
        Log.v("Json response:::", data);
        if (tasksID == Constants.TaskID.USER_PROFILE_TASK_ID) {
            if (data != null) {

                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {

                    //String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    // AppInstance.userObj = (User) dataParserObj.parseDataForObject(jsonParsedData, "user");
                    //utilObj.saveDataInSharedPreferences("Users", context.MODE_PRIVATE, "_token", AppInstance.userObj._token);

                    serviceRedirectionObj.onSuccessRedirection(tasksID, data);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }
                Log.v("Json response:", data);

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        } else if (tasksID == Constants.TaskID.CUSTOMER_PROFILE_TASK_ID) {
            if (data != null) {

                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
//                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, data);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }


            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }

        } else if (tasksID == Constants.TaskID.UPDATE_INTERPRETERPROFILE_TASK_ID) {
            if (data != null) {

                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, jsonParsedData);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }


            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }

        } else if (tasksID == Constants.TaskID.UPDATECUSTOMER_PROFILE_TASK_ID) {
            if (data != null) {

                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, jsonParsedData);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }


            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }

        } else if (tasksID == Constants.TaskID.UPDATE_AVAILAIBLITY_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, jsonParsedData);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        } else if (tasksID == Constants.TaskID.UPDATE_PROFILEIMAGE_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, jsonParsedData);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        } else if (tasksID == Constants.TaskID.GET_COUNTRYLIST_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    // String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, data);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        } else if (tasksID == Constants.TaskID.GET_SATELIST_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    // String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, data);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        } else if (tasksID == Constants.TaskID.GET_LANGUAGE_LIST_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    // String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, data);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        } else if (tasksID == Constants.TaskID.GET_LANGUAGE_LIST_INTERPRETETION_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    // String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, data);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        } else if (tasksID == Constants.TaskID.GET_INTERPRETATION_DETAILS_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    // String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, data);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        } else if (tasksID == Constants.TaskID.GET_INTERPRETATION_POOL_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    // String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, data);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }


        } else if (tasksID == Constants.TaskID.SET_CALL_DETAILS_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, jsonParsedData);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        } else if (tasksID == Constants.TaskID.SET_CALL_CANCEL_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, jsonParsedData);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        } else if (tasksID == Constants.TaskID.SET_CALL_CDR_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, jsonParsedData);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        } else if (tasksID == Constants.TaskID.GET_INTERPRETE_DASHBOARD_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    //String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, data);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }
        }
        else if (tasksID == Constants.TaskID.GET_INTERPRETE_CHAT_HISTORY_TASK_ID) {
            if (data != null) {
                Log.v("Json response:", data);
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));
                if (messageID == 200) {
                    //String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onSuccessRedirection(tasksID, data);
                } else {
                    String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE);
                    serviceRedirectionObj.onFailureRedirection(jsonParsedData);
                }

            } else {
                utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
            }

        }
    }
}
