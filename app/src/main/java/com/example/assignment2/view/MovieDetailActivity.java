package com.example.assignment2.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.assignment2.databinding.MovieDetailBinding;

import com.example.assignment2.viewmodel.FavViewModel;
import com.example.assignment2.viewmodel.MovieViewModel;


public class MovieDetailActivity extends AppCompatActivity {

    private MovieDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MovieDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // --------- receive data
        Intent intent = getIntent();
        String imdbID = intent.getStringExtra("imdbID");

        // --------- get data from viewmodel
        MovieViewModel movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        FavViewModel favViewModel = new ViewModelProvider(this).get(FavViewModel.class);

        movieViewModel.fetchMovieDetail(imdbID);

        // ---------- observe data
        movieViewModel.getMovieDetail().observe(this, movie -> {
            if (movie != null) {
                binding.title.setText(movie.getTitle());
                binding.year.setText(movie.getYear());
                binding.plot.setText(movie.getPlot());
                binding.rating.setText(movie.getImdbRating());
                binding.runtime.setText(movie.getRuntime());
                binding.genre.setText(movie.getGenre());

                Glide.with(MovieDetailActivity.this)
                        .load(movie.getPosterUrl())
                        .into(binding.poster);

                // ---------- add to favorites button listener
                binding.addFavBtn.setOnClickListener(v -> {
                    favViewModel.addToFavorites(movie);
                });
            } else {
                Toast.makeText(this, "Movie not found", Toast.LENGTH_SHORT).show();
            }
        });


        movieViewModel.getToastMessage().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // ---------- back button
        binding.backButton.setOnClickListener(v -> finish());

        // --------- add button observe toast message
        favViewModel.getToastMessage().observe(this, message -> {
            Toast.makeText(MovieDetailActivity.this, message, Toast.LENGTH_SHORT).show();
        });
    }
}