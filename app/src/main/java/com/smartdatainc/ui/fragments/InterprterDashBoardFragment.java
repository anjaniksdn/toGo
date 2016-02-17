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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.smartdatainc.activities.InterpreterDashboardActivity;
import com.smartdatainc.app.ooVooSdkSampleShowApp;
import com.smartdatainc.call.CNMessage;
import com.smartdatainc.call.CNMessage.CNMessageType;
import com.smartdatainc.dataobject.InterepreterdashBoard;
import com.smartdatainc.dataobject.InterprterDashBoardRequest;
import com.smartdatainc.dataobject.Language;
import com.smartdatainc.dataobject.User;
import com.smartdatainc.dataobject.UserProfile;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p/>
 * to handle interaction events.
 * Use the {@link InterprterDashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InterprterDashBoardFragment extends BaseFragment implements ServiceRedirection,ooVooSdkSampleShowApp.CallNegotiationListener {
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
    TextView profilename,callEarnings,totalCall,callMinutes;
    private AlertDialog callDialogBuilder = null;
    private AlertDialog callReceiverDialog = null;
    ArrayList<Language> totallist = new ArrayList<>();
    private Utility utilObj;
    UserProfile userProfile;
    CircularImageView profileimageview;
    Button manageprofile,customerfeedback;
    private ToggleButton avaiblitytoggle;
    // AQuery object
    private AQuery aq;
    String email = "";
    String id = "";
    String token="";
    Uri mImageUri;
    String mProfilePic;
    Bitmap mBitmap;
    private int count = 0;
    private int enabledReceivesCount = 0;
    ArrayList<String> toList = new ArrayList<String>();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InterprterDashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InterprterDashBoardFragment newInstance(String param1, String param2) {
        InterprterDashBoardFragment fragment = new InterprterDashBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InterprterDashBoardFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        profilename = (TextView) view.findViewById(R.id.profilename);
        callEarnings = (TextView) view.findViewById(R.id.callEarnings);
        totalCall = (TextView) view.findViewById(R.id.totalCall);
        callMinutes= (TextView) view.findViewById(R.id.callMinutes);
        profileimageview = (CircularImageView) view.findViewById(R.id.profileimageview);
        avaiblitytoggle = (ToggleButton) view.findViewById(R.id.avaiblitytoggle);

        manageprofile = (Button) view.findViewById(R.id.manageprofile);
        customerfeedback= (Button) view.findViewById(R.id.customerfeedback);
        manageprofile.setOnClickListener(onClickListener);
        customerfeedback.setOnClickListener(onClickListener);
        avaiblitytoggle.setOnClickListener(onClickListener);
        profileimageview.setOnClickListener(onClickListener);
        callDialogBuilder = new AlertDialog.Builder(getActivity()).create();

        View outgoingCallDialog = inflater.inflate(R.layout.outgoing_call_dialog, null);
        outgoingCallDialog.setAlpha(0.5f);
        callDialogBuilder.setView(outgoingCallDialog);

        Button cancelButton = (Button) outgoingCallDialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(onClickListener);

        callDialogBuilder.setCancelable(false);

        app().addCallNegotiationListener(this);
        initdata();


        /*avaiblitytoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                    String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
                    User userObj = new User();
                    userObj.Authorization = token;
                    userProfileManager.updateAviblity(userObj);
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });*/

        token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
        id = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "id");
        email= utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "email");

       /* if (isNetworkAvailable(getActivity())) {
            utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
            InterprterDashBoardRequest interprterDashBoardRequest = new InterprterDashBoardRequest();
            interprterDashBoardRequest.Authorization = token;
            interprterDashBoardRequest.id =id;
            interprterDashBoardRequest.type  ="interpreter";
            userProfileManager.interpreterDashBoard(interprterDashBoardRequest);
        } else {
            utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
        }*/

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    public void initdata() {
        utilObj = new Utility(getActivity());
        userProfileManager = new UserProfileManager(getActivity(), this);
        aq = new AQuery(getActivity());
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.profile_icon)
                .showImageOnFail(R.drawable.profile_icon).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();



    }

    @Override
    public void onResume() {
        super.onResume();

        if (isNetworkAvailable(getActivity())) {
            try {
                //Thread.sleep(3000);
                utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                InterprterDashBoardRequest interprterDashBoardRequest = new InterprterDashBoardRequest();
                interprterDashBoardRequest.Authorization = token;
                interprterDashBoardRequest.id =id;
                interprterDashBoardRequest.type  ="interpreter";
                userProfileManager.interpreterDashBoard(interprterDashBoardRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
        }

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




    /*  */
    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.manageprofile:
                    Fragment fragment = new UpdateInterpreterProfile();
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
                case R.id. cancel_button:
                    callDialogBuilder.hide();
                    sendCNMessage(CNMessage.CNMessageType.Cancel, null);
                    break;
                case R.id.avaiblitytoggle:
                    String status;
                    if (isNetworkAvailable(getActivity())) {
                        utilObj.startLoader(getActivity(), R.drawable.image_for_rotation);
                        if (avaiblitytoggle.isChecked()) {

                            // Toast.makeText(getActivity(), "Hi", Toast.LENGTH_LONG).show();
                            status = "true";

                        } else {
                            status = "false";
                        }
                        String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
                        User userObj = new User();
                        InterepreterdashBoard interepreterdashBoard = new InterepreterdashBoard();
                        interepreterdashBoard.interpreter_availability = status;
                        interepreterdashBoard.id = id;
                        interepreterdashBoard.email = email;
                        userObj.Authorization = token;
                        userProfileManager.updateAviblity(interepreterdashBoard);
                    } else {
                        utilObj.showToast(getActivity(), Constants.NOINTERNET, 0);
                    }

                    //Do what you want for create_button
                    break;
                case R.id.profileimageview:
                    //setProfilePicDialog();
                    break;
                case R.id.customerfeedback:
                    // callUser();
                    break;
                default:
                    break;
            }


        }
    };
    /*@Override
    public void onClick(View v) {

        //callDialogBuilder.hide();
        Toast.makeText(getActivity(),"Hi",Toast.LENGTH_LONG).show();
        switch (v.getId()) {
            case R.id.cancel_button:
            {
                sendCNMessage(CNMessage.CNMessageType.Cancel, null);
            }
            break;
            case R.id.manageprofile:
                Fragment fragment = new UpdateInterpreterProfile();
                //Bundle args = new Bundle();
                //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
                //fragment.setArguments(args);

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.host_activity, fragment)
                        .commit();

                break;
         *//*   case R.id.avaiblitytoggle:
                if(avaiblitytoggle.isChecked())
                {
                    String token = utilObj.readDataInSharedPreferences("Users", getActivity().MODE_PRIVATE, "_token");
                    User userObj = new User();
                    userObj.Authorization = token;
                    ///userProfileManager.updateAviblity(userObj);
                }
                break;*//*

            default:
                break;
        }
    }*/

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
        if (taskID == Constants.TaskID.USER_PROFILE_TASK_ID) {
            boolean interpreter_availability = false;
            //  String[] languageArray = new String[20];
            String url = "";
            try {
                UserProfile userProfile = new UserProfile();
                JSONObject jsonObj = new JSONObject(data);
                JSONObject jsonobjUser = jsonObj.getJSONObject("user");
                JSONObject profile_img = jsonobjUser.getJSONObject("profile_img");


                url = profile_img.getString("url");
                String country = jsonobjUser.optString("email");
                email = jsonobjUser.optString("email");
                id = jsonobjUser.optString("id");
                String password = jsonobjUser.optString("password");
                String phone_number = jsonobjUser.optString("phone_number");
                String mylanguage = jsonobjUser.optString("mylanguage");
                String address = jsonobjUser.optString("address");
                String ein_taxId = jsonobjUser.optString("ein_taxId");
                String nickname = jsonobjUser.optString("nickname");
                String useruid = jsonobjUser.optString("uid");
                //  String status = jsonobjUser.optString("status");
                interpreter_availability = jsonobjUser.optBoolean("interpreter_availability");
                //String imageurl = jsonobj.getJSONObject("profile_img").getString("url");
                String first_name = jsonobjUser.getJSONObject("name").optString("first_name");
                String last_name = jsonobjUser.getJSONObject("name").optString("last_name");

                if (first_name != null) {
                    userProfile.setName(first_name + " " + last_name);
                }
                profilename.setText(userProfile.getName());
                // profilename.setText(interpreterProfile.getName());
                // String country = jsonobj.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (interpreter_availability) {
                avaiblitytoggle.setChecked(true);
                //avaiblitytoggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_on));
            } else {
                avaiblitytoggle.setChecked(false);
                // avaiblitytoggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_off));
            }

            if (taskID == Constants.TaskID.LOGIN_TASK_ID) {

            }

            if (url != null && url.length() > 0) {
                aq.id(R.id.profileimageview).image(url);
            }

        }
        if (taskID == Constants.TaskID.UPDATE_AVAILAIBLITY_TASK_ID) {

        }

        if (taskID == Constants.TaskID.GET_INTERPRETE_DASHBOARD_TASK_ID) {
            try {
                JSONObject jsonObj = new JSONObject(data);
                JSONArray getProfileInfojsonArray =jsonObj.optJSONArray("getProfileInfo");
                JSONObject profileArray = getProfileInfojsonArray.optJSONObject(0);
                JSONObject profile_imgObj = profileArray.optJSONObject("profile_img");
                String url = profile_imgObj.getString("url");
                boolean interpreter_availability = profileArray.optBoolean("interpreter_availability");
                String  nickname= profileArray.optString("interpreter_availability");
                //String imageurl = jsonobj.getJSONObject("profile_img").getString("url");
                String first_name = profileArray.getJSONObject("name").optString("first_name");
                String last_name = profileArray.getJSONObject("name").optString("last_name");


                //       getProfileInfojsonArray.optString()
                String  totalNoCalls = jsonObj.optString("totalNoCalls");
                String  totalCallMinutes = jsonObj.optString("totalCallMinutes");
                String  totalCallAmount = jsonObj.optString("totalCallAmount");
                profilename.setText(first_name +" "+last_name);
                callEarnings.setText(totalCallAmount);
                callMinutes.setText(totalCallMinutes);
                totalCall.setText(totalNoCalls);
                if (interpreter_availability) {
                    avaiblitytoggle.setChecked(true);
                    //avaiblitytoggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_on));
                } else {
                    avaiblitytoggle.setChecked(false);
                    // avaiblitytoggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_off));
                }
                if (url != null && url.length() > 0) {
                    aq.id(R.id.profileimageview).image(url);
                }

            } catch(Exception e)
            {

            }
        }

    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopLoader();
        // utilObj.showError(this, errorMessage, textViewObj, null);
        utilObj.showToast(getActivity(), errorMessage, 0);

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
        String profilePic = Base64.encodeToString(b, Base64.DEFAULT);
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

    /**
     * confirmation dialog password.
     */
    private void confirmationDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert_dialog);
        View v = getActivity().getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        TextView confirmTextView = (TextView) dialog.findViewById(R.id.confirm_text_view);
        confirmTextView.setText(getResources().getString(R.string.ok));
        TextView cancelTextView = (TextView) dialog.findViewById(R.id.cancel_text_view);
        TextView titleTextView = (TextView) dialog.findViewById(R.id.title_text_view);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.message_text_view);

        titleTextView.setText(getResources().getString(R.string.password_title));
        messageTextView.setText(getResources().getString(R.string.complete_profile_message));
        messageTextView.setVisibility(View.GONE);
        EditText passwordEditText = (EditText) dialog.findViewById(R.id.password_edit_text);

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void callUser()
    {
        boolean showDialog = sendCNMessage(CNMessage.CNMessageType.Calling, new ooVooSdkSampleShowApp.MessageCompletionHandler() {
            @Override
            public void onHandle(boolean state) {



                if (!state) {
                    count = 0 ;
                    Toast.makeText(getActivity(), R.string.fail_to_send_message, Toast.LENGTH_LONG).show();
                    callDialogBuilder.hide();
                    return  ;
                }

                count = enabledReceivesCount;
            }
        });

        if (showDialog) {
            callDialogBuilder.show();
        } else {
            Toast.makeText(getActivity(), R.string.no_receivers, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMessageReceived(CNMessage cnMessage) {

        if (app().getUniqueId().equals(cnMessage.getUniqueId())) {
            return;
        }

        if (cnMessage.getMessageType() == CNMessage.CNMessageType.AnswerAccept) {
            app().join(app().getConferenceId(), true);
        } else if (cnMessage.getMessageType() == CNMessage.CNMessageType.AnswerDecline) {
            count--;
            if (count <= 0) {
                callDialogBuilder.hide();
            }
        }  else if (cnMessage.getMessageType() == CNMessageType.Busy) {
            count--;
            if (count <= 0) {
                callDialogBuilder.hide();
            }
        }

    }
    private boolean sendCNMessage(CNMessageType type, ooVooSdkSampleShowApp.MessageCompletionHandler completionHandler)
    {
        ArrayList<String> toList = new ArrayList<String>();
        /*toList.add("sumit1234");
        toList.add("obaidr");
        toList.add("babul123");*/
        toList = InterpreterDashboardActivity.toList;

       /* for (int i = 0; i < callReceiverAdapter.getCount(); i++) {
            CallReceiverAdapter.CallReceiver receiver = (CallReceiverAdapter.CallReceiver) callReceiverAdapter.getItem(i);

            if (receiver.isCallEnabled() && toList.size() < MAX_CALL_RECEIVERS) {
                toList.add(receiver.getReceiverId());
            }
        }*/

        if (toList.isEmpty()) {
            return false;
        }

        app().sendCNMessage(toList, type, completionHandler);

        return true;
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

       /* if (callReceiverAdapter != null) {
            adapterSavedState = callReceiverAdapter.onSaveInstanceState();
        }
*/
        super.onDestroyView();
    }
}
