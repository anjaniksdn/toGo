package com.smartdatainc.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.smartdatainc.adapters.Cardadapter;
import com.smartdatainc.adapters.CountriesListAdapter;
import com.smartdatainc.adapters.LanguageAdapter;
import com.smartdatainc.adapters.LanguageListAdapter;
import com.smartdatainc.adapters.StateListAdapter;
import com.smartdatainc.dataobject.Country;
import com.smartdatainc.dataobject.CountryData;
import com.smartdatainc.dataobject.Customer;
import com.smartdatainc.dataobject.Language;
import com.smartdatainc.dataobject.Name;
import com.smartdatainc.dataobject.Payment_info;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateInterpreterProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateInterpreterProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateCustomerProfileFragment extends BaseFragment implements ServiceRedirection, View.OnClickListener, LanguageListInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView profilename, uid, mDobTextView, profileemail, profilbankacount;
    //MultiSelectSpinner languageSpinner;
    private TextView mLanguageTextView;
    Spinner genderspinner, cardtypeSpinner;
    CircularImageView profileimageview;
    EditText addresstext, usernickname, profiledescription, firstname, lastname, profilephone, zipcodeedit, cvvtext, cityedit, cardnumber;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String message;
    Spinner expirymonth, expiryyear;
    private AQuery aq;
    HashMap<String, Integer> languagemap;
    LanguageAdapter languageadapter;
    UserProfileManager userProfileManager;
    ArrayList<Language> totallist = new ArrayList<>();
    private Utility utilObj;
    UserProfile userProfile;
    ArrayList<String> genderitems = new ArrayList<String>();
    ArrayList<String> cardtypeitems = new ArrayList<String>();
    ArrayList<String> expiryyearitems = new ArrayList<String>();
    ArrayList<String> expirymonthitems = new ArrayList<String>();
    SimpleDateFormat mDateFormatter;
    int mYear, mMonth, mDay, mDateOfBirth, mCurrentYear, mCurrentMonth, mCurrentDay;
    Calendar mCurrentDateTime;
    String month;
    ImageView expirymonthedit, descriptionedit,expiryyearedit, cvvedit,profilephoneedit, cardnumberedit, editlastname, editcity, editzip, editnickname, editfirstname, editaddress;
    Spinner statespinner, countryspinner, expirymonthspinner, expiryyearspinner;
    String email;
    String id;
    Map<String, String> languageidmap;
    ArrayAdapter<String> expiryyearadapter;
    ArrayAdapter<String> expirymonthadapter;
    Uri mImageUri;
    String mProfilePic;
    Bitmap mBitmap;
    String password;
    String phone_number;
    String mylanguage;
    String address;
    String cardnumberfromweb;
    String cardtype;
    String expyear;
    String cvvnumber;
    String expmonth;
    String nickname;
    String useruid;
    String last_name;
    String country;
    String state;
    String city;
    String gender;
    String zipcode;
    String first_name;
    String about_user;

    boolean phoneedtitable = false;
    boolean firstnameeditable = false;
    boolean lastnameeditable = false;
    boolean addresseditable = false;
    boolean nicknameeditable = false;
    boolean zipeditable = false;
    boolean cityeditable = false;
    boolean cardnumbereditable = false;
    boolean profiledescriptioneditable = false;
    boolean makeEditable = true;
    boolean expirymontheditable = false;
    boolean expiryyeareditable = false;
    boolean cvvediteditable = false;
    ArrayList<Country> items;
    ArrayList<State> stateitems;
    private ArrayList<Language> mLanguageArrayList;
    private ArrayList<String> mSelectedItems;
    private ArrayList<String> mSelectedItemImage;
    LanguageListAdapter languageListAdapter;
    private ScrollView mScrollview;
    private ListView mLanguageListView;
    private boolean mCheckedList = false;
    private ArrayList<Language> mSelectedLanguageList;



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

        // Inflate the country_list_item for this fragment
        container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_updatecustomer_profile, container, false);
        profilename = (TextView) view.findViewById(R.id.profilename);
        profileemail = (TextView) view.findViewById(R.id.profileemail);
//        profilepassword = (TextView) view.findViewById(R.id.profilepassword);
        firstname = (EditText) view.findViewById(R.id.uesername);
        lastname = (EditText) view.findViewById(R.id.lastname);
        addresstext = (EditText) view.findViewById(R.id.address);
        profilephone = (EditText) view.findViewById(R.id.profilephone);
        profiledescription = (EditText) view.findViewById(R.id.profiledescription);
        cityedit = (EditText) view.findViewById(R.id.cityedit);
        profilephoneedit = (ImageView) view.findViewById(R.id.profilephoneedit);
        editlastname = (ImageView) view.findViewById(R.id.editlastname);
        editaddress = (ImageView) view.findViewById(R.id.editaddress);
        expiryyearedit = (ImageView) view.findViewById(R.id.expiryyearedit);
        descriptionedit= (ImageView) view.findViewById(R.id.descriptionedit);
        expirymonthedit = (ImageView) view.findViewById(R.id.expirymonthedit);
        editfirstname = (ImageView) view.findViewById(R.id.editfirstname);
        editnickname = (ImageView) view.findViewById(R.id.editnickname);
        editzip = (ImageView) view.findViewById(R.id.editzip);
        zipcodeedit = (EditText) view.findViewById(R.id.zipcode);
        editcity = (ImageView) view.findViewById(R.id.editcity);
        cvvedit = (ImageView) view.findViewById(R.id.cvvedit);
        // expirydateedit= (ImageView)view.findViewById(R.id.expirydateedit);
        cardnumberedit = (ImageView) view.findViewById(R.id.cardnumberedit);
        cvvtext = (EditText) view.findViewById(R.id.cvvtext);
        statespinner = (Spinner) view.findViewById(R.id.statespinner);
        countryspinner = (Spinner) view.findViewById(R.id.countryspinner);
        countryspinner.setBackgroundResource(R.drawable.dropdown);
        expiryyearspinner = (Spinner) view.findViewById(R.id.expiryyearspinner);
        //expiryyearspinner.getSelectedView().setEnabled(false);
        expiryyearspinner.setEnabled(false);
        expirymonthspinner = (Spinner) view.findViewById(R.id.expirymonthspinner);
        //profiletaxid = (TextView) view.findViewById(R.id.profiletaxid);
        profilbankacount = (TextView) view.findViewById(R.id.profilbankacount);
        //languageSpinner = (MultiSelectSpinner) view.findViewById(R.id.language);
        mLanguageTextView = (TextView) view.findViewById(R.id.language);
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


        genderspinner = (Spinner) view.findViewById(R.id.genderspinner);
        cardtypeSpinner = (Spinner) view.findViewById(R.id.cardtypeSpinner);
        //expirydate = (EditText)view.findViewById(R.id.expirydate);
        cardnumber = (EditText) view.findViewById(R.id.cardnumber);
        //cardnumber= (EditText)view.findViewById(R.id.cardnumber);
        genderitems.add("Female");
        genderitems.add("Male");
        Cardadapter genderAdapter = new Cardadapter(getActivity(), genderitems);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, genderitems);
        genderspinner.setAdapter(genderAdapter);
        cardtypeitems.add("Visa");
        cardtypeitems.add("MasterCard");
        cardtypeitems.add("Maestro");
        cardtypeitems.add("Cirrus");
        Cardadapter cardadapter = new Cardadapter(getActivity(), cardtypeitems);
        // ArrayAdapter<String> cardtypeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, cardtypeitems);
        cardtypeSpinner.setAdapter(cardadapter);
        //certificate= (TextView) view.findViewById(R.id.certificate);
        mDobTextView = (TextView) view.findViewById(R.id.dob);
        usernickname = (EditText) view.findViewById(R.id.nickname);
        profileimageview = (CircularImageView) view.findViewById(R.id.profileimageview);
        uid = (TextView) view.findViewById(R.id.uid);
        mDobTextView.setOnClickListener(onClickListener);
        profileimageview.setOnClickListener(onClickListener);
        //expirydate.setOnClickListener(onClickListener);
        profilephoneedit.setOnClickListener(onClickListener);
        profileimageview.setOnClickListener(onClickListener);
        editlastname.setOnClickListener(onClickListener);
        editaddress.setOnClickListener(onClickListener);
        editfirstname.setOnClickListener(onClickListener);
        editnickname.setOnClickListener(onClickListener);
        editzip.setOnClickListener(onClickListener);
        editcity.setOnClickListener(onClickListener);
        // expirydateedit.setOnClickListener(onClickListener);
        cardnumberedit.setOnClickListener(onClickListener);
        expiryyearedit.setOnClickListener(onClickListener);
        expirymonthedit.setOnClickListener(onClickListener);
        descriptionedit.setOnClickListener(onClickListener);
        cvvedit.setOnClickListener(onClickListener);

        expirymonthitems.add("01");
        expirymonthitems.add("02");
        expirymonthitems.add("03");
        expirymonthitems.add("04");
        expirymonthitems.add("05");
        expirymonthitems.add("06");
        expirymonthitems.add("07");
        expirymonthitems.add("08");
        expirymonthitems.add("09");
        expirymonthitems.add("10");
        expirymonthitems.add("11");
        expirymonthitems.add("12");
        expiryyearitems.add("2016");
        expiryyearitems.add("2017");
        expiryyearitems.add("2018");
        expiryyearitems.add("2019");
        expiryyearitems.add("2020");
        expiryyearitems.add("2021");
        expiryyearitems.add("2022");
        expiryyearitems.add("2023");
        expiryyearitems.add("2024");
        expiryyearitems.add("2025");
        expiryyearitems.add("2026");
        expiryyearitems.add("2027");
        expiryyearitems.add("2028");


        expiryyearadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, expirymonthitems);
        expirymonthspinner.setAdapter(expiryyearadapter);
        expirymonthadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, expiryyearitems);
        expiryyearspinner.setAdapter(expirymonthadapter);
        initData();
        bindListener();
        bindDateCalender();
        if (isNetworkAvailable(getActivity())) {
            String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
            utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
            User userObj = new User();
            userObj.Authorization = token;
            userProfileManager.customerProfile(userObj);
        } else {
            utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
        }

        countryspinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
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
                                //utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
                            }
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // showToast("Spinner1: unselected");
                    }
                });
        statespinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {


                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // showToast("Spinner1: unselected");
                    }
                });

        //updateCustomerProfile
        return view;


    }

    private void bindListener() {
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

    @Override
    public void onLanguageRemove(int position) {
        //  mSelectedItems.remove(position);
        mSelectedLanguageList.remove(position);
        languageListAdapter.notifyDataSetChanged();
        updateProfile();
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
        mSelectedItems = items;
        mSelectedItemImage = itemImage;
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

        if (taskID == Constants.TaskID.LOGIN_TASK_ID) {
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
        if (taskID == Constants.TaskID.CUSTOMER_PROFILE_TASK_ID) {
            //  String[] languageArray = new String[20];
            String url = "";
            try {
                UserProfile userProfile = new UserProfile();
                JSONObject jsonObj = new JSONObject(data);
                JSONObject jsonobjUser = jsonObj.getJSONObject("user");
                boolean profileCompletion = jsonobjUser.getBoolean("profileCompletion");
                //String country = jsonobjUser.optString("email");
                JSONObject profile_img = jsonobjUser.optJSONObject("profile_img");
                if (profile_img != null) {
                    url = profile_img.getString("url");
                }
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
                        Log.v("Lang", language);
                    }
                }
              //  mylanguage = jsonobjUser.optString("mylanguage");
                address = jsonobjUser.optString("address");
                //ein_taxId  = jsonobjUser.optString("ein_taxId");
                nickname = jsonobjUser.optString("nickname");
                useruid = jsonobjUser.optString("uid");
                id = jsonobjUser.optString("id");
                gender = jsonobjUser.optString("gender");
                about_user = jsonobjUser.optString("about_user");
                String dob = jsonobjUser.optString("dob");
                String formatdate = dob.split("T")[0];
                //String formatdate = splitdate[0];
                String updatedDate = utilObj.formatDateTime(formatdate, "MMM DD YYYY", "YYYY MM DD");
                //String ds1 = "2007-06-30";
                try {
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                    if (formatdate != null) {
                        String ds2 = sdf2.format(sdf1.parse(formatdate));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //String imageurl = jsonobj.getJSONObject("profile_img").getString("url");
                try {
                    first_name = jsonobjUser.optJSONObject("name").optString("first_name");
                    last_name = jsonobjUser.optJSONObject("name").optString("last_name");
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                JSONObject payment_infoObj = jsonobjUser.optJSONObject("payment_info");
                if(payment_infoObj!=null) {
                    cardtype = payment_infoObj.optString("card_type");
                    cardnumberfromweb = payment_infoObj.optString("card_number");
                    expyear = payment_infoObj.optString("exp_year");
                    expmonth = payment_infoObj.optString("exp_month");
                    cvvnumber = payment_infoObj.optString("cvv");
                    if (cardnumberfromweb != null) {
                        cardnumber.setText(cardnumberfromweb);
                    }
                    expirymonthspinner.setSelection(getIndex(expirymonthspinner, expmonth));
                    expiryyearspinner.setSelection(getIndex(expiryyearspinner, expyear));
                }

            /*
            "payment_info" : {
        "cardtype" : "visa",
        "cardnumber" : "4111111111111111",
        "expmonth" : "04",
        "expyear" : "2016",
        "cvv" : "587"
    }
             */
            /*
             cardtypeitems.add("Visa");
        cardtypeitems.add("MasterCard");
        cardtypeitems.add("Maestro");
        cardtypeitems.add("Cirrus");
             */

                if (cardtype != null) {
                    if (cardtype.equalsIgnoreCase("Visa")) {
                        cardtypeSpinner.setSelection(0);
                    }
                    if (cardtype.equalsIgnoreCase("MasterCard")) {
                        cardtypeSpinner.setSelection(1);
                    }
                    if (cardtype.equalsIgnoreCase("Maestro")) {
                        cardtypeSpinner.setSelection(2);
                    }
                    if (cardtype.equalsIgnoreCase("Cirrus")) {
                        cardtypeSpinner.setSelection(3);
                    }
                }
                if (gender != null) {
                    if (gender.equalsIgnoreCase("female")) {
                        genderspinner.setSelection(0);
                    } else {
                        genderspinner.setSelection(1);
                    }
                }
                if (dob != null && dob.length() > 0) {
                    mDobTextView.setText(formatdate);
                }

                profileemail.setText(email);
                addresstext.setText(address);
                profilephone.setText(phone_number);
                profiledescription.setText(about_user);

                profileemail.setText(email);
                addresstext.setText(address);
                profilephone.setText(phone_number);

                firstname.setText(first_name);
                lastname.setText(last_name);
                usernickname.setText(nickname);
                uid.setText(useruid);
                cityedit.setText(city);
                uid.setText(useruid);
                cvvtext.setText(cvvnumber);

                List<String> languageTypeList = new ArrayList();

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
                    //  utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (taskID == Constants.TaskID.UPDATECUSTOMER_PROFILE_TASK_ID) {

            Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();

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
                    countryspinner.setBackgroundResource(R.drawable.dropdown);

                    // countryspinner.set(R.drawable.dropdown);

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
                        mLanguageArrayList.add(languageDataModel);
                    }
                    //multiSelectLanguageDialog();
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
                    languageListAdapter = new LanguageListAdapter(getActivity(), mSelectedLanguageList, UpdateCustomerProfileFragment.this);
                    mLanguageListView.setAdapter(languageListAdapter);
                }

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

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dob:

                    datePickerDialog();
                    //Do what you want for select_contact
                    break;

                case R.id.profilephoneedit:

                    if (phoneedtitable) {
                        updateProfile();
                        profilephone.setEnabled(false);
                        profilephone.setFocusable(false);
                        phoneedtitable = false;
                        makeEditable = true;
                        profilephoneedit.setBackgroundResource(R.drawable.edit_black);

                    } else {
                        if (makeEditable) {
                            profilephone.setEnabled(true);
                            profilephone.setFocusable(true);
                            profilephone.requestFocus();
                            phoneedtitable = true;
                            makeEditable = false;
                            profilephoneedit.setBackgroundResource(R.drawable.tick_green);
                        }else
                        {
                            validateEditatble();
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
                            firstname.requestFocus();
                            firstname.setEnabled(true);
                            firstnameeditable = true;
                            makeEditable = false;
                            editfirstname.setBackgroundResource(R.drawable.tick_green);
                        }else
                        {
                            validateEditatble();
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
                            lastname.requestFocus();
                            lastnameeditable = true;
                            makeEditable = false;
                            editlastname.setBackgroundResource(R.drawable.tick_green);
                        }else
                        {
                            validateEditatble();
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
                            addresstext.requestFocus();
                            addresseditable = true;
                            makeEditable = false;
                            editaddress.setBackgroundResource(R.drawable.tick_green);
                        }else
                        {
                            validateEditatble();
                        }
                    }
                    break;
                case R.id.editnickname:
                    if (nicknameeditable) {
                        usernickname.setEnabled(false);
                        nicknameeditable = false;
                        makeEditable = true;
                        updateProfile();
                        editnickname.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            usernickname.setEnabled(true);
                            usernickname.requestFocus();
                            nicknameeditable = true;
                            makeEditable = false;
                            editnickname.setBackgroundResource(R.drawable.tick_green);
                        }else
                        {
                            validateEditatble();
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
                            zipcodeedit.requestFocus();
                            zipeditable = true;
                            makeEditable = false;
                            editzip.setBackgroundResource(R.drawable.tick_green);
                        }else
                        {
                            validateEditatble();
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
                            cityedit.requestFocus();
                            cityeditable = true;
                            makeEditable = false;
                            editcity.setBackgroundResource(R.drawable.tick_green);
                        }else
                        {
                            validateEditatble();
                        }
                    }
                    // inflateView("test");
                    break;
           /*     case R.id.expirydateedit:
                    if(expirydateeditable)
                    {
                        expirydate.setEnabled(false);
                        expirydateeditable = false;
                        makeEditable = true;
                        updateProfile();
                    }else {
                        if(makeEditable) {
                            expirydate.setEnabled(true);
                            expirydateeditable = true;
                            makeEditable = false;
                        }
                    }
                    // inflateView("test");
                    break;*/
                case R.id.cardnumberedit:
                    if (cardnumbereditable) {
                        cardnumber.setEnabled(false);
                        cardnumbereditable = false;
                        makeEditable = true;
                        updateProfile();
                        cardnumberedit.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            cardnumber.setEnabled(true);
                            cardnumber.requestFocus();
                            cardnumbereditable = true;
                            makeEditable = false;
                            cardnumberedit.setBackgroundResource(R.drawable.tick_green);
                        }else
                        {
                            validateEditatble();
                        }
                    }
                    // inflateView("test");
                    break;
                case R.id.expiryyearedit:
                    if (expiryyeareditable) {
                        expiryyearspinner.setClickable(false);
                        expiryyearspinner.setEnabled(false);
                        expiryyeareditable = false;
                        makeEditable = true;
                        updateProfile();
                        expiryyearedit.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            expiryyearspinner.setClickable(true);
                            expiryyearspinner.setEnabled(true);
                            expiryyeareditable = true;
                            makeEditable = false;
                            expiryyearedit.setBackgroundResource(R.drawable.tick_green);
                        }else
                        {
                            validateEditatble();
                        }
                    }
                    // inflateView("test");
                    break;
                case R.id.expirymonthedit:
                    if (expirymontheditable) {
                        expirymonthspinner.setClickable(false);
                        expirymonthspinner.setEnabled(false);
                        expirymontheditable = false;
                        makeEditable = true;
                        updateProfile();
                        expirymonthedit.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            expirymonthspinner.setClickable(true);
                            expirymonthspinner.setEnabled(true);
                            expirymontheditable = true;
                            makeEditable = false;
                            expirymonthedit.setBackgroundResource(R.drawable.tick_green);
                        }else
                        {
                            validateEditatble();
                        }
                    }
                    // inflateView("test");
                    break;
                case R.id.cvvedit:
                    if (cvvediteditable) {
                        cvvtext.setEnabled(false);
                        cvvediteditable = false;
                        makeEditable = true;
                        updateProfile();
                        cvvedit.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            cvvtext.setEnabled(true);
                            cvvtext.requestFocus();
                            cvvediteditable = true;
                            makeEditable = false;
                            cvvedit.setBackgroundResource(R.drawable.tick_green);
                        }else
                        {
                            validateEditatble();
                        }
                    }
                    break;
                case R.id.descriptionedit:
                    if (profiledescriptioneditable) {
                        profiledescription.setEnabled(false);
                        profiledescriptioneditable = false;
                        makeEditable = true;
                        updateProfile();
                        descriptionedit.setBackgroundResource(R.drawable.edit_black);
                    } else {
                        if (makeEditable) {
                            profiledescription.setEnabled(true);
                            profiledescription.requestFocus();
                            profiledescriptioneditable = true;
                            makeEditable = false;
                            descriptionedit.setBackgroundResource(R.drawable.tick_green);
                        }else
                        {
                            validateEditatble();
                        }
                    }
                    // inflateView("test");
                    break;
                case R.id.profileimageview:
                    setProfilePicDialog();
                    break;
              /*  case R.id.expirydate:
                    expirydatePickerDialog();
                    break;*/
                default:
                    break;
            }


        }
    };

    public void datePickerDialog() {
        final Calendar mCalendar = Calendar.getInstance();
        mDateFormatter = new SimpleDateFormat(Utility.DATE_FORMAT, Locale.ENGLISH);
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(year, monthOfYear, dayOfMonth);
                mYear = year;
                mMonth = monthOfYear + 1; // moth starts with 0, there for need to add 1;
                mDay = dayOfMonth;

                String dateSetter = (new StringBuilder().append(mMonth).append("-")
                        .append(mDay).append("-").append(mYear).append(""))
                        .toString();
                if (dateSetter != null) {
                    try {
                        mCalendar.setTime(mDateFormatter.parse(dateSetter));
                    } catch (java.text.ParseException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                    month = month_date.format(mCalendar.getTime());
                }
                mDobTextView.setText(dateSetter);
              /*  mDateOfBirth = calculateAge();
                if (mDateOfBirth > 14) {
                    //Log.e("mDateOfBirth", mDateOfBirth + "");
                    mDobTextView.setText(mDateFormatter.format(mCalendar.getTime()));
                } else {
                    //mDobTextView.setText("");
                    mAlertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.app_name), getString(R.string.birth_date_validation), false);
                }*/

            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        mDatePickerDialog.show();
    }
  /*  public void expirydatePickerDialog() {
        final Calendar mCalendar = Calendar.getInstance();
        mDateFormatter = new SimpleDateFormat(Utility.DATE_FORMAT, Locale.ENGLISH);
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(year, monthOfYear, dayOfMonth);
                mYear = year;
                mMonth = monthOfYear + 1; // moth starts with 0, there for need to add 1;
                mDay = dayOfMonth;

                String dateSetter = (new StringBuilder().append(mMonth).append("-")
                        .append(mDay).append("-").append(mYear).append(""))
                        .toString();
                if (dateSetter != null) {
                    try {
                        mCalendar.setTime(mDateFormatter.parse(dateSetter));
                    } catch (java.text.ParseException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                    month = month_date.format(mCalendar.getTime());
                }
                expirydate.setText(dateSetter);
              *//*  mDateOfBirth = calculateAge();
                if (mDateOfBirth > 14) {
                    //Log.e("mDateOfBirth", mDateOfBirth + "");
                    mDobTextView.setText(mDateFormatter.format(mCalendar.getTime()));
                } else {
                    //mDobTextView.setText("");
                    mAlertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.app_name), getString(R.string.birth_date_validation), false);
                }*//*

            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        //mDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        mDatePickerDialog.show();
    }*/

    /**
     * calendar setting
     */
    private void bindDateCalender() {
        final Calendar calendar = Calendar.getInstance();
        mCurrentDateTime = calendar;
        DateFormat dateFormat = new SimpleDateFormat(Utility.DATE_FORMAT);
        if (mDobTextView.getText().toString() != null) {
            try {
                calendar.setTime(dateFormat.parse(mDobTextView.getText().toString()));
            } catch (java.text.ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat month_date = new SimpleDateFormat("MMM");
            month = month_date.format(calendar.getTime());
        } else {
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat month_date = new SimpleDateFormat("MMM");
            month = month_date.format(calendar.getTime());
        }

        mCurrentYear = mYear;
        mCurrentMonth = mMonth + 1;
        mCurrentDay = mDay;

        if (mCurrentDateTime.compareTo
                (calendar)
                > 0) {
            //mDateOfBirth = calculateAge();
            // Log.e("mDateOfBirth1", mDateOfBirth + "");
        }
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
            if (validatingRequired())
            {
                utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
                User userObj = new User();
                email = profileemail.getText().toString();
                cardnumberfromweb = cardnumber.getText().toString();
                address = addresstext.getText().toString();
                cardtype = cardtypeSpinner.getSelectedItem().toString();
                phone_number = profilephone.getText().toString();
                zipcode = zipcodeedit.getText().toString();
                //ein_taxId = profiletaxid.getText().toString();
                useruid = uid.getText().toString();
                nickname = usernickname.getText().toString();
                first_name = firstname.getText().toString();
                last_name = lastname.getText().toString();
                cvvnumber = cvvtext.getText().toString();
               // String selectedlanguageId = "";
                expmonth = expirymonthspinner.getSelectedItem().toString();
                expyear = expiryyearspinner.getSelectedItem().toString();

                about_user = profiledescription.getText().toString();
                String selectedlanguageId[] = mylanguage.split(",");
                Name name = new Name();

                name.first_name = first_name;
                name.last_name = last_name;
                Customer customerprofile = new Customer();
                customerprofile.phone_number = phone_number;
                customerprofile.address = address;
                customerprofile.name = name;
                customerprofile.id = id;
                customerprofile.city = city;
                customerprofile.country = country;
                customerprofile.state = state;
                customerprofile.email = email;
                customerprofile.password = password;
                customerprofile.nickname = nickname;
                customerprofile.about_user = about_user;
                Payment_info payment_info = new Payment_info();
                payment_info.card_type = cardtype;
                payment_info.card_number = cardnumberfromweb;
                payment_info.exp_month = expmonth;
                payment_info.exp_year = expyear;
                payment_info.cvv = cvvnumber;
                customerprofile.payment_info = payment_info;

                //customerprofile.ein_taxId =  ein_taxId;
                customerprofile.zipcode = zipcode;
                customerprofile.mylanguage = selectedlanguageId;
                userObj.Authorization = token;
                userProfileManager.updateCustomerProfile(customerprofile);
                profilephone.setEnabled(false);
                phoneedtitable = false;
            }
        } else {
            utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
        }
    }


    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void enableCompulsouryFields() {
        profiledescription.setEnabled(true);
        profiledescriptioneditable = true;
        makeEditable = false;
        descriptionedit.setBackgroundResource(R.drawable.tick_green);

        cvvtext.setEnabled(true);
        cvvediteditable = true;
        makeEditable = false;
        cvvedit.setBackgroundResource(R.drawable.tick_green);

        addresstext.setEnabled(true);
        addresseditable = true;
        makeEditable = false;
        editaddress.setBackgroundResource(R.drawable.tick_green);

        profilephone.setEnabled(true);
        phoneedtitable = true;
        makeEditable = false;
        profilephoneedit.setBackgroundResource(R.drawable.tick_green);

        expirymonthspinner.setClickable(true);
        expirymontheditable = true;
        makeEditable = false;
        expirymonthedit.setBackgroundResource(R.drawable.tick_green);

        expiryyearspinner.setClickable(true);
        expiryyeareditable = true;
        makeEditable = false;
        expiryyearedit.setBackgroundResource(R.drawable.tick_green);

        cardnumber.setEnabled(true);
        cardnumbereditable = true;
        makeEditable = false;
        cardnumberedit.setBackgroundResource(R.drawable.tick_green);

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

    public void disableCompulsouryFields() {
        makeEditable = true;
        profilephone.setEnabled(false);
        phoneedtitable = false;

        profilephoneedit.setBackgroundResource(R.drawable.edit_black);

        expirymonthspinner.setClickable(false);
        expirymontheditable = false;

        expirymonthedit.setBackgroundResource(R.drawable.edit_black);

        expiryyearspinner.setClickable(false);
        expiryyeareditable = false;

        expiryyearedit.setBackgroundResource(R.drawable.edit_black);

        cardnumber.setEnabled(false);
        cardnumbereditable = false;

        cardnumberedit.setBackgroundResource(R.drawable.edit_black);

        cityedit.setEnabled(false);
        cityeditable = false;

        editcity.setBackgroundResource(R.drawable.edit_black);

        lastname.setEnabled(false);
        lastnameeditable = false;

        editlastname.setBackgroundResource(R.drawable.edit_black);

        zipcodeedit.setEnabled(false);
        zipeditable = false;

        editzip.setBackgroundResource(R.drawable.edit_black);

        usernickname.setEnabled(false);
        nicknameeditable = false;

        editnickname.setBackgroundResource(R.drawable.edit_black);

        lastname.setEnabled(false);
        lastnameeditable = false;

        editlastname.setBackgroundResource(R.drawable.edit_black);

        firstname.setEnabled(false);
        firstnameeditable = false;

        editfirstname.setBackgroundResource(R.drawable.edit_black);

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

    public void multiSelectLanguageDialog()

    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.multi_select_language_dialog);
        final ListView languageList = (ListView) dialog.findViewById(R.id.language_list_view);
        //languageList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //languageList.setItemsCanFocus(false);
        final LanguageAdapter languageAdapter = new LanguageAdapter(getActivity(), 1, mLanguageArrayList,mylanguage, false, UpdateCustomerProfileFragment.this);
        languageList.setAdapter(languageAdapter);

        languageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Language language = mLanguageArrayList.get(i);
                String url = language.getIcon();
                url = url.replace("<img src='", "");
                url = url.replace("'  />", "");
                mSelectedItems.add(language.getLanguage());
                mSelectedItemImage.add(url);
                mSelectedLanguageList = new ArrayList<Language>();
                mSelectedLanguageList.add(language);
                mylanguage = language.getLanguageId();
                LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams((int) (800), LinearLayout.LayoutParams.WRAP_CONTENT);
                mLanguageListView.setLayoutParams(mParam);

                languageListAdapter = new LanguageListAdapter(getActivity(),mSelectedLanguageList, UpdateCustomerProfileFragment.this);
                mLanguageListView.setAdapter(languageListAdapter);
                dialog.dismiss();
            }
        });


        TextView saveTextView = (TextView) dialog.findViewById(R.id.save_text_view);
        TextView cancelTextView = (TextView) dialog.findViewById(R.id.cancel_text_view);
        saveTextView.setVisibility(View.GONE);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedItemImage.size() > 1) {
                    LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams((int) (800), (int) (350));
                    mLanguageListView.setLayoutParams(mParam);
                    updateProfile();
                } else {
                    LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams((int) (800), LinearLayout.LayoutParams.WRAP_CONTENT);
                    mLanguageListView.setLayoutParams(mParam);
                }

                languageListAdapter = new LanguageListAdapter(getActivity(), mSelectedLanguageList, UpdateCustomerProfileFragment.this);
                mLanguageListView.setAdapter(languageListAdapter);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean validatingRequired() {
        String message = "";
        email = profileemail.getText().toString();
        cardnumberfromweb = cardnumber.getText().toString();
        address = addresstext.getText().toString();

        phone_number = profilephone.getText().toString();
        zipcode = zipcodeedit.getText().toString();
        //ein_taxId = profiletaxid.getText().toString();
        useruid = uid.getText().toString();
        nickname = usernickname.getText().toString();
        first_name = firstname.getText().toString();
        last_name = lastname.getText().toString();
        cvvnumber = cvvtext.getText().toString();
        city = cityedit.getText().toString();
        expmonth = expirymonthspinner.getSelectedItem().toString();
        expyear = expiryyearspinner.getSelectedItem().toString();
        cvvnumber = cvvtext.getText().toString();

        //validate the content


        if(phone_number.isEmpty())
        {
            message = getResources().getString(R.string.PhoneNumberMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(first_name.isEmpty())
        {
            message = getResources().getString(R.string.FirstnameMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(last_name.isEmpty())
        {
            message = getResources().getString(R.string.lastnameMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(address.isEmpty())
        {
            message = getResources().getString(R.string.AddressMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(zipcode.isEmpty())
        {
            message = getResources().getString(R.string.ZipcodeMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(city.isEmpty())
        {
            message = getResources().getString(R.string.CityMessage);
            utilObj.showToast(getActivity(), message, 0);

        }

        if(nickname.isEmpty())
        {
            message = getResources().getString(R.string.NicknameMessage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(cvvnumber.isEmpty())
        {
            message = getResources().getString(R.string.CVVmesseage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(cardnumberfromweb.isEmpty())
        {
            message = getResources().getString(R.string.Cardnumbermesseage);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(expmonth.isEmpty())
        {
            message = getResources().getString(R.string.Expirymonth);
            utilObj.showToast(getActivity(), message, 0);

        }
        if(expyear.isEmpty())
        {
            message = getResources().getString(R.string.ExpiryYear);
            utilObj.showToast(getActivity(), message, 0);

        }


        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }
    public void validateEditatble()
    {
        String message="";


        if(firstnameeditable)
        {
            message = getResources().getString(R.string.FirstnameMessage);
            utilObj.showToast(getActivity(), message, 0);
            return ;

        }
        if(lastnameeditable)
        {
            message = getResources().getString(R.string.lastnameMessage);
            utilObj.showToast(getActivity(), message, 0);
            return ;

        }
        if(addresseditable)
        {
            message = getResources().getString(R.string.AddressMessage);
            utilObj.showToast(getActivity(), message, 0);
            return ;

        }
        if(phoneedtitable)
        {
            message = getResources().getString(R.string.EmailErrorMessage);
            utilObj.showToast(getActivity(), message, 0);
            return ;

        }
        if(zipeditable)
        {
            message = getResources().getString(R.string.ZipcodeMessage);
            utilObj.showToast(getActivity(), message, 0);
            return ;

        }
        if(cityeditable)
        {
            message = getResources().getString(R.string.CityMessage);
            utilObj.showToast(getActivity(), message, 0);
            return ;
        }

        if(nicknameeditable)
        {
            message = getResources().getString(R.string.NicknameMessage);
            utilObj.showToast(getActivity(), message, 0);
            return ;

        }

        if(cardnumbereditable)
        {
            message = getResources().getString(R.string.Cardnumbermesseage);
            utilObj.showToast(getActivity(), message, 0);
            return ;

        }
        if(cvvediteditable)
        {
            message = getResources().getString(R.string.CVVmesseage);
            utilObj.showToast(getActivity(), message, 0);
            return ;

        }
        if(expirymontheditable)
        {
            message = getResources().getString(R.string.Expirymonth);
            utilObj.showToast(getActivity(), message, 0);
            return ;

        }
        if(expiryyeareditable)
        {
            message = getResources().getString(R.string.ExpiryYear);
            utilObj.showToast(getActivity(), message, 0);
            return ;

        }



    }
}
