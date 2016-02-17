package com.smartdatainc.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartdatainc.app.ooVooSdkSampleShowApp;
import com.smartdatainc.call.CNMessage;
import com.smartdatainc.dataobject.User;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.UserProfileManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.CircularImageView;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anjanikumar on 11/1/16.
 */

public class CustomerDashboardFragment extends BaseFragment implements ooVooSdkSampleShowApp.CallNegotiationListener, ServiceRedirection, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView managemyprofile, mOrderInterpretatoinTextView;
    private TextView mUserNameTextView, mNickNameTextView, mAboutUserTextView, mLanguageTextView, mEmailIDTextView;
    private Utility utilObj;
    UserProfileManager userProfileManager;
    private View mRootView;
    AQuery mAQuery;
    private CircularImageView mProfileImageView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InterprterDashBoardFragment.
     */
// TODO: Rename and change types and number of parameters
    public static CustomerDashboardFragment newInstance(String param1, String param2) {
        CustomerDashboardFragment fragment = new CustomerDashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CustomerDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the country_list_item for this fragment
        mRootView = inflater.inflate(R.layout.customer_dashboard_fragment, container, false);

        bindView();
        bindListener();
        utilObj = new Utility(getActivity());
        userProfileManager = new UserProfileManager(getActivity(), this);
        String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
        if (isNetworkAvailable(getActivity())) {
            utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
            User userObj = new User();
            userObj.Authorization = token;
            userProfileManager.customerProfile(userObj);
        } else {
            utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
        }
        return mRootView;
    }

    private void bindView() {
        managemyprofile = (TextView) mRootView.findViewById(R.id.managemyprofile);
        mProfileImageView = (CircularImageView) mRootView.findViewById(R.id.profileimageview);
        mUserNameTextView = (TextView) mRootView.findViewById(R.id.user_name_text_view);
        mNickNameTextView = (TextView) mRootView.findViewById(R.id.nick_name_text_view);
        mAboutUserTextView = (TextView) mRootView.findViewById(R.id.about_user_text_view);
        mLanguageTextView = (TextView) mRootView.findViewById(R.id.customer_language_text_view);
        mEmailIDTextView = (TextView) mRootView.findViewById(R.id.customer_email_text_view);
        mOrderInterpretatoinTextView = (TextView) mRootView.findViewById(R.id.order_interpretation_text_view);

    }

    public void bindListener() {
        managemyprofile.setOnClickListener(onClickListener);
        mOrderInterpretatoinTextView.setOnClickListener(this);

    }

    @Override
    public void onSuccessRedirection(int taskID) {
        if (utilObj != null) {
            utilObj.stopLoader();
        }

    }

    @Override
    public void onSuccessRedirection(int taskID, String jsonData) {
        if (utilObj != null) {
            utilObj.stopLoader();
        }
        //if (taskID == Constants.TaskID.USER_PROFILE_TASK_ID) {
        try {
            JSONObject jsonObj = new JSONObject(jsonData);
            JSONObject jsonObjUser = jsonObj.getJSONObject("user");
            String languageName = jsonObj.optString("languageName");
            JSONObject jsonObjName = jsonObjUser.optJSONObject("name");
            try {
                if (jsonObjName != null) {
                    mUserNameTextView.setText(jsonObjName.optString("first_name") + " " + jsonObjName.optString("last_name"));
                }
                mNickNameTextView.setText(jsonObjUser.optString("nickname"));
                mAboutUserTextView.setText(jsonObjUser.optString("about_user"));
                mLanguageTextView.setText(jsonObj.optString("languageName"));
                mEmailIDTextView.setText(jsonObjUser.optString("email"));
            }catch(Exception e)
            {
                e.printStackTrace();
            }

            JSONObject jsonObjProfileImage = jsonObjUser.optJSONObject("profile_img");
            mAQuery = new AQuery(getActivity());
            if(jsonObjProfileImage!=null) {
                mAQuery.id(R.id.profileimageview).image(jsonObjProfileImage.optString("url"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        if (utilObj != null) {
            utilObj.stopLoader();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_interpretation_text_view:
                Fragment fragment = new InterpretationFragment();
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.host_activity, fragment)
                        .commit();
                break;
        }
    }

// TODO: Rename method, update argument and hook method into UI event


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private boolean sendCNMessage(CNMessage.CNMessageType type, ooVooSdkSampleShowApp.MessageCompletionHandler completionHandler) {
       /* ArrayList<String> toList = new ArrayList<String>();
        for (int i = 0; i < callReceiverAdapter.getCount(); i++) {
            CallReceiverAdapter.CallReceiver receiver = (CallReceiverAdapter.CallReceiver) callReceiverAdapter.getItem(i);

            if (receiver.isCallEnabled() && toList.size() < MAX_CALL_RECEIVERS) {
                toList.add(receiver.getReceiverId());
            }
        }

        if (toList.isEmpty()) {
            return false;
        }

        app().sendCNMessage(toList, type, completionHandler);*/

        return true;
    }

    @Override
    public void onMessageReceived(CNMessage cnMessage) {
       /* if (app().getUniqueId().equals(cnMessage.getUniqueId())) {
            return;
        }

        if (cnMessage.getMessageType() == CNMessage.CNMessageType.AnswerAccept) {
            app().join(app().getConferenceId(), true);
        } else if (cnMessage.getMessageType() == CNMessage.CNMessageType.AnswerDecline) {
            count--;
            if (count <= 0) {
                callDialogBuilder.hide();
            }
        }  else if (cnMessage.getMessageType() == CNMessage.CNMessageType.Busy) {
            count--;
            if (count <= 0) {
                callDialogBuilder.hide();
            }
        }*/
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.managemyprofile:
                    Fragment fragment = new UpdateCustomerProfileFragment();
                    //Bundle args = new Bundle();
                    //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
                    //fragment.setArguments(args);

                    // Insert the fragment by replacing any existing fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.host_activity, fragment)
                            .commit();

                    //Do what you want for select_contact
                    break;

                default:
                    break;
            }


        }
    };
  /*  @Override
    public void onClick(View v) {

        *//*callDialogBuilder.hide();

        switch (v.getId()) {
            case R.id.cancel_button:
            {
                sendCNMessage(CNMessage.CNMessageType.Cancel, null);
            }
            break;

            default:
                break;
        }*//*
    }*/
}