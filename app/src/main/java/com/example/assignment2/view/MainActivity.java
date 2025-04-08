package com.example.assignment2.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignment2.R;
import com.example.assignment2.databinding.MovieMainBinding;


public class MainActivity extends AppCompatActivity {

    private MovieMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MovieMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ----- default fragment
        loadFragment(new SearchFragment());

        // ------ btn click listeners
        binding.searchNav.setOnClickListener(v -> {
            loadFragment(new SearchFragment());
        });

        binding.favNav.setOnClickListener(v -> {
            loadFragment(new FavFragment());
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}