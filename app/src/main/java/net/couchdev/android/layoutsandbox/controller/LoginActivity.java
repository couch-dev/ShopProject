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
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;
import net.couchdev.android.layoutsandbox.model.ServerMock;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText userEmail = (EditText) findViewById(R.id.emailEdit);
        final EditText password = (EditText) findViewById(R.id.passwordEdit);
        ImageButton viewPass = (ImageButton) findViewById(R.id.viewPassButton);
        viewPass.setOnTouchListener(new View.OnTouchListener() {
            private int inputType;
            private int selection;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!password.getText().toString().isEmpty()) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            inputType = password.getInputType();
                            selection = password.getSelectionStart();
                            password.setInputType(inputType | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            password.setSelection(selection);
                            break;
                        case MotionEvent.ACTION_UP:
                            password.setInputType(inputType);
                            password.setSelection(selection);
                            break;
                    }
                }
                return false;
            }
        });

        Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] user = ServerMock.getInstance().login(userEmail.getText().toString(),
                        "" + password.getText().toString().hashCode());
                if(user.length == 2 ){
                    Database.setLoggedInUser(user[0], user[1], "" + password.getText().toString().hashCode());
                    finish();
                    if(Database.getInstance().isComplete()){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Database.getInstance().updateLastLoggedInUser();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, ChooseActivity.class);
                        startActivity(intent);
                    }
                } else if(ServerMock.INVALID_LOGIN.equals(user[0])){
                    Toast.makeText(LoginActivity.this, "Username or Email could not be found", Toast.LENGTH_SHORT).show();
                } else if(ServerMock.INVALID_PASS.equals(user[0])){
                    Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        Button infoButton = (Button) findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                builder.setTitle("Password Restrictions");
                builder.setMessage("Your password must contain:\n\tA lower case letter\n\tAn upper" +
                        " case letter\n\tA digit\n\tA special character\n\tAt least 8 characters");
                builder.create().show();
            }
        });
    }
}
