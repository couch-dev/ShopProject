package net.couchdev.android.layoutsandbox.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;
import net.couchdev.android.layoutsandbox.model.ServerMock;
import net.couchdev.android.layoutsandbox.tools.FileTools;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Tim on 22.12.2016.
 */

public class ChatActivity extends AppCompatActivity {

    private static final String LOG_TAG = ChatActivity.class.getSimpleName();

    public static final String EXTRA_USERNAME = "username";

    private ScrollView scrollView;
    private static int id = 584;
    private EditText messageEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String chatTitle = getIntent().getStringExtra(EXTRA_USERNAME);
        getSupportActionBar().setTitle(chatTitle);

        HashMap<String, Integer> msgs = FileTools.getUnreadMessages();
        if(msgs.containsKey(chatTitle)){
            msgs.remove(chatTitle);
            FileTools.setUnreadMessages(msgs);
        }

        messageEdit = (EditText) findViewById(R.id.messageEdit);

        ImageButton sendButton = (ImageButton) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getMessage();
                String username = Database.getInstance().getUserdata().getUsername();
                messageEdit.setText("");
                ServerMock.getInstance().sendMessage(ChatActivity.this, username, message, id);
                addMessage(message, username, true, true);
            }
        });

        scrollView = (ScrollView) findViewById(R.id.scrollMain);

    }

    private void addMessage(String text, String name, boolean isOwn, boolean privateChat){
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.scrollLayout);
        RelativeLayout textLayout;
        if(isOwn){
            textLayout = (RelativeLayout) RelativeLayout.inflate(this, R.layout.textview_sent, null);
        } else if(privateChat){
            textLayout = (RelativeLayout) RelativeLayout.inflate(this, R.layout.textview_received_private, null);
        } else{
            textLayout = (RelativeLayout) RelativeLayout.inflate(this, R.layout.textview_received, null);
        }
        TextView textMessage = (TextView) textLayout.findViewById(R.id.textMessage);
        textMessage.setText(text);
        TextView textTime = (TextView) textLayout.findViewById(R.id.textTime);
        textTime.setText(getTimestamp(true));
        if(!isOwn && !privateChat){
            TextView textName = (TextView) textLayout.findViewById(R.id.textName);
            textName.setText(name);
        }
        int childCount = layout.getChildCount();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        if(childCount > 0) {
            View lastChild = layout.getChildAt(childCount - 1);
            params.addRule(RelativeLayout.BELOW, lastChild.getId());
        }
        if(isOwn){
            params.setMargins(0, 0, 10, 20);
        } else {
            params.setMargins(10, 0, 0, 20);
        }
        textLayout.setLayoutParams(params);
        textLayout.setId(id);
        Log.d(LOG_TAG, "text id: " + id);
        id++;
        if(isOwn){
            textMessage.setEnabled(false);
            View triangleView = textLayout.findViewById(R.id.triangleView);
            triangleView.setEnabled(false);
        }
        layout.addView(textLayout);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        regainFocus();
                    }
                });
            }
        });
    }

    public void receive(String text, String name, boolean privateChat){
        addMessage(text, name, false, privateChat);
    }

    protected String getTimestamp(boolean sent){
        Calendar cal = Calendar.getInstance();
        String time = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE));
//        String date = String.format("%02d.%02d.%d", cal.get(Calendar.DAY_OF_MONTH),
//                cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR));
        return time;
    }

    public void regainFocus() {
        messageEdit.requestFocus();
    }

    public void notifyMessageSent(int viewId){
        Log.d(LOG_TAG, "notify message id: " + id);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.scrollLayout);
        View message = layout.findViewById(viewId);
        TextView textMessage = (TextView) message.findViewById(R.id.textMessage);
        textMessage.setEnabled(true);
        View triangleView = message.findViewById(R.id.triangleView);
        triangleView.setEnabled(true);
    }

    private String getMessage(){
        EditText messageEdit = (EditText) findViewById(R.id.messageEdit);
        return messageEdit.getText().toString();
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
