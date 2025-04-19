package com.example.randomly;

public class Product {
    private String name;
    private int quantity;
    private int bought;

    public Product(String name, int quantity, int bought) {
        this.name = name;
        this.quantity = quantity;
        this.bought = bought;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBought() {
        return bought;
    }

    public void setBought(int bought) {
        this.bought = bought;
    }
}