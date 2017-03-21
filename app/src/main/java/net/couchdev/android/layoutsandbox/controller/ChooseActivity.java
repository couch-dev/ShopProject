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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;

public class ChooseActivity extends AppCompatActivity{

    private static final int REQUEST_FINNISH = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        final Button privateButton = (Button) findViewById(R.id.privateButton);
        final Button businessButton = (Button) findViewById(R.id.businessButton);
        privateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(privateButton.isActivated()) {
                    Database.getInstance().addUserData(true);
                    Intent intent = new Intent(ChooseActivity.this, IDActivity.class);
                    startActivityForResult(intent, REQUEST_FINNISH);
                } else{
                    privateButton.setActivated(true);
                    privateButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    privateButton.setTextColor(getResources().getColor(R.color.white));
                    privateButton.setText(R.string.private_person_detail);

                    businessButton.setActivated(false);
                    businessButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
                    businessButton.setTextColor(getResources().getColor(R.color.black));
                    businessButton.setText(R.string.business);
                }
            }
        });
        businessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(businessButton.isActivated()) {
                    Database.getInstance().addUserData(false);
                } else{
                    businessButton.setActivated(true);
                    businessButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    businessButton.setTextColor(getResources().getColor(R.color.white));
                    businessButton.setText(R.string.business_detail);

                    privateButton.setActivated(false);
                    privateButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
                    privateButton.setTextColor(getResources().getColor(R.color.black));
                    privateButton.setText(R.string.private_person);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_FINNISH){
            finish();
        }
    }
}
