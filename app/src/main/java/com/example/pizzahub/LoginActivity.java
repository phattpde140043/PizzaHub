package com.example.pizzahub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences pref;
    TextView SignupButton;
    TextInputLayout username_txt, password_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        Button LoginButton = this.findViewById(R.id.btn_login);
        SignupButton = (TextView) findViewById(R.id.tv_login_signup);
        username_txt = findViewById(R.id.edt_login_username);
        password_txt = findViewById(R.id.edt_login_password);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = username_txt.getEditText().getText().toString();
                String password = password_txt.getEditText().getText().toString();
                if (validateUserName() & Checklogin(username, password)) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("token", "username=" + username + "&password=" + password + "&lastlogin=" + Calendar.getInstance().getTime().toString() + "&.");
                    editor.commit();
                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean Checklogin(String UserName, String Password) {
        String API_Password = "default";
        // Gọi API với tham số là Username để lấy Password sau đó gán và API_Password.

        if (Password.isEmpty()) {
            password_txt.setError("Field can not be empty");
            return false;
        } else if (API_Password.equals(Password)) {
            password_txt.setError(null);
            password_txt.setErrorEnabled(false);
            return true;
        } else {
            password_txt.setError("Incorrect Password");
            return false;
        }
    }

    private boolean validateUserName() {
        String val = username_txt.getEditText().getText().toString().trim();
        String checkspaces = "/(\\s)/";
        if (val.isEmpty()) {
            username_txt.setError("Field can not be empty");
            return false;
        } else {
            username_txt.setError(null);
            username_txt.setErrorEnabled(false);
            return true;
        }
    }

//    private boolean validatePassword() {
//        String val = password_txt.getEditText().getText().toString().trim();
//
//        if (val.isEmpty()) {
//            password_txt.setError("Field can not be empty");
//            return false;
//        } else {
//            password_txt.setError(null);
//            password_txt.setErrorEnabled(false);
//            return true;
//        }
//    }
}