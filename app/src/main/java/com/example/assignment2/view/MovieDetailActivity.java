package com.example.assignment2.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.assignment2.databinding.MovieDetailBinding;
import com.example.assignment2.model.Movie;
import com.example.assignment2.utils.ApiClient;

import com.example.assignment2.viewmodel.MovieViewModel;

import java.util.List;

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

        // -------- call movie detail api
        ApiClient.getMovieDetail(imdbID, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(List<Movie> movieList) {
            }

            @Override
            public void onSuccess(Movie movieDetail) {
                runOnUiThread(() -> {
                    MovieViewModel.setMovieDetail(movieDetail);

                    // ----- update ui
                    binding.title.setText(movieDetail.getTitle());
                    binding.year.setText(movieDetail.getYear());
                    binding.plot.setText(movieDetail.getPlot());
                    binding.rating.setText(movieDetail.getImdbRating());
                    binding.studio.setText(movieDetail.getProduction());

                    // ----- update image
                    Glide.with(MovieDetailActivity.this)
                            .load(movieDetail.getPosterUrl())
                            .into(binding.poster);
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(MovieDetailActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                });

            }
        });

        // ---------- back button
        binding.backButton.setOnClickListener(v -> finish());
    }
}