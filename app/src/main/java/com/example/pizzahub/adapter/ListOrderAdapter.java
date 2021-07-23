package com.example.pizzahub.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pizzahub.DetailOrderActivity;
import com.example.pizzahub.MainActivity;
import com.example.pizzahub.R;
import com.example.pizzahub.eventbus.MyUpdateCartEvent;
import com.example.pizzahub.model.CartModel;
import com.example.pizzahub.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ListOrderAdapter extends RecyclerView.Adapter<ListOrderAdapter.MyCartViewHolder> {

    private Context context;
    private List<Order> OrderList;

    public ListOrderAdapter(Context context, List<Order> cartModelList) {
        this.context = context;
        this.OrderList = cartModelList;

    }

    @NonNull
    @NotNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MyCartViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.item_listorder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyCartViewHolder holder, int position) {
        Log.d("Load Order","Pizza list:"+OrderList.get(position).getLsPizza().size());
        Glide.with(context)
                .load(OrderList.get(position).getLsPizza().get(0).getImage())
                .into(holder.imageView);
        String Name_Order="";
        Name_Order+= OrderList.get(position).getLsPizza().get(0).getName()+((OrderList.get(position).getLsPizza().size()>1)?(" +"+(OrderList.get(position).getLsPizza().size()-1))+" others":" ");
        holder.txtPrice.setText(new StringBuilder("").append(OrderList.get(position).getTotal()));
        holder.txtName.setText(new StringBuilder().append(Name_Order));
        holder.txtQuantity.setText(new StringBuilder().append(OrderList.get(position).getStatus()));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickEvent(OrderList.get(position).getKey());
            }
        });
        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickEvent(OrderList.get(position).getKey());
            }
        });
        holder.txtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickEvent(OrderList.get(position).getKey());
            }
        });
        holder.txtQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickEvent(OrderList.get(position).getKey());
            }
        });

    }

    private void OnClickEvent(String key){
        Intent intent = new Intent(context, DetailOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("Order_key",key);
        context.startActivity(intent);
        //context.finish();
    }

    @Override
    public int getItemCount() {
        return OrderList.size();
    }

    public class MyCartViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imageView_listOrder)
        ImageView imageView;
        @BindView(R.id.txt_listOrder)
        TextView txtName;
        @BindView(R.id.txtPrice_listOrder)
        TextView txtPrice;
        @BindView(R.id.txtQuantity_listOrder)
        TextView txtQuantity;

        Unbinder unbinder;


        public MyCartViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }


}
