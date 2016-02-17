package com.smartdatainc.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smartdatainc.toGo.R;

public class CustomerFeedbackFragment extends BaseFragment {
    private View mRootView;
    ListView customerfeedbackListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_customer_feedback, container, false);
        customerfeedbackListView = (ListView)mRootView.findViewById(R.id.customerfeedbackListView);
        return mRootView;
    }
}
