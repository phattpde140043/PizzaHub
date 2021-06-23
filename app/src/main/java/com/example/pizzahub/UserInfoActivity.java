package com.example.pizzahub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

public class UserInfoActivity extends AppCompatActivity {

    Button UpdateButton;
    Button ChangePass;
    ConstraintSet.Layout userInfo;
    TextView ID_txt;
    TextView Nametxt;
    TextView Gendertxt;
    TextView DOBtxt;
    TextView Address_txt;
    TextView Phone_txt;
    TextView Email_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        UpdateButton = this.findViewById(R.id.Update_button);
        ChangePass = this.findViewById(R.id.ChangePass_button);
        Nametxt= this.findViewById(R.id.name_field);
        Gendertxt= this.findViewById(R.id.gender_field);
        DOBtxt= this.findViewById(R.id.DOB_field);
        Address_txt= this.findViewById(R.id.Address_field);
        Phone_txt=  this.findViewById(R.id.Phoned_field);
        Email_txt= this.findViewById(R.id.Email_field);
        UpdateButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), UpdateUserInfoActivity.class);
                intent.putExtra("Id",ID_txt.getText());
                intent.putExtra("Name",Nametxt.getText());
                intent.putExtra("Gender",Gendertxt.getText());
                intent.putExtra("DOB",DOBtxt.getText());
                intent.putExtra("Address",Address_txt.getText());
                intent.putExtra("Phone",Phone_txt.getText());
                intent.putExtra("Email",Email_txt.getText());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        ChangePass.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ChangePassActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}