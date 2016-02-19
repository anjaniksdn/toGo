package com.smartdatainc.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartdatainc.toGo.R;

public class InterpreterSettingFragment extends BaseFragment implements  View.OnClickListener{
    private View mRootView;
    TextView changepassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        mRootView = inflater.inflate(R.layout.fragment_interprener_setting, container, false);
        changepassword = (TextView)mRootView.findViewById(R.id.changepassword);
        changepassword.setOnClickListener(this);

        return mRootView;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.changepassword:
                Fragment fragment = new ChangePasswordFragment();
                //Bundle args = new Bundle();
                //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
                //fragment.setArguments(args);

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.host_activity, fragment)
                        .commit();
                break;

        }
    }
}
