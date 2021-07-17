package com.example.pizzahub.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzahub.ConfirmCartActivity;
import com.example.pizzahub.MainActivity;
import com.example.pizzahub.R;
import com.example.pizzahub.adapter.MyCartAdapter;
import com.example.pizzahub.eventbus.MyUpdateCartEvent;
import com.example.pizzahub.listener.ICartLoadListener;
import com.example.pizzahub.model.CartModel;
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

public class CartFragment extends Fragment implements ICartLoadListener {

    @BindView(R.id.recycler_cart)
    RecyclerView recyclerCart;
    @BindView(R.id.mainLayout_cart)
    RelativeLayout mainLayout;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    //    @BindView(R.id.btnOrder)
    Button btnOrder;


    ICartLoadListener cartLoadListener;
    View v;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event) {
        loadCartFromFirebase();

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_cart, container, false);
        btnOrder = v.findViewById(R.id.btnOrder);
        init();
        loadCartFromFirebase();
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ConfirmCartActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//                finish();
//                Fragment fragment = new ConfirmCartActivity();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });

        return v;
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

                        if (snapshot.exists()) {
                            for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                                CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                                cartModel.setKey(cartSnapshot.getKey());
                                cartModels.add(cartModel);
                            }
                            cartLoadListener.onCartLoadSuccess(cartModels);
                        } else {
                            for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                                CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                                cartModel.setKey(cartSnapshot.getKey());
                                cartModels.add(cartModel);
                            }
                            cartLoadListener.onCartLoadSuccess(cartModels);
                            cartLoadListener.onCartLoadFailed("Cart empty");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    private void init() {
        ButterKnife.bind(this, v); // true


        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerCart.setLayoutManager(layoutManager);
        recyclerCart.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));


    }


    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        double sum = 0;
        for (CartModel cartModel : cartModelList) {
            sum += cartModel.getTotalPrice();
        }
        txtTotal.setText(new StringBuilder("$").append(sum));
        MyCartAdapter adapter = new MyCartAdapter(getContext(), cartModelList);
        recyclerCart.setAdapter(adapter);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }
}