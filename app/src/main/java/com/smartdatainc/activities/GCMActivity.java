package com.smartdatainc.activities;

import android.content.Intent;
import android.os.Bundle;
import com.smartdatainc.services.RegistrationIntentService;
import com.smartdatainc.utils.Utility;

/**
 * Created by Anurag Sethi on 25-06-2015.
 */
public class GCMActivity extends AppActivity {

    private Utility utilObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        if(utilObj.validateGooglePlayServices(this)) {
            //start IntentService to register this application with GCM
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }

    public void initData() {
        utilObj = new Utility(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        utilObj.validateGooglePlayServices(this);
    }
}
