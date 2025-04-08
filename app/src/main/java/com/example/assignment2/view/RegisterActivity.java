package com.example.assignment2.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.assignment2.databinding.UserRegisterBinding;
import com.example.assignment2.model.Movie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private UserRegisterBinding binding;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ---------- click yo go to login page
        binding.loginClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // ---------- register listener
        mAuth = FirebaseAuth.getInstance();
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ----- grab email and password
                String email = binding.emailText.getText().toString().trim();
                String password = binding.passwordText.getText().toString().trim();

                // ------ validation
                if (email.isEmpty()) {
                    binding.emailText.setError("Please enter email");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailText.setError("Invalid email address");
                    return;
                }

                if (password.isEmpty()) {
                    binding.passwordText.setError("Please enter password");
                    return;
                }

                if (password.length() < 6) {
                    binding.passwordText.setError("Password must be at least 6 characters");
                    return;
                }

                // ------ call register method
                registerUser(email, password);
            }
        });
    }

    // ---------- register method
    private void registerUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("tag", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Register successfully!", Toast.LENGTH_SHORT).show();

                            //------- firestore collection
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("email", user.getEmail());
                            ArrayList<Movie> favorites = new ArrayList<>();
                            userInfo.put("favorites", favorites);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            db.collection("users")
                                    .document(user.getUid())
                                    .set(userInfo)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "User information saved!");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("Firestore", "Error saving user info", e);
                                    });

                            // ----- go back to login page
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        } else{
                            Log.d("tag", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}