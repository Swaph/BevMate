package com.example.bevmate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bevmate.R;
import com.example.bevmate.models.Customer;
import java.util.List;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.CustomerViewHolder> {

    private List<Customer> customerList;

    public CustomersAdapter(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.customerNameTextView.setText("Name: " + customer.getName());
        holder.customerEmailTextView.setText("Email: " + customer.getEmail());
        holder.customerPhoneTextView.setText("Phone: " + customer.getPhone());
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameTextView;
        TextView customerEmailTextView;
        TextView customerPhoneTextView;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.customer_name);
            customerEmailTextView = itemView.findViewById(R.id.customer_email);
            customerPhoneTextView = itemView.findViewById(R.id.customer_phone);
        }
    }
}
