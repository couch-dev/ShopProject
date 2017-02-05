package net.couchdev.android.layoutsandbox.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;
import net.couchdev.android.layoutsandbox.model.ServerMock;
import net.couchdev.android.layoutsandbox.model.Tools;

/**
 * Created by Tim on 26.11.2016.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init(){
        Tools.init(getApplicationContext());
        Database.init(getApplicationContext());
        ServerMock.init(getApplicationContext());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            String[] lastLogin = Database.getInstance().getLastLogin();
            if(lastLogin != null){
                String[] user = ServerMock.getInstance().login(lastLogin[0], lastLogin[1]);
                if(user.length == 2){
                    Database.setLoggedInUser(lastLogin[0], lastLogin[1], lastLogin[2]);
                    finish();
                    if(Database.getInstance().isComplete()){
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        Database.getInstance().updateLastLoggedInUser();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, ChooseActivity.class);
                        startActivity(intent);
                    }
                } else{
                    // login failed: Should be impossible at this point
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

}