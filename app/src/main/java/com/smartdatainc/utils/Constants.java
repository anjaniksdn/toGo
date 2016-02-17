package com.smartdatainc.utils;

/**
 * Created by Anurag Sethi
 * The class handles the constants used in the application
 */
public class Constants {
    public final static int GALLERY_PICTURE = 100;
    public final static int CAMERA_PICTURE = 101;
    public final static int GET = 1;
    public final static int POST = 2;
    public static String USER_EMAIL = "email";
    public static String TOKEN = "token";
    public static final String PREFS_NAME = "toGoPrefs";
    public static final String APPHEADER = "toGO";
    public static final String NOINTERNET = "No Internet Connection. Please check your internet connection or try again later.";


    /**
     * Handles the SplashScreen constants
     */
    public static class SplashScreen {
        /**
         * The parameter is used to manage the splash screen delay
         */
        public static int SPLASH_DELAY_LENGTH = 80;


    }

    public static class SocailNewtork {
        //These are constants used for build the urls
     /*Constants used for Linked-in authorisation process*/
    /*Linked-in Oauth2.0 keys*/
        public final static int LOGIN_TYPE_LINKED_IN = 3;
        public static final String API_KEY = "75fc9lo1l8ossk";
        public static final String SECRET_KEY = "X4nONy2gLNYYJe2P";
        //This is any string we want to use. This will be used for avoid CSRF attacks. You can generate one here: http://strongpasswordgenerator.com/
        public static final String STATE = "E3ZYKC1T6H2yP4z";
        public static final String REDIRECT_URI = "http://www.networkafterwork.com/auth/linkedin/callback";

        public static final String SCOPES = "r_basicprofile%20r_emailaddress";
        public static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
        public static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
        public static final String SECRET_KEY_PARAM = "client_secret";
        public static final String RESPONSE_TYPE_PARAM = "response_type";
        public static final String GRANT_TYPE_PARAM = "grant_type";
        public static final String GRANT_TYPE = "authorization_code";
        public static final String RESPONSE_TYPE_VALUE = "code";
        public static final String CLIENT_ID_PARAM = "client_id";
        public static final String SCOPE_PARAM = "scope";
        public static final String STATE_PARAM = "state";
        public static final String REDIRECT_URI_PARAM = "redirect_uri";
        public static final String QUESTION_MARK = "?";
        public static final String AMPERSAND = "&";
        public static final String EQUALS = "=";

        public static final String PROFILE_URL = "https://api.linkedin.com/v1/people/~:";
        public static final String OAUTH_ACCESS_TOKEN_PARAM = "oauth2_access_token";
        public static final String lINKED_IN_SERVICE_FORMAT = "format=json";
        public static final String LOGIN_WITH_LINKED_IN_KEY = "linkedInLogin";
    }

    /**
     * Handles webservice constants
     */
    public static class WebServices {

        //public static String WS_BASE_URL = "http://172.24.2.222.8103/";
        //public static String WS_USER_AUTHENTICATION = WS_BASE_URL + "/users/users/login";
//http://172.10.55.110:3000/signup
        //public static String WS_BASE_URL = "http://192.155.246.146:9175";
        //public static String WS_BASE_URL = "http://172.10.55.110:3000";
        public static String WS_BASE_URL = "http://54.153.22.179:3000";
        //public static String WS_USER_AUTHENTICATION = WS_BASE_URL + "/WebServices/signIn";
        public static String WS_USER_AUTHENTICATION = WS_BASE_URL + "/authenticate";
        public static String WS_USER_PROFILE = WS_BASE_URL + "/api/getAgentInfo";
        public static String WS_CUSTOMER_PROFILE = WS_BASE_URL + "/api/getUserProfile";
        public static String WS_UPDATE_AGENT = WS_BASE_URL + "/api/updateAgentAvailability";
        public static String WS_UPDATE_USERPROFILE = WS_BASE_URL + "/api/updateUserProfile";
        public static String WS_UPDATE_INTERPRETERPROFILE = WS_BASE_URL + "/api/updateAgentProfile";
        public static String WS_UPDATE_PROFILEIMAGE = WS_BASE_URL + "/api/upload";
        public static String WS_GETCOUNTRY = WS_BASE_URL + "/getCountryList";
        public static String WS_GETSTATE = WS_BASE_URL + "/getState";
        public static String WS_GETDASHBOARDDATA = WS_BASE_URL + "/api/getDashboardData";
        public static String WS_GETCHATDETAILS = WS_BASE_URL + "/api/getCDRData";


        //public static String WS_USER_SIGNUP = WS_BASE_URL + "/WebServices/signUp";
        public static String WS_USER_SIGNUP = WS_BASE_URL + "/signUp";
        /*public static String WS_USER_FORGOTPASS = WS_BASE_URL + "/WebServices/forgotPassword";*/
        public static String WS_USER_FORGOTPASS = WS_BASE_URL + "/forgot";


        public static String WS_PUBLIC_EVENTLIST = WS_BASE_URL + "/WebServices/publicEventList";

        public static String WS_MEDIA_UPLOAD_TO_SERVER = WS_BASE_URL + "/gallery/services/uploadFile";
        public static String WS_MEDIA_DELETE_FROM_SERVER = WS_BASE_URL + "/gallery/services/deleteFile";
        public static String WS_MEDIA_DOWNLOAD_FROM_SERVER = WS_BASE_URL + "/gallery/services/allFileList";
        public static String WS_SWIPE_REFRESH_EXAMPLE_URL = WS_BASE_URL + "/swipe/swipe/swipeRefreshMovieList";
        public static String WS_GCM_DEVICE_ID_TO_SERVER = WS_BASE_URL + "/notification/notifications/saveDeviceIdForGcm";
        public static String WS_GET_LANGUAGE_LIST = WS_BASE_URL + "/getLanguageList";
        //public static String WS_GET_LANGUAGE_PRICE = WS_BASE_URL + "/getLanguagePrice";

        public static String WS_GET_LANGUAGE_PRICE = WS_BASE_URL + "/getLanguagePrice";
        public static String WS_GET_LANGUAGE_POOL = WS_BASE_URL + "/getInterpreterByLanguage";
        public static String WS_POST_CALL_DETAILS = WS_BASE_URL + "/api/saveCallDetails";
        public static String WS_POST_CALL_CDR = WS_BASE_URL + "/api/createCDR";
        public static String WS_CHANGEPASSWORD = WS_BASE_URL + "/api/changePassword";

        public static String WS_UPDATEINTERPRETERCALLS = WS_BASE_URL + "/updateInterepreterCallStatus";
    }

    /**
     * Handles the TaskIDs so as to differentiate the web service return values
     */

    public static class TaskID {
        public static int LOGIN_TASK_ID = 100;
        public static int USER_PROFILE_TASK_ID = 108;
        public static int FORGOT_PASSWORD_TASK_ID = 101;
        public static int MEDIA_FILE_UPLOAD_TASK_ID = 102;
        public static int MEDIA_FILE_DELETE_TASK_ID = 103;
        public static int MEDIA_FILE_DOWNLOAD_URL_TASK_ID = 104;
        public static int SWIPE_REFRESH_TASK_ID = 105;
        public static int SEND_GCM_DEVICE_ID_TO_SERVER_TASK_ID = 106;
        public static int SIGNUP_TASK_ID = 107;
        public static int PUBLIC_EVENT_TASK_ID = 107;
        //availability
        public static int UPDATE_AVAILAIBLITY_TASK_ID = 110;
        public static int CUSTOMER_PROFILE_TASK_ID = 112;
        public static int UPDATE_USERPROFILE_TASK_ID = 113;
        public static int UPDATE_INTERPRETERPROFILE_TASK_ID = 114;

        public static int UPDATECUSTOMER_PROFILE_TASK_ID = 115;
        public static int UPDATE_PROFILEIMAGE_TASK_ID = 116;
        public static int GET_COUNTRYLIST_TASK_ID = 117;
        public static int GET_SATELIST_TASK_ID = 118;
        public static int GET_CUSTOMER_DASHBOARD_DETAILS_TASK_ID = 119;
        public static int GET_LANGUAGE_LIST_TASK_ID = 120;
        public static int GET_INTERPRETATION_DETAILS_TASK_ID = 121;
        public static int GET_INTERPRETATION_POOL_TASK_ID = 122;
        public static int SET_CALL_DETAILS_TASK_ID = 123;
        public static int SET_CALL_CDR_TASK_ID = 124;
        public static int SET_CALL_CANCEL_TASK_ID = 125;
        public static int GET_LANGUAGE_LIST_INTERPRETETION_TASK_ID = 125;
        public static int GET_INTERPRETE_DASHBOARD_TASK_ID = 126;
        public static int GET_INTERPRETE_CHAT_HISTORY_TASK_ID = 127;
        public static int CHANGE_PASSWORD = 128;
        public static int INTERPRETER_CALL_STATUS_TASK_ID = 129;


    }

    /**
     * Handles the ButtonTags so as to differentiate them in showAlertDialog()
     */

    public static class ButtonTags {

        public static String TAG_NETWORK_SERVICE_ENABLE = "network services";
        public static String TAG_LOCATION_SERVICE_ENABLE = "location services";
    }

    /**
     * Handles the JSON Parsing
     */
    public static class JsonParsing {
        public static int PARSING_JSON_FOR_MESSAGE_ID = 1;
        public static int PARSING_JSON_FOR_RESULT = 2;
        public static int PARSING_JSON_FOR_STATUS = 3;
        public static int PARSING_JSON_FOR_MESSAGE = 4;
    }

    /**
     * Handles the Bugsense Key
     */
    public static class BugSenseConstants {
        public static String SPLUNK_API_KEY = "1c9deceb";
    }

    /**
     * Handles the DebugLog constants
     */
    public static class DebugLog {
        /**
         * Will be the name of the project
         */
        public static String APP_TAG = "toGo";
        /**
         * APP_MODE = 1 means Debug Mode
         * APP_MODE = 0 means Live Mode
         * Must change to 0 when going live
         */
        public static int APP_MODE = 1;
        /**
         * Name of the directory in which log file needs to be saved
         */
        public static String APP_ERROR_DIR_NAME = "toGo";
        /**
         * Name of the log file
         */
        public static String APP_ERROR_LOG_FILE_NAME = "log.txt";
    }

    /**
     * Handles the constant for Google Play Services
     */
    public static class GooglePlayService {
        public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    }

    /**
     * Handles the requestCodes
     */
    public static class RequestCodes {

        public static int RESULT_GALLERY_LOAD_IMAGE = 1;
        public static int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
        public static int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
        public static int MEDIA_TYPE_IMAGE = 1;
        public static int MEDIA_TYPE_VIDEO = 2;
    }

    /**
     * Handles the Media constants
     */
    public static class Media {
        public static String IMAGE_DIRECTORY_NAME = "toGo";

        /**
         * GALLERY_TYPE = 1 means that both images and video will be downloaded
         * GALLERY_TYPE = 2 means that only images will be downloaded
         * GALLERY_TYPE = 3 means that only videos will be downloaded
         */
        public static int GALLERY_TYPE = 1;

        public static int GALLERY_MEDIA_DISPLAY_WIDTH = 300;
        public static int GALLERY_MEDIA_DISPLAY_HEIGHT = 300;
    }

    /**
     * Handles the constants for GCM (Google Cloud Messaging)
     */
    public static class GCM {
        public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
        public static String GCM_SENDER_ID = "";
    }

    public static class FACEBOOK {
        public static final String EMAIL = "email";
        public static final String NAME = "name";
    }

    /**
     * Handles the constants for GoogleAnalytics
     */
    public static class GoogleAnalytics {
        public static String trackingID = "";
    }
}
