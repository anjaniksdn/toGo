package com.smartdatainc.ui.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartdatainc.adapters.LanguageAdapter;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateUserProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateUserProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateCustomerProfileFragment extends  Fragment implements ServiceRedirection {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView profilename,usernickname,uid,profileemail,profilepassword,uesername,addresstext,profilephone,profiledescription,profiletaxid,profilbankacount,certificate;
    Spinner languageSpinner;
    CircularImageView profileimageview;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    HashMap<String,Integer> languagemap;
    LanguageAdapter languageadapter;
    UserProfileManager userProfileManager;
    ArrayList<Language> totallist=new ArrayList<>();
    private Utility utilObj;
    UserProfile userProfile;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateUserProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateUserProfile newInstance(String param1, String param2) {
        UpdateUserProfile fragment = new UpdateUserProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UpdateCustomerProfileFragment() {
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

        View view = inflater.inflate(R.layout.fragment_updatecustomer_profile, container, false);
        profilename = (TextView) view.findViewById(R.id.profilename);
        profileemail = (TextView) view.findViewById(R.id.profileemail);
        profilepassword = (TextView) view.findViewById(R.id.profilepassword);
        uesername = (TextView) view.findViewById(R.id.uesername);
        addresstext = (TextView) view.findViewById(R.id.address);
        profilephone = (TextView) view.findViewById(R.id.profilephone);
        profiledescription = (TextView) view.findViewById(R.id.profiledescription);
        profiletaxid = (TextView) view.findViewById(R.id.profiletaxid);
        profilbankacount = (TextView) view.findViewById(R.id.profilbankacount);
        languageSpinner = (Spinner) view.findViewById(R.id.language);
        certificate= (TextView) view.findViewById(R.id.certificate);
        usernickname = (TextView) view.findViewById(R.id.nickname);
        profileimageview = (CircularImageView) view.findViewById(R.id.profileimageview);
        uid = (TextView) view.findViewById(R.id.uid);
        initData();
        String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
        utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        User userObj = new User();
        userObj.Authorization = token;
        userProfileManager.userProfile(userObj);
        return view;


    }

    public void initData() {


        utilObj = new Utility(getActivity());
        userProfile = new UserProfile();


        languagemap = new HashMap<>();

        languagemap.put("AF", R.drawable.android_icon);
        languagemap.put("SQ",R.drawable.android_icon);
        languagemap.put("HY",R.drawable.android_icon);
        languagemap.put("EU",R.drawable.android_icon);
        languagemap.put("BN",R.drawable.android_icon);
        languagemap.put("BG",R.drawable.android_icon);
        languagemap.put("CA",R.drawable.android_icon);
        languagemap.put("KM'",R.drawable.android_icon);
        languagemap.put("ZH",R.drawable.android_icon);
        languagemap.put("HR",R.drawable.android_icon);
        languagemap.put("CS",R.drawable.android_icon);
        languagemap.put("DA",R.drawable.android_icon);
        languagemap.put("NL'",R.drawable.android_icon);
        languagemap.put("EN'",R.drawable.android_icon);
        languagemap.put("ET",R.drawable.android_icon);
        languagemap.put("FJ",R.drawable.android_icon);
        languagemap.put("FI",R.drawable.android_icon);
        languagemap.put("FR",R.drawable.android_icon);
        languagemap.put("KA",R.drawable.android_icon);
        languagemap.put("DE",R.drawable.android_icon);
        languagemap.put("EL",R.drawable.android_icon);
        languagemap.put("GU",R.drawable.android_icon);
        languagemap.put("HE",R.drawable.android_icon);
        languagemap.put("HI",R.drawable.android_icon);
        languagemap.put("HU",R.drawable.android_icon);
        languagemap.put("IS",R.drawable.android_icon);
        languagemap.put("ID",R.drawable.android_icon);
        languagemap.put("GA",R.drawable.android_icon);
        languagemap.put("IT",R.drawable.android_icon);
        languagemap.put("JA",R.drawable.android_icon);
        languagemap.put("JW",R.drawable.android_icon);
        languagemap.put("KO",R.drawable.android_icon);
        languagemap.put("LA",R.drawable.android_icon);
        languagemap.put("LV",R.drawable.android_icon);
        languagemap.put("LT",R.drawable.android_icon);
        languagemap.put("MK",R.drawable.android_icon);
        languagemap.put("MS",R.drawable.android_icon);
        languagemap.put("ML",R.drawable.android_icon);
        languagemap.put("MT",R.drawable.android_icon);
        languagemap.put("MI",R.drawable.android_icon);
        languagemap.put("MR",R.drawable.android_icon);
        languagemap.put("MN",R.drawable.android_icon);
        languagemap.put("NE",R.drawable.android_icon);
        languagemap.put("NO",R.drawable.android_icon);
        languagemap.put("FA",R.drawable.android_icon);
        languagemap.put("PL",R.drawable.android_icon);
        languagemap.put("PT",R.drawable.android_icon);
        languagemap.put("PA",R.drawable.android_icon);
        languagemap.put("QU",R.drawable.android_icon);
        languagemap.put("RO",R.drawable.android_icon);
        languagemap.put("RU",R.drawable.android_icon);
        languagemap.put("SM",R.drawable.android_icon);
        languagemap.put("SR",R.drawable.android_icon);
        languagemap.put("SK",R.drawable.android_icon);
        languagemap.put("SL",R.drawable.android_icon);
        languagemap.put("ES",R.drawable.android_icon);
        languagemap.put("SW",R.drawable.android_icon);
        languagemap.put("SV",R.drawable.android_icon);
        languagemap.put("TA",R.drawable.android_icon);
        languagemap.put("TT",R.drawable.android_icon);
        languagemap.put("TE",R.drawable.android_icon);
        languagemap.put("TH",R.drawable.android_icon);
        languagemap.put("BO",R.drawable.android_icon);
        languagemap.put("TO",R.drawable.android_icon);
        languagemap.put("TR",R.drawable.android_icon);
        languagemap.put("UK",R.drawable.android_icon);
        languagemap.put("UR",R.drawable.android_icon);
        languagemap.put("UZ",R.drawable.android_icon);
        languagemap.put("VI",R.drawable.android_icon);
        languagemap.put("CY",R.drawable.android_icon);
        languagemap.put("XH", R.drawable.android_icon);




      /*  languagemap.put("AF",R.drawable.android_icon));
        languagemap.put("SQ",R.drawable.android_icon));
        languagemap.put("AR",R.drawable.android_icon));
        languagemap.put("HY",R.drawable.android_icon));
        languagemap.put("EU",R.drawable.android_icon));
        languagemap.put("Aud",R.drawable.android_icon));*/





        // Typeface face = Typeface.createFromAsset(getAssets(), "fonts/robot_regular.ttf");
        //   btnSignUpObj.setTypeface(face);
        //  mtextForgtPass.setTypeface(face);
        userProfileManager = new UserProfileManager(getActivity(), this);

       /* twitterLoginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        twitterLoginButton.setBackgroundResource(R.drawable.tw);
        twitterLoginButton.setHeight(convertDpToPixel(60, LoginActivity.this));
        twitterLoginButton.setWidth(convertDpToPixel(60, LoginActivity.this));
        twitterLoginButton.setCompoundDrawablePadding(0);
        twitterLoginButton.setPadding(0, 0, 0, 0);
        twitterLoginButton.setText("");
        twitterLoginButton.setTextSize(18);*/

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
        String[] languageArray = new String[20];
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
            //String imageurl = jsonobj.getJSONObject("profile_img").getString("url");
            String first_name = jsonobjUser.getJSONObject("name").optString("first_name");
            String last_name = jsonobjUser.getJSONObject("name").optString("last_name");
            profileemail.setText(email);
            addresstext.setText(address);
            profilephone.setText(phone_number);
            profiledescription.setText("");
            profiletaxid.setText(ein_taxId);
            uid.setText(useruid);
            usernickname.setText(nickname);
            if(mylanguage!=null) {
                languageArray = mylanguage.split(",");
                for(int l=0 ; l<languageArray.length ;l++)
                {
                    Language languageObj = new Language();
                    String language = languageArray[l];
                    Integer id = languagemap.get(language);
                    languageObj.setImageId(id);
                    languageObj.setText(language);
                    totallist.add(languageObj);

                    languageadapter=new LanguageAdapter(getActivity(),
                            R.layout.language_spinner,R.id.txt,totallist);
                    languageSpinner.setAdapter(languageadapter);
                }
                List<String> languageArryaList =  Arrays.asList(languageArray);

            }
            // ArrayList<String> languageArryaList = Arrays.asList(languageArray);


         /*   languageadapter=new LanguageAdapter(getActivity(),
                    R.layout.language_spinner,R.id.txt,list);
            language.setAdapter(languageadapter);*/
            // profileemail = (TextView) view.findViewById(R.id.profileemail);
           /* profilepassword = (TextView) view.findViewById(R.id.profilepassword);
           // uesername = (TextView) view.findViewById(R.id.uesername);
           // address = (TextView) view.findViewById(R.id.address);
        //    profilephone = (TextView) view.findViewById(R.id.profilephone);
            profiledescription = (TextView) view.findViewById(R.id.profiledescription);
            profiletaxid = (TextView) view.findViewById(R.id.profiletaxid);
            profilbankacount = (TextView) view.findViewById(R.id.profilbankacount);
            language = (Spinner) view.findViewById(R.id.language);
            certificate= (TextView) view.findViewById(R.id.certificate);*/

         /*   userProfile.setAddress(address);
            userProfile.setEmail(email);
           // userProfile.setImageurl(imageurl);
            userProfile.setPhonenumber(phone_number);
            userProfile.setPassword(password);*/
            if(first_name!=null) {
                userProfile.setName(first_name + " " + last_name);
            }
            profilename.setText(userProfile.getName());
            uesername.setText(userProfile.getName());
            // String country = jsonobj.getString("email");
        }catch (JSONException e)
        {
            e.printStackTrace();
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
