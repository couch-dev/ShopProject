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

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.ShopItemSerializable;
import net.couchdev.android.layoutsandbox.tools.Tools;

public class SaleItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        ShopItemSerializable shopItem =
                (ShopItemSerializable) intent.getSerializableExtra(MainActivity.EXTRA_ITEM);

        ImageView itemImage = (ImageView) findViewById(R.id.itemImage);
        TextView itemTitle = (TextView) findViewById(R.id.itemTitle);
        TextView itemOwner = (TextView) findViewById(R.id.itemOwner);
        TextView itemBrief = (TextView) findViewById(R.id.itemBrief);
        TextView itemPrice = (TextView) findViewById(R.id.itemPrice);

        itemImage.setImageBitmap(Tools.getTmpBitmap());
        itemTitle.setText(shopItem.getTitle());
        itemOwner.setText(shopItem.getUser());
        itemBrief.setText(shopItem.getBrief());
        itemPrice.setText(String.format("%.2f%s", shopItem.getPrice(), shopItem.getCurrency()));
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
