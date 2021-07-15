package com.example.pizzahub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pizzahub.R;
import com.example.pizzahub.model.Pizza;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyPizzaAdapter extends RecyclerView.Adapter<MyPizzaAdapter.MyPizzaViewHolder> {

    private Context context;
    private List<Pizza> pizzaList;

    public MyPizzaAdapter(Context context, List<Pizza> pizzaList) {
        this.context = context;
        this.pizzaList = pizzaList;
    }

    @NonNull
    @NotNull
    @Override
    public MyPizzaViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MyPizzaViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_menu,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyPizzaViewHolder holder, int position) {
        Glide.with(context)
                .load(pizzaList.get(position).getImage())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("$").append(pizzaList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder().append(pizzaList.get(position).getName()));

    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }

    public class MyPizzaViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtPrice)
        TextView txtPrice;

        private Unbinder unbinder;
        public MyPizzaViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}