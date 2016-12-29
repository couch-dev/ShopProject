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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.ServerMock;
import net.couchdev.android.layoutsandbox.view.CheckableEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final CheckableEditText username = (CheckableEditText) findViewById(R.id.usernameEdit);
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !username.getText().toString().isEmpty()){
                    username.setActivated(true);
                    if(!ServerMock.getInstance().checkUsername(username.getText().toString())){
                        // TODO: "Username already exists!"
                        username.setChecked(false);
                    } else{
                        username.setChecked(true);
                    }
                } else{
                    username.setActivated(false);
                }
            }
        });
        final CheckableEditText email = (CheckableEditText) findViewById(R.id.emailEdit);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !email.getText().toString().isEmpty()){
                    email.setActivated(true);
                    if(!validateEmail(email.getText().toString())){
                        // TODO: "Email is invalid!"
                        email.setChecked(false);
                    } else{
                        email.setChecked(true);
                    }
                } else{
                    email.setActivated(false);
                }
            }
        });
        final CheckableEditText password = (CheckableEditText) findViewById(R.id.passwordEdit);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !password.getText().toString().isEmpty()){
                    password.setActivated(true);
                    if(!validatePass(password.getText().toString())){
                        // TODO: "Password must contain lower and upper case letter, a digit and a special character!"
                        password.setChecked(false);
                    } else{
                        password.setChecked(true);
                    }
                } else{
                    password.setActivated(false);
                }
            }
        });
        final CheckableEditText password2 = (CheckableEditText) findViewById(R.id.repeatPasswordEdit);
        password2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !password2.getText().toString().isEmpty()){
                    password2.setActivated(true);
                    if(!password2.getText().toString().equals(password.getText().toString())){
                        // TODO: "Passwords must be the same!"
                        password2.setChecked(false);
                    } else{
                        password2.setChecked(true);
                    }
                } else{
                    password2.setActivated(false);
                }
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.getOnFocusChangeListener().onFocusChange(null, false);
                email.getOnFocusChangeListener().onFocusChange(null, false);
                password.getOnFocusChangeListener().onFocusChange(null, false);
                password2.getOnFocusChangeListener().onFocusChange(null, false);
                if(username.isChecked() && email.isChecked() && password.isChecked() && password2.isChecked()){
                    if(ServerMock.getInstance().createUser(username.getText().toString(),
                            email.getText().toString(), password.getText().toString())){
                        finish();
                    } else{
                        Toast.makeText(RegisterActivity.this, "Email already used!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static boolean validateEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    public static boolean validatePass(String password) {
        Pattern passwordPattern = Pattern.compile(
                "^(?=.*[0-9])(?=.*[a-zäöüß])(?=.*[A-ZÄÖÜß])(?=.*[@;:,.#~!§%&/()=?_ +*-]).{8,}$");
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.find();
    }
}
