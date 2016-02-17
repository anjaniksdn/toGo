package com.smartdatainc.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smartdatainc.dataobject.ChangePassword;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.LoginManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.PasswordValidator;
import com.smartdatainc.utils.Utility;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain getActivity() fragment must implement the
 * {@link ChangePasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of getActivity() fragment.
 */
public class ChangePasswordFragment extends BaseFragment implements ServiceRedirection {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String newpassword;
    private String lname;
    private String password;
    private String cpassword;
    private String email;
    private String dob;
    private EditText newpasswordObj;
    // private EditText lnameObj;
    // private EditText emailObj;
    //private EditText dobObj;
    private EditText passwordObj;
    private EditText cpasswordObj;
    private TextView textViewObj;
    private Button btnChnagepassword;
    private Utility utilObj;
    private String message;
    // private RadioGroup selectusertype;
    private ChangePassword changePassword;
    private LoginManager loginManagerObj;
    PasswordValidator passwordValidator;
    // RadioButton radiointerpreter;
    //  RadioButton selectuser ;
    String usertype;
    Context contex;

    private OnFragmentInteractionListener mListener;

    /**
     * Use getActivity() factory method to create a new instance of
     * getActivity() fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */

    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChangePasswordFragment() {
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
        // Inflate the layout for getActivity() fragment

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        utilObj = new Utility(getActivity());

        String id = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "id");
        String email = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "email");
        String usertype = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "usertype");
        changePassword = new ChangePassword();
        contex= getActivity();
        passwordValidator = new PasswordValidator();
        loginManagerObj = new LoginManager(contex,this);
        passwordObj = (EditText) view.findViewById(R.id.password);
        newpasswordObj = (EditText) view.findViewById(R.id.newpassword);
        cpasswordObj = (EditText) view.findViewById(R.id.cpassword);
        btnChnagepassword = (Button) view.findViewById(R.id.btnChnagepassword);
        textViewObj = (TextView) view.findViewById(R.id.errorMessage);
        bindControls();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void bindControls() {

        //Login Button click
        btnChnagepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newpassword = newpasswordObj.getText().toString();
                // lname=lnameObj.getText().toString();
                password = passwordObj.getText().toString();
                cpassword = cpasswordObj.getText().toString();
                //   dob=dobObj.getText().toString();


                if (validatingRequired()) {

                    utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                    changePassword.newpassword = newpassword;
                    changePassword.email = email;
                    changePassword.currentpassword = password;
                    changePassword.type = usertype;
                    loginManagerObj.changePasswprd(changePassword);
                }

            }
        });
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
        if (taskID == Constants.TaskID.CHANGE_PASSWORD) {
            try {
                JSONObject jsonObj = new JSONObject(jsonData);
                String message =  jsonObj.optString("message");
                utilObj.showToast(getActivity(),message, 0);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        if (utilObj != null) {
            utilObj.stopLoader();
        }
                utilObj.showToast(getActivity(),errorMessage, 0);



    }

    /**
     * getActivity() interface must be implemented by activities that contain getActivity()
     * fragment to allow an interaction in getActivity() fragment to be communicated
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
    private boolean validatingRequired() {
        message = "";

        password = passwordObj.getText().toString();
        newpassword = newpasswordObj.getText().toString();
        cpassword = cpasswordObj.getText().toString();
//        boolean check =  utilObj.checkEmail(email);
        //validate the content

       if(newpassword.isEmpty()) {
            message = getResources().getString(R.string.newpassworderrormsg);
            //utilObj.showError(getActivity(), message, textViewObj, emailObj);
            utilObj.showToast(getActivity(),message,0);
        }
        else if(password.isEmpty()) {
            message = getResources().getString(R.string.PasswordErrorMessage);
            //utilObj.showError(getActivity(), message, textViewObj, passwordObj);
            utilObj.showToast(getActivity(),message,0);
        }
        else if(!cpassword.isEmpty())
        {
            boolean validation = passwordValidator.validate(password);
            if(!validation)
            {
                message = getResources().getString(R.string.PasswordErrorMessage);
                //utilObj.showError(getActivity(), message, textViewObj, passwordObj);
                utilObj.showToast(getActivity(),message,0);
            }
        }
        else if(cpassword.isEmpty())
        {
            message = getResources().getString(R.string.CNPasswordErrorMessage);
            //utilObj.showError(getActivity(), message, textViewObj, passwordObj);
            utilObj.showToast(getActivity(),message,0);
        }
        else if(!cpassword.equalsIgnoreCase(password))
        {


            message = getResources().getString(R.string.PasswordNotMatchErrorMessage);
            //utilObj.showError(getActivity(), message, textViewObj, passwordObj);
            utilObj.showToast(getActivity(),message,0);

        }

        if(message.equalsIgnoreCase("") || message == null) {
            return true;
        }
        else {
            return false;
        }

    }
}
