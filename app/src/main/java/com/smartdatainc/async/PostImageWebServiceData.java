package com.smartdatainc.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.smartdatainc.dataobject.Upload;
import com.smartdatainc.interfaces.CallBack;
import com.smartdatainc.utils.Utility;

import sdei.support.lib.communication.HttpUtility;

/**
 * Created by anjanikumar on 19/1/16.
 */
public class PostImageWebServiceData extends AsyncTask<String, Void, String> {

    boolean showLoader = false;
    String result = "";
    String url;
    String postData;
    Context context;
    CallBack callbackObj;
    HttpUtility httpUtilityObj;
    int tasksID;
    String Authentication = "12345";
    String resultData;
    Upload upload;
    Utility utilObj;

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    /**
     * Constructor
     *
     * @param contextObj The Context from where the method is called
     * @param Url        Web Service URL to be called
     * @param listnerObj object of interface CallBack
     * @param jsonString the JSON object of posting data to the web service
     * @param tasksID    the ID to differential multiple webservice calls
     * @return none
     */

    public PostImageWebServiceData(Context contextObj, String Url, CallBack listnerObj, String jsonString, int tasksID) {
        this.context = contextObj;
        this.postData = jsonString;
        this.url = Url;
        this.callbackObj = listnerObj;
        this.tasksID = tasksID;
        // this.upload = upload;
        this.httpUtilityObj = new HttpUtility(contextObj);
        utilObj = new Utility(context);
    }

    /**
     * The method executes before the background processing starts and
     * shows the loader in case its visibility is set as true
     *
     * @return none
     */
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
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
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(String... params) {
        try {


            //result = httpUtilityObj.getPostResults(this.url, this.postData,Authentication);
            String Authentication = utilObj.readDataInSharedPreferences("Users", 0, "_token");
            if (Authentication == null) {
                Authentication = "12345";
            }
            if (Authentication.length() == 0) {
                Authentication = "12345";
            }
            result = httpUtilityObj.getPostResults(this.url, this.postData, Authentication);

            Log.v("interpreterProfile::", result);

        } catch (Exception e) {
            Log.e("exception from web", e.toString());
            result = "";
        }
        return result;
    }

    /**
     * The method executes after the background processing ends
     *
     * @param result the parsed string output from the provided URL
     * @return none
     */

    @Override
    protected void onPostExecute(String result) {
        callbackObj.onResult(result, tasksID);
    }
}
