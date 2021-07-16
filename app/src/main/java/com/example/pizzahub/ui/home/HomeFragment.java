package com.example.pizzahub.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.pizzahub.MainActivity;
import com.example.pizzahub.adapter.MyPizzaAdapter;
import com.example.pizzahub.eventbus.MyUpdataCartEvent;
import com.example.pizzahub.listener.ICartLoadListener;
import com.example.pizzahub.listener.IPizzaLoadListener;
import com.example.pizzahub.model.CartModel;
import com.example.pizzahub.model.Pizza;
import com.example.pizzahub.R;

import com.example.pizzahub.utils.SpaceItemDecoration;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements IPizzaLoadListener, ICartLoadListener {

    @BindView(R.id.recycler_pizza)
    RecyclerView recyclerPizza;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    @BindView(R.id.badge)
    NotificationBadge badge;
    @BindView(R.id.btnCart)
    FrameLayout btnCart;

    IPizzaLoadListener pizzaLoadListener;
    ICartLoadListener cartLoadListener;
    View v;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(MyUpdataCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdataCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(MyUpdataCartEvent event)
    {
        countCartItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_home, container, false);

        init();
        loadDrinkFromFirebase();
        countCartItem();

        return v;
    }

    private void loadDrinkFromFirebase() {
        List<Pizza> pizzas = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Pizza")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot pizzaSnapshot:snapshot.getChildren())
                            {
                                Pizza pizza = pizzaSnapshot.getValue(Pizza.class);
                                pizza.setKey(pizzaSnapshot.getKey());
                                pizzas.add(pizza);
                            }
                            pizzaLoadListener.onPizzaLoadSuccess(pizzas);
                        }
                        else
                            pizzaLoadListener.onPizzaLoadFailed("Can't find Pizza");
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        pizzaLoadListener.onPizzaLoadFailed(error.getMessage());
                    }
                });
    }

    public void init(){
        ButterKnife.bind(this, v);

        pizzaLoadListener = this;
        cartLoadListener = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerPizza.setLayoutManager(gridLayoutManager);
        recyclerPizza.addItemDecoration(new SpaceItemDecoration());
    }
    @Override
    public void onPizzaLoadSuccess(List<Pizza> pizzaList) {
        MyPizzaAdapter adapter = new MyPizzaAdapter(getContext(),pizzaList,cartLoadListener);
        recyclerPizza.setAdapter(adapter);
    }

    @Override
    public void onPizzaLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        int cartSum = 0;
        for(CartModel cartModel: cartModelList)
            cartSum += cartModel.getQuantity();
        badge.setNumber(cartSum);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child("UserId")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot cartSnapshot:snapshot.getChildren())
                        {
                            CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                            cartModel.setKey(cartSnapshot.getKey());
                            cartModels.add(cartModel);
                        }
                        cartLoadListener.onCartLoadSuccess(cartModels);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }
}