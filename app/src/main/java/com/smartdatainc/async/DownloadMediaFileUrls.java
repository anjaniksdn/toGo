package com.smartdatainc.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.smartdatainc.interfaces.CallBack;

import sdei.support.lib.communication.HttpUtility;

/**
 * Created by Anurag Sethi on 27-05-2015.
 */
public class DownloadMediaFileUrls extends AsyncTask<String,Void,String> {

    String result;
    Context context;
    String url;
    String jsonString;
    CallBack callbackObj;
    int tasksID;
    HttpUtility httpUtilityObj;

    public DownloadMediaFileUrls(Context contextObj, String Url, String jsonString, CallBack listnerObj, int tasksID) {

        this.context = contextObj;
        this.url = Url;
        this.jsonString = jsonString;
        this.callbackObj = listnerObj;
        this.tasksID = tasksID;
        httpUtilityObj = new HttpUtility(contextObj);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(String... params) {
        try{
            result = httpUtilityObj.getPostResults(this.url, this.jsonString,null);

        }
        catch(Exception e) {
            Log.e("exception from web", e.toString());
            result = "";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        callbackObj.onResult(result, tasksID);
    }


}
