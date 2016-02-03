package com.smartdatainc.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.smartdatainc.interfaces.CallBack;
import com.smartdatainc.utils.Utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import sdei.support.lib.communication.HttpUtility;

/**
 * Created by Anurag Sethi on 08-04-2015.
 * The class will handle the asynctask for the webservice call
 */
public class GetWebServiceData extends AsyncTask <String, Void, String>{

    boolean showLoader = false;
    String result = "";
    String url;
    String postData;
    Context context;
    CallBack callbackObj;
    HttpUtility httpUtilityObj;
    int tasksID;
    String Authentication="12345";
    String resultData;
    Utility utilObj;

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    /**
     * Constructor
     * @param contextObj The Context from where the method is called
     * @param Url Web Service URL to be called
     * @param listnerObj object of interface CallBack
     * @param jsonString the JSON object of posting data to the web service
     * @param tasksID the ID to differential multiple webservice calls
     * @return none
     */

    public GetWebServiceData(Context contextObj, String Url, CallBack listnerObj, String jsonString, int tasksID) {
        this.context = contextObj;
        this.postData = jsonString;
        this.url = Url;
        this.callbackObj = listnerObj;
        this.tasksID = tasksID;
        this.httpUtilityObj = new HttpUtility(contextObj);
        utilObj = new Utility(context);
    }
    /**
     * The method executes before the background processing starts and
     *  shows the loader in case its visibility is set as true
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
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(String... params) {
        try{


            String Authentication = utilObj.readDataInSharedPreferences("Users",0, "_token");
            if(Authentication == null)
            {
                Authentication="12345";
            }
            if(Authentication.length()== 0)
            {
                Authentication="12345";
            }
            result = httpUtilityObj.getPostResults(this.url, this.postData,Authentication);

            //result = getPostResults(this.url, this.postData,Authentication);
        }
        catch(Exception e) {
            utilObj.stopLoader();
            Log.e("exception from web", e.toString());
            result = "";
        }
        return result;
    }

    /**
     * The method executes after the background processing ends
     * @param result the parsed string output from the provided URL
     * @return none
     */

    @Override
    protected void onPostExecute(String result) {
        callbackObj.onResult(result, tasksID);
    }



  /*  public String getPostResults(String url, String jsonToPost, String authorizeToken) {
        //if(this.isNetworkAvailable()) {
            InputStream mInputStreamis = null;

            try {
                DefaultHttpClient e = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(e.getParams(), 5000);
                HttpConnectionParams.setSoTimeout(e.getParams(), 10000);
                HttpPost post = new HttpPost(url);
                if(!authorizeToken.isEmpty()) {
                    post.setHeader("Authorization", authorizeToken);
                }

                StringEntity se = new StringEntity(jsonToPost, "UTF-8");
                se.setContentType(new BasicHeader("Content-Type", "application/json"));
                post.setEntity(se);
                HttpResponse response = e.execute(post);
                if(response != null) {
                    mInputStreamis = response.getEntity().getContent();
                }
            } catch (SocketException var9) { result = httpUtilityObj.getPostResults(this.url, this.postData,Authentication);
                Log.e("Caught in socket", "Error  ===== " + var9.toString());
            } catch (Exception var10) {
                Log.e("Caught in exception", "Error  ===== " + var10.toString());
            }

            resultData = this.convertResponseToString(mInputStreamis);
       *//* } else {
            this.resultData = null;
        }*//*

        return resultData;
    }*/



    public String convertResponseToString(InputStream InputStream) {
        String mResult = "";

        try {
            BufferedReader e = new BufferedReader(new InputStreamReader(InputStream, "UTF8"), 8);
            StringBuilder mStringBuilder = new StringBuilder();
            mStringBuilder.append(e.readLine() + "\n");
            String line = "0";

            while((line = e.readLine()) != null) {
                mStringBuilder.append(line + "\n");
            }

            InputStream.close();
            mResult = mStringBuilder.toString();
            return mResult;
        } catch (Exception var6) {
            var6.printStackTrace();
            return mResult;
        }
    }


    /*public boolean isNetworkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)this.context.getSystemService("connectivity");
        if(connMgr == null) {
            return false;
        } else {
            NetworkInfo[] info = connMgr.getAllNetworkInfo();
            if(info != null) {
                for(int i = 0; i < info.length; ++i) {
                    if(info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }

            return false;
        }
    }*/



}
