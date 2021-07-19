package com.example.pizzahub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

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

        Save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("Fragment_number",3);
                startActivity(intent);
                finish();
            }
        });


    }
}