package com.smartdatainc.ui.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.smartdatainc.utils.MultiSelectSpinner;
import com.smartdatainc.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateUserProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateUserProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateUserProfile extends Fragment implements ServiceRedirection {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView profilename,usernickname,uid,profileemail,profilepassword,uesername,profilephone,profiledescription,profiletaxid,profilbankacount,certificate;
    MultiSelectSpinner languageSpinner;
    EditText addresstext;
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
    Map<String,String> languageidmap;
   // private LinearLayout mContainerView = null;
  //  private Button mAddInsurenceButton = null;
  //  private View mExclusiveEmptyView = null;
    String languages[] ={"Arabic","Hindi","English","Japanese","Russian","German","French","Korean"};
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

    public UpdateUserProfile() {
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

        View view = inflater.inflate(R.layout.fragment_update_user_profile, container, false);
       // mContainerView = (LinearLayout)view.findViewById(R.id.parentView);
       // mAddInsurenceButton = (Button)view.findViewById(R.id.btnAddNewItem);
        profilename = (TextView) view.findViewById(R.id.profilename);
        profileemail = (TextView) view.findViewById(R.id.profileemail);
        profilepassword = (TextView) view.findViewById(R.id.profilepassword);
        uesername = (TextView) view.findViewById(R.id.uesername);
        addresstext = (EditText) view.findViewById(R.id.address);
        profilephone = (TextView) view.findViewById(R.id.profilephone);
        profiledescription = (TextView) view.findViewById(R.id.profiledescription);
        profiletaxid = (TextView) view.findViewById(R.id.profiletaxid);
        profilbankacount = (TextView) view.findViewById(R.id.profilbankacount);
      //  languageSpinner = (Spinner) view.findViewById(R.id.language);
        languageSpinner = (MultiSelectSpinner)view.findViewById(R.id.language);

        certificate= (TextView) view.findViewById(R.id.certificate);
        usernickname = (TextView) view.findViewById(R.id.nickname);
        profileimageview = (CircularImageView) view.findViewById(R.id.profileimageview);
        profileimageview.setBorderWidth(10);
        profileimageview.setBorderColor(R.color.grey);
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
        languagemap.put("NL",R.drawable.android_icon);
        languagemap.put("EN",R.drawable.android_icon);
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
        String allLanguageArray[]={"Afrikaans",
                "Albanian",
                "Arabic",
                "Armenian",
                "Basque",
                "Bengali",
                "Bulgarian",
                "Catalan",
                "Cambodian",
                "Chinese (Mandarin)",
                "Croatian",
                "Czech",
                "Danish",
                "Dutch",
                "English",
                "Estonian",
                "Fiji",
                "Finnish",
                "French",
                "Georgian",
                "German",
                "Greek",
                "Gujarati",
                "Hebrew",
                "Hindi",
                "Hungarian",
                "Icelandic",
                "Indonesian",
                "Irish",
                "Italian",
                "Japanese",
                "Javanese",
                "Korean",
                "Latin",
                "Latvian",
                "Lithuanian",
                "Macedonian",
                "Malay",
                "Malayalam",
                "Maltese",
                "Maori",
                "Marathi",
                "Mongolian",
                "Nepali",
                "Norwegian",
                "Persian",
                "Polish",
                "Portuguese",
                "Punjabi",
                "Quechua",
                "Romanian",
                "Russian",
                "Samoan",
                "Serbian",
                "Slovak",
                "Slovenian",
                "Spanish",
                "Swahili",
                "Swedish",
                "Tamil",
                "Tatar",
                "Telugu",
                "Thai",
                "Tibetan",
                "Tonga",
                "Turkish",
                "Ukrainian",
                "Urdu",
                "Uzbek",
                "Vietnamese",
                "Welsh",
                "Xhosa"};
        languageSpinner.setItems(allLanguageArray);
/*
{"id": 'AF',"name": "Afrikaans"},
{"id": 'SQ',"name": "Albanian"},
{"id": "AR","name": "Arabic"},
{"id": 'HY',"name": "Armenian"},
{"id": 'EU',"name": "Basque"},
{"id": 'BN',"name": "Bengali"},
{"id": 'BG',"name": "Bulgarian"},
{"id": 'CA',"name": "Catalan"},
{"id": 'KM',"name": "Cambodian"},
{"id": 'ZH',"name": "Chinese (Mandarin)"},
{"id": 'HR',"name": "Croatian"},
{"id": 'CS',"name": "Czech"},
{"id": 'DA',"name": "Danish"},
{"id": 'NL',"name": "Dutch"},
{"id": 'EN',"name": "English"},
{"id": 'ET',"name": "Estonian"},
{"id": 'FJ',"name": "Fiji"},
{"id": 'FI',"name": "Finnish"},
{"id": 'FR',"name": "French"},
{"id": 'KA',"name": "Georgian"},
{"id": 'DE',"name": "German"},
{"id": 'EL',"name": "Greek"},
{"id": 'GU',"name": "Gujarati"},
{"id": 'HE',"name": "Hebrew"},
{"id": 'HI',"name": "Hindi"},
{"id": 'HU',"name": "Hungarian"},
{"id": 'IS',"name": "Icelandic"},
{"id": 'ID',"name": "Indonesian"},
{"id": 'GA',"name": "Irish"},
{"id": 'IT',"name": "Italian"},
{"id": 'JA',"name": "Japanese"},
{"id": 'JW',"name": "Javanese"},
{"id": 'KO',"name": "Korean"},
{"id": 'LA',"name": "Latin"},
{"id": 'LV',"name": "Latvian"},
{"id": 'LT',"name": "Lithuanian"},
{"id": 'MK',"name": "Macedonian"},
{"id": 'MS',"name": "Malay"},
{"id": 'ML',"name": "Malayalam"},
{"id": 'MT',"name": "Maltese"},
{"id": 'MI',"name": "Maori"},
{"id": 'MR',"name": "Marathi"},
{"id": 'MN',"name": "Mongolian"},
{"id": 'NE',"name": "Nepali"},
{"id": 'NO',"name": "Norwegian"},
{"id": 'FA',"name": "Persian"},
{"id": 'PL',"name": "Polish"},
{"id": 'PT',"name": "Portuguese"},
{"id": 'PA',"name": "Punjabi"},
{"id": 'QU',"name": "Quechua"},
{"id": 'RO',"name": "Romanian"},
{"id": 'RU',"name": "Russian"},
{"id": 'SM',"name": "Samoan"},
{"id": 'SR',"name": "Serbian"},
{"id": 'SK',"name": "Slovak"},
{"id": 'SL',"name": "Slovenian"},
{"id": 'ES',"name": "Spanish"},
{"id": 'SW',"name": "Swahili"},
{"id": 'SV',"name": "Swedish"},
{"id": 'TA',"name": "Tamil"},
{"id": 'TT',"name": "Tatar"},
{"id": 'TE',"name": "Telugu"},
{"id": 'TH',"name": "Thai"},
{"id": 'BO',"name": "Tibetan"},
{"id": 'TO',"name": "Tonga"},
{"id": 'TR',"name": "Turkish"},
{"id": 'UK',"name": "Ukrainian"},
{"id": 'UR',"name": "Urdu"},
{"id": 'UZ',"name": "Uzbek"},
{"id": 'VI',"name": "Vietnamese"},
{"id": 'CY',"name": "Welsh"},
{"id": 'XH',"name": "Xhosa"}
 */
        languageidmap = new HashMap<>();
        languageidmap.put("AF","Afrikaans");
        languageidmap.put("SQ","Albanian");
        languageidmap.put("AR","Arabic");
        languageidmap.put("HY","Armenian");
        languageidmap.put("EU","Basque");
        languageidmap.put("BN","Bengali");
        languageidmap.put("BG","Bulgarian");
        languageidmap.put("CA","Catalan");
        languageidmap.put("KM'","Cambodian");
        languageidmap.put("ZH","Chinese (Mandarin)");
        languageidmap.put("HR","Croatian");
        languageidmap.put("CS","Czech");
        languageidmap.put("DA","Danish");
        languageidmap.put("NL","Dutch");
        languageidmap.put("EN","English");
        languageidmap.put("ET","Estonian");
        languageidmap.put("FJ","Fiji");
        languageidmap.put("FI","Finnish");
        languageidmap.put("FR","French");
        languageidmap.put("KA","Georgian");
        languageidmap.put("DE","German");
        languageidmap.put("EL","Greek");
        languageidmap.put("GU","Gujarati");
        languageidmap.put("HE","Hebrew");
        languageidmap.put("HI","Hindi");
        languageidmap.put("HU","Hungarian");
        languageidmap.put("IS","Icelandic");
        languageidmap.put("ID","Indonesian");
        languageidmap.put("GA","Irish");
        languageidmap.put("IT","Italian");
        languageidmap.put("JA","Japanese");
        languageidmap.put("JW","Javanese");
        languageidmap.put("KO","Korean");
        languageidmap.put("LA","Latin");
        languageidmap.put("LV","Latvian");
        languageidmap.put("LT","Lithuanian");
        languageidmap.put("MK","Macedonian");
        languageidmap.put("MS","Malay");
        languageidmap.put("ML","Malayalam");
        languageidmap.put("MT","Maltese");
        languageidmap.put("MI","Maori");
        languageidmap.put("MR","Marathi");
        languageidmap.put("MN","Mongolian");
        languageidmap.put("NE","Nepali");
        languageidmap.put("NO","Norwegian");
        languageidmap.put("FA","Persian");
        languageidmap.put("PL","Polish");
        languageidmap.put("PT","Portuguese");
        languageidmap.put("PA","Punjabi");
        languageidmap.put("QU","Quechua");
        languageidmap.put("RO","Romanian");
        languageidmap.put("RU","Russian");
        languageidmap.put("SM","Samoan");
        languageidmap.put("SR","Serbian");
        languageidmap.put("SK","Slovak  ");
        languageidmap.put("SL","Slovenian");
        languageidmap.put("ES","Spanish");
        languageidmap.put("SW","Swahili");
        languageidmap.put("SV","Swedish");
        languageidmap.put("TA","Tamil");
        languageidmap.put("TT","Tatar");
        languageidmap.put("TE","Telugu");
        languageidmap.put("TH","Thai");
        languageidmap.put("BO","Tibetan");
        languageidmap.put("TO","Tonga");
        languageidmap.put("TR","Turkish");
        languageidmap.put("UK","Ukrainian");
        languageidmap.put("UR","Urdu");
        languageidmap.put("UZ","Uzbek");
        languageidmap.put("VI","Vietnamese");
        languageidmap.put("CY","Welsh");
        languageidmap.put("XH","Xhosa");
        

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
        ClientFieldClickListener clientFieldClickListener = new ClientFieldClickListener();
        //mAddInsurenceButton.setOnClickListener(clientFieldClickListener);

    }
    private class ClientFieldClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
//                case R.id.btnAddNewItem:
//                    inflateView("test");
//                    break;


            }
        }
    }
    private void inflateView(final String rowValue)
{    /*

        LayoutInflater mInflator = (LayoutInflater) getActivity().getSystemService("layout_inflater");
        final View rowView = (View)mInflator.inflate(R.layout.row, null);;

        //rowView = rowView.inflate(0x7f030062, null);
        final ImageView deleteButton = (ImageView)rowView.findViewById(R.id.buttonDelete);
        final EditText fieldMemberDOB = (EditText)rowView.findViewById(R.id.languageadded);

        //TextView textview = (TextView)rowView.findViewById(0x7f050661);


        deleteButton.setOnClickListener(new android.view.View.OnClickListener() {


            public void onClick(View view) {
                onDeleteClicked(view);
            }


        });


        fieldMemberDOB.addTextChangedListener(new TextWatcher() {

            @SuppressLint("NewApi")
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    mAddInsurenceButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.INVISIBLE);
                    if (mExclusiveEmptyView != null && mExclusiveEmptyView != rowView) {
                        mContainerView.removeView(mExclusiveEmptyView);
                    }
                    mExclusiveEmptyView = rowView;
                    return;
                }
                if (mExclusiveEmptyView == rowView) {
                    mExclusiveEmptyView = null;
                }
                mAddInsurenceButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            }

            public void beforeTextChanged(CharSequence charsequence, int k, int l, int i1) {
            }

            public void onTextChanged(CharSequence charsequence, int k, int l, int i1)
            {
            }


        });

        mContainerView.addView(rowView, mContainerView.getChildCount() - 1);
        //updatePolicyNumber();
        return;*/
    }
    //Delete Views
   /* public void onDeleteClicked(View view)
    {
        mContainerView.removeView((View) view.getParent());

    }*/
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
            List<String> languageTypeList = new ArrayList();
            if(mylanguage!=null) {
                String[]  languageArray = mylanguage.split(",");
                for(int l=0 ; l<languageArray.length ;l++)
                {
                    Language languageObj = new Language();
                    String language = languageArray[l];
                    languageTypeList.add(languageidmap.get(language));
                    ;
                  /*  Integer id = languagemap.get(language);
                    languageObj.setImageId(id);
                    languageObj.setText(language);*/
                    totallist.add(languageObj);


                }
                if(languageTypeList != null && languageTypeList.size() > 0)
                {
                    languageSpinner.setSelection(languageTypeList);
                }

            }
          /*  if(totallist!=null) {
                languageadapter = new LanguageAdapter(getActivity(),
                        R.layout.language_spinner, R.id.txt, totallist);
                languageSpinner.setAdapter(languageadapter);
            }*/
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
