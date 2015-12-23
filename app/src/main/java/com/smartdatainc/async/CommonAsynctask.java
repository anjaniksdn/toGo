package com.smartdatainc.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.smartdatainc.helpers.HTTPServiceHelper;
import com.smartdatainc.interfaces.AsynctaskCallBack;
import com.smartdatainc.utils.AlertDialogManager;
import com.smartdatainc.utils.Utility;

import org.apache.http.NameValuePair;

import java.util.List;

public class CommonAsynctask extends AsyncTask<String, Void, String> {

	private Context mContext;
	ProgressDialog progressDialog;
	private AsynctaskCallBack asynctaskCallBack;
	String responseKeyString;
	String jsonParamsString;
	List<NameValuePair> inputPairs;
	boolean isNetWorkAvailable = false;
	AlertDialogManager alertDialogManager;
	private String mErrorMessage;
	private String mErrorTitle;
	private boolean isHeader;
	int httpMethodType;

	public CommonAsynctask(AsynctaskCallBack asynctaskCallBack, Context context, String responseKey, List<NameValuePair> params, boolean isHeader, int method) {
		this.mContext = context;
		this.asynctaskCallBack = asynctaskCallBack;
		this.responseKeyString = responseKey;
		this.inputPairs = params;
		this.alertDialogManager = new AlertDialogManager();
		this.isHeader = isHeader;
		this.httpMethodType = method;
		this.jsonParamsString = jsonParamsString;
	}

	@Override
	protected String doInBackground(String... webServiceURL) {
		String jsonString = null;
		HTTPServiceHelper httpServiceHelper = new HTTPServiceHelper();

		if(Utility.isConnectingToInternet(mContext)){
			isNetWorkAvailable = true;
		// Making a request to url and getting response
			jsonString = httpServiceHelper.makeServiceCall(webServiceURL[0],
					httpMethodType, inputPairs, isHeader, mContext);

			Log.d("Response: ", "> " + jsonString);
		}else{
			isNetWorkAvailable = false;
		}

		return jsonString;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		if(isNetWorkAvailable == false){
			/*mErrorMessage = mContext.getResources().getString(
					R.string.network_not_available_msg);
			mErrorTitle = mContext.getResources().getString(
					R.string.network_not_available_title);*/
			alertDialogManager.showAlertDialog(mContext, mErrorTitle,
					mErrorMessage, false);
		}else {
			Bundle bundle = new Bundle();
			bundle.putString(responseKeyString, result);
			asynctaskCallBack.onTaskDone(bundle);
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage("Please wait...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

}
