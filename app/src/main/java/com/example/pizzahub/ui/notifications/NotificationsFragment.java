package com.example.pizzahub.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.pizzahub.LoginActivity;
import com.example.pizzahub.R;
import com.example.pizzahub.databinding.FragmentNotificationsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    Button logout;
    DatabaseReference mData;

    TextView ID_field;
    TextView Name_field;
    TextView Gender_field;
    TextView DOB_field;
    TextView Address_field;
    TextView Phone_field;
    TextView Email_field;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        logout=(Button) root.findViewById(R.id.Logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(
                        getContext(),
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut();

                Intent i2 = new Intent(getContext(), LoginActivity.class);
                startActivity(i2);
                getActivity().finish();
            }
        });

        ID_field = (TextView) root.findViewById(R.id.ID_field);
        Name_field= (TextView) root.findViewById(R.id.name_field);
        Gender_field = (TextView) root.findViewById(R.id.gender_field);
        DOB_field = (TextView) root.findViewById(R.id.DOB_field);
        Address_field = (TextView) root.findViewById(R.id.Address_field);
        Phone_field = (TextView) root.findViewById(R.id.Phoned_field);
        Email_field = (TextView) root.findViewById(R.id.Email_field);

        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                ID_field.setText(ID);

                if(snapshot.child(ID).child("name").getValue()==null){
                    mData.child("User").child(ID).child("name").setValue("");
                    Name_field.setText("");
                }else{
                    Name_field.setText(snapshot.child(ID).child("name").getValue().toString());
                }

                if(snapshot.child(ID).child("Gender").getValue()==null){
                    mData.child("User").child(ID).child("Gender").setValue("");
                    Gender_field.setText("");
                }else{
                    Gender_field.setText(snapshot.child(ID).child("Gender").getValue().toString());
                }

                if(snapshot.child(ID).child("DOB").getValue()==null){
                    mData.child("User").child(ID).child("DOB").setValue("");
                    DOB_field.setText("");
                }else{
                    DOB_field.setText(snapshot.child(ID).child("DOB").getValue().toString());
                }

                if(snapshot.child(ID).child("Address").getValue()==null){
                    mData.child("User").child(ID).child("Address").setValue("");
                    Address_field.setText("");
                }else{
                    Address_field.setText(snapshot.child(ID).child("Address").getValue().toString());
                }

                Email_field.setText(snapshot.child(ID).child("email").getValue().toString());

                if(snapshot.child(ID).child("Phone").getValue()==null){
                    mData.child("User").child(ID).child("Phone").setValue("");
                    Phone_field.setText("");
                }else{
                    Phone_field.setText(snapshot.child(ID).child("Phone").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}