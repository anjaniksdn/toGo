package com.smartdatainc.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smartdatainc.adapters.ChatDetailsAdapter;
import com.smartdatainc.dataobject.ChatDetails;
import com.smartdatainc.dataobject.InterprterDashBoardRequest;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.UserProfileManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatDetailsFragment extends BaseFragment implements ServiceRedirection {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ChatDetailsAdapter chatDetailsAdapter;
    private ListView feedbackdetails;
    private  UserProfileManager userProfileManager;
    private Utility utilObj;
    Context context;
    String type;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatDetailsFragment newInstance(String param1, String param2) {
        ChatDetailsFragment fragment = new ChatDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatDetailsFragment() {
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
        container.removeAllViews();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_details, container, false);
        feedbackdetails = (ListView)view.findViewById(R.id.feedbackdetails);
        userProfileManager = new UserProfileManager(getActivity(), this);
        utilObj = new Utility(getActivity());
        String id = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "id");
        type = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "usertype");
        if (isNetworkAvailable(getActivity())) {
            utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
            InterprterDashBoardRequest interprterDashBoardRequest = new InterprterDashBoardRequest();
            interprterDashBoardRequest.id =id;
            interprterDashBoardRequest.type = type;
            userProfileManager.chatDetails(interprterDashBoardRequest);
        } else {
            utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
        }
        context =  getActivity();
        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSuccessRedirection(int taskID) {

    }

    @Override
    public void onSuccessRedirection(int taskID, String jsonData) {
        if (utilObj != null) {
            utilObj.stopLoader();
        }
        if (taskID == Constants.TaskID.GET_INTERPRETE_CHAT_HISTORY_TASK_ID) {

            ArrayList<ChatDetails> chatDetailsList;
            try {
                chatDetailsList = new ArrayList<ChatDetails>();
                JSONObject jsonObjData = new JSONObject(jsonData);
                JSONArray cDRDatajsonArray = jsonObjData.optJSONArray("CDRData");
                for(int j=0;j < cDRDatajsonArray.length();j++)
                {
                    ChatDetails chatDetails = new ChatDetails();
                    JSONObject jsonObj = (JSONObject) cDRDatajsonArray.get(j);
                    int callDuration = jsonObj.optInt("duration");
                    String calldurationFinal = "";
                    int totalMinutes= callDuration/60;
                    long totalSecs = callDuration%60;
                    if(totalSecs > 1)
                    {
                        totalMinutes =  totalMinutes + 1;
                    }
                    if(totalMinutes == 1)
                    {
                        calldurationFinal = totalMinutes + "m";
                    }
                    else if(totalMinutes > 1)
                    {
                        calldurationFinal = totalMinutes + "ms";
                    }

                    JSONObject fromlanguageobj = jsonObj.optJSONObject("fromlanguage");
                    String fromlanguage = fromlanguageobj.optString("language");

                    JSONObject tolanguageObj = jsonObj.optJSONObject("tolanguage");
                    String tolanguage = tolanguageObj.optString("language");

                    //  JSONObject call_fromObj = jsonObj.optJSONObject("call_from");
                    JSONObject callObj;
                    if (type.equalsIgnoreCase("interpreter")){

                        callObj = jsonObj.optJSONObject("call_from");
                    }else
                    {
                        callObj = jsonObj.optJSONObject("call_to");
                    }

                    if(callObj!=null) {
                        JSONObject profile_imgObj = callObj.optJSONObject("profile_img");
                        if(profile_imgObj!=null) {
                            String url = profile_imgObj.optString("url");
                            chatDetails.setImageurl(url);
                        }
                        JSONObject nameObj = callObj.optJSONObject("name");
                        String first_name = nameObj.optString("first_name");
                        String last_name = nameObj.optString("last_name");
                        String nickname = callObj.optString("nickname");
                        chatDetails.setInterpreterNickname(nickname);
                    }
                    chatDetails.setCallDuration(""+calldurationFinal);
                    chatDetails.setFromLanguage(fromlanguage);
                    chatDetails.setToLanguage(tolanguage);
                    chatDetailsList.add(chatDetails);

                }

                chatDetailsAdapter =  new ChatDetailsAdapter(context,chatDetailsList);
                feedbackdetails.setAdapter(chatDetailsAdapter);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        if (utilObj != null) {
            utilObj.stopLoader();
        }
        utilObj.showToast(getActivity(),errorMessage,0);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
