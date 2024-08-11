package com.example.bevmate.fragments.admin;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bevmate.R;
import com.example.bevmate.adapters.OrdersAdapter;
import com.example.bevmate.models.Order;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> orderList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        db = FirebaseFirestore.getInstance();
        ordersRecyclerView = view.findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(orderList);
        ordersRecyclerView.setAdapter(ordersAdapter);

        loadOrders();

        return view;
    }

    private void loadOrders() {
        db.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        for (DocumentSnapshot document : result) {
                            Order order = document.toObject(Order.class);
                            orderList.add(order);
                        }
                        ordersAdapter.notifyDataSetChanged();
                    } else {
                        FirebaseFirestoreException e = (FirebaseFirestoreException) task.getException();
                        Log.e("OrdersFragment", "Error loading orders", e);
                        Snackbar.make(getView(), "Failed to load orders. Please try again later.", Snackbar.LENGTH_LONG)
                                .setAction("Retry", v -> loadOrders())
                                .show();
                    }
                });
    }
}
