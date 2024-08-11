package com.example.bevmate.models;

import java.util.Date;
import java.util.List;

public class Order {
    private String orderId;
    private String status;
    private double totalAmount;

    public Order(Object o, String customerId, String branchId, double totalAmount, Date date, List<OrderItem> cart) {
        // Required empty constructor for Firestore
    }

    public Order(String orderId, String status, double totalAmount) {
        this.orderId = orderId;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
