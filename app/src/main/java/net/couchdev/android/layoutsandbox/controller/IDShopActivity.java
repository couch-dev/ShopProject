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
import net.couchdev.android.layoutsandbox.model.Userdata;
import net.couchdev.android.layoutsandbox.tools.FileTools;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class IDShopActivity extends AppCompatActivity{

    private static final String LOG_TAG = IDShopActivity.class.getSimpleName();

    private static final int REQUEST_FINNISH = 42;
    private static final int PICK_IMAGE = 1;
    private Uri outputFileUri;
    private Bitmap profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_shop);

        final EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        final EditText descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);

        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString();
                String description = descriptionEdit.getText().toString();
                if(name.length() > 2) {
                    Database.getInstance().updateUserData(name, description);
                    Intent intent = new Intent(IDShopActivity.this, AddressActivity.class);
                    startActivityForResult(intent, REQUEST_FINNISH);
                } else{
                    Toast.makeText(IDShopActivity.this, "Please enter a first and a last name", Toast.LENGTH_SHORT).show();
                }
                if(profilePic != null) {
                    saveProfilePic(profilePic);
                }
            }
        });
        Userdata userdata = Database.getInstance().getUserdata();
        if(userdata != null){
            nameEdit.setText(userdata.getFirstName());
            descriptionEdit.setText(userdata.getLastName());
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
                final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                        outputFileUri = FileProvider.getUriForFile(IDShopActivity.this,
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
            Toast.makeText(IDShopActivity.this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }
}
