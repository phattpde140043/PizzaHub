package com.example.pizzahub.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzahub.MyAdapter;
import com.example.pizzahub.Pizza;
import com.example.pizzahub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<Pizza> pList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    View v;
    DatabaseReference reference;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = v.findViewById(R.id.recyclerRow);

        reference = FirebaseDatabase.getInstance().getReference("Pizza");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("abc ", "a");
                for (DataSnapshot child : snapshot.getChildren()) {
                    String name = child.child("name").getValue().toString();
                    String price = child.child("price").getValue().toString();
                    String image = child.child("image").getValue().toString();
                    Pizza p = new Pizza(name, image, Integer.parseInt(price));
                    pList.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
        mAdapter = new MyAdapter(getContext(), pList);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }
}

