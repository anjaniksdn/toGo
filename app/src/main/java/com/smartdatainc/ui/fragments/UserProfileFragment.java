package com.smartdatainc.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.smartdatainc.app.ooVooSdkSampleShowApp;
import com.smartdatainc.call.CNMessage;
import com.smartdatainc.dataobject.Language;
import com.smartdatainc.dataobject.User;
import com.smartdatainc.dataobject.UserProfile;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.UserProfileManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.CircularImageView;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends BaseFragment implements ServiceRedirection,View.OnClickListener, ooVooSdkSampleShowApp.CallNegotiationListener   {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    UserProfileManager userProfileManager;
    TextView profilename;
    ArrayList<Language> totallist=new ArrayList<>();
    private Utility utilObj;
    UserProfile userProfile;
    CircularImageView profileimageview;
    Button manageprofile;
    private ToggleButton avaiblitytoggle;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserProfileFragment() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        profilename = (TextView)view.findViewById(R.id.profilename);
        avaiblitytoggle =(ToggleButton)view.findViewById(R.id.avaiblitytoggle);

        manageprofile = (Button)view.findViewById(R.id.manageprofile);
        initdata();
        String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");

        utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        User userObj = new User();
        userObj.Authorization = token;
        userProfileManager.userProfile(userObj);
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event


public void initdata()
{
    utilObj = new Utility(getActivity());
    userProfileManager = new UserProfileManager(getActivity(), this);
}

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
    private boolean sendCNMessage(CNMessage.CNMessageType type, ooVooSdkSampleShowApp.MessageCompletionHandler completionHandler)
    {
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
    public void onMessageReceived(CNMessage cnMessage)
    {
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
    @Override
    public void onClick(View v) {

        //callDialogBuilder.hide();

        switch (v.getId()) {
            case R.id.cancel_button:
            {
                sendCNMessage(CNMessage.CNMessageType.Cancel, null);
            }
            break;
            case R.id.manageprofile:
                Fragment fragment = new UpdateUserProfile();
                //Bundle args = new Bundle();
                //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
                //fragment.setArguments(args);

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.host_activity, fragment)
                        .commit();

                break;
            case R.id.avaiblitytoggle:
                if(avaiblitytoggle.isChecked())
                {
                    String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
                    User userObj = new User();
                    userObj.Authorization = token;
                    userProfileManager.userProfile(userObj);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopLoader();

        if(taskID == Constants.TaskID.LOGIN_TASK_ID) {
            //call the intent for the next activity
            // User userobj = (User)userObj;
            // int userid =  userobj.userID;
          /*  Intent intentObj = new Intent(this, User.class);
            startActivity(intentObj);*/
        }
    }
    public void onSuccessRedirection(int taskID,String data) {
        if(utilObj!=null) {
            utilObj.stopLoader();
        }
        String interpreter_availability ="";
        //  String[] languageArray = new String[20];
        try {
            UserProfile userProfile = new UserProfile();
            JSONObject jsonObj = new JSONObject(data);
            JSONObject jsonobjUser = jsonObj.getJSONObject("user");
            String country = jsonobjUser.optString("email");
            String email = jsonobjUser.optString("email");
            String password = jsonobjUser.optString("password");
            String phone_number = jsonobjUser.optString("phone_number");
            String mylanguage = jsonobjUser.optString("mylanguage");
            String address = jsonobjUser.optString("address");
            String ein_taxId  = jsonobjUser.optString("ein_taxId");
            String nickname= jsonobjUser.optString("nickname");
            String useruid = jsonobjUser.optString("uid");
             interpreter_availability= jsonobjUser.optString("interpreter_availability");
            //String imageurl = jsonobj.getJSONObject("profile_img").getString("url");
            String first_name = jsonobjUser.getJSONObject("name").optString("first_name");
            String last_name = jsonobjUser.getJSONObject("name").optString("last_name");

            if(first_name!=null) {
                userProfile.setName(first_name + " " + last_name);
            }
            profilename.setText(userProfile.getName());
           // profilename.setText(userProfile.getName());
            // String country = jsonobj.getString("email");
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        if(interpreter_availability !=null)
        {
            if(interpreter_availability.equalsIgnoreCase("false"))
            {
                avaiblitytoggle.setChecked(false);
            }
            else
            {
                avaiblitytoggle.setChecked(true);
            }
        }

        if(taskID == Constants.TaskID.LOGIN_TASK_ID) {
            //  Toast.makeText(getApplicationContext(),jsonMesseage,Toast.LENGTH_LONG).show();
           /* app().login(email, email);
            if (app().isOnline()) {
                Intent intentObj = new Intent(getApplicationContext(), InterpreterDashboardActivity.class);
                startActivity(intentObj);
                //  Toast.makeText(getApplicationContext(),"online",Toast.LENGTH_LONG).show();
            }*/
        }
    }
    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopLoader();
        // utilObj.showError(this, errorMessage, textViewObj, null);
        utilObj.showToast(getActivity(), errorMessage, 0);

    }
}
