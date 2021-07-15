package com.example.pizzahub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextInputLayout edtEmail;
    Button btnReset;
    TextView tvBackToLogin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmail = findViewById(R.id.edt_reset_email);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        tvBackToLogin = (TextView) findViewById(R.id.tv_reset_backtologin);

        mAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail()) {
                    return;
                } else {
                    resetPassword();
                }
            }
        });
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void resetPassword() {
        String email = edtEmail.getEditText().getText().toString().trim();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Please check mail to reset password!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "Reset password failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
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