package com.example.ap_assignment4.Assignment3_classes;

import java.io.Serializable;

public class Item implements Comparable<Item>, Serializable {
    private String name;
    private double price;
    private boolean available;
    private String category;
    private static final long serialVersionUID = 1L;

    public Item(String name, double price,boolean available, String category){
        this.name = name;
        this.price = price;
        this.available = available;
        this.category = category;
    }
    @Override
    public String toString() {
        return name + "," + price + "," + available + "," + category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    public void setAvailable(boolean isAvailable) {
        this.available = isAvailable;
    }

    public void setCategory(String newCategory) {
        this.category = newCategory;
    }

    @Override
    public int compareTo(Item i) {
        return Double.compare(this.getPrice(), i.getPrice());
    }
}
