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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;
import net.couchdev.android.layoutsandbox.tools.Tools;
import net.couchdev.android.layoutsandbox.model.Userdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserDataActivity extends AppCompatActivity{

    private static final String LOG_TAG = UserDataActivity.class.getSimpleName();

    private static final int PICK_IMAGE = 1;
    private Uri outputFileUri;
    private Bitmap profilePic;

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
        final Userdata userdata = Database.getInstance().getUserdata();
        final TextView username = (TextView) findViewById(R.id.usernameText);
        username.setText(userdata.getUsername());
        final EditText firstName = (EditText) findViewById(R.id.firstNameEdit);
        firstName.setText(userdata.getFirstName());
        final EditText lastName = (EditText) findViewById(R.id.lastNameEdit);
        lastName.setText(userdata.getLastName());
        EditText email = (EditText) findViewById(R.id.emailEdit);
        email.setText(userdata.getEmail());
        TextView dateOfBirth = (TextView) findViewById(R.id.dateOfBirthText);
        dateOfBirth.setText(Tools.dateToString(userdata.getDateOfBirth()));
        // address
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner,
                Tools.getSortedCountries());
        final Spinner country = (Spinner) findViewById(R.id.countrySpinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        country.setAdapter(adapter);
        country.setSelection(adapter.getPosition(userdata.getCountry()));
        final EditText addressLine1Edit = (EditText) findViewById(R.id.addressLine1Edit);
        addressLine1Edit.setText(userdata.getAddressLine1());
        final EditText addressLine2Edit = (EditText) findViewById(R.id.addressLine2Edit);
        addressLine2Edit.setText(userdata.getAddressLine2());
        final EditText addressLine3Edit = (EditText) findViewById(R.id.addressLine3Edit);
        addressLine3Edit.setText(userdata.getAddressLine3());
        final EditText stateEdit = (EditText) findViewById(R.id.stateEdit);
        stateEdit.setText(userdata.getState());
        final EditText zipEdit = (EditText) findViewById(R.id.zipEdit);
        zipEdit.setText(userdata.getZip());
        final EditText cityEdit = (EditText) findViewById(R.id.cityEdit);
        cityEdit.setText(userdata.getCity());
        // image
        final ImageView profileImage = (ImageView) findViewById(R.id.profileImage);
        final Bitmap profilePicture = Tools.getProfilePic();
        if(profilePicture != null){
            profileImage.setImageBitmap(profilePicture);
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
                final String fname = Tools.getImageName();
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
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database database = Database.getInstance();
                if(firstName.getText().toString().length() > 1 && lastName.getText().toString().length() > 1){
                    database.updateUserData(firstName.getText().toString(), lastName.getText().toString(),
                            userdata.getDateOfBirthString());
                }
                if(addressLine1Edit.getText().toString().length() >= 3 && cityEdit.getText().toString().length() >= 3
                        && zipEdit.getText().toString().length() >= 3){
                    database.updateUserData(addressLine1Edit.getText().toString(), addressLine2Edit.getText().toString(),
                            addressLine3Edit.getText().toString(), country.getSelectedItem().toString(),
                            stateEdit.getText().toString(), cityEdit.getText().toString(), zipEdit.getText().toString());
                }
                if(profilePic != null){
                    saveProfilePic(profilePic);
                }
                finish();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Log.d(LOG_TAG, "PICK_IMAGE - RESULT_OK");
                    boolean fromCamera = false;
                    try {
                        Uri imageUri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                        File tmpImage = Tools.createTmpFile(imageBitmap);
                        Log.d(LOG_TAG, "tmpImage = " + tmpImage);
                        outputFileUri = FileProvider.getUriForFile(UserDataActivity.this,
                                "net.couchdev.layoutsandbox.fileprovider", tmpImage);
                    } catch (Exception e) {
                        fromCamera = true;
                    }
                    try {
                        InputStream imageStream = getContentResolver().openInputStream(outputFileUri);
                        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                        int maxSize = Math.min(imageBitmap.getWidth(), imageBitmap.getHeight());
                        int x = (imageBitmap.getWidth() - maxSize) / 2;
                        int y = (imageBitmap.getHeight() - maxSize) / 2;
                        imageBitmap = Bitmap.createBitmap(imageBitmap, x, y, maxSize, maxSize);
                        // rotate image if necessary
                        if(fromCamera){
                            ExifInterface exif = new ExifInterface(outputFileUri.getPath());
                            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                            int rotationInDegrees = exifToDegrees(rotation);
                            Matrix matrix = new Matrix();
                            if (rotation != 0) {
                                matrix.preRotate(rotationInDegrees);
                            }
                            imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, maxSize, maxSize, matrix, true);
                        }
                        profilePic = imageBitmap;
                        ImageView profileImage = (ImageView) findViewById(R.id.profileImage);
                        profileImage.setImageBitmap(profilePic);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Tools.deleteTmpFiles();
                } else {
                    Log.d(LOG_TAG, "PICK_IMAGE - fail");
                }
                break;
        }
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    private boolean saveProfilePic(Bitmap bitmap){
        File path = new File(getExternalFilesDir(null), "profile");
        File dstFile = new File(path, Tools.getUniqueProfilePicName());
        try {
            FileOutputStream out = new FileOutputStream(dstFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            ImageView profileImage = (ImageView) findViewById(R.id.profileImage);
            profileImage.setImageBitmap(bitmap);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
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
