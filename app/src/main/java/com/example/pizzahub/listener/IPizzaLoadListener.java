package com.example.pizzahub.listener;



import com.example.pizzahub.model.Pizza;

import java.util.List;

public interface IPizzaLoadListener {
    void onPizzaLoadSuccess(List<Pizza> pizzaList);
    void onPizzaLoadFailed(String message);
}