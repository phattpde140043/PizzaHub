package com.example.pizzahub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref=getApplicationContext().getSharedPreferences("MyPref",    Context.MODE_PRIVATE);

        Button LoginButton = this.findViewById(R.id.btn_login);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username_txt = findViewById(R.id.edt_login_username);
                EditText password_txt = findViewById(R.id.edt_login_password);
                String username = username_txt.getText().toString();
                String password = password_txt.getText().toString();
                if(Checklogin(username,password)){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("token","username="+username+"&password="+password+"&lastlogin="+ Calendar.getInstance().getTime().toString()+"&.");
                    editor.commit();
                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    username_txt.setText("");
                    password_txt.setText("");
                }
            }
        });
    }

    private boolean Checklogin(String UserName, String Password){
        String API_Password ="default";
        // Gọi API với tham số là Username để lấy Password sau đó gán và API_Password.

        if(API_Password.equals(Password)){
            return true;
        }
        return false;
    }

    protected void onPause() {
        super.onPause();
        EditText username_txt = findViewById(R.id.edt_login_username);
        EditText password_txt = findViewById(R.id.edt_login_password);
        username_txt.setText("");
        password_txt.setText("");
    }
}