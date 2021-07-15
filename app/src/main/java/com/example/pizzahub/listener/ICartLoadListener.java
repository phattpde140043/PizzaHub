package com.example.pizzahub.listener;

import com.example.pizzahub.model.CartModel;
import com.example.pizzahub.model.Pizza;

import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailed(String message);
}
