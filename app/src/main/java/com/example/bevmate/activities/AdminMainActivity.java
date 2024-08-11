package com.example.bevmate.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bevmate.R;
import com.example.bevmate.fragments.admin.CustomersFragment;
import com.example.bevmate.fragments.admin.OrdersFragment;
import com.example.bevmate.fragments.admin.ReportsFragment;
import com.example.bevmate.fragments.admin.StockManagementFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // Initialize the BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_admin);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Load the default fragment (e.g., OrdersFragment) on startup
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin,
                    new OrdersFragment()).commit();
        }
    }

    // Bottom Navigation Item Selected Listener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.nav_orders) {
                    selectedFragment = new OrdersFragment();
                } else if (itemId == R.id.nav_customers) {
                    selectedFragment = new CustomersFragment();
                } else if (itemId == R.id.nav_stock) {
                    selectedFragment = new StockManagementFragment();
                } else if (itemId == R.id.nav_reports) {
                    selectedFragment = new ReportsFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin,
                            selectedFragment).commit();
                }

                return true;
            };
}
