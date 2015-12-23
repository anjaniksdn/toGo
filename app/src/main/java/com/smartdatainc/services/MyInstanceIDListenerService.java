package com.smartdatainc.services;


import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.smartdatainc.app.ApplicationSettings;
import com.smartdatainc.app.ooVooSdkSampleShowApp;


/**
 * Created by Anurag Sethi on 25-06-2015.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService{

    private static final String TAG = "MyInstanceIDLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        ooVooSdkSampleShowApp application = (ooVooSdkSampleShowApp) getApplication();
        ApplicationSettings settings = application.getSettings();
        String username = settings.get(ApplicationSettings.Username);
        settings.remove(username);

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
    // [END refresh_token]
}
