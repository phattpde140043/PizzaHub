package com.example.pizzahub.model;

import java.util.ArrayList;

public class Order {
    private String key,userid, name, phone, address, total, status, orderTime;
    ArrayList<Pizza> lsPizza;

    public Order() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<Pizza> getLsPizza() {
        return lsPizza;
    }

    public void setLsPizza(ArrayList<Pizza> lsPizza) {
        this.lsPizza = lsPizza;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Order(String userid, String name, String phone, String address, String total, String status, String orderTime) {
        this.userid = userid;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.status = status;
        this.orderTime = orderTime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
