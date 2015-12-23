package com.smartdatainc.managers;

import android.content.Context;

import com.smartdatainc.dataobject.Event;
import com.smartdatainc.interfaces.CallBack;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

/**
 * Created by ashutoshm on 15/9/15.
 */
public class EventManager implements CallBack {
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
    public EventManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }


    /**
     * Calls the Web Service of forgot password
     *
     * @param eventObj having the required data
     * @return none
     */
    public void publicEventList(Event eventObj) {
        String jsonString = utilObj.convertObjectToJson(eventObj);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.PUBLIC_EVENT_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_PUBLIC_EVENTLIST, this, jsonString, tasksID);

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

        if (tasksID == Constants.TaskID.PUBLIC_EVENT_TASK_ID) {
            if (data != null) {

                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_STATUS));


            }
        }
    }
}