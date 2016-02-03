package com.smartdatainc.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.smartdatainc.app.ooVooSdkSampleShowApp;
import com.smartdatainc.dataobject.AppInstance;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Anjani Kumar
 * The activity is used for handling the splash screen operations along with redirection to login screen
 * after the splash screen delay is exhausted
 */
public class MainActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "JlGF9bsbxiTtJad60TY4rRBG2";
    private static final String TWITTER_SECRET = "fa28p5Pmjp0ynswMLri3hA9YWczpVjfCCINKaSBJg0ixAyyN7m";
    //fa28p5Pmjp0ynswMLri3hA9YWczpVjfCCINKaSBJg0ixAyyN7m

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        //setActionBar("TOGO");
        //initializing the data
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.smartdatainc.toGo", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        initData();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig));
        //handler to close the splash activity after the set time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Intent to call the Login Activity
                Intent intentObj = new Intent(MainActivity.this, UserTypeActivity.class);
                startActivity(intentObj);
                MainActivity.this.finish();

                MainActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }, Constants.SplashScreen.SPLASH_DELAY_LENGTH);
    }


    /**
     * The method is used to initialize the objects
     *
     * @return none
     */
    public void initData() {
       app().reautorize();
        AppInstance.getAppInstance();
    }

    ooVooSdkSampleShowApp app() {
        return ((ooVooSdkSampleShowApp) getApplication());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
