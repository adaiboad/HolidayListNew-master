package com.example.adaiboad.realtravelapp;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.adaiboad.realtravelapp.Adapters.ImageAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Deborah on 10/12/2017.
 */


public class GalleryActivity extends Activity{


    private static final int ACTION_TAKE_PHOTO_B = 1;
    private Button.OnClickListener mTakePicOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
                }
            };

    private boolean canSaveExternal;

    private Uri photoUri;

    private String mCurrentPhotoPath;
    private GridView gridview;
    private ImageAdapter imageAdapter;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";


    // permissions requests
    private final int permissionRequestCode = 1;
    //private int grantResults[];

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent1 = new Intent(GalleryActivity.this, MainActivity.class);
                    startActivity(intent1);
                case R.id.navigation_map:
                    Intent intent2 = new Intent(GalleryActivity.this, MapsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.navigation_gallery:
                    Intent intent3 = new Intent(GalleryActivity.this, GalleryActivity.class);
                    startActivity(intent3);
                    break;
            }
            return false;
        }
    };



    /* Gallery album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    /**
     * Get all of the image files from the Android data folder used by the app.
     *
     * @return a list of file names of the image files
     */
    private List<String> getPictureFiles() {
        ArrayList<String> imageFileNames = new ArrayList<>();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] listOfFiles = storageDir.listFiles();
        Log.i("AJB", "storage dir when getting picture files is " + storageDir.getAbsolutePath());

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Log.i("AJB", "Image File " + listOfFiles[i].getName());
                imageFileNames.add(listOfFiles[i].getAbsolutePath());
            } else if (listOfFiles[i].isDirectory()) {
                Log.i("AJB", "Directory " + listOfFiles[i].getName());
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        return imageFileNames;

    }

    /**
     * The location in the users Pictures folder for storing the images.
     *
     * @return a folder in the user's Pictures folder where images will be stored.
     */

    /**
     * Create an image file in the pictures folder for this app.
     * Look in Android/data/MYPACKAGE/files/Pictures.
     *
     * @return a new file in the app's Pictures folder in Android/data.
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                JPEG_FILE_SUFFIX,         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        //mCurrentImageFile = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, getAlbumDir());
        return image;

    }

    /**
     * Send off an intent to take a picture.  Will be handled by the camera.
     *
     * @param actionCode the code for this intent.
     */
    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch (actionCode) {
            case ACTION_TAKE_PHOTO_B:
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    File f = null;

                    try {
                        f = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        f = null;
                        mCurrentPhotoPath = null;
                    }
                    if (f != null) {

                        photoUri = FileProvider.getUriForFile(this,
                                "com.example.android.fileprovider",
                                f);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        Log.i("AJB", "file is " + f.getAbsolutePath());
                        Log.i("AJB", "extra output " + photoUri.toString());
                        startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO_B);
                    }
                } // if
                break;

            default:
                //startActivityForResult(takePictureIntent, actionCode);
                break;
        } // switch


    }

    /**
     * Update the grid view to add the new image.  Also copy the image to the User's Pictures folder.
     */
    private void handlePhoto() {
        Log.i("AJB", "Handle BIG photo");
        if (mCurrentPhotoPath != null) {
            Log.i("AJB", "BIG photo located at " + mCurrentPhotoPath);
            imageAdapter.addImage(mCurrentPhotoPath);
            imageAdapter.notifyDataSetChanged();

            mCurrentPhotoPath = null;
        } else {
            Log.i("AJB", "BIG photo but no path");
        }


    }


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        /*
         * Ask the user to agree permission to write to external storage.  In this case the User's
         * Pictures folder.
         */
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            canSaveExternal = false;
            //if you dont have required permissions ask for it (only required for API 23+)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissionRequestCode);

        } else {
            canSaveExternal = true;
        }

        /* Find the button for taking a picture and configure it to fire the intent to take a picture */
        Button picBtn = (Button) findViewById(R.id.btnIntend);
        setBtnListenerOrDisable(
                picBtn,
                mTakePicOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        /* The grid view where the pictures will be displayed */
        gridview = (GridView) findViewById(R.id.gridview);
        /* The image adapter that contains a list of filenames to be displayed in the grid */
        imageAdapter = new ImageAdapter(this);
        /* Read the file names from the app's Picture folder */
        readImageFiles();
        /* notify the data changed */
        imageAdapter.notifyDataSetChanged();
        /* now set the adapter for the grid view */
        gridview.setAdapter(imageAdapter);

        /* Set an onclick listener for the grid view for when we click on an image */
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(GalleryActivity.this, imageAdapter.getImageFileName(position),
                        Toast.LENGTH_SHORT).show();
            }
        });



    }





    @Override // android recommended class to handle permissions
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case permissionRequestCode: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults != null && grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canSaveExternal = true;

                    Log.d("permission", "granted");
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.uujm
                    Toast.makeText(GalleryActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();

                    //app cannot function without this permission for now so close it...
                    //onDestroy();
                    canSaveExternal = false;
                }
                return;
            }

            // other 'case' line to check for other
            // permissions this app might request
        }
    }

    /**
     * Add the image files to the image adapter.
     */
    private void readImageFiles() {
        for (String f : getPictureFiles()) {
            imageAdapter.addImage(f);
        }
    }

    /**
     * Copy the image to the User's Pictures folder.
     */



    /**
     * Process the result of the picture being taken by the camera.
     *
     * @param requestCode the original request code in the intent.
     * @param resultCode  the result code.
     * @param data        the additional data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("AJB", "Activity result " + requestCode + " result was " + resultCode);
        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B: {
                if (resultCode == RESULT_OK) {
                    handlePhoto();
                }
                break;
            } // ACTION_TAKE_PHOTO_B


        } // switch
    }

    @Override
    public void onResume() {
        super.onResume();
        imageAdapter.clear();
        readImageFiles();
        imageAdapter.notifyDataSetChanged();
    }

    // Some lifecycle callbacks so that the image can survive orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
     *
     * @param context The application's environment.
     * @param action  The Intent action to check for availability.
     * @return True if an Intent with the specified action can be sent and
     * responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }


}
