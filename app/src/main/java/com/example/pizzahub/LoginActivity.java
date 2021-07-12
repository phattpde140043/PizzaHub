package com.example.pizzahub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences pref;
    TextView SignupButton;
    TextInputLayout edtEmail, edtPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        if (pref.getString("token", null )!=null){
            Log.d("STATE","token:"+pref.getString("token", null ));
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        Button LoginButton = this.findViewById(R.id.btn_login);
        SignupButton = (TextView) findViewById(R.id.tv_login_signup);
        edtEmail = findViewById(R.id.edt_login_email);
        edtPassword = findViewById(R.id.edt_login_password);

        mAuth = FirebaseAuth.getInstance();
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getEditText().getText().toString().trim();
                String password = edtPassword.getEditText().getText().toString().trim();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("token", "email=" + email + "&password=" + password + "&lastlogin=" + Calendar.getInstance().getTime().toString() + "&.");
                            editor.commit();
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to login! Please check your email and password", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

    private boolean validatePassword() {
        String val = edtPassword.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            edtPassword.setError("Field can not be empty");
            return false;
        } else {
            edtPassword.setError("Incorrect Password");
            return false;
        }
    }

    private boolean validateEmail() {
        String val = edtEmail.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
        if (val.isEmpty()) {
            edtEmail.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            edtEmail.setError("Invalid Mail!");
            return false;
        } else {
            edtEmail.setError(null);
            edtEmail.setErrorEnabled(false);
            return true;
        }
    }


}