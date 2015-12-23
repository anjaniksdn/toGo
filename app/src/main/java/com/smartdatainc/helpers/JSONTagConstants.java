package com.smartdatainc.helpers;

/**
 * An interface that holds the various json related constants.
 */
public interface JSONTagConstants {

    // Response status
    public final String RESPONSE_TAG_STATUS = "status";
    public final String RESPONSE_STATUS_SUCCESS = "true";
    public final String RESPONSE_STATUS_FAILURE = "false";
    public final String RESPONSE_RESULTS_MESSAGE = "message";
    public final String RESPONSE_RESULTS_DATA = "data";

    //LinkedIn details
    public final String LINKED_IN_RESPONSE_EMAIL_ADDRESS = "emailAddress";
    public final String LINKED_IN_RESPONSE_FIRST_NAME = "firstName";
    public final String LINKED_IN_RESPONSE_LAST_NAME = "lastName";

    //public final String USER_ID="id";
    //public final String USER_ROLE_ID="role_id";
    public final String USER_TYPE_ID = "user_type";
    public final String USER_EMAIL = "email";
    public final String USER_FIRST_NAME = "first_name";
    public final String USER_LAST_NAME = "last_name";
    public final String USER_ADDRESS = "address_1";
    public final String USER_DOB = "dob";
    public final String USER_PASSWORD = "password";
    public final String AUTH_TOKEN = "_token";
    public final String LOGIN_TYPE = "login_type";
    public final String USER_GENDER = "gender";
    public final String USER_ID = "id";
    public final String USER_PROFILE_PICTURE = "profileImage";

    public final String DATA = "data";
    //SignUp address
    public final String ADDRESS = "address";
    //Event list parser
    public final String EVENT_ID = "id";
    public final String EVENT_TITLE = "title";
    public final String EVENT_DATE = "event_date";
    public final String EVENT_START_TIME = "start_time";
    public final String EVENT_END_TIME = "end_time";
    public final String EVENT_PHOTO = "photo";
    public final String EVENT_IMAGE_URL = "imgurl";
    public final String EVENT_VENUE = "location";
    public final String EVENT_DESCRIPTION = "description";
    public final String EVENT_CITY = "city";
    public final String EVENT_AMOUNT = "price";

    //my profile data parser
    public final String MY_PROFILE_JOB_TITLE = "jobtitle";
    //profile data parser
    public final String USER = "User";
    public final String USER_FAVOURITE_INDUSTRY = "UserFavouriteIndustry";
    public final String PROFILE_ID = "id";
    public final String PROFILE_FIRST_NAME = "first_name";
    public final String PROFILE_LAST_NAME = "last_name";
    public final String PROFILE_TITLE = "title";
    public final String PROFILE_COMPANY_NAME = "company_name";
    public final String PROFILE_EMAIL = "email";
    public final String PROFILE_GENDER = "gender";
    public final String PROFILE_DOB = "dob";
    public final String PROFILE_PASSWORD = "password";
    public final String PROFILE_IMAGE = "image";
    public final String PROFILE_SUBSCRIPTION_ID = "subscription_id";
    public final String PROFILE_SUBSCRIPTION_TYPE = "subscription_type";
    public final String PROFILE_ADDRESS1 = "address_1";
    public final String PROFILE_ADDRESS2 = "address_2";
    public final String PROFILE_COUNTRY_ID = "country_id";
    public final String PROFILE_STATE_ID = "state_id";
    public final String PROFILE_CITY = "city";
    public final String PROFILE_ZIP = "zip";
    public final String PROFILE_PHONE_NO = "phone_no";
    public final String PROFILE_INDUSTRY_ID = "industry_id";
    public final String PROFILE_WHAT_I_DO = "what_i_do";
    public final String PROFILE_WHO_I_WANT_TO_MEET = "who_i_want_to_meet";
    public final String PROFILE_STATUS = "status";
    public final String PROFILE_IS_DELETED = "is_deleted";
    public final String PROFILE_CREATED = "created";
    public final String PROFILE_MODIFIED = "modified";
    public final String PROFILE_FACEBOOK_ID = "facebook_id";
    public final String PROFILE_LINKED_IN_ID = "linkedin_id";
    public final String PROFILE_TOKEN = "_token";
    public final String PROFILE_APN_DEVICE_TOKEN = "_apn_device_token";
    public final String PROFILE_GCM_DEVICE_TOKEN = "_gcm_device_token";
    public final String PROFILE_USER_TYPE = "user_type";
    public final String PROFILE_ACTIVATION_TOKEN = "activation_token";
    public final String PROFILE_FORGOT_PASS_TOKEN = "forgot_pass_token";
    public final String PROFILE_EXPIRY_TIME_TOKEN = "expiry_time_token";

    //Filter event constant
    public static final String EVENT_NAME = "event_name";
    public static final String EVENT_LOCATION = "event_location";
    public static final String EVENT_START_DATE = "event_start_date";

    //Attendees list parse
    public static final String NAW_EVENT_ID = "event_id";

    //Paypal payment information tags
    public static final String PAYMENT_ID = "id";
    public static final String PLATFORM = "platform";
    public static final String CREATE_TIME = "create_time";
    public static final String PAYMENT_STATE = "state";
    public static final String PAYMENT_CLIENT = "client";
    public static final String PAYMENT_RESPONSE = "response";

    // Register events
    public static final String REGISTER_CREATE_TIME = "createTime";
    public static final String REGISTER_AMOUNT = "amount";


}
