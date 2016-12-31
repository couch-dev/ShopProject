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
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
        username.setInputFilter(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = "";
                for(int i=0; i<s.length(); i++){
                    char cai = s.charAt(i);
                    if(validateChar(cai)){
                        text += cai;
                    }
                }
                Log.e("TextWatcher", "text: '" + text + "', s: '" + s.toString() + "'");
                if(!text.equals(s.toString())){
                    username.setText(text);
                    username.setSelection(text.length());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        username.addInputErrorHandler(new CheckableEditText.InputErrorHandler(){
            @Override
            public String errorText() {
                return "At least 5 characters";
            }
            @Override
            public boolean errorCondition(){
                return username.getText().toString().length() < 5;
            }
            @Override
            public void onInputError(){}
        });
        username.addInputErrorHandler(new CheckableEditText.InputErrorHandler(){
            @Override
            public String errorText() {
                return "Username already exists";
            }
            @Override
            public boolean errorCondition(){
                return !ServerMock.getInstance().checkUsername(username.getText().toString());
            }
            @Override
            public void onInputError(){}
        });
        final CheckableEditText email = (CheckableEditText) findViewById(R.id.emailEdit);
        email.addInputErrorHandler(new CheckableEditText.InputErrorHandler(){
            @Override
            public String errorText() {
                return "Email invalid";
            }
            @Override
            public boolean errorCondition(){
                return !validateEmail(email.getText().toString());
            }
            @Override
            public void onInputError(){}
        });
        email.addInputErrorHandler(new CheckableEditText.InputErrorHandler(){
            @Override
            public String errorText() {
                return "Email already used";
            }
            @Override
            public boolean errorCondition(){
                return !ServerMock.getInstance().checkEmail(email.getText().toString());
            }
            @Override
            public void onInputError(){}
        });
        final CheckableEditText password = (CheckableEditText) findViewById(R.id.passwordEdit);
        password.addInputErrorHandler(new CheckableEditText.InputErrorHandler(){
            @Override
            public String errorText() {
                return "Password invalid";
            }
            @Override
            public boolean errorCondition(){
                return !validatePass(password.getText().toString());
            }
            @Override
            public void onInputError(){
                Toast toast = Toast.makeText(RegisterActivity.this, "Password must contain:\n- a lower case letter\n- an upper" +
                        " case letter\n- a digit\n- a special character\n- 8 characters at least", Toast.LENGTH_LONG);
                toast.getView().setBackgroundResource(R.drawable.toast_bg_error);
                toast.show();
            }
        });
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
                            password.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
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
        final CheckableEditText password2 = (CheckableEditText) findViewById(R.id.repeatPasswordEdit);
        password2.addInputErrorHandler(new CheckableEditText.InputErrorHandler(){
            @Override
            public String errorText() {
                return "Passwords must be the same";
            }
            @Override
            public boolean errorCondition(){
                String pass1 = password.getText().toString();
                String pass2 = password2.getText().toString();
                Log.e("Passwords", "pass1: " + pass1 + ", pass2: " + pass2);
                return !password2.getText().toString().equals(password.getText().toString());
            }
            @Override
            public void onInputError(){}
        });
        ImageButton viewPass2 = (ImageButton) findViewById(R.id.viewPassButton2);
        viewPass2.setOnTouchListener(new View.OnTouchListener() {
            private int inputType;
            private int selection;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!password2.getText().toString().isEmpty()) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            inputType = password2.getInputType();
                            selection = password2.getSelectionStart();
                            password2.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
                            password2.setSelection(selection);
                            break;
                        case MotionEvent.ACTION_UP:
                            password2.setInputType(inputType);
                            password2.setSelection(selection);
                            break;
                    }
                }
                return false;
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
                        Toast.makeText(RegisterActivity.this, "Nice, " + username.getText().toString() + "!" +
                                " You are now registered.", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(RegisterActivity.this, "Email already used!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean validateChar(char c){
        return Character.isLetterOrDigit(c) || c == '-' || c == '_' || c == '.';
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
