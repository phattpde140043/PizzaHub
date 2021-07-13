package com.example.pizzahub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout edtSignupName, edtSignupMail, edtSignupPassword, edtSignupRePassword;
    Button btnSignup;
    TextView tvBackToLogin;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        edtSignupName = findViewById(R.id.edt_signup_name);
        edtSignupMail = findViewById(R.id.edt_signup_mail);
        edtSignupPassword = findViewById(R.id.edt_signup_password);
        edtSignupRePassword = findViewById(R.id.edt_signup_repassword);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        tvBackToLogin = (TextView) findViewById(R.id.tv_signup_backtologin);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName() | !validateEmail() | !validatePassword() | !validateRePassword()) {
                    return;
                } else {
                    String password, name, email;
                    password = edtSignupPassword.getEditText().getText().toString().trim();
                    name = edtSignupName.getEditText().getText().toString().trim();
                    email = edtSignupMail.getEditText().getText().toString().trim();


                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    Log.e("break", "3");
                                    if (task.isSuccessful()) {
                                        User u = new User(password, name, email);
                                        database = FirebaseDatabase.getInstance();
                                        reference = database.getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        reference.setValue(u);

                                        Toast.makeText(getBaseContext(), "Sign up successfully!", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        edtSignupMail.setError("Email is exist!");
                                    }
                                }
                            });


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

    private boolean validateName() {
        String val = edtSignupName.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            edtSignupName.setError("Field can not be empty");
            return false;
        } else {
            edtSignupName.setError(null);
            edtSignupName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = edtSignupMail.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
        if (val.isEmpty()) {
            edtSignupMail.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            edtSignupMail.setError("Invalid Mail!");
            return false;
        } else {
            edtSignupMail.setError(null);
            edtSignupMail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = edtSignupPassword.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z0-9])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                //"(?=S+$)" +           //no white spaces
                "(.{6,})" +               //at least 6 characters
                "$";
        if (val.isEmpty()) {
            edtSignupPassword.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            edtSignupPassword.setError("Password should contain 6 characters!");
            return false;
        } else {
            edtSignupPassword.setError(null);
            edtSignupPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateRePassword() {
        String val = edtSignupRePassword.getEditText().getText().toString().trim();
        String pass = edtSignupPassword.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            edtSignupRePassword.setError("Field can not be empty");
            return false;
        } else if (!val.equals(pass)) {
            edtSignupRePassword.setError("Not match Password");
            return false;
        } else {
            edtSignupRePassword.setError(null);
            edtSignupRePassword.setErrorEnabled(false);
            return true;
        }
    }

}