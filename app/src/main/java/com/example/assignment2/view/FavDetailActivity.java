package com.example.assignment2.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.assignment2.databinding.FavDetailBinding;
import com.example.assignment2.viewmodel.FavViewModel;

public class FavDetailActivity extends AppCompatActivity {

    private FavDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FavDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ---------- view model
        FavViewModel favViewModel = new ViewModelProvider(this).get(FavViewModel.class);

        // -------- receive and display data
        String imdbID = getIntent().getStringExtra("imdbID");
        favViewModel.loadMovieByImdbID(imdbID);

        favViewModel.getSelectedMovie().observe(this, movie -> {
            if (movie != null) {
                // ----- update ui
                binding.title.setText(movie.getTitle());
                binding.plot.setText(movie.getPlot());
                binding.year.setText(movie.getYear());
                binding.runtime.setText(movie.getRuntime());
                binding.rating.setText(movie.getImdbRating());
                binding.genre.setText(movie.getGenre());
                Glide.with(this)
                        .load(movie.getPosterUrl())
                        .into(binding.poster);
            } else {
                Toast.makeText(this, "Movie not found", Toast.LENGTH_SHORT).show();
            }
        });

        // ---------- back button
        binding.backButton.setOnClickListener(v -> finish());

        // ---------- delete button listener
        binding.deleteBtn.setOnClickListener(v -> {
            favViewModel.deleteMovie(imdbID,
                    () -> {
                        Toast.makeText(this, "Deleted from favorites", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    e -> Toast.makeText(this, "Error deleting the movie", Toast.LENGTH_SHORT).show()
            );
        });

        // ---------- update button listener
        binding.updateBtn.setOnClickListener(v -> {
            String newPlot = binding.plot.getText().toString().trim();

            if (newPlot.isEmpty()) {
                Toast.makeText(this, "Plot cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            favViewModel.updatePlot(imdbID, newPlot,
                    () -> {
                        Toast.makeText(this, "Plot updated successfully", Toast.LENGTH_SHORT).show();
                    },
                    e -> {
                        Toast.makeText(this, "Error updating plot", Toast.LENGTH_SHORT).show();
                    }
            );
        });
    }
}