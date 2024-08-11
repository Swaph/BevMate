package com.example.bevmate.fragments.user;

import android.content.Intent;
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
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.example.bevmate.R;
import com.example.bevmate.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ImageView profileImage;
    private TextView profileName, profileEmail;
    private Button btnEditProfile, btnMyOrders, btnLogout, btnDeleteAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cusprofile, container, false);

        profileImage = view.findViewById(R.id.profile_image);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnMyOrders = view.findViewById(R.id.btn_my_orders);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnDeleteAccount = view.findViewById(R.id.btn_delete_account);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            loadUserProfile(user.getUid());
        }

        btnEditProfile.setOnClickListener(v -> navigateToEditProfile());
        btnLogout.setOnClickListener(v -> logOut());
        btnDeleteAccount.setOnClickListener(v -> deleteAccount());

        return view;
    }

    private void loadUserProfile(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");

                        profileName.setText(username);
                        profileEmail.setText(email);

                        // Load profile image if you have one
                        Glide.with(this).load(R.drawable.ic_profile).into(profileImage);
                    } else {
                        showError("Profile not found.");
                    }
                })
                .addOnFailureListener(e -> showError("Failed to load profile: " + e.getMessage()));
    }

    private void navigateToEditProfile() {
        Fragment editProfileFragment = new EditProfileFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, editProfileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void logOut() {
        try {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } catch (Exception e) {
            showError("Failed to log out: " + e.getMessage());
        }
    }

    private void deleteAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    db.collection("users").document(user.getUid())
                            .delete()
                            .addOnSuccessListener(aVoid -> logOut())
                            .addOnFailureListener(e -> showError("Failed to delete user data: " + e.getMessage()));
                } else {
                    showError("Failed to delete account: " + task.getException().getMessage());
                }
            }).addOnFailureListener(e -> showError("Failed to delete account: " + e.getMessage()));
        } else {
            showError("User not found. Please log in again.");
        }
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
