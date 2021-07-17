package com.example.pizzahub.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pizzahub.R;
import com.example.pizzahub.eventbus.MyUpdateCartEvent;
import com.example.pizzahub.model.CartModel;
import com.example.pizzahub.ui.cart.CartFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {

    private Context context;
    private List<CartModel> cartModelList;

    public MyCartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @NotNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MyCartViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.item_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyCartViewHolder holder, int position) {
        Glide.with(context)
                .load(cartModelList.get(position).getImage())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("$").append(cartModelList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder().append(cartModelList.get(position).getName()));
        holder.txtQuantity.setText(new StringBuilder().append(cartModelList.get(position).getQuantity()));

        //Event
        holder.btnMinus.setOnClickListener(v -> {
            minusCartItem(holder,cartModelList.get(position));
        });
        holder.btnPlus.setOnClickListener(v -> {
            plusCartItem(holder,cartModelList.get(position));
        });

        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Do you really want to delete item")
                    .setNegativeButton("CANCEL", (dialog1, which) -> dialog1.dismiss())
                    .setPositiveButton("OK", (dialog12, which) -> {

                        //Temp remove
                       // notifyItemRemoved(position);

                        deleteFromFirebase(cartModelList.get(position));
                        dialog12.dismiss();

                    }).create();

            dialog.show();


        });
    }

    private void deleteFromFirebase(CartModel cartModel) {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(user)
                .child(cartModel.getKey())
                .removeValue()
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));

    }

    private void plusCartItem(MyCartViewHolder holder, CartModel cartModel) {
        cartModel.setQuantity(cartModel.getQuantity()+1);
        cartModel.setTotalPrice(cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));

        holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
        updateFirebase(cartModel);
    }

    private void minusCartItem(MyCartViewHolder holder, CartModel cartModel) {
        if(cartModel.getQuantity() > 1)
        {
            cartModel.setQuantity(cartModel.getQuantity()-1);
            cartModel.setTotalPrice(cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));

            // Update quantity
            holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
            updateFirebase(cartModel);
        }

    }

    private void updateFirebase(CartModel cartModel) {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(user)
                .child(cartModel.getKey())
                .setValue(cartModel)
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class MyCartViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.btnMinus)
        ImageView btnMinus;
        @BindView(R.id.btnPlus)
        ImageView btnPlus;
        @BindView(R.id.btnDelete)
        ImageView btnDelete;
        @BindView(R.id.imageView_cart)
        ImageView imageView;
        @BindView(R.id.txtName_cart)
        TextView txtName;
        @BindView(R.id.txtPrice_cart)
        TextView txtPrice;
        @BindView(R.id.txtQuantity_cart)
        TextView txtQuantity;

        Unbinder unbinder;


        public MyCartViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
