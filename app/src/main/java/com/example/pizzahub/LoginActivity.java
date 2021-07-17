package com.example.pizzahub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    //    SharedPreferences pref;
    TextView SignupButton, ForgotPassword;
    TextInputLayout edtEmail, edtPassword;
    Button LoginButton, LoginWithGG;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    private ActivityResultLauncher<Intent> loginWithGGActivityResultLauncher;
    FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference reference;
//    private final static int RC_SIGN_IN = 123;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        LoginButton = this.findViewById(R.id.btn_login);
        SignupButton = (TextView) findViewById(R.id.tv_login_signup);
        edtEmail = findViewById(R.id.edt_login_email);
        edtPassword = findViewById(R.id.edt_login_password);
        ForgotPassword = findViewById(R.id.tv_login_forgot_pass);
        LoginWithGG = findViewById(R.id.btn_login_with_gg);

        mAuth = FirebaseAuth.getInstance();
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getEditText().getText().toString().trim();
                String password = edtPassword.getEditText().getText().toString().trim();
                if (!validateEmail() | !validatePassword()) {
                    return;
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                LoginSuccess();
                            } else {
                                Toast.makeText(LoginActivity.this, "Failed to login! Please check your email and password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //                ActivityResultLauncher<Intent>
        loginWithGGActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Log.e("TAG", "RESULT_OK");
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            try {
                                // Google Sign In was successful, authenticate with Firebase
                                GoogleSignInAccount account = task.getResult(ApiException.class);
                                Log.e("TAG", "firebaseAuthWithGoogle:" + account.getId());
                                firebaseAuthWithGoogle(account.getIdToken());
                            } catch (ApiException e) {
                                // Google Sign In failed, update UI appropriately
                                Log.e("TAG", "Google sign in failed", e);
                            }
                        } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                            Log.e("TAG", "RESULT_CANCELED");
                        } else if (result.getResultCode() == Activity.RESULT_FIRST_USER) {
                            Log.e("TAG", "RESULT_FIRST_USER");
                        }
                    }
                });
        LoginWithGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Configure Google Sign In
                signIn();
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

    private void LoginSuccess() {

        Toast.makeText(LoginActivity.this, "Login successfully!", Toast.LENGTH_LONG).show();
        currentUser = mAuth.getCurrentUser();
        Log.e("TAG", currentUser.getEmail());

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        loginWithGGActivityResultLauncher.launch(signInIntent);
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                User u = new User(user.getEmail());
                                database = FirebaseDatabase.getInstance();
                                reference = database.getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                reference.setValue(u);
                            }
                            LoginSuccess();
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to login! Please check your email and password", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }

    private boolean validatePassword() {
        String val = edtPassword.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            edtPassword.setError("Field can not be empty");
            return false;
        } else {
            edtEmail.setError(null);
            edtEmail.setErrorEnabled(false);
            return true;
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