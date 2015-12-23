package com.smartdatainc.helpers;

import android.content.Context;
import android.util.Base64;

import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class HTTPServiceHelper {

    static String response = null;
/*    public final static int GET = 1;
    public final static int POST = 2;*/


    public HTTPServiceHelper() {

    }

    /*
     * Making service call
     *
     * @url - url to make request
     *
     * @method - http request method
     */
    /*
	 * Making service call
	 * 
	 * @url - url to make request
	 * 
	 * @method - http request method
	 * 
	 * @params - http request params
	 */
    public String makeServiceCall(String url, int method, List<NameValuePair> params, boolean isHeader, Context context) {
        try {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams
                    .setConnectionTimeout(httpParameters, 35000);
            HttpConnectionParams.setSoTimeout(httpParameters, 35000);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == Constants.POST) {

                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                if(isHeader){
                    Utility utility = new Utility(context);
                    String token = utility.readDataInSharedPreferences(Constants.PREFS_NAME,0,Constants.USER_EMAIL);
                    String email = utility.readDataInSharedPreferences(Constants.PREFS_NAME,0,Constants.TOKEN);
                    String authStr = email+":"+token;

                    String base64EncodedCredentials = Base64.encodeToString(authStr.getBytes(), Base64.NO_WRAP);
                    httpPost.addHeader("Authorization", "Basic " + base64EncodedCredentials);
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == Constants.GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                //httpGet.addHeader("Content-type", "application/json");
                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 9. return result
        return response;

    }

   /* private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }*/
}
