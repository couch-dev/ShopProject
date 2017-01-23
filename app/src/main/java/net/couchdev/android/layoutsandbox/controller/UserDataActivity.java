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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;
import net.couchdev.android.layoutsandbox.model.Tools;
import net.couchdev.android.layoutsandbox.model.Userdata;

public class UserDataActivity extends AppCompatActivity{

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
