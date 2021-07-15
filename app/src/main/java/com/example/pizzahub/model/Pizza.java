package com.example.pizzahub.model;

public class Pizza {
    String key, name, size, image;
    Integer price;

    public Pizza() {
    }



    public Pizza(String name, String size, String image, Integer price) {
        this.name = name;
        this.size = size;
        this.image = image;
        this.price = price;
    }
    public Pizza(String name, String image, Integer price) {
        this.name = name;
        this.size = size;
        this.image = image;
        this.price = price;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
