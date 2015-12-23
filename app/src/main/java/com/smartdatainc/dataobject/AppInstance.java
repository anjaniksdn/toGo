package com.smartdatainc.dataobject;

import com.smartdatainc.utils.Constants;

import sdei.support.lib.debug.LogUtility;

import java.util.ArrayList;
                                import java.util.List;

/**
 * Created by Anurag Sethi
 * The class is defined on single instance.
 */
public class AppInstance {

    private static AppInstance appInstance = null;
    public static LogUtility logObj;
    public static User userObj;
    public static Media mediaObj;
    public static MediaGallery mediaGalleryObj;
    public static List<SwipeRefresh> swipeRefreshListObj;

    /**
     * To initialize the appInstance Object
     * @return singleton instance
     */

    public static AppInstance getAppInstance() {
        if(appInstance == null) {
            appInstance = new AppInstance();

            userObj = new User();
            
             /**
             * The object will manage the Media information
             */
            mediaObj = new Media();

            /**
             * The object will manage the media gallery information
             */
            mediaGalleryObj = new MediaGallery();
             
             /**
             * The object will manage the swipeToRefresh arrayList
             */
            swipeRefreshListObj = new ArrayList<SwipeRefresh>();

            /**
             * the object will manage the logs in the logcat
             */
            logObj = new LogUtility(Constants.DebugLog.APP_MODE, Constants.DebugLog.APP_TAG);
        }

        return appInstance;
    }

}
