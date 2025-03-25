package com.example.assignment2.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment2.databinding.MovieSearchBinding;
import com.example.assignment2.model.Movie;
import com.example.assignment2.utils.ApiClient;
import com.example.assignment2.view.adapter.MovieAdapter;
import com.example.assignment2.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

    private MovieSearchBinding binding;
    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;
    private List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MovieSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movieAdapter = new MovieAdapter(movieList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(movieAdapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // ---------- live data
        movieViewModel.getMovies().observe(this, movies -> {
            movieAdapter.updateMovies(movies);
        });

        //  ---------- listener for search button
        binding.searchButton.setOnClickListener(v -> {
            String query = binding.searchText.getText().toString().trim();
            if (!query.isEmpty()) {
                searchMovies(query);
            } else {
                Toast.makeText(SearchActivity.this, "Please enter a movie title", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchMovies(String query) {
        ApiClient.getMovieList(query, this, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                movieViewModel.setMovies(movies);
            }

            @Override
            public void onSuccess(Movie movieDetail) {
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(SearchActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // ------------ click movie to the movie detail screen
    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("imdbID", movie.getImdbID());
        startActivity(intent);
    }
}