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
package net.couchdev.android.layoutsandbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_spinner, dayArray);
        Spinner daySpinner = (Spinner) findViewById(R.id.daySpinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        daySpinner.setAdapter(adapter);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthArray);
        Spinner monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        monthSpinner.setAdapter(adapter);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearArray);
        Spinner yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        yearSpinner.setAdapter(adapter);
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
