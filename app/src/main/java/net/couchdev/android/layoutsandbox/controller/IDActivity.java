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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;
import net.couchdev.android.layoutsandbox.view.CheckableEditText;

import java.util.Calendar;

public class IDActivity extends AppCompatActivity{

    private static final int REQUEST_FINNISH = 42;
    private Spinner daySpinner;
    private Spinner monthSpinner;
    private Spinner yearSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, dayArray);
        daySpinner = (Spinner) findViewById(R.id.daySpinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        daySpinner.setAdapter(adapter);

        adapter = new ArrayAdapter<>(this, R.layout.item_spinner, monthArray);
        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        monthSpinner.setAdapter(adapter);

        adapter = new ArrayAdapter<>(this, R.layout.item_spinner, yearArray);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        yearSpinner.setAdapter(adapter);

        final CheckableEditText firstName = (CheckableEditText) findViewById(R.id.firstNameEdit);
        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !firstName.getText().toString().isEmpty()){
                    firstName.setActivated(true);
                    if(firstName.getText().toString().length() < 3){
                        // TODO: "First name invalid!"
                        firstName.setChecked(false);
                    } else{
                        firstName.setChecked(true);
                    }
                } else{
                    firstName.setActivated(false);
                }
            }
        });
        final CheckableEditText lastName = (CheckableEditText) findViewById(R.id.lastNameEdit);
        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !lastName.getText().toString().isEmpty()){
                    lastName.setActivated(true);
                    if(lastName.getText().toString().length() < 3){
                        // TODO: "Last name invalid!"
                        lastName.setChecked(false);
                    } else{
                        lastName.setChecked(true);
                    }
                } else{
                    lastName.setActivated(false);
                }
            }
        });

        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName.getOnFocusChangeListener().onFocusChange(null, false);
                lastName.getOnFocusChangeListener().onFocusChange(null, false);
                if(firstName.isChecked() && lastName.isChecked()) {
                    Database.getInstance().updateUserData(firstName.getText().toString(),
                            lastName.getText().toString(), dateOfBirth());
                    Intent intent = new Intent(IDActivity.this, AddressActivity.class);
                    startActivityForResult(intent, REQUEST_FINNISH);
                }
            }
        });
    }

    private String dateOfBirth(){
        int day = daySpinner.getSelectedItemPosition() + 1;
        int month = monthSpinner.getSelectedItemPosition() + 1;
        int year = 2009 - yearSpinner.getSelectedItemPosition();
        return String.format("%02d.%02d.%d", day, month, year);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_FINNISH){
            setResult(RESULT_OK);
            finish();
        }
    }
}
