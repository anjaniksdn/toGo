package com.smartdatainc.activities;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.smartdatainc.dataobject.AppInstance;
import com.smartdatainc.dataobject.MediaGallery;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.MediaManager;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;
import com.smartdatainc.utils.Utility;

import sdei.support.lib.media.MediaUtility;

public class ChoosePhotoActivity extends AppActivity implements ServiceRedirection {

    Utility utilObj;
    MediaUtility mediaUtilityObj;
    Button fromCameraObj;
    Button fromGalleryObj;
    Button uploadImageObj;
    Button resizeImageObj;
    Button rotateImageObj;
    Button deleteImageObj;
    Button recordVideoObj;
    Button btnGalleryObj;
    ImageView imageViewObj;
    VideoView videoViewObj;
    MediaManager mediaManagerObj;
    MediaGallery mediaGalleryObj;

    private Uri fileUri;
    String mediaPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);

        initData();
        bindControls();
    }

    /**
     * Initializes the objects
     * @return none
     */
    public void initData() {
        utilObj = new Utility(this);
        mediaUtilityObj = new MediaUtility(this, Constants.Media.IMAGE_DIRECTORY_NAME);
        fromCameraObj = (Button) findViewById(R.id.btnFromCamera);
        fromGalleryObj = (Button) findViewById(R.id.btnFromGallery);
        imageViewObj = (ImageView) findViewById(R.id.imageView1);
        videoViewObj = (VideoView) findViewById(R.id.videoView);

        recordVideoObj = (Button) findViewById(R.id.btnRecordVideo);

        uploadImageObj = (Button) findViewById(R.id.uploadImage);
        resizeImageObj = (Button) findViewById(R.id.resizeImage);
        rotateImageObj = (Button) findViewById(R.id.rotateImage);
        deleteImageObj = (Button) findViewById(R.id.deleteImage);

        btnGalleryObj = (Button) findViewById(R.id.btnGallery);

        mediaManagerObj = new MediaManager(this, this);
        mediaGalleryObj = new MediaGallery();
    }

    /**
     * Binds the UI controls
     * @return none
     */
    public void bindControls() {

        //From Camera
        fromCameraObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaUtilityObj.setMediaType(Constants.RequestCodes.MEDIA_TYPE_IMAGE);

                Intent intentObj = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = mediaUtilityObj.getOutputMediaFileUri();
                intentObj.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                // start the image capture Intent
                startActivityForResult(intentObj, Constants.RequestCodes.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

            }
        });


        //From Gallery
        fromGalleryObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentObj, Constants.RequestCodes.RESULT_GALLERY_LOAD_IMAGE);
            }
        });

        //Rotate image
        rotateImageObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap rotatedBitmapObj = mediaUtilityObj.rotateImage(90, imageViewObj);
                imageViewObj.setImageBitmap(rotatedBitmapObj);
            }
        });


        //Resize image
        resizeImageObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap scaledBitmapObj = mediaUtilityObj.scaleImage(100, 150, imageViewObj);
                imageViewObj.setImageBitmap(scaledBitmapObj);
            }
        });

        //delete image
        deleteImageObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilObj.startLoader(ChoosePhotoActivity.this, R.drawable.image_for_rotation);
                mediaManagerObj.deleteMedia(AppInstance.mediaObj);

                mediaUtilityObj.resetImageView(imageViewObj);
                resetMediaActionButton();
            }
        });

        //record video
        recordVideoObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoViewObj.setVisibility(View.VISIBLE);
                imageViewObj.setVisibility(View.GONE);

                mediaUtilityObj.setMediaType(Constants.RequestCodes.MEDIA_TYPE_VIDEO);

                Intent intentObj = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                fileUri = mediaUtilityObj.getOutputMediaFileUri();

                //set video quality
                intentObj.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intentObj.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                // start the video capture Intent
                startActivityForResult(intentObj, Constants.RequestCodes.CAMERA_CAPTURE_VIDEO_REQUEST_CODE);

            }
        });

        //upload media to server
        uploadImageObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilObj.startLoader(ChoosePhotoActivity.this, R.drawable.image_for_rotation);
                mediaManagerObj.uploadMediaFile(mediaPath);
            }
        });

        //View Gallery
        btnGalleryObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilObj.startLoader(ChoosePhotoActivity.this, R.drawable.image_for_rotation);
                mediaGalleryObj.galleryType = Constants.Media.GALLERY_TYPE;
                mediaManagerObj.downloadMediaFileUrls(mediaGalleryObj);
            }
        });
    }


    private void resetMediaActionButton() {
        uploadImageObj.setVisibility(View.GONE);
        resizeImageObj.setVisibility(View.GONE);
        rotateImageObj.setVisibility(View.GONE);
        deleteImageObj.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Cursor cursor = null;
        int columnIndex = 0;


        if(requestCode == Constants.RequestCodes.RESULT_GALLERY_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            imageViewObj.setVisibility(View.VISIBLE);
            videoViewObj.setVisibility(View.GONE);
            Uri selectedImage = data.getData();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                String selectedPath = selectedImage.getPath().replace("/ACTUAL", "");

                if(selectedPath.contains("-1")) {
                    selectedPath = selectedPath.substring(6);
                }
                else {
                    selectedPath = selectedPath.substring(5);
                }

                selectedImage = Uri.parse(selectedPath);
            }

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPath = cursor.getString(columnIndex);

            cursor.close();

            imageViewObj.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
            //make upload and resize image button enable
            uploadImageObj.setVisibility(View.VISIBLE);
            resizeImageObj.setVisibility(View.VISIBLE);
            rotateImageObj.setVisibility(View.VISIBLE);
            deleteImageObj.setVisibility(View.VISIBLE);
        }
        else if(requestCode == Constants.RequestCodes.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                imageViewObj.setVisibility(View.VISIBLE);
                videoViewObj.setVisibility(View.GONE);

                // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();

                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;

                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                imageViewObj.setImageBitmap(bitmap);
                //make upload and resize image button enable
                uploadImageObj.setVisibility(View.VISIBLE);
                resizeImageObj.setVisibility(View.VISIBLE);
                rotateImageObj.setVisibility(View.VISIBLE);
                deleteImageObj.setVisibility(View.VISIBLE);

                mediaPath = fileUri.getPath();

            }
            else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                utilObj.showToast(ChoosePhotoActivity.this, getResources().getString(R.string.image_capture_cancelled), 1);
                resetMediaActionButton();
            } else {
                // failed to capture image
                utilObj.showToast(ChoosePhotoActivity.this, getResources().getString(R.string.failed_capture_image), 1);
                resetMediaActionButton();
            }
        }
        else if(requestCode == Constants.RequestCodes.CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {

            if(resultCode == RESULT_OK) {
                imageViewObj.setVisibility(View.GONE);
                videoViewObj.setVisibility(View.VISIBLE);
                videoViewObj.setVideoPath(fileUri.getPath());
                videoViewObj.start();

                mediaPath = fileUri.getPath();

                //make upload and resize image button enable
                uploadImageObj.setVisibility(View.VISIBLE);
                resizeImageObj.setVisibility(View.GONE);
                rotateImageObj.setVisibility(View.GONE);
                deleteImageObj.setVisibility(View.GONE);

            }
            else if(resultCode == RESULT_CANCELED) {
                utilObj.showToast(ChoosePhotoActivity.this, getResources().getString(R.string.video_capture_cancelled), 1);
                resetMediaActionButton();
            }
            else {
                utilObj.showToast(ChoosePhotoActivity.this, getResources().getString(R.string.failed_capture_video), 1);
                resetMediaActionButton();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * The interface method implemented in the java files
     *
     * @param taskID the id based on which the relevant action is performed
     * @return none
     */
    @Override
    public void onSuccessRedirection(int taskID) {

        if(taskID == Constants.TaskID.MEDIA_FILE_UPLOAD_TASK_ID) {
            utilObj.stopLoader();
            utilObj.showToast(this, "Media file uploaded successfully", 1);
        }
        else if(taskID == Constants.TaskID.MEDIA_FILE_DELETE_TASK_ID) {
            utilObj.stopLoader();
            utilObj.showToast(this, "Media file deleted successfully", 1);
        }
        else if(taskID == Constants.TaskID.MEDIA_FILE_DOWNLOAD_URL_TASK_ID) {
            utilObj.stopLoader();
            /*int size = AppInstance.mediaGalleryObj.arrayListMedia.size();
            AppInstance.logObj.printLog("size=" + size);*/

            Intent intentObj = new Intent(this, ShowMediaGalleryActivity.class);
            startActivity(intentObj);
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
        utilObj.stopLoader();
        utilObj.showToast(this, errorMessage, 1);
    }
    public void onSuccessRedirection(int taskID,String jsonMesseage) {

    }
}