package com.smartdatainc.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartdatainc.adapters.CountriesListAdapter;
import com.smartdatainc.adapters.LanguageAdapter;
import com.smartdatainc.adapters.LanguageListAdapter;
import com.smartdatainc.adapters.StateListAdapter;
import com.smartdatainc.dataobject.Country;
import com.smartdatainc.dataobject.CountryData;
import com.smartdatainc.dataobject.Interpreter;
import com.smartdatainc.dataobject.Language;
import com.smartdatainc.dataobject.Name;
import com.smartdatainc.dataobject.State;
import com.smartdatainc.dataobject.Upload;
import com.smartdatainc.dataobject.User;
import com.smartdatainc.dataobject.UserProfile;
import com.smartdatainc.interfaces.LanguageListInterface;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.UserProfileManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.CircularImageView;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.ExifUtil;
import com.smartdatainc.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateInterpreterProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateInterpreterProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateInterpreterProfile extends BaseFragment implements ServiceRedirection, LanguageListInterface, OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. AonClickListenerRG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView profilename, uid, profileemail, profiletaxid, profilbankacount, certificate;
    //MultiSelectSpinner
    private TextView mLanguageTextView;
    EditText addresstext, firstname, lastname, profilephone, cityedit, zipcodeedit, usernickname;
    CircularImageView profileimageview;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String email, id;
    HashMap<String, Integer> languagemap;
    LanguageAdapter languageadapter;
    LanguageListAdapter languageListAdapter;
    UserProfileManager userProfileManager;
    ArrayList<Language> totallist = new ArrayList<>();
    private Utility utilObj;
    UserProfile userProfile;
    Map<String, String> languageidmap;
    ImageView profilephoneedit, editfirstname, editlastname, editaddress, editnickname, certificatesedit,editzip, editcity;
    boolean phoneedtitable = false;
    boolean firstnameeditable = false;
    boolean lastnameeditable = false;
    boolean addresseditable = false;
    boolean nicknameeditable = false;
    boolean zipeditable = false;
    boolean cityeditable = false;
    boolean certificateseditable = false;


    private AQuery aq;
    Uri mImageUri;
    String mProfilePic;
    Bitmap mBitmap;
    Spinner statespinner, countryspinner;
    boolean makeEditable = true;

    String password;
    String phone_number;
    String mylanguage;
    String address;
    String ein_taxId;
    String nickname;
    String useruid;
    String first_name;
    String last_name;
    String country;
    String state;
    String city;
    String zipcode;
    ArrayList<Country> items;
    ArrayList<State> stateitems;
    private ArrayList<Language> mLanguageArrayList;
    private ScrollView mScrollview;
    String element[];
    private ArrayList<String> mSelectedItems;
    private ArrayList<Language> mSelectedLanguageList;
    private ArrayList<String> mSelectedItemImage;
    // private LinearLayout mContainerView = null;
    //  private Button mAddInsurenceButton = null;
    //  private View mExclusiveEmptyView = null;
    String languages[] = {"Arabic", "Hindi", "English", "Japanese", "Russian", "German", "French", "Korean"};
    private ListView mLanguageListView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateInterpreterProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateInterpreterProfile newInstance(String param1, String param2) {
        UpdateInterpreterProfile fragment = new UpdateInterpreterProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UpdateInterpreterProfile() {
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

        View view = inflater.inflate(R.layout.fragment_update_user_profile, container, false);
        // mContainerView = (LinearLayout)view.findViewById(R.id.parentView);
        // mAddInsurenceButton = (Button)view.findViewById(R.id.btnAddNewItem);
        profilename = (TextView) view.findViewById(R.id.profilename);
        profileemail = (TextView) view.findViewById(R.id.profileemail);
        // profilepassword = (TextView) view.findViewById(R.id.profilepassword);
        firstname = (EditText) view.findViewById(R.id.uesername);
        lastname = (EditText) view.findViewById(R.id.lastname);
        addresstext = (EditText) view.findViewById(R.id.address);
        profilephone = (EditText) view.findViewById(R.id.profilephone);
        cityedit = (EditText) view.findViewById(R.id.city);
        zipcodeedit = (EditText) view.findViewById(R.id.zipcode);
        certificate = (EditText) view.findViewById(R.id.certificate);
        profilephone.setEnabled(false);
        /// profiledescription = (TextView) view.findViewById(R.id.profiledescription);
        profiletaxid = (TextView) view.findViewById(R.id.profiletaxid);
        profilbankacount = (TextView) view.findViewById(R.id.profilbankacount);
        mLanguageTextView = (TextView) view.findViewById(R.id.language);
        statespinner = (Spinner) view.findViewById(R.id.state);
        countryspinner = (Spinner) view.findViewById(R.id.country);
        profilephoneedit = (ImageView) view.findViewById(R.id.profilephoneedit);
        editfirstname = (ImageView) view.findViewById(R.id.editfirstname);
        editlastname = (ImageView) view.findViewById(R.id.editlastname);
        editzip = (ImageView) view.findViewById(R.id.editzip);
        editaddress = (ImageView) view.findViewById(R.id.editaddress);
        editnickname = (ImageView) view.findViewById(R.id.editnickname);
        editcity = (ImageView) view.findViewById(R.id.editcity);

        certificatesedit = (ImageView) view.findViewById(R.id.certificatesedit);
        usernickname = (EditText) view.findViewById(R.id.nickname);
        profileimageview = (CircularImageView) view.findViewById(R.id.profileimageview);
        profileimageview.setBorderWidth(10);
        profileimageview.setBorderColor(R.color.grey);
        uid = (TextView) view.findViewById(R.id.uid);

        mScrollview = (ScrollView) view.findViewById(R.id.scrollview);
        mLanguageListView = (ListView) view.findViewById(R.id.language_list_view);
        mLanguageListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScrollview.requestDisallowInterceptTouchEvent(true);

                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_UP:
                        mScrollview.requestDisallowInterceptTouchEvent(false);
                        break;
                }

                return false;
            }
        });

        initData();
        initListener();
        if (isNetworkAvailable(getActivity())) {
            String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
            utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
            User userObj = new User();
            userObj.Authorization = token;
            userProfileManager.interpreterProfile(userObj);
        } else {
            utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
        }
        countryspinner.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {


                        Country country = items.get(position);
                        if (country.getCountryId() != "") {
                            //utilObj.showToast(getActivity(), country.getCountryId(), 0);
                            if (isNetworkAvailable(getActivity())) {
                                String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
                                utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                                CountryData countryData = new CountryData();
                                countryData.Authorization = token;
                                countryData.country = country.getCountryId();
                                userProfileManager.getStateList(countryData);
                            } else {
                                utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
                            }
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // showToast("Spinner1: unselected");
                    }
                });
        statespinner.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {


                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // showToast("Spinner1: unselected");
                    }
                });

        return view;


    }

    private void initListener() {
        mLanguageTextView.setOnClickListener(this);
    }

    public void initData() {


        utilObj = new Utility(getActivity());
        userProfile = new UserProfile();
        aq = new AQuery(getActivity());
        mSelectedItems = new ArrayList();
        mSelectedItemImage = new ArrayList();
        mSelectedLanguageList = new ArrayList();

        userProfileManager = new UserProfileManager(getActivity(), this);
        ClientFieldClickListener clientFieldClickListener = new ClientFieldClickListener();
        profilephoneedit.setOnClickListener(clientFieldClickListener);
        profileimageview.setOnClickListener(clientFieldClickListener);
        editlastname.setOnClickListener(clientFieldClickListener);
        editaddress.setOnClickListener(clientFieldClickListener);
        editfirstname.setOnClickListener(clientFieldClickListener);
        editnickname.setOnClickListener(clientFieldClickListener);
        editzip.setOnClickListener(clientFieldClickListener);
        editcity.setOnClickListener(clientFieldClickListener);
        certificatesedit.setOnClickListener(clientFieldClickListener);


    }

    @Override
    public void onLanguageRemove(int position) {
        mSelectedItems.remove(position);
        mSelectedLanguageList.remove(position);
        languageListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLanguageSelectObj(ArrayList<Language> items) {
        mSelectedLanguageList = items;
        String selectedlanguageId="";
        Iterator<Language> languageItr = mSelectedLanguageList.iterator();
        {
            int i=0;
            while(languageItr.hasNext())
            {
                Language lang = languageItr.next();
                //selectedlanguageId = lang.getLanguageId();
                if (i == 0) {
                    selectedlanguageId =lang.getLanguageId();
                } else {
                    selectedlanguageId = selectedlanguageId + "," + lang.getLanguageId();
                }
                i++;
            }
        }
        mylanguage = selectedlanguageId;
    }

    @Override
    public void onLanguageSelect(ArrayList<String> items, ArrayList<String> itemImage) {
        //position
        mSelectedItems = items;
        mSelectedItemImage = itemImage;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.language:
                multiSelectLanguageDialog();
               /* if (isNetworkAvailable(getActivity())) {
                    String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
                    utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                    User userObj = new User();
                    userObj.Authorization = token;
                    userProfileManager.getLanguageList(userObj);
                } else {
                    utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
                }*/

                break;
        }
    }

    private class ClientFieldClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.profilephoneedit:

                    if (phoneedtitable) {
                        updateProfile();
                        profilephone.setEnabled(false);
                        phoneedtitable = false;
                        makeEditable = true;
                        profilephoneedit.setBackgroundResource(R.drawable.edit_black);


                    } else {
                        if (makeEditable) {
                            profilephone.setEnabled(true);
                            phoneedtitable = true;
                            makeEditable = false;
                            profilephoneedit.setBackgroundResource(R.drawable.tick_green);
                        }
                        else{
                            String msg = validateEditatble();
                        }
                    }
                    // inflateView("test");
                    break;
                case R.id.editfirstname:
                    if (firstnameeditable) {
                        updateProfile();
                        firstname.setEnabled(false);
                        firstnameeditable = false;
                        makeEditable = true;
                        editfirstname.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            firstname.setEnabled(true);
                            firstnameeditable = true;
                            makeEditable = false;
                            editfirstname.setBackgroundResource(R.drawable.tick_green);
                        } else{
                            String msg = validateEditatble();
                        }
                    }
                    // inflateView("test");
                    break;
                case R.id.editlastname:
                    if (lastnameeditable) {
                        lastname.setEnabled(false);
                        updateProfile();
                        lastnameeditable = false;
                        makeEditable = true;
                        editlastname.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            lastname.setEnabled(true);
                            lastnameeditable = true;
                            makeEditable = false;
                            editlastname.setBackgroundResource(R.drawable.tick_green);
                        } else{
                            String msg = validateEditatble();
                        }
                    }
                    // inflateView("test");
                    break;
                case R.id.editaddress:
                    if (addresseditable) {
                        addresstext.setEnabled(false);
                        addresseditable = false;
                        makeEditable = true;
                        updateProfile();
                        editaddress.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            addresstext.setEnabled(true);
                            addresseditable = true;
                            makeEditable = false;
                            editaddress.setBackgroundResource(R.drawable.tick_green);
                        } else{
                            String msg = validateEditatble();
                        }
                    }
                case R.id.editnickname:
                    if (nicknameeditable) {
                        addresstext.setEnabled(false);
                        nicknameeditable = false;
                        makeEditable = true;
                        updateProfile();
                        editnickname.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            usernickname.setEnabled(true);
                            nicknameeditable = true;
                            makeEditable = false;
                            editnickname.setBackgroundResource(R.drawable.tick_green);
                        } else{
                            String msg = validateEditatble();
                        }
                    }
                    // inflateView("test");
                    break;
                case R.id.editzip:
                    if (zipeditable) {
                        zipcodeedit.setEnabled(false);
                        zipeditable = false;
                        makeEditable = true;
                        updateProfile();
                        editzip.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            zipcodeedit.setEnabled(true);
                            zipeditable = true;
                            makeEditable = false;
                            editzip.setBackgroundResource(R.drawable.tick_green);
                        } else{
                            String msg = validateEditatble();
                        }
                    }
                    // inflateView("test");
                    break;
                case R.id.editcity:
                    if (cityeditable) {
                        cityedit.setEnabled(false);
                        cityeditable = false;
                        makeEditable = true;
                        updateProfile();
                        editcity.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            cityedit.setEnabled(true);
                            cityeditable = true;
                            makeEditable = false;
                            editcity.setBackgroundResource(R.drawable.tick_green);
                        } else{
                            String msg = validateEditatble();
                        }
                    }
                    // inflateView("test");
                    break;
                case R.id.certificatesedit:
                    if (certificateseditable) {
                        certificate.setEnabled(false);
                        certificateseditable = false;
                        makeEditable = true;
                        updateProfile();
                        certificatesedit.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            certificate.setEnabled(true);
                            certificateseditable = true;
                            makeEditable = false;
                            certificatesedit.setBackgroundResource(R.drawable.tick_green);
                        } else{
                            String msg = validateEditatble();
                        }
                    }
                    // inflateView("test");
                    break;


                case R.id.profileimageview:
                    setProfilePicDialog();
                    break;
            }
        }
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

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopLoader();

        if (taskID == Constants.TaskID.UPDATE_INTERPRETERPROFILE_TASK_ID) {
            //call the intent for the next activity
            // User userobj = (User)userObj;
            // int userid =  userobj.userID;
          /*  Intent intentObj = new Intent(this, User.class);
            startActivity(intentObj);*/
        }
    }

    public void onSuccessRedirection(int taskID, String data) {
        if (utilObj != null) {
            utilObj.stopLoader();
        }
        //  String[] languageArray = new String[20];
        if (taskID == Constants.TaskID.USER_PROFILE_TASK_ID) {
            String url = "";
            try {
                UserProfile userProfile = new UserProfile();
                JSONObject jsonObj = new JSONObject(data);
                JSONObject jsonobjUser = jsonObj.optJSONObject("user");
                boolean profileCompletion = jsonobjUser.optBoolean("profileCompletion");
                country = jsonobjUser.optString("country");
                email = jsonobjUser.optString("email");
                password = jsonobjUser.optString("password");
                phone_number = jsonobjUser.optString("phone_number");
                JSONArray mylanguageArray =jsonobjUser.optJSONArray("mylanguage");
                if(mylanguageArray!= null) {
                    for (int length=0;length<mylanguageArray.length();length++) {
                        String language = (String)mylanguageArray.get(length);
                        if(length == 0)
                        {
                            mylanguage = language;
                        }
                        else
                        {
                            mylanguage = mylanguage +","+language;
                        }
                        Log.v("Lang",language);
                    }
                }
              //  mylanguage = jsonobjUser.optString("mylanguage");
                address = jsonobjUser.optString("address");
                ein_taxId = jsonobjUser.optString("ein_taxId");
                nickname = jsonobjUser.optString("nickname");
                useruid = jsonobjUser.optString("uid");
                state = jsonobjUser.optString("state");
            /*String first_name;
            String last_name ;
            String country;
            String state;
            String city;
            String zipcode;*/
                city = jsonobjUser.optString("city");
                id = jsonobjUser.optString("id");
                zipcode = jsonobjUser.optString("zipcode");
                if(zipcode!=null)
                {
                    zipcodeedit.setText(zipcode);
                }
                JSONObject profile_img = jsonobjUser.optJSONObject("profile_img");
                if (profile_img != null) {
                    url = profile_img.getString("url");
                }
                //String imageurl = jsonobj.getJSONObject("profile_img").getString("url");
                JSONObject nameObject = jsonobjUser.optJSONObject("name");
                if (nameObject != null) {
                    first_name = jsonobjUser.optJSONObject("name").optString("first_name");
                    last_name = jsonobjUser.optJSONObject("name").optString("last_name");
                }
                if (email != null) {

                    profileemail.setText(email);
                }
                if (address != null) {
                    addresstext.setText(address);
                }
                if (phone_number != null) {
                    profilephone.setText(phone_number);
                }
                //profiledescription.setText("");
                profiletaxid.setText(ein_taxId);
                uid.setText(useruid);
                // profilepassword.setText(password);
                usernickname.setText(nickname);
                cityedit.setText(city);
                firstname.setText(first_name);
                lastname.setText(last_name);
                List<String> languageTypeList = new ArrayList();
               /* if (mylanguage != null) {
                    String[] languageArray = mylanguage.split(",");
                    for (int l = 0; l < languageArray.length; l++) {
                        Language languageObj = new Language();
                        String language = languageArray[l];
                        languageTypeList.add(languageidmap.get(language));
                        ;
                  *//*  Integer id = languagemap.get(language);
                    languageObj.setImageId(id);
                    languageObj.setText(language);*//*
                        totallist.add(languageObj);

                    }
                    if (languageTypeList != null && languageTypeList.size() > 0) {
                        //  languageSpinner.setSelection(languageTypeList);
                    }
                    languageListAdapter = new LanguageListAdapter(getActivity(), totallist, this);
                    mLanguageListView.setAdapter(languageListAdapter);
                }

*/


                if (first_name != null) {
                    userProfile.setName(first_name + " " + last_name);
                }
                profilename.setText(userProfile.getName());

                if (url != null && url.length() > 0) {
                    aq.id(R.id.profileimageview).image(url);
                }
                if (profileCompletion) {

                } else {
                    enableCompulsouryFields();
                }
                if (isNetworkAvailable(getActivity())) {
                    String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
                    utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                    User userObj = new User();
                    userObj.Authorization = token;
                    userProfileManager.getCountryList(userObj);
                } else {
                    utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
                }


                // String country = jsonobj.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (taskID == Constants.TaskID.UPDATE_INTERPRETERPROFILE_TASK_ID) {
            // Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();

            Fragment fragment = new UpdateInterpreterProfile();
            //Bundle args = new Bundle();
            //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
            //fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.host_activity, fragment)
                    .commit();

        }
        if (taskID == Constants.TaskID.UPDATE_PROFILEIMAGE_TASK_ID) {
            // Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();

        }
        if (taskID == Constants.TaskID.GET_COUNTRYLIST_TASK_ID) {
            try {
                JSONObject jsonObj = new JSONObject(data);
                JSONArray jsonobjArray = jsonObj.optJSONArray("data");
                items = new ArrayList<Country>();
                if (jsonobjArray != null) {
                    for (int c = 0; c < jsonobjArray.length(); c++) {
                        Country country = new Country();
                        //HashMap<String, String> countryNameCodeMap = new HashMap<String,String>();
                        JSONObject contryjsonobj = jsonobjArray.getJSONObject(c);
                        String countryName = contryjsonobj.optString("countryName");
                        String countryCode = contryjsonobj.optString("countryCode");
                        country.setCountryName(countryName);
                        country.setCountryId(countryCode);
                        //countryNameCodeMap.put(countrycode,countr);
                        items.add(country);


                    }
                    CountriesListAdapter countriesListAdapter = new CountriesListAdapter(getActivity(), items);

                    countryspinner.setAdapter(countriesListAdapter);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (taskID == Constants.TaskID.GET_SATELIST_TASK_ID) {
            try {
                JSONObject jsonObj = new JSONObject(data);
                JSONArray jsonobjArray = jsonObj.optJSONArray("data");
                stateitems = new ArrayList<State>();
                if (jsonobjArray != null) {
                    for (int c = 0; c < jsonobjArray.length(); c++) {
                        State state = new State();
                        //HashMap<String, String> countryNameCodeMap = new HashMap<String,String>();
                        JSONObject contryjsonobj = jsonobjArray.getJSONObject(c);
                        String stateName = contryjsonobj.optString("stateName");
                        //String statecode = contryjsonobj.optString("statecode");
                        state.setStateName(stateName);
                        // country.setCountryId(countrycode);
                        //countryNameCodeMap.put(countrycode,countr);
                        stateitems.add(state);


                    }
                    StateListAdapter stateListAdapter = new StateListAdapter(getActivity(), stateitems);
                    statespinner.setAdapter(stateListAdapter);
                    if (isNetworkAvailable(getActivity())) {
                        String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
                        utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                        User userObj = new User();
                        userObj.Authorization = token;
                        userProfileManager.getLanguageList(userObj);
                    } else {
                        utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (taskID == Constants.TaskID.GET_LANGUAGE_LIST_TASK_ID) {
            utilObj.stopLoader();
            try {
                JSONObject jsonObj = new JSONObject(data);
                JSONArray jsonObjArray = jsonObj.optJSONArray("data");
                mLanguageArrayList = new ArrayList<>();
                if (jsonObjArray != null) {
                    for (int c = 0; c < jsonObjArray.length(); c++) {
                        Language languageDataModel = new Language();
                        JSONObject languageJsonObj = jsonObjArray.getJSONObject(c);
                        String id = languageJsonObj.optString("id");
                        String createdAt = languageJsonObj.optString("createdAt");
                        String icon = languageJsonObj.optString("icon");
                        String languageId = languageJsonObj.optString("languageId");
                        String language = languageJsonObj.optString("language");

                        languageDataModel.setId(id);
                        languageDataModel.setCreatedAt(createdAt);
                        languageDataModel.setIcon(icon);
                        languageDataModel.setLanguageId(id);
                        languageDataModel.setLanguage(language);
                        languageDataModel.setMySelectedlanuguages(mylanguage);
                        mLanguageArrayList.add(languageDataModel);
                    }
                    //multiSelectLanguageDialog();
                }
                Iterator<Language> languageListitr = mLanguageArrayList.iterator();
                String selectedLangugeArray[] = mylanguage.split(",");
                while(languageListitr.hasNext())
                {
                    Language languaeitemObj = languageListitr.next();
                    if(selectedLangugeArray!=null) {
                        for (int l = 0; l < selectedLangugeArray.length; l++) {

                            if (selectedLangugeArray[l].equalsIgnoreCase(languaeitemObj.getLanguageId())) {
                                //languageCheckBox.setChecked(true);
                                mSelectedLanguageList.add(languaeitemObj);
                                mSelectedItemImage.add(languaeitemObj.getIcon());
                                mSelectedItems.add(languaeitemObj.getLanguage());
                            }

                        }
                    }
                }

                if (mSelectedItemImage.size() > 1) {
                    LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams((int) (800), (int) (350));
                    mLanguageListView.setLayoutParams(mParam);
                } else {
                    LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams((int) (800), LinearLayout.LayoutParams.WRAP_CONTENT);
                    mLanguageListView.setLayoutParams(mParam);
                }
                languageListAdapter = new LanguageListAdapter(getActivity(), mSelectedLanguageList, UpdateInterpreterProfile.this);
                mLanguageListView.setAdapter(languageListAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopLoader();
        // utilObj.showError(this, errorMessage, textViewObj, null);
        // utilObj.showToast(getActivity(), errorMessage, 0);

    }

    public void setProfilePicDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.selectProfilePicTitle)
                .setPositiveButton(R.string.gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent galleryIntent =
                                new Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        startActivityForResult(galleryIntent, Constants.GALLERY_PICTURE);
                    }
                })
                .setNegativeButton(R.string.camera, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent pictureActionIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File filePhoto = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg");
                        mImageUri = Uri.fromFile(filePhoto);
                        pictureActionIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        //getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        startActivityForResult(pictureActionIntent,
                                Constants.CAMERA_PICTURE);
                    }
                })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case Constants.CAMERA_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    getActivity().getContentResolver().notifyChange(mImageUri, null);
                    String strPath = String.valueOf(getRealPathFromURI(mImageUri));
                    if (strPath != null) {

                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(strPath, options);

                        // Calculate inSampleSize
                        options.inSampleSize = 2;

                        options.inJustDecodeBounds = false;
                        mBitmap = BitmapFactory.decodeFile(strPath, options);


                        if (mBitmap != null) {
                            Bitmap rotateBitmap = ExifUtil.rotateBitmap(strPath, mBitmap);
                            mProfilePic = getEncodedImage(rotateBitmap);
                            //mProfilePic = mProfilePic;
                            //Log.v("Camera Picbase64", mProfilePic);
                            profileimageview.setImageBitmap(rotateBitmap);
                            uploadImage();

                        } else
                            profileimageview.setImageResource(R.drawable.profile_icon);
                    } else
                        profileimageview.setImageResource(R.drawable.profile_icon);
                }

                break;
            case Constants.GALLERY_PICTURE:
                if (getPicturePath(data) != null) {

                    File mFile = new File(getPicturePath(data));
                    if (mFile != null) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        mBitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath(), options);
                        mProfilePic = getEncodedImage(mBitmap);
                        //mProfilePic = mProfilePic;
                        //Log.v("Gallery Picbase64", mProfilePic);
                        profileimageview.setImageBitmap(mBitmap);
                        uploadImage();
                    }
                }
                break;
            default:
                break;
        }
    }

    private String getEncodedImage(Bitmap bitmap) {
        ByteArrayOutputStream bAos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bAos);
        byte[] b = bAos.toByteArray();
        String profilePic = Base64.encodeToString(b, Base64.NO_WRAP);
        return profilePic;
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    /**
     * picture path from intent
     */
    private String getPicturePath(Intent data) {
        Uri selectedImage = data.getData();

        if (selectedImage == null) {
            return null;
        }

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    public void updateProfile() {
        if (isNetworkAvailable(getActivity())) {
            utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
            String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
            User userObj = new User();
            email = profileemail.getText().toString();
            address = addresstext.getText().toString();
            phone_number = profilephone.getText().toString();
            String selectedlanguageId[] = mylanguage.split(",");
           // ArrayList<Language> languageList =  languageListAdapter.getLanguageList();
            /*Iterator<Language> languageItr = mSelectedLanguageList.iterator();
            {
                int i=0;
                while(languageItr.hasNext())
                {
                    Language lang = languageItr.next();
                    //selectedlanguageId = lang.getLanguageId();
                    if (i == 0) {
                        selectedlanguageId =lang.getLanguageId();
                    } else {
                        selectedlanguageId = selectedlanguageId + "," + lang.getLanguageId();
                    }
                }
            }
*/
            // mylanguage = languageSpinner.getSelectedItemsAsString().toString().trim().replaceAll("\\[|\\]", "");
           /* String selectedlanguages[] = mylanguage.split(",");
            for (String key : languageidmap.keySet()) {
                if (selectedlanguages != null) {

                    for (int l = 0; l < selectedlanguages.length; l++) {

                        String language = selectedlanguages[l];
                        if (language.equalsIgnoreCase(languageidmap.get(key))) {
                            if (l == 0) {
                                selectedlanguageId = key;
                            } else {
                                selectedlanguageId = selectedlanguageId + "," + key;
                            }
                        }

                    }

                }
            }*/
            Log.v("languages", selectedlanguageId.toString());

            Name name = new Name();
            zipcode = zipcodeedit.getText().toString();
            ein_taxId = profiletaxid.getText().toString();
            useruid = uid.getText().toString();
            nickname = usernickname.getText().toString();
            first_name = firstname.getText().toString();
            last_name = lastname.getText().toString();
            name.first_name = first_name;
            name.last_name = last_name;
            Interpreter interpreterprofile = new Interpreter();
            interpreterprofile.phone_number = phone_number;
            interpreterprofile.address = address;
            interpreterprofile.name = name;
            interpreterprofile.id = id;
            interpreterprofile.city = city;
            interpreterprofile.country = country;
            interpreterprofile.state = state;
            interpreterprofile.email = email;
            interpreterprofile.password = password;
            interpreterprofile.nickname = nickname;
            interpreterprofile.ein_taxId = ein_taxId;
            interpreterprofile.zipcode = zipcode;
            interpreterprofile.mylanguage = selectedlanguageId;
            userObj.Authorization = token;
            userProfileManager.updateInterpreterProfile(interpreterprofile);
            profilephone.setEnabled(false);
            phoneedtitable = false;
        } else {
            utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
        }
    }

    public void uploadImage() {
        utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
        String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
        Upload upload = new Upload();
        email = profileemail.getText().toString();
        upload.service_type = "api";
        upload.email = email;
        upload.file = "data:image/jpeg;base64," + mProfilePic;
        upload.id = id;
        userProfileManager.updateProfileImage(upload);
    }

    public void enableCompulsouryFields() {
        profilephone.setEnabled(true);
        phoneedtitable = true;
        makeEditable = false;
        profilephoneedit.setBackgroundResource(R.drawable.tick_green);


        cityedit.setEnabled(true);
        cityeditable = true;
        makeEditable = false;
        editcity.setBackgroundResource(R.drawable.tick_green);

        lastname.setEnabled(true);
        lastnameeditable = true;
        makeEditable = false;
        editlastname.setBackgroundResource(R.drawable.tick_green);

        zipcodeedit.setEnabled(true);
        zipeditable = true;
        makeEditable = false;
        editzip.setBackgroundResource(R.drawable.tick_green);

        usernickname.setEnabled(true);
        nicknameeditable = true;
        makeEditable = false;
        editnickname.setBackgroundResource(R.drawable.tick_green);

        lastname.setEnabled(true);
        lastnameeditable = true;
        makeEditable = false;
        editlastname.setBackgroundResource(R.drawable.tick_green);

        firstname.setEnabled(true);
        firstnameeditable = true;
        makeEditable = false;
        editfirstname.setBackgroundResource(R.drawable.tick_green);

    }

    /* public void multiSelectList() {
         final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
         //dialog.setTitle(getResources().getString(R.string.job_function_list));
         final ArrayList<String> jobFunctionList = new ArrayList<String>();
         final ArrayList<String> jobFunctionId = new ArrayList<String>();
         mSelectedItems = new ArrayList();
         final ArrayList<String> mSelectedItemID = new ArrayList();
         for (int i = 0; i < mLanguageArrayList.size(); i++) {
             jobFunctionList.add(mLanguageArrayList.get(i).getLanguage());
             jobFunctionId.add(mLanguageArrayList.get(i).getLanguageId());
         }
         element = jobFunctionList.toArray(new String[jobFunctionList.size()]);
         dialog.setMultiChoiceItems(element, null, new DialogInterface.OnMultiChoiceClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                 if (isChecked) {
                     // If the user checked the item, build an array containing the selected items _id's.
                     mSelectedItems.add(jobFunctionList.get(which));
                     mSelectedItemID.add(jobFunctionId.get(which));
                 } else if (mSelectedItems.contains(jobFunctionId.get(which))) {
                     // Else, if the user changes his mind and de-selects an item, remove it
                     mSelectedItems.remove(jobFunctionList.get(which));
                     mSelectedItemID.remove(jobFunctionId.get(which));
                 }
             }
         });
         dialog.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {

                 // mJobFunctions = TextUtils.join(",", mSelectedItems);
                 //Log.e("mJobFunctions", mJobFunctions);
                 //  mJobID = TextUtils.join(",", mSelectedItemID);
                 //Log.e("mJobFunctions", mJobID);
                 languageListAdapter = new LanguageListAdapter(getActivity(), mSelectedItems, UpdateInterpreterProfile.this);
                 mLanguageListView.setAdapter(languageListAdapter);

             }
         });
         dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
             }
         });
         dialog.show();
     }*/
    public void multiSelectLanguageDialog()

    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.multi_select_language_dialog);
        final ListView languageList = (ListView) dialog.findViewById(R.id.language_list_view);
        languageList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //languageList.setItemsCanFocus(false);
        final LanguageAdapter languageAdapter = new LanguageAdapter(getActivity(), 1, mLanguageArrayList,mylanguage, true, UpdateInterpreterProfile.this);
        languageList.setAdapter(languageAdapter);

        TextView saveTextView = (TextView) dialog.findViewById(R.id.save_text_view);
        TextView cancelTextView = (TextView) dialog.findViewById(R.id.cancel_text_view);
        cancelTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        saveTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedItemImage.size() > 1) {
                    LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams((int) (800), (int) (350));
                    mLanguageListView.setLayoutParams(mParam);
                } else {
                    LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams((int) (800), LinearLayout.LayoutParams.WRAP_CONTENT);
                    mLanguageListView.setLayoutParams(mParam);
                }
                languageListAdapter = new LanguageListAdapter(getActivity(), mSelectedLanguageList, UpdateInterpreterProfile.this);
                mLanguageListView.setAdapter(languageListAdapter);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public String validateEditatble()
    {
        String message="";

        if(phoneedtitable)
        {
            message = getResources().getString(R.string.EmailErrorMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(firstnameeditable)
        {
            message = getResources().getString(R.string.FirstnameMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(lastnameeditable)
        {
            message = getResources().getString(R.string.lastnameMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(addresseditable)
        {
            message = getResources().getString(R.string.AddressMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(zipeditable)
        {
            message = getResources().getString(R.string.ZipcodeMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(cityeditable)
        {
            message = getResources().getString(R.string.CityMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(certificateseditable)
        {
            message = getResources().getString(R.string.CertificatesMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(nicknameeditable)
        {
            message = getResources().getString(R.string.NicknameMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        return  message;

    }
}

