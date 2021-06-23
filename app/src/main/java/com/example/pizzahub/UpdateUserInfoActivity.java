package com.example.pizzahub;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

public class UpdateUserInfoActivity extends AppCompatActivity {

    Button UpdateButton;
    ConstraintSet.Layout userInfo;
    String Id;
    String Name;
    String Gender;
    String DOB;
    String Address;
    String Phone;
    String Email;
    EditText Name_txt;
    EditText Gender_txt;
    EditText DOB_txt;
    EditText Address_txt;
    EditText Phone_txt;
    EditText Email_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_info);
        TextView ID = this.findViewById(R.id.ID_field);
        ID.setText(Id+"");
        Name_txt = this.findViewById(R.id.name_field);
        Name_txt.setText(Name+"");
        Gender_txt = this.findViewById(R.id.gender_field);
        Gender_txt.setText(Gender+"");
        DOB_txt = this.findViewById(R.id.DOB_field);
        DOB_txt.setText(DOB+"");
        Address_txt = this.findViewById(R.id.Address_field);
        Address_txt.setText(Address+"");
        Phone_txt = this.findViewById(R.id.Phoned_field);
        Phone_txt.setText(Phone+"");
        Email_txt = this.findViewById(R.id.Email_field);
        Email_txt.setText(Email+"");
    }
}