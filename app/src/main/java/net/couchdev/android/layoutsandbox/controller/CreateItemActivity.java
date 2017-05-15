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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;
import net.couchdev.android.layoutsandbox.model.ServerMock;
import net.couchdev.android.layoutsandbox.model.ShopItem;
import net.couchdev.android.layoutsandbox.tools.FileTools;
import net.couchdev.android.layoutsandbox.tools.Tools;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateItemActivity extends AppCompatActivity{

    private static final int PICK_IMAGE = 1;
    private static final String LOG_TAG = CreateItemActivity.class.getSimpleName();
    private Uri outputFileUri;
    private Bitmap itemBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createitem);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] currencyArray = getResources().getStringArray(R.array.currency);
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, currencyArray);
        final Spinner currency = (Spinner) findViewById(R.id.currencySpinner);
        currencyAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        currency.setAdapter(currencyAdapter);
        final EditText shopPrice = (EditText) findViewById(R.id.editPrice);
        shopPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    try {
                        float price = Float.parseFloat(shopPrice.getText().toString());
                        price = Math.round(price * 100) / 100.0f;
                        shopPrice.setText("" + price);
                    } catch (NumberFormatException e) {
                        shopPrice.setText("");
                    }
                }
            }
        });
        final TextView catText = (TextView) findViewById(R.id.textCategories);
        Button catButton = (Button) findViewById(R.id.categoriesButton);
        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCategoriesDialog dialog = new SelectCategoriesDialog(CreateItemActivity.this,
                        new SelectCategoriesDialog.ResultListener() {
                            @Override
                            public void onResult(ArrayList<String> result) {
                                catText.setText(Tools.enumerate(result, ", "));
                            }
                        });
                dialog.show();
            }
        });

        ImageView itemImage = (ImageView) findViewById(R.id.itemImage);
        itemImage.getDrawable().setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.MULTIPLY);
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
        itemImage.setOnClickListener(chooseImageClick);
        editImage.setOnClickListener(chooseImageClick);

        final EditText itemTitle = (EditText) findViewById(R.id.itemTitle);
        final EditText itemBrief = (EditText) findViewById(R.id.itemBrief);
        final EditText itemPrice = (EditText) findViewById(R.id.editPrice);
        final TextView textCategories = (TextView) findViewById(R.id.textCategories);
        final CheckBox checkDelivery = (CheckBox) findViewById(R.id.checkDelivery);
        final CheckBox checkPickup = (CheckBox) findViewById(R.id.checkPickup);
        Button createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = Database.getInstance().getUserdata().getUsername();
                String title = itemTitle.getText().toString();
                String brief = itemBrief.getText().toString();
                String descr = "";
                String[] categories = textCategories.getText().toString().split(",");
                ArrayList<String> catgrs = new ArrayList<>();
                for(int i=0; i< categories.length; i++){
                    catgrs.add(categories[i].trim());
                }
                double price = 0;
                try {
                    price = Double.parseDouble(itemPrice.getText().toString());
                } catch (NumberFormatException e){
                }
                String curr = (String) currency.getSelectedItem();
                boolean isDelivery = checkDelivery.isChecked();
                boolean isPickup = checkPickup.isChecked();
                ShopItem item = new ShopItem(user, title, brief, descr, catgrs, itemBitmap,
                        price, curr, isDelivery, isPickup);
                if(item.isValid()){
                    ServerMock.getInstance().addShopItem(item);
                    Toast.makeText(CreateItemActivity.this, "Your item will be listed in the shop now!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else{
                    Toast.makeText(CreateItemActivity.this, "Not all fields are filled in correctly",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
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
                        outputFileUri = FileProvider.getUriForFile(CreateItemActivity.this,
                                "net.couchdev.layoutsandbox.fileprovider", tmpImage);
                    } catch (Exception e) {
                        fromCamera = true;
                    }
                    try {
                        InputStream imageStream = getContentResolver().openInputStream(outputFileUri);
                        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                        /*int maxSize = Math.min(imageBitmap.getWidth(), imageBitmap.getHeight());
                        int x = (imageBitmap.getWidth() - maxSize) / 2;
                        int y = (imageBitmap.getHeight() - maxSize) / 2;
                        imageBitmap = Bitmap.createBitmap(imageBitmap, x, y, maxSize, maxSize);*/
                        // rotate image if necessary
                        if(fromCamera){
                            ExifInterface exif = new ExifInterface(outputFileUri.getPath());
                            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                            int rotationInDegrees = exifToDegrees(rotation);
                            Matrix matrix = new Matrix();
                            if (rotation != 0) {
                                matrix.preRotate(rotationInDegrees);
                            }
                            imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(),
                                    imageBitmap.getHeight(), matrix, true);
                        }
                        itemBitmap = imageBitmap;
                        ImageView profileImage = (ImageView) findViewById(R.id.itemImage);
                        profileImage.setImageBitmap(itemBitmap);
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

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
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
