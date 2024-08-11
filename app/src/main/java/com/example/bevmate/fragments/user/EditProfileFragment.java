package com.example.bevmate.fragments.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bevmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText etProfileName, etProfileEmail, etProfilePassword;
    private Button btnSaveChanges;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        etProfileName = view.findViewById(R.id.et_profile_name);
        etProfileEmail = view.findViewById(R.id.et_profile_email);
        etProfilePassword = view.findViewById(R.id.et_profile_password);
        btnSaveChanges = view.findViewById(R.id.btn_save_changes);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            loadUserProfile(user.getUid());
        }

        btnSaveChanges.setOnClickListener(v -> saveProfileChanges(user.getUid()));

        return view;
    }

    private void loadUserProfile(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");

                        etProfileName.setText(username);
                        etProfileEmail.setText(email);
                    } else {
                        showError("Profile not found.");
                    }
                })
                .addOnFailureListener(e -> showError("Failed to load profile: " + e.getMessage()));
    }

    private void saveProfileChanges(String userId) {
        String newUsername = etProfileName.getText().toString().trim();
        String newEmail = etProfileEmail.getText().toString().trim();
        String newPassword = etProfilePassword.getText().toString().trim();

        if (TextUtils.isEmpty(newUsername)) {
            etProfileName.setError("Username is required");
            return;
        }

        if (TextUtils.isEmpty(newEmail)) {
            etProfileEmail.setError("Email is required");
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("username", newUsername);
        updates.put("email", newEmail);

        db.collection("users").document(userId).update(updates)
                .addOnSuccessListener(aVoid -> showError("Profile updated successfully"))
                .addOnFailureListener(e -> showError("Failed to update profile: " + e.getMessage()));

        // Update Firebase Authentication Email
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && !TextUtils.isEmpty(newEmail)) {
            user.updateEmail(newEmail)
                    .addOnSuccessListener(aVoid -> showError("Email updated successfully"))
                    .addOnFailureListener(e -> showError("Failed to update email: " + e.getMessage()));
        }

        // Update Firebase Authentication Password
        if (user != null && !TextUtils.isEmpty(newPassword)) {
            user.updatePassword(newPassword)
                    .addOnSuccessListener(aVoid -> showError("Password updated successfully"))
                    .addOnFailureListener(e -> showError("Failed to update password: " + e.getMessage()));
        }
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
