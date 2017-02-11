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
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;
import net.couchdev.android.layoutsandbox.model.ServerMock;
import net.couchdev.android.layoutsandbox.tools.Tools;
import net.couchdev.android.layoutsandbox.view.CheckableEditText;

public class AddressActivity extends AppCompatActivity{

    private static final int REQUEST_ACCEPT = 1;
    private CheckBox checkAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner,
                Tools.getSortedCountries());
        final Spinner country = (Spinner) findViewById(R.id.countrySpinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        country.setAdapter(adapter);

        final CheckableEditText firstLine = (CheckableEditText) findViewById(R.id.addressLine1Edit);
        firstLine.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !firstLine.getText().toString().isEmpty()){
                    firstLine.setActivated(true);
                    if(firstLine.getText().toString().length() < 3){
                        // TODO: "Address line invalid!"
                        firstLine.setChecked(false);
                    } else{
                        firstLine.setChecked(true);
                    }
                } else{
                    firstLine.setActivated(false);
                }
            }
        });
        final RelativeLayout layoutMore = (RelativeLayout) findViewById(R.id.layoutMore);
        final TextView moreHeader = (TextView) findViewById(R.id.moreHeader);
        moreHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutMore.getVisibility() == View.GONE){
                    layoutMore.setVisibility(View.VISIBLE);
                    ImageView arrow = (ImageView) findViewById(R.id.arrowImage);
                    arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up_small));
                } else{
                    layoutMore.setVisibility(View.GONE);
                    ImageView arrow = (ImageView) findViewById(R.id.arrowImage);
                    arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down_small));
                }
            }
        });
        moreHeader.callOnClick();
        final EditText secondLine = (EditText) findViewById(R.id.addressLine2Edit);
        final EditText thirdLine = (EditText) findViewById(R.id.addressLine3Edit);
        final EditText state = (EditText) findViewById(R.id.stateEdit);
        final CheckableEditText city = (CheckableEditText) findViewById(R.id.cityEdit);
        city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !city.getText().toString().isEmpty()){
                    city.setActivated(true);
                    if(city.getText().toString().length() < 3){
                        // TODO: "City invalid!"
                        city.setChecked(false);
                    } else{
                        city.setChecked(true);
                    }
                } else{
                    city.setActivated(false);
                }
            }
        });
        final CheckableEditText zip = (CheckableEditText) findViewById(R.id.zipEdit);
        zip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !zip.getText().toString().isEmpty()){
                    zip.setActivated(true);
                    if(zip.getText().toString().length() < 3){
                        // TODO: "ZIP invalid!"
                        zip.setChecked(false);
                    } else{
                        zip.setChecked(true);
                    }
                } else{
                    zip.setActivated(false);
                }
            }
        });

        checkAccept = (CheckBox) findViewById(R.id.acceptCheck);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLine.getOnFocusChangeListener().onFocusChange(null, false);
                city.getOnFocusChangeListener().onFocusChange(null, false);
                zip.getOnFocusChangeListener().onFocusChange(null, false);
                if(checkAccept.isChecked() && firstLine.isChecked() && city.isChecked() &&
                        zip.isChecked() && country.getSelectedItemPosition() != 0){
                    Database.getInstance().updateUserData(firstLine.getText().toString(), secondLine.getText().toString(),
                            thirdLine.getText().toString(), country.getSelectedItem().toString(), state.getText().toString(),
                            city.getText().toString(), zip.getText().toString());
                    Database.getInstance().setComplete(true);
                    Database.getInstance().updateLastLoggedInUser();
                    Intent intent = new Intent(AddressActivity.this, MainActivity.class);
                    startActivity(intent);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        Button agbButton = (Button) findViewById(R.id.agbButton);
        agbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, AGBActivity.class);
                startActivityForResult(intent, REQUEST_ACCEPT);
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_ACCEPT){
            checkAccept.setChecked(true);
        }
    }
}
