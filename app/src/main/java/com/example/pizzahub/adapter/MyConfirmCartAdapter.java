package com.example.pizzahub.adapter;

import android.content.Context;
import android.content.ReceiverCallNotAllowedException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pizzahub.R;
import com.example.pizzahub.model.CartModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyConfirmCartAdapter extends RecyclerView.Adapter<MyConfirmCartAdapter.MyCartViewHolder> {

    private Context context;
    private List<CartModel> cartModelList;

    public MyConfirmCartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @NotNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MyConfirmCartAdapter.MyCartViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_confirmcart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyCartViewHolder holder, int position) {
        Glide.with(context)
                .load(cartModelList.get(position).getImage())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("$").append(cartModelList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder().append(cartModelList.get(position).getName()));
        holder.txtQuantity.setText(new StringBuilder("x").append(cartModelList.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class MyCartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView_confirmcart)
        ImageView imageView;
        @BindView(R.id.txtName_confirmcart)
        TextView txtName;
        @BindView(R.id.txtPrice_confirmcart)
        TextView txtPrice;
        @BindView(R.id.txtQuantity_confirmcart)
        TextView txtQuantity;

        Unbinder unbinder;
        public MyCartViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
