package com.example.assignment2.view;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.assignment2.databinding.FragmentSearchBinding;
import com.example.assignment2.model.Movie;
import com.example.assignment2.utils.ApiClient;
import com.example.assignment2.view.adapter.MovieSearchAdapter;
import com.example.assignment2.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements MovieSearchAdapter.OnMovieClickListener {
    private FragmentSearchBinding binding;
    private MovieSearchAdapter movieAdapter;
    private MovieViewModel movieViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        // ----- adapter
        movieAdapter = new MovieSearchAdapter(new ArrayList<>(), this);
        binding.recyclerMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerMovie.setAdapter(movieAdapter);

        // ----- viewModel
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        // ------ live data observer
        movieViewModel.getMovies().observe(getViewLifecycleOwner(), movies -> {
            movieAdapter.updateMovies(movies);
        });

        // ---------- search button listener
        binding.searchBtn.setOnClickListener(v -> {
            String query = binding.searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                movieViewModel.searchMovies(query, getContext());
            } else {
                Toast.makeText(getContext(), "Please enter a movie title", Toast.LENGTH_SHORT).show();
            }
        });

        movieViewModel.getToastMessage().observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });
        return binding.getRoot();
    }


    // ------------ click movie to the movie detail screen
    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("imdbID", movie.getImdbID());
        startActivity(intent);
    }
}