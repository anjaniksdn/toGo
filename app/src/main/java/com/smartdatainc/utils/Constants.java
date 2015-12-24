package com.smartdatainc.utils;

/**
 * Created by Anurag Sethi
 * The class handles the constants used in the application
 */
public class Constants {
    public final static int GET = 1;
    public final static int POST = 2;
    public  static String USER_EMAIL= "email";
    public  static String TOKEN= "token";
    public static final String PREFS_NAME = "toGoPrefs";
    /**
     * Handles the SplashScreen constants
     */
    public static class SplashScreen {
        /**
         * The parameter is used to manage the splash screen delay
         */
        public  static int SPLASH_DELAY_LENGTH = 60;


    }
public static class SocailNewtork{
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
    public static final String RESPONSE_TYPE_VALUE ="code";
    public static final String CLIENT_ID_PARAM = "client_id";
    public static final String SCOPE_PARAM = "scope";
    public static final String STATE_PARAM = "state";
    public static final String REDIRECT_URI_PARAM = "redirect_uri";
    public static final String QUESTION_MARK = "?";
    public static final String AMPERSAND = "&";
    public static final String EQUALS = "=";

    public static final String PROFILE_URL = "https://api.linkedin.com/v1/people/~:";
    public static final String OAUTH_ACCESS_TOKEN_PARAM ="oauth2_access_token";
    public static final String lINKED_IN_SERVICE_FORMAT ="format=json";
    public static final String LOGIN_WITH_LINKED_IN_KEY ="linkedInLogin";
}

    /**
     * Handles webservice constants
     */
    public static class WebServices {

        //public static String WS_BASE_URL = "http://172.24.2.222.8103/";
        //public static String WS_USER_AUTHENTICATION = WS_BASE_URL + "/users/users/login";
//http://172.10.55.110:3000/signup
        //public static String WS_BASE_URL = "http://192.155.246.146:9175";
        public static String WS_BASE_URL = "http://172.10.55.110:3000";
        public static String WS_USER_AUTHENTICATION = WS_BASE_URL + "/WebServices/signIn";

        //public static String WS_USER_SIGNUP = WS_BASE_URL + "/WebServices/signUp";
        public static String WS_USER_SIGNUP = WS_BASE_URL + "/signUp";
        public static String WS_USER_FORGOTPASS = WS_BASE_URL + "/WebServices/forgotPassword";

        public static String WS_PUBLIC_EVENTLIST = WS_BASE_URL + "/WebServices/publicEventList";

        public static String WS_MEDIA_UPLOAD_TO_SERVER = WS_BASE_URL + "/gallery/services/uploadFile";
        public static String WS_MEDIA_DELETE_FROM_SERVER = WS_BASE_URL + "/gallery/services/deleteFile";
        public static String WS_MEDIA_DOWNLOAD_FROM_SERVER = WS_BASE_URL + "/gallery/services/allFileList";
        public static String WS_SWIPE_REFRESH_EXAMPLE_URL = WS_BASE_URL + "/swipe/swipe/swipeRefreshMovieList";
        public static String WS_GCM_DEVICE_ID_TO_SERVER = WS_BASE_URL + "/notification/notifications/saveDeviceIdForGcm";

    }

    /**
     * Handles the TaskIDs so as to differentiate the web service return values
     */

    public static class TaskID {
        public static int LOGIN_TASK_ID = 100;
        public static int FORGOT_PASSWORD_TASK_ID = 101;
         public static int MEDIA_FILE_UPLOAD_TASK_ID = 102;
        public static int MEDIA_FILE_DELETE_TASK_ID = 103;
        public static int MEDIA_FILE_DOWNLOAD_URL_TASK_ID = 104;
        public static int SWIPE_REFRESH_TASK_ID = 105;
        public static int SEND_GCM_DEVICE_ID_TO_SERVER_TASK_ID = 106;
        public static int SIGNUP_TASK_ID = 107;
        public static int PUBLIC_EVENT_TASK_ID = 107;
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
        public static int PARSING_JSON_FOR_STATUS =3;
        public static int PARSING_JSON_FOR_MESSAGE =4;
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
        public static String APP_TAG = "timetoturnupparty";
        /**
         * APP_MODE = 1 means Debug Mode
         * APP_MODE = 0 means Live Mode
         * Must change to 0 when going live
         */
        public static int APP_MODE = 1;
        /**
         * Name of the directory in which log file needs to be saved
         */
        public static String APP_ERROR_DIR_NAME = "timetoturnupparty"; 
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
        public static String IMAGE_DIRECTORY_NAME = "timetoturnupparty";

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
    /**
     * Handles the constants for GoogleAnalytics
     */
    public static class GoogleAnalytics {
        public static String trackingID = "";
    }
}
