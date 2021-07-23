package com.example.pizzahub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.pizzahub.ui.cart.CartFragment;
import com.example.pizzahub.ui.notifications.NotificationsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    DatabaseReference mData;

    Button Save_btn;
    Button Close_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_info);

        Name_txt = (EditText) this.findViewById(R.id.Name_txt);
        Gender_txt = (EditText) this.findViewById(R.id.Gendertxt);
        DOB_txt = (EditText) this.findViewById(R.id.DOPtxt);
        Address_txt = (EditText) this.findViewById(R.id.Address_txt);
        Phone_txt = (EditText) this.findViewById(R.id.Phone_txt);
        Email_txt = (EditText) this.findViewById(R.id.email_txt);

        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Name_txt.setText(snapshot.child(ID).child("name").getValue().toString());
                Gender_txt.setText(snapshot.child(ID).child("Gender").getValue().toString());
                DOB_txt.setText(snapshot.child(ID).child("DOB").getValue().toString());
                Address_txt.setText(snapshot.child(ID).child("Address").getValue().toString());
                Phone_txt.setText(snapshot.child(ID).child("Phone").getValue().toString());
                Email_txt.setText(snapshot.child(ID).child("email").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        Save_btn= (Button)this.findViewById(R.id.ChangePass_button);
        Save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Name_txt.getText() != null && Gender_txt.getText() != null && DOB_txt.getText() != null &&
                        Address_txt.getText() != null && Phone_txt.getText() != null && Email_txt.getText() != null &&
                        (!Name_txt.getText().toString().isEmpty()) && (!Gender_txt.getText().toString().isEmpty()) &&
                        (!DOB_txt.getText().toString().isEmpty()) && (!Address_txt.getText().toString().isEmpty()) &&
                        (!Phone_txt.getText().toString().isEmpty()) && (!Email_txt.getText().toString().isEmpty())) {
                    String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    mData.child("User").child(ID).child("name").setValue(Name_txt.getText().toString());
                    mData.child("User").child(ID).child("Gender").setValue(Gender_txt.getText().toString());
                    mData.child("User").child(ID).child("DOB").setValue(DOB_txt.getText().toString());
                    mData.child("User").child(ID).child("Address").setValue(Address_txt.getText().toString());
                    mData.child("User").child(ID).child("Phone").setValue(Phone_txt.getText().toString());
                    mData.child("User").child(ID).child("Email").setValue(Email_txt.getText().toString());
                    Toast.makeText(getBaseContext(), "Update information success!", Toast.LENGTH_LONG).show();
                    /*Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("Fragment_number", 3);
                    startActivity(intent);
                    finish();*/
                    onBackPressed();

                } else {
                    Toast.makeText(getBaseContext(), "Update information failed!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Close_btn = (Button)this.findViewById(R.id.Logout_button);
        Close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("Fragment_number",3);
                startActivity(intent);
                finish();*/
                onBackPressed();
            }
        });

        DOB_txt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction()==KeyEvent.ACTION_DOWN){
                    if((keyCode>=KeyEvent.KEYCODE_0&&keyCode<=KeyEvent.KEYCODE_9)||keyCode==KeyEvent.KEYCODE_SLASH){
                    }else{
                        keyCode=KeyEvent.KEYCODE_D;
                    }
                }
                return false;
            }
        });

        DOB_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!validateDate(DOB_txt.getText().toString())){
                        DOB_txt.setText("");
                        Toast.makeText(getBaseContext(),"Plese type dd/mm/yyyy",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Email_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!validateEmail(Email_txt.getText().toString())){
                        Email_txt.setText("");
                    }
                }
            }
        });
    }

    public boolean validateDate(String DOB){
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(DOB);

        return matcher.matches();
    }
    public boolean validateEmail(String DOB){
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(DOB);

        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}