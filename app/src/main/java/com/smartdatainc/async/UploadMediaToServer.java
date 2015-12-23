package com.smartdatainc.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.smartdatainc.dataobject.AppInstance;
import com.smartdatainc.interfaces.CallBack;

import sdei.support.lib.communication.HttpUtility;

/**
 * Created by Anurag Sethi on 12-05-2015.
 */
public class UploadMediaToServer extends AsyncTask<String, Void, String> {

    String result = "";
    String filePath;
    Context contextObj;
    String url,Authentication;
    HttpUtility httpUtilityObj;
    CallBack callbackObj;
    int tasksID;


    public UploadMediaToServer(Context context, String url, String filePath, CallBack listnerObj, int taskID) {
        this.contextObj = context;
        this.url = url;
        this.filePath = filePath;
        this.callbackObj = listnerObj;
        this.tasksID = taskID;
        httpUtilityObj = new HttpUtility(context);
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
            result = httpUtilityObj.uploadMediaToServer(this.url, this.filePath, null);

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
