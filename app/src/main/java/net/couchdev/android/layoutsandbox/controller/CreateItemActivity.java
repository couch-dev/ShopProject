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

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import net.couchdev.android.layoutsandbox.R;

public class CreateItemActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createitem);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView itemImage = (ImageView) findViewById(R.id.itemImage);
        itemImage.getDrawable().setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.MULTIPLY);

        String[] currencyArray = getResources().getStringArray(R.array.currency);
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, currencyArray);
        Spinner currency = (Spinner) findViewById(R.id.currencySpinner);
        currencyAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        currency.setAdapter(currencyAdapter);
        String[] tagsArray = getResources().getStringArray(R.array.tags);
        ArrayAdapter<String> tagsAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, tagsArray);
        Spinner tags = (Spinner) findViewById(R.id.tagSpinner);
        tagsAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        tags.setAdapter(tagsAdapter);
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
