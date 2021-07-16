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
                ID_field.setText(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Name_field.setText(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue().toString());
                Gender_field.setText(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Gender").getValue().toString());
                DOB_field.setText(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("DOB").getValue().toString());
                Address_field.setText(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Address").getValue().toString());
                Email_field.setText(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").getValue().toString());
                Phone_field.setText(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Phone").getValue().toString());
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