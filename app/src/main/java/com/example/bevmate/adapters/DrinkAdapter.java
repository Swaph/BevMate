package com.example.bevmate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bevmate.R;
import java.util.ArrayList;
import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    private List<String> drinkList;
    private List<String> drinkListFull;  // Backup list for filtering

    public DrinkAdapter(List<String> drinkList) {
        this.drinkList = drinkList;
        this.drinkListFull = new ArrayList<>(drinkList);  // Copy the original list
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drink, parent, false);
        return new DrinkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        String currentDrink = drinkList.get(position);
        holder.textViewDrinkName.setText(currentDrink);
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    public void filter(String query) {
        if (query.isEmpty()) {
            drinkList.clear();
            drinkList.addAll(drinkListFull);
        } else {
            List<String> filteredList = new ArrayList<>();
            for (String drink : drinkListFull) {
                if (drink.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(drink);
                }
            }
            drinkList.clear();
            drinkList.addAll(filteredList);
        }
        notifyDataSetChanged();
    }

    public static class DrinkViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDrinkName;

        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDrinkName = itemView.findViewById(R.id.text_view_drink_name);
        }
    }
}
