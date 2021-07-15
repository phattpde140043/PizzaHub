package com.example.pizzahub.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzahub.LoginActivity;
import com.example.pizzahub.MainActivity;
import com.example.pizzahub.Pizza;
import com.example.pizzahub.R;
import com.example.pizzahub.databinding.FragmentHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

//    private HomeViewModel homeViewModel;
//    private FragmentHomeBinding binding;

    RecyclerView recyclerView;
    View v;
    DatabaseReference reference;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home, container, false);
        reference = FirebaseDatabase.getInstance().getReference("Pizza");
        recyclerView = v.findViewById(R.id.recyclerRow);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Pizza> options =
                new FirebaseRecyclerOptions.Builder<Pizza>()
                        .setQuery(reference, Pizza.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Pizza, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Pizza model) {
                reference = FirebaseDatabase.getInstance().getReference().child("Pizza").child(getRef(position).getKey());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue().toString();
                        String price = snapshot.child("price").getValue().toString();
                        String image = snapshot.child("image").getValue().toString();
                        holder.mName.setText(name);
                        holder.mPrice.setText("$" + price);
                        Picasso.get().load(image).into(holder.mImage);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_menu, viewGroup, false);
                ViewHolder holder = new ViewHolder(v);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName, mPrice;
        ImageView mImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.rcName);
            mImage = itemView.findViewById(R.id.rcImage);
            mPrice = itemView.findViewById(R.id.rcPrice);
        }
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
}