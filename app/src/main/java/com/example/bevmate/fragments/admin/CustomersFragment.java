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
import com.example.bevmate.adapters.CustomersAdapter;
import com.example.bevmate.models.Customer;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class CustomersFragment extends Fragment {

    private RecyclerView customersRecyclerView;
    private CustomersAdapter customersAdapter;
    private List<Customer> customerList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customers, container, false);

        db = FirebaseFirestore.getInstance();
        customersRecyclerView = view.findViewById(R.id.customers_recycler_view);
        customersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        customerList = new ArrayList<>();
        customersAdapter = new CustomersAdapter(customerList);
        customersRecyclerView.setAdapter(customersAdapter);

        loadCustomers();

        return view;
    }

    private void loadCustomers() {
        db.collection("customers")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        for (DocumentSnapshot document : result) {
                            Customer customer = document.toObject(Customer.class);
                            customerList.add(customer);
                        }
                        customersAdapter.notifyDataSetChanged();
                    } else {
                        FirebaseFirestoreException e = (FirebaseFirestoreException) task.getException();
                        Log.e("CustomersFragment", "Error loading customers", e);
                        Snackbar.make(getView(), "Failed to load customers. Please try again later.", Snackbar.LENGTH_LONG)
                                .setAction("Retry", v -> loadCustomers())
                                .show();
                    }
                });
    }
}
