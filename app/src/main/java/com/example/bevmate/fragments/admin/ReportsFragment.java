package com.example.bevmate.fragments.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bevmate.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

public class ReportsFragment extends Fragment {

    private TextView tvTotalOrders, tvTotalSales, tvLowStockItems, tvTotalCustomers;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        db = FirebaseFirestore.getInstance();

        tvTotalOrders = view.findViewById(R.id.tv_total_orders);
        tvTotalSales = view.findViewById(R.id.tv_total_sales);
        tvLowStockItems = view.findViewById(R.id.tv_low_stock_items);
        tvTotalCustomers = view.findViewById(R.id.tv_total_customers);

        loadReports();

        return view;
    }

    private void loadReports() {
        // Load total orders
        db.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        int totalOrders = result.size();
                        double totalSales = 0.0;
                        for (DocumentSnapshot document : result) {
                            Double orderTotal = document.getDouble("totalAmount");
                            if (orderTotal != null) {
                                totalSales += orderTotal;
                            }
                        }
                        tvTotalOrders.setText("Total Orders: " + totalOrders);
                        tvTotalSales.setText("Total Sales: $" + String.format("%.2f", totalSales));
                    } else {
                        Log.e("ReportsFragment", "Error loading orders report", task.getException());
                    }
                });

        // Load low stock items
        db.collection("products")
                .whereLessThanOrEqualTo("stockLevel", 10)  // Assuming 10 is the low stock threshold
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int lowStockItems = task.getResult().size();
                        tvLowStockItems.setText("Low Stock Items: " + lowStockItems);
                    } else {
                        Log.e("ReportsFragment", "Error loading stock report", task.getException());
                    }
                });

        // Load total customers
        db.collection("customers")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int totalCustomers = task.getResult().size();
                        tvTotalCustomers.setText("Total Customers: " + totalCustomers);
                    } else {
                        Log.e("ReportsFragment", "Error loading customers report", task.getException());
                    }
                });
    }
}
