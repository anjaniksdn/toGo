package com.smartdatainc.managers;

import android.content.Context;

import com.smartdatainc.dataobject.AppInstance;
import com.smartdatainc.dataobject.Media;
import com.smartdatainc.dataobject.MediaGallery;
import com.smartdatainc.interfaces.CallBack;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.ResponseCodes;
import com.smartdatainc.utils.Utility;
import java.util.ArrayList;

/**
 * Created by Anurag Sethi on 13-05-2015.
 */
public class MediaManager implements CallBack {


    Context contextObj;
    Utility utilObj;
    CommunicationManager commObj;
    ServiceRedirection serviceRedirectionObj;
    int tasksID;

    /**
     * Constructor
     * @param context  The Context from where the method is called
     * @param successRedirectionListener The listener interface for receiving action events
     * @return none
     */
    public MediaManager(Context context, ServiceRedirection successRedirectionListener) {

        contextObj = context;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }

    /**
     * The method will upload the media file to server
     * @param filePath path of the media file to be uploaded
     */
    public void uploadMediaFile(String filePath) {

        commObj = new CommunicationManager(contextObj);
        tasksID = Constants.TaskID.MEDIA_FILE_UPLOAD_TASK_ID;
        commObj.MediaUploadWebService(contextObj, Constants.WebServices.WS_MEDIA_UPLOAD_TO_SERVER, filePath, this, tasksID);
    }

    /**
     * The method will delete the media file from the server
     * @param mediaObj mediaObject
     */
    public void deleteMedia(Media mediaObj) {
        String jsonString = utilObj.convertObjectToJson(mediaObj);
        commObj = new CommunicationManager(contextObj);
        tasksID = Constants.TaskID.MEDIA_FILE_DELETE_TASK_ID;
        commObj.MediaDeleteWebService(contextObj, Constants.WebServices.WS_MEDIA_DELETE_FROM_SERVER, jsonString, this, tasksID);
    }

    /**
     * The method will download all the saved media files on the server
     */
    public void downloadMediaFileUrls(MediaGallery mediaGalleryObj) {
        String jsonString = utilObj.convertObjectToJson(mediaGalleryObj);
        AppInstance.logObj.printLog("jsonString=" + jsonString);

        commObj = new CommunicationManager(contextObj);
        tasksID = Constants.TaskID.MEDIA_FILE_DOWNLOAD_URL_TASK_ID;
        commObj.MediaDownloadWebService(contextObj, Constants.WebServices.WS_MEDIA_DOWNLOAD_FROM_SERVER, jsonString, this, tasksID);
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
        if (tasksID == Constants.TaskID.MEDIA_FILE_UPLOAD_TASK_ID) {
            if (data != null) {
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE_ID));

                switch (messageID) {

                    case ResponseCodes.Success:
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                        break;

                    case ResponseCodes.NoDataFound:
                        errorMessage = contextObj.getResources().getString(R.string.no_data_received);
                        break;

                    case ResponseCodes.IncorrectFileFormat:
                        errorMessage = contextObj.getResources().getString(R.string.incorrect_file_format);
                        break;
                    default:
                        errorMessage = contextObj.getResources().getString(R.string.invalid_message);
                        break;

                }

                if(!errorMessage.isEmpty()) {
                    serviceRedirectionObj.onFailureRedirection(errorMessage);
                }

            }
        }
        else if (tasksID == Constants.TaskID.MEDIA_FILE_DELETE_TASK_ID) {
            if (data != null) {
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE_ID));

                switch (messageID) {
                    case ResponseCodes.Success:
                        String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_RESULT);
                        AppInstance.mediaObj = (Media) dataParserObj.parseDataForObject(jsonParsedData, "media");
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                        break;

                    case ResponseCodes.NoDataFound:
                        errorMessage = contextObj.getResources().getString(R.string.no_data_received);
                        break;

                    case ResponseCodes.FileDoesNotExists:
                        errorMessage = contextObj.getResources().getString(R.string.file_does_not_exists);
                        break;
                    default:
                        errorMessage = contextObj.getResources().getString(R.string.invalid_message);
                        break;
                }

                if(!errorMessage.isEmpty()) {
                    serviceRedirectionObj.onFailureRedirection(errorMessage);
                }

            }

        }
        else if(tasksID == Constants.TaskID.MEDIA_FILE_DOWNLOAD_URL_TASK_ID) {
            if (data != null) {
                //parse your json data here
                DataParser dataParserObj = new DataParser();
                int messageID = Integer.parseInt(dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_MESSAGE_ID));

                switch (messageID) {
                    case ResponseCodes.Success:
                        String jsonParsedData = dataParserObj.parseJsonString(data, Constants.JsonParsing.PARSING_JSON_FOR_RESULT);
                        AppInstance.mediaGalleryObj.arrayListMedia = (ArrayList<Media>)dataParserObj.parseDataForObject(jsonParsedData, "mediaUrl");
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                        break;

                    case ResponseCodes.NoDataFound:
                        errorMessage = contextObj.getResources().getString(R.string.no_data_received);
                        break;

                    default:
                        errorMessage = contextObj.getResources().getString(R.string.invalid_message);
                        break;
                }

                if(!errorMessage.isEmpty()) {
                    serviceRedirectionObj.onFailureRedirection(errorMessage);
                }

            }
        }
    }

}
