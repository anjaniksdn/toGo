package com.smartdatainc.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.smartdatainc.dataobject.AppInstance;
import com.smartdatainc.toGo.R;

/**
 * Created by Anurag Sethi on 02-06-2015.
 */
public class ShowVideoActivity extends AppActivity {

    VideoView videoViewObj;
    ProgressDialog progressDialogObj;

    String videoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_video);

        initData();
        bindControls();
    }

    public void initData() {
        videoViewObj = (VideoView) findViewById(R.id.videoView);
    }

    public void bindControls() {

        Intent intentObj = getIntent();
        videoURL = intentObj.getStringExtra("videoURL");

        AppInstance.logObj.printLog("videoURL=" + videoURL);

        //Create the progressbar
        progressDialogObj = new ProgressDialog(ShowVideoActivity.this);

        //Set progressbar title
        progressDialogObj.setTitle(getResources().getString(R.string.streaming_video));

        //set progressbar message
        progressDialogObj.setMessage(getResources().getString(R.string.buffering_video));

        progressDialogObj.setIndeterminate(false);
        progressDialogObj.setCancelable(false);
        progressDialogObj.show();

        try {
            //start mediacontroller
            MediaController mediacontroller = new MediaController(ShowVideoActivity.this);
            mediacontroller.setAnchorView(videoViewObj);

            Uri video = Uri.parse(videoURL);
            videoViewObj.setMediaController(mediacontroller);
            videoViewObj.setVideoURI(video);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        videoViewObj.requestFocus();
        videoViewObj.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialogObj.dismiss();
                videoViewObj.start();
            }
        });
    }

}
