package com.example.pizzahub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Pizza> mPizza;
    private Context mContext;

    public MyAdapter(Context c, ArrayList<Pizza> mPizza) {
        mContext = c;
        this.mPizza = mPizza;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_menu, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pizza pizza = mPizza.get(position);
        holder.mName.setText(pizza.getName());
        holder.mPrice.setText(pizza.getPrice() + "$");
        Picasso.get().load(pizza.getImage()).into(holder.mImage);
        holder.recyclerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, InfoActivity.class);
//                intent.putExtra("infoId", product.getId());
//                intent.putExtra("infoName", product.getName());
//                intent.putExtra("infoPrice", product.getPrice());
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPizza.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout recyclerLayout;
        TextView mName, mPrice;
        ImageView mImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.rcName);
            mImage = itemView.findViewById(R.id.rcImage);
            mPrice = itemView.findViewById(R.id.rcPrice);
            recyclerLayout = itemView.findViewById(R.id.recyclerLayout);
        }
    }
}
