package com.smartdatainc.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdatainc.adapters.LanguageAdapter;
import com.smartdatainc.app.ooVooSdkSampleShowApp;
import com.smartdatainc.call.CNMessage;
import com.smartdatainc.dataobject.CallDeatils;
import com.smartdatainc.dataobject.Language;
import com.smartdatainc.dataobject.User;
import com.smartdatainc.dataobject.UserProfile;
import com.smartdatainc.interfaces.LanguageListInterface;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.UserProfileManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.CircularNoBorderImageView;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p/>
 * to handle interaction events.
 * Use the {@link InterpretationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InterpretationFragment extends BaseFragment implements ServiceRedirection,ooVooSdkSampleShowApp.CallNegotiationListener, View.OnClickListener, LanguageListInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    UserProfileManager userProfileManager;
    TextView profilename;
    ArrayList<Language> totallist = new ArrayList<>();
    private Utility utilObj;
    UserProfile userProfile;
    Button manageprofile;
    private ToggleButton avaiblitytoggle;
    // AQuery object
    String email = "";
    String id = "";
    Uri mImageUri;
    String mProfilePic;
    Bitmap mBitmap;
    String tolanguageId = "";
    String fromlanguageId = "";
    String mylanguage;
    private ooVooSdkSampleShowApp application = null;
    private CircularNoBorderImageView mFromLanguageImageView, mToLanguageImageView;
    private TextView mFromLanguageTextView, mToLanguageTextView, mAmountTextView,mConfirmCall;
    private int count = 0;
    private View mRootView;
    private ArrayList<Language> mLanguageArrayList;
    boolean isFrom;
    public static ArrayList<String> toList ;
    public static ArrayList<String> toUserIdList ;
    AQuery mAQuery;
    private AlertDialog callDialogBuilder = null;
    private AlertDialog callReceiverDialog = null;
    boolean checkcallStatus =false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InterprterDashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InterpretationFragment newInstance(String param1, String param2) {
        InterpretationFragment fragment = new InterpretationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InterpretationFragment() {
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
        mRootView = inflater.inflate(R.layout.fragment_select_interpretation, container, false);
        application = (ooVooSdkSampleShowApp) getActivity().getApplication();
        application.setContext(getActivity());
        bindData();
        bindListener();
        callDialogBuilder = new AlertDialog.Builder(getActivity()).create();

        View outgoingCallDialog = inflater.inflate(R.layout.outgoing_call_dialog, null);
        outgoingCallDialog.setAlpha(0.5f);
        callDialogBuilder.setView(outgoingCallDialog);

        Button callCancelButton = (Button) outgoingCallDialog.findViewById(R.id.cancel_button);
        callCancelButton.setOnClickListener(this);

        callDialogBuilder.setCancelable(false);

        app().addCallNegotiationListener(this);
        if (isNetworkAvailable(getActivity())) {
            String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
            utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
            User userObj = new User();
            userObj.Authorization = token;
            userProfileManager.customerProfile(userObj);
        } else {
            utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
        }
        return mRootView;
    }

    private void bindListener() {
        mFromLanguageImageView.setOnClickListener(this);
        mFromLanguageTextView.setOnClickListener(this);
        mToLanguageImageView.setOnClickListener(this);
        mToLanguageTextView.setOnClickListener(this);
        mConfirmCall.setOnClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event


    public void bindData() {
        mAQuery = new AQuery(getActivity());
        utilObj = new Utility(getActivity());
        userProfileManager = new UserProfileManager(getActivity(), this);
        mFromLanguageImageView = (CircularNoBorderImageView) mRootView.findViewById(R.id.from_language_image_view);
        mToLanguageImageView = (CircularNoBorderImageView) mRootView.findViewById(R.id.to_language_image_view);
        mFromLanguageTextView = (TextView) mRootView.findViewById(R.id.from_language_text_view);
        mToLanguageTextView = (TextView) mRootView.findViewById(R.id.to_language_text_view);
        mAmountTextView = (TextView) mRootView.findViewById(R.id.amount_text_view);
        mConfirmCall =(TextView) mRootView.findViewById(R.id.confirmCall);


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

    private boolean sendCNMessage(CNMessage.CNMessageType type, ooVooSdkSampleShowApp.MessageCompletionHandler completionHandler)
    {

        if (toList.isEmpty()) {
            return false;
        }

        app().sendCNMessage(toList, type, completionHandler);

        return true;
    }
    public void callUser()
    {
        app().generateConferenceId();
        boolean showDialog = sendCNMessage(CNMessage.CNMessageType.Calling, new ooVooSdkSampleShowApp.MessageCompletionHandler() {
            @Override
            public void onHandle(boolean state) {



                if (!state) {
                    // count = 0 ;
                    Toast.makeText(getActivity(), R.string.fail_to_send_message, Toast.LENGTH_LONG).show();
                    callDialogBuilder.hide();
                    return  ;
                }

                // count = enabledReceivesCount;
            }
        });

        if (showDialog) {
            callDialogBuilder.show();
            new CountDownTimer(30000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    if(callDialogBuilder.isShowing()) {
                        callDialogBuilder.dismiss();
                        sendCNMessage(CNMessage.CNMessageType.Cancel, null);
                        if(!checkcallStatus) {
                            dismissCall();
                        }else
                        {
                            Log.v("Elsetag","Else");
                        }
                    }
                }
            }.start();
        } else {
            //  Toast.makeText(getActivity(), R.string.no_receivers, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopLoader();

    }
    @Override
    public void onDestroyView()
    {
        if (callReceiverDialog != null) {
            callReceiverDialog.getWindow().setSoftInputMode(0);
            callReceiverDialog.hide();
        }
        callDialogBuilder.hide();
        app().removeCallNegotiationListener(this);
        super.onDestroyView();
    }

    public void onSuccessRedirection(int taskID, String data) {
        if (utilObj != null) {
            utilObj.stopLoader();
        }
        if (taskID == Constants.TaskID.GET_LANGUAGE_LIST_TASK_ID) {
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
                        languageDataModel.setLanguageId(languageId);
                        languageDataModel.setLanguage(language);
                        mLanguageArrayList.add(languageDataModel);
                    }
                    multiSelectLanguageDialog();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else   if (taskID == Constants.TaskID.GET_LANGUAGE_LIST_INTERPRETETION_TASK_ID) {
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
                    while (languageListitr.hasNext()) {
                        Language languaeitemObj = languageListitr.next();
                        if (selectedLangugeArray != null) {
                            for (int l = 0; l < selectedLangugeArray.length; l++) {

                                if (selectedLangugeArray[l].equalsIgnoreCase(languaeitemObj.getLanguageId())) {
                                    //languageCheckBox.setChecked(true);
                                    //  mSelectedLanguageList.add(languaeitemObj);
                                    String url = languaeitemObj.getIcon();
                                    url = url.replace("<img src='", "");
                                    url = url.replace("'  />", "");
                                    languaeitemObj.getIcon();
                                    languaeitemObj.getLanguage();
                                    mFromLanguageTextView.setText(languaeitemObj.getLanguage());
                                    fromlanguageId = languaeitemObj.getId();
                                    mAQuery.id(R.id.from_language_image_view).image(Constants.WebServices.WS_BASE_URL + url);
                                }

                            }
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (taskID == Constants.TaskID.CUSTOMER_PROFILE_TASK_ID) {
            UserProfile userProfile = new UserProfile();
            try {
                JSONObject jsonObj = new JSONObject(data);
                JSONObject jsonobjUser = jsonObj.getJSONObject("user");
                JSONArray mylanguageArray = jsonobjUser.optJSONArray("mylanguage");
                if (mylanguageArray != null) {
                    for (int length = 0; length < mylanguageArray.length(); length++) {
                        String language = (String) mylanguageArray.get(length);
                        if (length == 0) {
                            mylanguage = language;
                        } else {
                            mylanguage = mylanguage + "," + language;
                        }
                        Log.v("Lang", language);
                    }
                }
                if (isNetworkAvailable(getActivity())) {
                    String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
                    utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                    User userObj = new User();
                    userObj.Authorization = token;
                    userProfileManager.getLanguageListForInterpretion(userObj);
                } else {
                    utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        } else if (taskID == Constants.TaskID.GET_INTERPRETATION_DETAILS_TASK_ID) {
            String languagePrice = "";
            try {
                JSONObject jsonObj = new JSONObject(data);
                JSONArray calluserArrayObj = jsonObj.optJSONArray("data");

                if (calluserArrayObj != null) {
                    for (int c = 0; c < calluserArrayObj.length(); c++) {
                        JSONObject languageJsonObj = calluserArrayObj.getJSONObject(c);
                        languagePrice = languageJsonObj.optString("languagePrice");
                    }
                }
                utilObj.saveDataInSharedPreferences("Call", getActivity().MODE_PRIVATE, "languagePrice", "" + languagePrice);

                mAmountTextView.setText(getResources().getString(R.string.interpreter_charge_amount) + " $" + languagePrice + ".00" + " per min.");


            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (taskID == Constants.TaskID.GET_INTERPRETATION_POOL_TASK_ID) {
            String languagePrice = "";
            try {
                JSONObject jsonObj = new JSONObject(data);
                JSONArray calluserArrayObj = jsonObj.optJSONArray("data");
                toList = new ArrayList<String>();
                toUserIdList = new ArrayList<String>();
                for (int languageArray = 0; languageArray < calluserArrayObj.length(); languageArray++) {
                    JSONObject languageJsonObj = calluserArrayObj.getJSONObject(languageArray);
                    String uid = languageJsonObj.get("uid").toString();
                    String id = languageJsonObj.get("id").toString();
                    toList.add(uid);
                    toUserIdList.add(id);

                }
                String poolId = jsonObj.optString("poolId");

               /* if (data.contains("price")) {
                    JSONArray jsonObjArray = jsonObj.optJSONArray("price");
                    if (jsonObjArray != null) {
                        for (int c = 0; c < jsonObjArray.length(); c++) {
                            JSONObject languageJsonObj = jsonObjArray.getJSONObject(c);
                            languagePrice = languageJsonObj.optString("languagePrice");
                        }
                    }
                    mAmountTextView.setText(getResources().getString(R.string.interpreter_charge_amount) + " $" + languagePrice + ".00" + " per min.");
                } else {
                    // mAmountTextView.setText(jsonObj.getString("message"));
                }*/

                utilObj.saveDataInSharedPreferences("Call", getActivity().MODE_PRIVATE, "tolanguageId", tolanguageId);
                utilObj.saveDataInSharedPreferences("Call", getActivity().MODE_PRIVATE, "fromlanguageId", fromlanguageId);
                utilObj.saveDataInSharedPreferences("Call", getActivity().MODE_PRIVATE, "poolId", poolId);
                callUser();


            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (taskID == Constants.TaskID.SET_CALL_DETAILS_TASK_ID) {
            if (data != null) {

                //utilObj.showToast(getActivity(), data, 1);
                app().join(app().getConferenceId(), true);


            }
        } else if (taskID == Constants.TaskID.SET_CALL_CANCEL_TASK_ID) {
            if (data != null) {

                //utilObj.showToast(getActivity(), data, 1);
                //  app().join(app().getConferenceId(), true);
                sendCNMessage(CNMessage.CNMessageType.Cancel, null);

            }
        }


    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopLoader();
        // utilObj.showError(this, errorMessage, textViewObj, null);
        utilObj.showToast(getActivity(), errorMessage, 0);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.from_language_image_view:
            case R.id.from_language_text_view:
                callLanguageList();
                isFrom = true;
                break;
            case R.id.to_language_image_view:
                break;
            case R.id.confirmCall:

                if (isNetworkAvailable(getActivity())) {
                    if(toList!=null) {
                        toList.clear();
                    }
                    String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
                    utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                    User userObj = new User();
                    //userObj.Authorization = token;

                    // language.getLanguageId();
                    // userObj.fromLanguage = mFromLanguageTextView.getText().toString();
                    //userObj.toLanguage = mToLanguageTextView.getText().toString();
                    userObj.fromLanguage = fromlanguageId;
                    userObj.toLanguage = tolanguageId;
                    userProfileManager.interpreterPoolDetails(userObj);
                }

                //callUser();
                break;

            case R.id.cancel_button:
                callDialogBuilder.hide();


                dismissCall();

                //  String date = utilObj.getDateCurrentTimeZone(timeStamp);
          /*  Timestamp timestamp = new Timestamp(timeStamp);
            Date date = new Date(timestamp.getTime());*/
              /*  long timeStamp = System.currentTimeMillis();
                String[] interpreterId = toUserIdList.toArray(new String[toUserIdList.size()]);
                String userid = utilObj.readDataInSharedPreferences("Users", 0, "id");
                String poolId = utilObj.readDataInSharedPreferences("Call", 0, "poolId");

                CallDeatils  callDeatils = new CallDeatils();
                callDeatils.poolId = poolId;
                callDeatils.userId = userid;
                callDeatils.interpreterId = interpreterId;
                callDeatils.callReceivedBy = "";
                callDeatils.callId = "";
                callDeatils.start_time = ""+timeStamp;


                userProfileManager.userCallCancel(callDeatils);*/

                break;
            case R.id.to_language_text_view:
                callLanguageList();
                isFrom = false;
                break;

        }
    }

    public void multiSelectLanguageDialog()

    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.multi_select_language_dialog);
        ListView languageList = (ListView) dialog.findViewById(R.id.language_list_view);
        String mylanguage="";
        String toId="";
        LanguageAdapter languageAdapter = new LanguageAdapter(getActivity(), 1, mLanguageArrayList, mylanguage,false, InterpretationFragment.this);
        languageList.setAdapter(languageAdapter);
        languageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Language language = mLanguageArrayList.get(i);
                String url = language.getIcon();
                url = url.replace("<img src='", "");
                url = url.replace("'  />", "");
                if (isFrom) {
                    mFromLanguageTextView.setText(language.getLanguage());
                    fromlanguageId = language.getId();
                    mAQuery.id(R.id.from_language_image_view).image(Constants.WebServices.WS_BASE_URL + url);
                } else {
                    tolanguageId = language.getId();
                    mToLanguageTextView.setText(language.getLanguage());
                    mAQuery.id(R.id.to_language_image_view).image(Constants.WebServices.WS_BASE_URL + url);
                }

                dialog.dismiss();
                if (!mFromLanguageTextView.getText().toString().equalsIgnoreCase("") && !mToLanguageTextView.getText().toString().equalsIgnoreCase("")) {
                    if (isNetworkAvailable(getActivity())) {
                        String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
                        utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                        User userObj = new User();
                        //userObj.Authorization = token;

                        // language.getLanguageId();
                        // userObj.fromLanguage = mFromLanguageTextView.getText().toString();
                        //userObj.toLanguage = mToLanguageTextView.getText().toString();
                        userObj.fromLanguage = fromlanguageId;
                        userObj.toLanguage = tolanguageId;
                        utilObj.saveDataInSharedPreferences("Call", getActivity().MODE_PRIVATE, "fromlanguageId",""+fromlanguageId);
                        utilObj.saveDataInSharedPreferences("Call", getActivity().MODE_PRIVATE, "tolanguageId",""+tolanguageId);
                        userProfileManager.interpreterLanguageDetails(userObj);
                    } else {
                        utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
                    }
                }
            }
        });

        TextView saveTextView = (TextView) dialog.findViewById(R.id.save_text_view);
        TextView cancelTextView = (TextView) dialog.findViewById(R.id.cancel_text_view);
        //cancelTextView.setVisibility(View.GONE);
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
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void callLanguageList() {
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
    public void callLanguageListForDeafultLanguage() {
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

    @Override
    public void onLanguageRemove(int position) {

    }

    @Override
    public void onLanguageSelectObj(ArrayList<Language> items) {

    }

    @Override
    public void onLanguageSelect(ArrayList<String> items, ArrayList<String> itemImage) {

    }


    @Override
    public void onMessageReceived(CNMessage cnMessage) {

        if (app().getUniqueId().equals(cnMessage.getUniqueId())) {
            return;
        }


        if (cnMessage.getMessageType() == CNMessage.CNMessageType.AnswerAccept) {
            callDialogBuilder.hide();


            checkcallStatus = true;

            String from = cnMessage.getFromId();
            String to = cnMessage.getToId();
            String callId = cnMessage.getConferenceId();
            long timeStamp = cnMessage.getTimeStamp();

            //  String date = utilObj.getDateCurrentTimeZone(timeStamp);
          /*  Timestamp timestamp = new Timestamp(timeStamp);
            Date date = new Date(timestamp.getTime());*/
            String[] interpreterId = toUserIdList.toArray(new String[toUserIdList.size()]);
            String userid = utilObj.readDataInSharedPreferences("Users", 0, "id");
            String poolId = utilObj.readDataInSharedPreferences("Call", 0, "poolId");

            CallDeatils  callDeatils = new CallDeatils();
            callDeatils.poolId = poolId;
            callDeatils.userId = userid;
            callDeatils.interpreterId = interpreterId;
            callDeatils.callReceivedBy = from;
            callDeatils.callId = callId;
            callDeatils.start_time = ""+timeStamp;


            userProfileManager.userCallDetail(callDeatils);
            //String sharedPrefName, int mode, String key, String value
            // utilObj.saveDataInSharedPreferences("Call", getActivity().MODE_PRIVATE, "from",from);
            //  utilObj.saveDataInSharedPreferences("Call", getActivity().MODE_PRIVATE, "to",to);
            utilObj.saveDataInSharedPreferences("Call", getActivity().MODE_PRIVATE, "starttimeStamp",""+timeStamp);
            utilObj.saveDataInSharedPreferences("Call", getActivity().MODE_PRIVATE, "callto",from);

        } else if (cnMessage.getMessageType() == CNMessage.CNMessageType.AnswerDecline) {
            count--;
            if (count <= 0) {
                //callDialogBuilder.hide();
            }
        }  else if (cnMessage.getMessageType() == CNMessage.CNMessageType.Busy) {
            count--;
            if (count <= 0) {
                callDialogBuilder.hide();
            }
        }
        else if (cnMessage.getMessageType() == CNMessage.CNMessageType.EndCall) {
            //  callDialogBuilder.hide();
            String from = cnMessage.getFromId();
            String to = cnMessage.getToId();
            long timeStamp = cnMessage.getTimeStamp();
            app().join(app().getConferenceId(), true);



        }

    }
    public void dismissCall()
    {
        long timeStamp = System.currentTimeMillis();
        String[] interpreterId = toUserIdList.toArray(new String[toUserIdList.size()]);
        String userid = utilObj.readDataInSharedPreferences("Users", 0, "id");
        String poolId = utilObj.readDataInSharedPreferences("Call", 0, "poolId");

        CallDeatils  callDeatils = new CallDeatils();
        callDeatils.poolId = poolId;
        callDeatils.userId = userid;
        callDeatils.interpreterId = interpreterId;
        callDeatils.callReceivedBy = "";
        callDeatils.callId = "";
        callDeatils.start_time = ""+timeStamp;


        userProfileManager.userCallCancel(callDeatils);
    }
}
