package com.example.bevmate.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bevmate.R;
import com.example.bevmate.adapters.OrderAdapter;
import com.example.bevmate.models.OrderItem;  // Import this line
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<OrderItem> orderItemList = new ArrayList<>();
    private TextView totalPriceTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cusorder, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_orders);
        totalPriceTextView = view.findViewById(R.id.total_price);

        db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(orderItemList);
        recyclerView.setAdapter(orderAdapter);

        loadOrders();

        return view;
    }

    private void loadOrders() {
        // Replace "userId" with the actual user ID when fetching orders
        String userId = "userId"; // This should be dynamically fetched based on the logged-in user

        db.collection("users")
                .document(userId)
                .collection("cart")
                .get()
                .addOnSuccessListener(this::populateOrderList);
    }

    private void populateOrderList(QuerySnapshot querySnapshot) {
        orderItemList.clear();
        long totalPrice = 0;
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            OrderItem orderItem = document.toObject(OrderItem.class);
            if (orderItem != null) {
                orderItemList.add(orderItem);
                totalPrice += orderItem.getPrice() * orderItem.getQuantity();
            }
        }
        orderAdapter.notifyDataSetChanged();
        totalPriceTextView.setText("Total Price: Ksh " + totalPrice);
    }
}
