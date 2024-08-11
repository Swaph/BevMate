package com.example.bevmate.models;

public class OrderItem {
    private String drinkId;
    private String name;
    private long price;
    private int quantity;

    // Empty constructor for Firestore serialization
    public OrderItem() {
    }

    public OrderItem(String drinkId, String name, long price, int quantity) {
        this.drinkId = drinkId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getter methods
    public String getDrinkId() {
        return drinkId;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
