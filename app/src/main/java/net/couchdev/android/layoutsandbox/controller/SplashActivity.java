package net.couchdev.android.layoutsandbox.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import net.couchdev.android.layoutsandbox.R;

/**
 * Created by Tim on 26.11.2016.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

}