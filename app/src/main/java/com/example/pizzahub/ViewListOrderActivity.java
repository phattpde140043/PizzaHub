package com.example.pizzahub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzahub.adapter.ListOrderAdapter;
import com.example.pizzahub.adapter.MyConfirmCartAdapter;
import com.example.pizzahub.listener.ICartLoadListener;
import com.example.pizzahub.model.CartModel;
import com.example.pizzahub.model.Order;
import com.example.pizzahub.model.Pizza;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewListOrderActivity extends AppCompatActivity {
    @BindView(R.id.recycler_listOrder)
    RecyclerView recyclerListOrder;
    @BindView(R.id.btnBack_listOrder)
    ImageView btnBack;

    View v;

    private FirebaseDatabase database;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_order);

        init();
        loadCartFromFirebase();


        reference = FirebaseDatabase.getInstance().getReference();


    }

    private void loadCartFromFirebase() {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<Order> orders = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Order Detail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        if(orderSnapshot.child("userid").getValue().toString().equals(user)){
                            Order order = orderSnapshot.getValue(Order.class);
                            order.setKey(orderSnapshot.getKey());
                            order.setLsPizza(new ArrayList<Pizza>());
                            for(DataSnapshot pizza : orderSnapshot.child("Pizza").getChildren()){
                                Pizza p = pizza.getValue(Pizza.class);
                                p.setKey(pizza.getKey());
                                order.getLsPizza().add(p);
                            }
                            orders.add(order);
                        }
                        OnLoadSuccess(orders);
                        Log.d("Load Order","size : "+orders.size());
                    }

                }else{
                    Log.e("Load Order","Can not load");
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void OnLoadSuccess(List<Order> orders){
        ListOrderAdapter adapter = new ListOrderAdapter(this, orders);
        recyclerListOrder.setAdapter(adapter);
    }


    private void init() {
        ButterKnife.bind(this); // true

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerListOrder.setLayoutManager(layoutManager);
        recyclerListOrder.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        btnBack.setOnClickListener(new View.OnClickListener() {
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