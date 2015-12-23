package com.smartdatainc.ui.fragments;

import android.app.Fragment;
import android.content.Context;

import com.smartdatainc.app.ApplicationSettings;
import com.smartdatainc.app.ooVooSdkSampleShowApp;

import sdei.support.lib.communication.HttpUtility;


public class BaseFragment extends Fragment {
	
	private BaseFragment back_fragment = null ;
	
	public void setKeepScreenOn(boolean state){
		if(getView() != null)
			getView().setKeepScreenOn(state);
	}

	ooVooSdkSampleShowApp app(){
		return ((ooVooSdkSampleShowApp) getActivity().getApplication()) ;
	}

	public ApplicationSettings settings() {
		return app().getSettings();
	}
	
	public BaseFragment getBackFragment() {
		return back_fragment;
	}

	public void setBackFragment(BaseFragment back_fragment) {
		this.back_fragment = back_fragment;
	}

	public boolean onBackPressed() {
	   return true ;
    }
	public boolean isNetworkAvailable(Context context) {
		HttpUtility httpUtilObj = new HttpUtility(context);
		return httpUtilObj.isNetworkAvailable();
	}
}
