package com.smartdatainc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.smartdatainc.adapters.GridViewAdapter;
import com.smartdatainc.dataobject.AppInstance;
import com.smartdatainc.dataobject.MediaGallery;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.MediaManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

import java.util.ArrayList;

import sdei.support.lib.media.MediaUtility;

/**
 * Created by Anurag Sethi on 26-05-2015.
 */
public class ShowMediaGalleryActivity extends ActionBarActivity implements ServiceRedirection {

    Utility utilObj;
    GridView gridviewObj;
    MediaUtility mediaUtilityObj;
    MediaGallery mediaGalleryObj;
    MediaManager mediaManagerObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery_view);

        initData();
        bindControls();
    }

    public void initData() {
        utilObj = new Utility(this);
        gridviewObj = (GridView) findViewById(R.id.gridview);
        mediaUtilityObj = new MediaUtility(this, null);
        mediaGalleryObj = new MediaGallery();
        mediaManagerObj = new MediaManager(this, this);
    }


    public void bindControls() {

        final ArrayList<String> mediaUrlsArrayList = new ArrayList<>();
        final ArrayList<String> mediaTypeArrayList = new ArrayList<>();

        int arrayListSize = AppInstance.mediaGalleryObj.arrayListMedia.size();

        for(int i = 0; i < arrayListSize; i++) {
            if(AppInstance.mediaGalleryObj.arrayListMedia.get(i).filetype.equalsIgnoreCase("video")) {
                mediaUrlsArrayList.add(i, AppInstance.mediaGalleryObj.arrayListMedia.get(i).thumbnail);
            }
            else {
                mediaUrlsArrayList.add(i, AppInstance.mediaGalleryObj.arrayListMedia.get(i).filepath);
            }

            mediaTypeArrayList.add(i, AppInstance.mediaGalleryObj.arrayListMedia.get(i).filetype);

        }

        GridViewAdapter adapterObj = new GridViewAdapter(this, R.layout.grid_item_layout, mediaUrlsArrayList, mediaTypeArrayList, mediaUtilityObj);
        gridviewObj.setAdapter(adapterObj);

        gridviewObj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(mediaTypeArrayList.get(position).equalsIgnoreCase("video")) {
                    String videoUrl = AppInstance.mediaGalleryObj.arrayListMedia.get(position).filepath;

                    Intent intentObj = new Intent(ShowMediaGalleryActivity.this, ShowVideoActivity.class);
                    intentObj.putExtra("videoURL", videoUrl);
                    startActivity(intentObj);

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_images_only) {
            utilObj.startLoader(ShowMediaGalleryActivity.this, R.drawable.image_for_rotation);
            mediaGalleryObj.galleryType = 2;
            mediaManagerObj.downloadMediaFileUrls(mediaGalleryObj);
            return true;
        }

        if(id == R.id.action_videos_only) {

            utilObj.startLoader(ShowMediaGalleryActivity.this, R.drawable.image_for_rotation);
            mediaGalleryObj.galleryType = 3;
            mediaManagerObj.downloadMediaFileUrls(mediaGalleryObj);
            return true;
        }

        if(id == R.id.action_both) {

            utilObj.startLoader(ShowMediaGalleryActivity.this, R.drawable.image_for_rotation);
            mediaGalleryObj.galleryType = 1;
            mediaManagerObj.downloadMediaFileUrls(mediaGalleryObj);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * The interface method implemented in the java files
     *
     * @param taskID the id based on which the relevant action is performed
     * @return none
     */
    @Override
    public void onSuccessRedirection(int taskID) {
        if(taskID == Constants.TaskID.MEDIA_FILE_DOWNLOAD_URL_TASK_ID) {
            utilObj.stopLoader();
            bindControls();
        }
    }

    /**
     * The interface method implemented in the java files
     *
     * @param errorMessage the error message to be displayed
     * @return none
     */
    @Override
    public void onFailureRedirection(String errorMessage) {

    }
    public void onSuccessRedirection(int taskID,String jsonMesseage) {

    }
}
