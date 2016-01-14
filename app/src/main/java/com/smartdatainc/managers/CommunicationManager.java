package com.smartdatainc.managers;

import android.content.Context;

import com.smartdatainc.async.DeleteMediaFile;
import com.smartdatainc.async.DownloadMediaFileUrls;
import com.smartdatainc.async.GetWebServiceData;
import com.smartdatainc.async.GetWebServiceGetData;
import com.smartdatainc.async.UploadMediaToServer;
import com.smartdatainc.interfaces.CallBack;

/**
 * Created by Anurag Sethi
 * This class will be responsible for the communication and will be used to call the webservice requests
 */
public class CommunicationManager {

    private Context context;

    /**
     * Constructor
     * @param contextObj  The Context from where the method is called
     * @return none
     */

    public CommunicationManager(Context contextObj) {
        this.context = contextObj;
    }

    /**
     * Call the required web service based on the URL
     * @param contextObj The Context from where the method is called
     * @param Url Web Service URL to be called
     * @param listnerObj object of interface CallBack
     * @param jsonString the JSON sting of posting data to the web service
     * @param tasksID the ID to differential multiple webservice calls
     * @return none
     */
    public void CallWebService(Context contextObj, String Url, CallBack listnerObj, String jsonString, int tasksID)
    {
        GetWebServiceData gwsdObj = new GetWebServiceData(contextObj, Url, listnerObj, jsonString, tasksID);
        gwsdObj.execute();
    }
    public void CallGetWebService(Context contextObj, String Url, CallBack listnerObj, String jsonString, int tasksID)
    {
        GetWebServiceGetData gwsdObj = new GetWebServiceGetData(contextObj, Url, listnerObj, jsonString, tasksID);
        gwsdObj.execute();
    }
    
     /**
     * Call the requried web service based on the URL for uploading media
     * @param contextObj The Context from where the method is called
     * @param Url Web Service URL to be called
     * @param filePath Path of the file which needs to be uploaded
     * @param listnerObj object of interface CallBack
     * @param tasksID the ID to differentiate multiple webservice calls
     * @return none
     */
    public void MediaUploadWebService(Context contextObj, String Url, String filePath, CallBack listnerObj, int tasksID) {

        UploadMediaToServer obj = new UploadMediaToServer(contextObj, Url, filePath, listnerObj, tasksID);
        obj.execute();
    }

    /**
     * Call the required web service based on the URL for deleting media
     * @param contextObj The context from where the method is called
     * @param Url Web Service URL to be called
     * @param jsonString the jsonData which needs to be posted on the server
     * @param listnerObj object of interface CallBack
     * @param tasksID the ID to differentiate multiple webservice calls
     * @return none
     */

    public void MediaDeleteWebService(Context contextObj, String Url, String jsonString, CallBack listnerObj, int tasksID) {
        DeleteMediaFile obj = new DeleteMediaFile(contextObj, Url, jsonString, listnerObj, tasksID);
        obj.execute();
    }

    /**
     * Call the required web service based on the URL for downloading the media
     * @param contextObj The context from where the method is called
     * @param Url web service URL to be called
     * @param jsonString the jsonData which needs to be posted on the server
     * @param listnerObj object of interface CallBack
     * @param tasksID the ID to differentiate multiple webservice calls
     * @return none
     */
    public void MediaDownloadWebService( Context contextObj, String Url, String jsonString, CallBack listnerObj, int tasksID) {
        DownloadMediaFileUrls obj = new DownloadMediaFileUrls(contextObj, Url, jsonString, listnerObj, tasksID);
        obj.execute();
    }
}
