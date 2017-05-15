package net.couchdev.android.layoutsandbox.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import net.couchdev.android.layoutsandbox.R;

/**
 * Created by Tim on 22.12.2016.
 */

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView) findViewById(R.id.salesList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_messages, R.id.usernameText,
                new String[]{"Username 1", "Username 2", "Username 3", "Username 4", "Username 5"});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
                intent.putExtra(ChatActivity.EXTRA_USERNAME, adapter.getItem(position));
                startActivity(intent);
            }
        });

        final SearchView shopSearch = (SearchView) findViewById(R.id.shopSearch);
        shopSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopSearch.setIconified(false);
            }
        });
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
