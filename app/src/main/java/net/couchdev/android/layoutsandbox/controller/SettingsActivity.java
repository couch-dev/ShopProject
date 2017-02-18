package net.couchdev.android.layoutsandbox.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.couchdev.android.layoutsandbox.R;

/**
 * Created by Tim on 18.02.2017.
 */

public class SettingsActivity extends AppCompatActivity {

    public static final String SIGN_OUT = "sign out";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ListView listView = (ListView) findViewById(R.id.listSettings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingsActivity.this, android.R.layout.simple_list_item_1,
                new String[]{"Sign out"});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(SIGN_OUT, true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
