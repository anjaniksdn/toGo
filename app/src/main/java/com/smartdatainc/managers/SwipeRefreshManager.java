package com.smartdatainc.managers;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.smartdatainc.dataobject.AppInstance;
import com.smartdatainc.dataobject.SwipeRefresh;
import com.smartdatainc.interfaces.CallBack;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.ResponseCodes;
import com.smartdatainc.utils.Utility;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Anurag Sethi on 29-06-2015.
 */
public class SwipeRefreshManager implements CallBack {


    private Context context;
    private ServiceRedirection serviceRedirectionObj;
    private Utility utilObj;
    private CommunicationManager communicationManagerObj;
    int tasksID;

    /**
     * Constructor
     * @param contextObj  The Context from where the method is called
     * @param successRedirectionListener The listener interface for receiving action events
     * @return none
     */
    public SwipeRefreshManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        serviceRedirectionObj = successRedirectionListener;
        utilObj = new Utility(contextObj);
    }

    /**
     * Calls the Web Service of fetching movie list
     * @param swipeRefreshObj - data object containing the offset value
     * @return none
     */
    public void fetchMovies(SwipeRefresh swipeRefreshObj) {
        String jsonString = utilObj.convertObjectToJson(swipeRefreshObj);
        AppInstance.logObj.printLog("jsonString=" + jsonString);
        communicationManagerObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.SWIPE_REFRESH_TASK_ID;
        communicationManagerObj.CallWebService(this.context, Constants.WebServices.WS_SWIPE_REFRESH_EXAMPLE_URL, this, jsonString, tasksID);
    }

    /**
     * The interface method implemented in the java files
     *
     * @param data    the result returned by the web service
     * @param tasksID the ID to differential multiple webservice calls
     * @return none
     */
    @Override
    public void onResult(String data, int tasksID) {
        String errorMessage = "";

        if(tasksID == Constants.TaskID.SWIPE_REFRESH_TASK_ID) {
            if(data != null) {
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE_ID));

                switch(messageID) {
                    case ResponseCodes.Success:
                        String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_RESULT);
                        /*AppInstance.swipeRefreshMovieObj = (SwipeRefresh) dataParserObj.parseDataForObject(jsonParsedData, "swipeRefresh");
                        serviceRedirectionObj.onSuccessRedirection(tasksID);*/

                        Type listType = new TypeToken<List<SwipeRefresh>>(){}.getType();
                        AppInstance.swipeRefreshListObj= (List<SwipeRefresh>) dataParserObj.parseJsonArray(jsonParsedData, listType);
                        serviceRedirectionObj.onSuccessRedirection(Constants.TaskID.SWIPE_REFRESH_TASK_ID);
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
