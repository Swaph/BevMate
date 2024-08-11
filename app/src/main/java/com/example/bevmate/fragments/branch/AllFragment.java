package com.example.bevmate.fragments.branch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bevmate.R;
import com.example.bevmate.adapters.DrinkAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllFragment extends Fragment {

    private RecyclerView recyclerView;
    private DrinkAdapter adapter;
    private FirebaseFirestore db;
    private List<String> drinkList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_all);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize drink list and adapter
        drinkList = new ArrayList<>();
        adapter = new DrinkAdapter(drinkList);
        recyclerView.setAdapter(adapter);

        // Load data from Firestore
        loadDrinksFromFirestore();

        return view;
    }

    private void loadDrinksFromFirestore() {
        db.collection("drinks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String drinkName = document.getString("name");
                            drinkList.add(drinkName);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to filter the list based on the search query
    public void filterList(String query) {
        adapter.filter(query);
    }
}
