package com.example.bevmate.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bevmate.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class DrinkDetailsFragment extends Fragment {

    private FirebaseFirestore db;
    private String drinkId;
    private ImageView drinkImage;
    private TextView drinkName, drinkDescription, drinkPrice;
    private Button btnDecrease, btnIncrease, btnAddToCart;
    private int quantity = 1;
    private long pricePerUnit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drink_details, container, false);

        drinkImage = view.findViewById(R.id.drink_image);
        drinkName = view.findViewById(R.id.drink_name);
        drinkDescription = view.findViewById(R.id.drink_description);
        drinkPrice = view.findViewById(R.id.drink_price);
        btnDecrease = view.findViewById(R.id.btn_decrease_quantity);
        btnIncrease = view.findViewById(R.id.btn_increase_quantity);
        btnAddToCart = view.findViewById(R.id.btn_add_to_cart);

        db = FirebaseFirestore.getInstance();

        // Assuming drinkId is passed as an argument
        if (getArguments() != null) {
            drinkId = getArguments().getString("drinkId");
        }

        loadDrinkDetails(drinkId);

        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updatePrice();
            }
        });

        btnIncrease.setOnClickListener(v -> {
            quantity++;
            updatePrice();
        });

        btnAddToCart.setOnClickListener(v -> {
            addToCart();
            Toast.makeText(getContext(), drinkName.getText() + " added to cart", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void loadDrinkDetails(String drinkId) {
        db.collection("drinks").document(drinkId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("description");
                String imageResourceId = documentSnapshot.getString("imageResourceId");
                pricePerUnit = documentSnapshot.getLong("prices.500ml"); // Example for 500ml price, adjust as needed

                drinkName.setText(name);
                drinkDescription.setText(description);
                drinkPrice.setText("Price: Ksh " + pricePerUnit);

                // Load image (assuming it's a URL)
                Glide.with(getContext()).load(imageResourceId).into(drinkImage);

                updatePrice();
            }
        });
    }

    private void updatePrice() {
        long totalPrice = pricePerUnit * quantity;
        drinkPrice.setText("Price: Ksh " + totalPrice);
    }

    private void addToCart() {
        // Example of adding the drink to the user's cart in Firestore
        db.collection("users").document("userId") // Replace with actual user ID
                .collection("cart")
                .add(new CartItem(drinkId, drinkName.getText().toString(), pricePerUnit, quantity));
    }

    public static class CartItem {
        public String drinkId;
        public String name;
        public long price;
        public int quantity;

        public CartItem() {
            // Empty constructor needed for Firestore serialization
        }

        public CartItem(String drinkId, String name, long price, int quantity) {
            this.drinkId = drinkId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
