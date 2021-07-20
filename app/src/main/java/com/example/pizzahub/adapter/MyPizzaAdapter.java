package com.example.pizzahub.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pizzahub.R;
import com.example.pizzahub.eventbus.MyUpdateCartEvent;
import com.example.pizzahub.listener.ICartLoadListener;
import com.example.pizzahub.listener.IRecyclerViewClickListener;
import com.example.pizzahub.model.CartModel;
import com.example.pizzahub.model.Pizza;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyPizzaAdapter extends RecyclerView.Adapter<MyPizzaAdapter.MyPizzaViewHolder> {

    private Context context;
    private List<Pizza> pizzaList;
    private ICartLoadListener iCartLoadListener;

    Dialog dialog;

    public MyPizzaAdapter(Context context, List<Pizza> pizzaList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.pizzaList = pizzaList;
        this.iCartLoadListener = iCartLoadListener;
    }

    @NonNull
    @NotNull
    @Override
    public MyPizzaViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MyPizzaViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyPizzaViewHolder holder, int position) {
        Glide.with(context)
                .load(pizzaList.get(position).getImage())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("$").append(pizzaList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder().append(pizzaList.get(position).getName()));

        holder.btnAddToCart.setOnClickListener(v -> {
            addToCart(pizzaList.get(position));
        });
        Pizza pizza = pizzaList.get(position);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_pizzapopup);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = (TextView) dialog.findViewById(R.id.popupName);
                TextView price = (TextView) dialog.findViewById(R.id.popupPrice);
                ImageView image = (ImageView) dialog.findViewById(R.id.popupImage);
                TextView size = (TextView) dialog.findViewById(R.id.popupSize);
                name.setText(pizza.getName());
                price.setText("$" + pizza.getPrice().toString());
                size.setText("Size " + pizza.getSize());
                Glide.with(context).load(pizza.getImage()).apply(new RequestOptions().override(450, 300))
                        .into(image);
                dialog.show();
            }
        });
    }

    private void addToCart(Pizza pizza) {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child(user);
        userCart.child(pizza.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) //If user already have item in cart
                        {
                            // Just update quantity and totalPrice
                            CartModel cartModel = snapshot.getValue(CartModel.class);
                            cartModel.setQuantity(cartModel.getQuantity() + 1);
                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("quantity", cartModel.getQuantity());
                            updateData.put("totalPrice", cartModel.getQuantity() * Float.parseFloat(cartModel.getPrice()));

                            userCart.child(pizza.getKey())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(aVoid -> {
                                        iCartLoadListener.onCartLoadFailed("Add To Cart Success");
                                    })
                                    .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));

                        } else // If item not have in cart, add new
                        {
                            CartModel cartModel = new CartModel();
                            cartModel.setName(pizza.getName());
                            cartModel.setImage(pizza.getImage());
                            cartModel.setKey(pizza.getKey());
                            cartModel.setPrice(pizza.getPrice().toString());
                            cartModel.setQuantity(1);
                            cartModel.setTotalPrice(Float.parseFloat(pizza.getPrice().toString()));

                            userCart.child(pizza.getKey())
                                    .setValue(cartModel)
                                    .addOnSuccessListener(aVoid -> {
                                        iCartLoadListener.onCartLoadFailed("Add To Cart Success");
                                    })
                                    .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));


                        }
                        EventBus.getDefault().postSticky(new MyUpdateCartEvent());
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        iCartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }

    public class MyPizzaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtPrice)
        TextView txtPrice;
        @BindView(R.id.img_add_to_cart)
        ImageView btnAddToCart;

        IRecyclerViewClickListener listener;

        public void setListener(IRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        private Unbinder unbinder;

        public MyPizzaViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onRecyclerClick(v, getAdapterPosition());
        }
    }
}