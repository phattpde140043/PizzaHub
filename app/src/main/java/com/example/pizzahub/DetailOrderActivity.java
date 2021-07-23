package com.example.pizzahub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzahub.adapter.ListOrderAdapter;
import com.example.pizzahub.adapter.MyConfirmCartAdapter;
import com.example.pizzahub.model.CartModel;
import com.example.pizzahub.model.Order;
import com.example.pizzahub.model.Pizza;
import com.example.pizzahub.ui.notifications.NotificationsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailOrderActivity extends AppCompatActivity {
    @BindView(R.id.recycler_listorderdetail)
    RecyclerView recyclerListOrderDetail;
    @BindView(R.id.btnBack_listorderdetail)
    ImageView btnBack;
    @BindView(R.id.txtTotal_listorderdetail)
    TextView Total_txt;
    @BindView(R.id.txtName_listOrderDetail)
    TextView Name_txt;
    @BindView(R.id.txtPhone_listOrderDetail)
    TextView Phone_txt;
    @BindView(R.id.txtAddress_listOrderDetail)
    TextView Address_txt;
    @BindView(R.id.txtTime_listOrderDetail)
    TextView Time_txt;
    @BindView(R.id.txtStatus_listOrderDetail)
    TextView Status_txt;

    View v;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    String key;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_order_detail);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            key=extras.getString("Order_key");
        }

        init();
        loadCartFromFirebase();


        reference = FirebaseDatabase.getInstance().getReference();


    }

    private void loadCartFromFirebase() {
        FirebaseDatabase.getInstance().getReference().child("Order Detail").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<CartModel> cartModels = new ArrayList<>();
                for(DataSnapshot cartSnapshot : snapshot.child("Pizza").getChildren()){
                    CartModel cartModel = new CartModel();
                    cartModel.setImage(cartSnapshot.child("image").getValue().toString());
                    cartModel.setName(cartSnapshot.child("name").getValue().toString());
                    cartModel.setPrice(cartSnapshot.child("price").getValue().toString());
                    cartModel.setQuantity(Integer.parseInt(cartSnapshot.child("quantity").getValue().toString()));
                    cartModel.setTotalPrice(Integer.parseInt(cartSnapshot.child("totalPrice").getValue().toString()));
                    cartModel.setKey(cartSnapshot.getKey());
                    cartModels.add(cartModel);
                }
                OnLoadSuccess(cartModels);
                Total_txt.setText(snapshot.child("total").getValue().toString());
                Name_txt.setText("Name: "+snapshot.child("name").getValue().toString());
                Phone_txt.setText("Phone: "+snapshot.child("phone").getValue().toString());
                Time_txt.setText("Order time: "+snapshot.child("orderTime").getValue().toString());
                Address_txt.setText("Address: "+snapshot.child("address").getValue().toString());
                Status_txt.setText("Status: "+snapshot.child("status").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void OnLoadSuccess(List<CartModel> cartModels){
        MyConfirmCartAdapter adapter = new MyConfirmCartAdapter(this, cartModels);
        recyclerListOrderDetail.setAdapter(adapter);
    }



    private void init() {
        ButterKnife.bind(this); // true

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerListOrderDetail.setLayoutManager(layoutManager);
        recyclerListOrderDetail.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ViewListOrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }


}