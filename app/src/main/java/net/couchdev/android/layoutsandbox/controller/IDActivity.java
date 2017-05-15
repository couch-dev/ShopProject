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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;
import net.couchdev.android.layoutsandbox.tools.FileTools;
import net.couchdev.android.layoutsandbox.tools.Tools;
import net.couchdev.android.layoutsandbox.model.Userdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class IDActivity extends AppCompatActivity{

    private static final String LOG_TAG = IDActivity.class.getSimpleName();

    private static final int REQUEST_FINNISH = 42;
    private Spinner daySpinner;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private static final int PICK_IMAGE = 1;
    private Uri outputFileUri;
    private Bitmap profilePic;
    private Calendar calendar;
    private static final int MAX_YEAR = 2009;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);

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
            yearArray[i] = "" + (MAX_YEAR - i);
        }
        calendar = Calendar.getInstance();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, dayArray);
        daySpinner = (Spinner) findViewById(R.id.daySpinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        daySpinner.setAdapter(adapter);

        adapter = new ArrayAdapter<>(this, R.layout.item_spinner, monthArray);
        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        monthSpinner.setAdapter(adapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calendar.set(Calendar.MONTH, position);
                resetDayValues();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        adapter = new ArrayAdapter<>(this, R.layout.item_spinner, yearArray);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        yearSpinner.setAdapter(adapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calendar.set(Calendar.YEAR, MAX_YEAR - position);
                resetDayValues();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        final EditText firstName = (EditText) findViewById(R.id.firstNameEdit);
        final EditText lastName = (EditText) findViewById(R.id.lastNameEdit);

        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first = firstName.getText().toString();
                String last = lastName.getText().toString();
                if(first.length() > 1 && last.length() > 1) {
                    Database.getInstance().updateUserData(first, last, dateOfBirth());
                    Intent intent = new Intent(IDActivity.this, AddressActivity.class);
                    startActivityForResult(intent, REQUEST_FINNISH);
                } else{
                    Toast.makeText(IDActivity.this, "Please enter a first and a last name", Toast.LENGTH_SHORT).show();
                }
                saveProfilePic(profilePic);
            }
        });
        Userdata userdata = Database.getInstance().getUserdata();
        if(userdata != null){
            firstName.setText(userdata.getFirstName());
            lastName.setText(userdata.getLastName());
            if(userdata.getDateOfBirth() != null) {
                calendar = userdata.getDateOfBirth();
                setDateFromCalendar();
            }
        }

        final ImageView profileImage = (ImageView) findViewById(R.id.profileImage);
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
                final String fname = FileTools.getImageName();
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
    }

    private void resetDayValues(){
        int pos = daySpinner.getSelectedItemPosition();
        String[] dayArray = new String[calendar.getActualMaximum(Calendar.DAY_OF_MONTH)];
        for (int i = 0; i < dayArray.length; i++) {
            dayArray[i] = "" + (i + 1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(IDActivity.this, R.layout.item_spinner, dayArray);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        daySpinner.setAdapter(adapter);
        daySpinner.setSelection(Math.min(adapter.getCount()-1, pos));
    }

    private void setDateFromCalendar(){
        daySpinner.setSelection(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        monthSpinner.setSelection(calendar.get(Calendar.MONTH));
        yearSpinner.setSelection(MAX_YEAR - calendar.get(Calendar.YEAR));
    }

    private String dateOfBirth(){
        int day = daySpinner.getSelectedItemPosition() + 1;
        int month = monthSpinner.getSelectedItemPosition() + 1;
        int year = MAX_YEAR - yearSpinner.getSelectedItemPosition();
        return String.format("%02d.%02d.%d", day, month, year);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_FINNISH:
                if(resultCode == RESULT_OK){
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Log.d(LOG_TAG, "PICK_IMAGE - RESULT_OK");
                    boolean fromCamera = false;
                    try {
                        Uri imageUri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                        File tmpImage = FileTools.createTmpFile(imageBitmap);
                        Log.d(LOG_TAG, "tmpImage = " + tmpImage);
                        outputFileUri = FileProvider.getUriForFile(IDActivity.this,
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
                    FileTools.deleteTmpFiles();
                } else {
                    Log.d(LOG_TAG, "PICK_IMAGE - fail");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    private void saveProfilePic(Bitmap bitmap){
        if(FileTools.saveAsProfilePic(bitmap)) {
            ImageView profileImage = (ImageView) findViewById(R.id.profileImage);
            profileImage.setImageBitmap(bitmap);
        } else{
            Toast.makeText(IDActivity.this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }
}
