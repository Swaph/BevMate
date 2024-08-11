package com.example.bevmate.fragments.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.bevmate.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class StockManagementFragment extends Fragment {
    private FirebaseFirestore db;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_stock_management, container, false);

            db = FirebaseFirestore.getInstance();

            checkStockLevels();

            return view;
        }

        private void checkStockLevels() {
            db.collection("products")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            for (DocumentSnapshot document : result) {
                                Long stockLevel = document.getLong("stockLevel");
                                Long threshold = document.getLong("threshold");

                                if (stockLevel != null && threshold != null && stockLevel < threshold) {
                                    String productName = document.getString("name");
                                    showRestockDialog(document.getId(), productName, stockLevel);
                                }
                            }
                        } else {
                            FirebaseFirestoreException e = (FirebaseFirestoreException) task.getException();
                            Log.e("StockManagementFragment", "Error checking stock levels", e);
                            Snackbar.make(getView(), "Error checking stock levels. Please try again later.", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", v -> checkStockLevels())
                                    .show();
                        }
                    });
        }

        private void showRestockDialog(String productId, String productName, long currentStock) {
            // Inflate the custom layout for the dialog
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View dialogView = inflater.inflate(R.layout.dialog_restock, null);

            // Create the dialog
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            dialogBuilder.setView(dialogView);
            AlertDialog dialog = dialogBuilder.create();

            // Set product name and current stock
            TextView tvProductName = dialogView.findViewById(R.id.tvProductName);
            tvProductName.setText(productName);

            EditText etNewStockLevel = dialogView.findViewById(R.id.etNewStockLevel);

            // Handle restock button
            Button btnRestock = dialogView.findViewById(R.id.btnRestock);
            btnRestock.setOnClickListener(v -> {
                String newStockString = etNewStockLevel.getText().toString().trim();
                if (!newStockString.isEmpty()) {
                    long newStockLevel = Long.parseLong(newStockString);
                    updateStockLevel(productId, newStockLevel);
                    dialog.dismiss();
                } else {
                    etNewStockLevel.setError("Please enter a valid stock quantity");
                }
            });

            // Handle cancel button
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(v -> dialog.dismiss());

            // Show the dialog
            dialog.show();
        }

        private void updateStockLevel(String productId, long newStockLevel) {
            db.collection("products").document(productId)
                    .update("stockLevel", newStockLevel)
                    .addOnSuccessListener(aVoid -> Snackbar.make(getView(), "Stock updated successfully", Snackbar.LENGTH_LONG).show())
                    .addOnFailureListener(e -> Snackbar.make(getView(), "Failed to update stock. Please try again.", Snackbar.LENGTH_LONG).show());
        }
    }
