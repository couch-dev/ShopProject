/*!*********************************************************************************
 * @file
 * @brief
 * @author    Tim Reimer <reimer@bury.com>
 *
 * @par Copyright
 * This code is the property of
 *
 *     Bury GmbH & Co. KG
 *     Robert-Koch-Str. 1-7
 *     D-32584 Loehne
 *
 *     Bury Sp. z o.o.
 *     ul. Wspolna 2A
 *     PL 35-205 Rzeszow
 *
 * @par Hints
 * For history information see the commit comments in the code repository.
 *
 **********************************************************************************/
package net.couchdev.android.layoutsandbox.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;
import net.couchdev.android.layoutsandbox.model.Tools;
import net.couchdev.android.layoutsandbox.model.Userdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserDataActivity extends AppCompatActivity{


    private static final int PICK_IMAGE = 42;
    private static final int CROP_IMAGE = 21;
    private Uri outputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdata);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] dayArray = new String[31];
        for(int i=0; i<dayArray.length; i++){
            dayArray[i] = "" + (i + 1);
        }
        String[] monthArray = new String[12];
        for(int i=0; i<monthArray.length; i++){
            monthArray[i] = "" + (i + 1);
        }
        String[] yearArray = new String[60];
        for(int i=0; i<yearArray.length; i++){
            yearArray[i] = "" + (2009 - i);
        }

        // id
        Userdata userdata = Database.getInstance().getUserdata();
        TextView username = (TextView) findViewById(R.id.usernameText);
        username.setText(userdata.getUsername());
        EditText firstName = (EditText) findViewById(R.id.firstNameEdit);
        firstName.setText(userdata.getFirstName());
        EditText lastName = (EditText) findViewById(R.id.lastNameEdit);
        lastName.setText(userdata.getLastName());
        EditText email = (EditText) findViewById(R.id.emailEdit);
        email.setText(userdata.getEmail());
        TextView dateOfBirth = (TextView) findViewById(R.id.dateOfBirthText);
        dateOfBirth.setText(Tools.dateToString(userdata.getDateOfBirth()));
        // address
        String[] countryArray = getResources().getStringArray(R.array.countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, countryArray);
        final Spinner country = (Spinner) findViewById(R.id.countrySpinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        country.setAdapter(adapter);
        country.setSelection(adapter.getPosition(userdata.getCountry()));
        EditText addressLine1Edit = (EditText) findViewById(R.id.addressLine1Edit);
        addressLine1Edit.setText(userdata.getAddressLine1());
        EditText addressLine2Edit = (EditText) findViewById(R.id.addressLine2Edit);
        addressLine2Edit.setText(userdata.getAddressLine2());
        EditText addressLine3Edit = (EditText) findViewById(R.id.addressLine3Edit);
        addressLine3Edit.setText(userdata.getAddressLine3());
        EditText stateEdit = (EditText) findViewById(R.id.stateEdit);
        stateEdit.setText(userdata.getState());
        EditText zipEdit = (EditText) findViewById(R.id.zipEdit);
        zipEdit.setText(userdata.getZip());
        EditText cityEdit = (EditText) findViewById(R.id.cityEdit);
        cityEdit.setText(userdata.getCity());
        // image
        ImageView profileImage = (ImageView) findViewById(R.id.profileImage);
        Bitmap profilePic = Tools.getProfilePic();
        if(profilePic != null){
            profileImage.setImageBitmap(profilePic);
        } else{
            profileImage.setImageResource(R.drawable.ic_profile_white);
            profileImage.getDrawable().setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.MULTIPLY);
        }
        ImageView editImage = (ImageView) findViewById(R.id.editImage);
        View.OnClickListener chooseImageClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Determine Uri of camera image to save
                final File root = new File(getExternalFilesDir(null), "tmp");
                root.mkdirs();
                final String fname = Tools.getUniqueImageName();
                final File sdImageMainDirectory = new File(root, fname);
                outputFileUri = Uri.fromFile(sdImageMainDirectory);
                // Camera
                final List<Intent> cameraIntents = new ArrayList<>();
                final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                final PackageManager packageManager = getPackageManager();
                final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
                for(ResolveInfo res : listCam) {
                    final String packageName = res.activityInfo.packageName;
                    final Intent intent = new Intent(captureIntent);
                    intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    intent.setPackage(packageName);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    cameraIntents.add(intent);
                }
                // Filesystem
//                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/*");
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                // Chooser of filesystem options
                final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");
                // Add the camera options.
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        };
        profileImage.setOnClickListener(chooseImageClick);
        editImage.setOnClickListener(chooseImageClick);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case PICK_IMAGE:
                if(resultCode == RESULT_OK){
                    try{
                        Uri imageUri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);

                        File tmpImage = Tools.createTmpFile(imageBitmap);
                        outputFileUri = FileProvider.getUriForFile(UserDataActivity.this,
                                "net.couchdev.layoutsandbox.fileprovider", tmpImage);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(outputFileUri, "image/*");
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 96);
                    intent.putExtra("outputY", 96);
                    intent.putExtra("noFaceDetection", true);
                    intent.putExtra("return-data", true);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, CROP_IMAGE);
                }
                break;
            case CROP_IMAGE:
                if(resultCode == RESULT_OK){
                    Bitmap croppedImage = data.getExtras().getParcelable("data");
                    ImageView profileImage = (ImageView) findViewById(R.id.profileImage);
                    profileImage.setImageBitmap(croppedImage);
                    // copy image
                    File tmpImage = Tools.createTmpFile(croppedImage);
                    File path = new File(getExternalFilesDir(null), "profile");
                    File dstFile = new File(path, Tools.getUniqueProfilePicName());
                    Tools.copyFile(tmpImage, dstFile);

                    Tools.deleteTmpFiles();
                }
                break;
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
