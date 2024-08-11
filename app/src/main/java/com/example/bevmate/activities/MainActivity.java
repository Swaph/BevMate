package com.example.bevmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.bevmate.R;
import com.example.bevmate.adapters.ViewPagerAdapter;
import com.example.bevmate.fragments.branch.AllFragment;
import com.example.bevmate.fragments.branch.EnergyFragment;
import com.example.bevmate.fragments.branch.JuiceFragment;
import com.example.bevmate.fragments.branch.SodaFragment;
import com.example.bevmate.fragments.branch.WaterFragment;
import com.example.bevmate.fragments.user.OrderFragment;
import com.example.bevmate.fragments.user.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private SearchView searchView;
    private ViewPagerAdapter adapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is logged in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // No user is signed in, redirect to LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // End MainActivity so that the user cannot return to it without logging in
            return; // Prevent the rest of the onCreate from running
        }

        // If user is logged in, continue to set up the main activity
        setContentView(R.layout.activity_main);

        // Initialize UI components
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        searchView = findViewById(R.id.search_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set up ViewPager with the adapter
        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Attach the ViewPager to the TabLayout
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("All");
                } else if (position == 1) {
                    tab.setText("Soda");
                } else if (position == 2) {
                    tab.setText("Energy");
                } else if (position == 3) {
                    tab.setText("Juice");
                } else if (position == 4) {
                    tab.setText("Water");
                }
            }
        }).attach();

        // Set up SearchView functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCurrentFragment(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCurrentFragment(newText);
                return false;
            }
        });

        // Handle Bottom Navigation Item Selections
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                viewPager.setVisibility(ViewPager2.VISIBLE);
                tabLayout.setVisibility(TabLayout.VISIBLE);
                searchView.setVisibility(SearchView.VISIBLE);
                findViewById(R.id.fragment_container).setVisibility(View.GONE);
                return true;
            } else if (itemId == R.id.nav_order) {
                selectedFragment = new OrderFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                viewPager.setVisibility(ViewPager2.GONE);
                tabLayout.setVisibility(TabLayout.GONE);
                searchView.setVisibility(SearchView.GONE);
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }

    private void filterCurrentFragment(String query) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("f" + viewPager.getCurrentItem());

        if (currentFragment instanceof AllFragment) {
            ((AllFragment) currentFragment).filterList(query);
        } else if (currentFragment instanceof SodaFragment) {
            ((SodaFragment) currentFragment).filterList(query);
        } else if (currentFragment instanceof EnergyFragment) {
            ((EnergyFragment) currentFragment).filterList(query);
        } else if (currentFragment instanceof JuiceFragment) {
            ((JuiceFragment) currentFragment).filterList(query);
        } else if (currentFragment instanceof WaterFragment) {
            ((WaterFragment) currentFragment).filterList(query);
        }
    }
}
