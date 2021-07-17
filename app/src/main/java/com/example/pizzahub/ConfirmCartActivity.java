package com.example.pizzahub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzahub.adapter.MyCartAdapter;
import com.example.pizzahub.adapter.MyConfirmCartAdapter;
import com.example.pizzahub.eventbus.MyUpdateCartEvent;
import com.example.pizzahub.listener.ICartLoadListener;
import com.example.pizzahub.model.CartModel;
import com.example.pizzahub.ui.cart.CartFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmCartActivity extends AppCompatActivity implements ICartLoadListener {
    @BindView(R.id.recycler_confirmcart)
    RecyclerView recyclerCart;
    @BindView(R.id.mainLayout_confirmcart)
    RelativeLayout mainLayout;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.txtTotal_confirmcart)
    TextView txtTotal;


    ICartLoadListener cartLoadListener;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_cart);

        init();
        loadCartFromFirebase();


//        return v;

    }

    private void loadCartFromFirebase() {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(user)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        if(snapshot.exists())
                        {
                            for(DataSnapshot cartSnapshot:snapshot.getChildren())
                            {
                                CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                                cartModel.setKey(cartSnapshot.getKey());
                                cartModels.add(cartModel);
                            }
                            cartLoadListener.onCartLoadSuccess(cartModels);
                        }
                        else {

                            cartLoadListener.onCartLoadFailed("Cart empty");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }


    private void init(){
        ButterKnife.bind(this); // true


        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCart.setLayoutManager(layoutManager);
        recyclerCart.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CartFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }


    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        double sum = 0;
        for(CartModel cartModel : cartModelList)
        {
            sum+=cartModel.getTotalPrice();
        }
        txtTotal.setText(new StringBuilder("$").append(sum));
        MyConfirmCartAdapter adapter = new MyConfirmCartAdapter(this, cartModelList);
        recyclerCart.setAdapter(adapter);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }
}