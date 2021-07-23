package com.example.pizzahub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ChangePassActivity extends AppCompatActivity {

    Button Close_btn;
    Button Save_btn;
    EditText CurrPass_txt;
    EditText NewPass_txt;
    EditText CfrNewPass_txt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        Save_btn = (Button) findViewById(R.id.SaveButton);
        Close_btn = (Button) findViewById(R.id.CloseButton);
        CurrPass_txt= (EditText) findViewById(R.id.curPass_txt);
        NewPass_txt= (EditText) findViewById(R.id.newPass_txt);
        CfrNewPass_txt=(EditText) findViewById(R.id.cfrPass_txt);

        Close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("Fragment_number",3);
                startActivity(intent);
                finish();
            }
        });

        CfrNewPass_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if((!CfrNewPass_txt.getText().toString().isEmpty())&&(!NewPass_txt.getText().toString().isEmpty())) {
                        if (CfrNewPass_txt.getText().toString().equals(NewPass_txt.getText().toString())){
                        }else{
                            CfrNewPass_txt.setText("");
                        }
                    }
                }
            }
        });

        Save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!CurrPass_txt.getText().toString().isEmpty())&&(!NewPass_txt.getText().toString().isEmpty())&&(!CfrNewPass_txt.getText().toString().isEmpty())) {
                    if (CfrNewPass_txt.getText().toString().equals(NewPass_txt.getText().toString())) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Log.d("Change Password", "email : " + user.getEmail() + " pass: " + CurrPass_txt.getText().toString());
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), CurrPass_txt.getText().toString());
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(NewPass_txt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("Change Password", "Password updated");
                                                Toast.makeText(getBaseContext(), "Change password success!", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.putExtra("Fragment_number", 3);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Log.e("Change Password", "Error password not updated");
                                                Toast.makeText(getBaseContext(), "Change password failed!", Toast.LENGTH_LONG).show();
                                                NewPass_txt.setText("");
                                                CurrPass_txt.setText("");
                                                CfrNewPass_txt.setText("");
                                            }
                                        }
                                    });
                                } else {
                                    Log.e("Change Password", "Error auth failed");
                                    NewPass_txt.setText("");
                                    CurrPass_txt.setText("");
                                    CfrNewPass_txt.setText("");
                                }
                            }
                        });
                    }else{
                        NewPass_txt.setText("");
                        CurrPass_txt.setText("");
                        CfrNewPass_txt.setText("");
                    }
                }
            }
        });


    }
}