package com.example.assignment2.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment2.databinding.FragmentFavBinding;
import com.example.assignment2.model.Movie;
import com.example.assignment2.view.adapter.FavAdapter;
import com.example.assignment2.viewmodel.FavViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavFragment extends Fragment implements FavAdapter.OnFavClickListener {

    private FragmentFavBinding binding;
    private FavAdapter favAdapter;
    private List<Movie> favList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavBinding.inflate(inflater, container, false);
        // ----- adapter
        favAdapter = new FavAdapter(favList, this);
        binding.recyclerFav.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerFav.setAdapter(favAdapter);

        // ----- viewModel
        FavViewModel favViewModel = new ViewModelProvider(this).get(FavViewModel.class);
        favViewModel.loadFavorites();
        // ------- observe
        favViewModel.getFavorites().observe(getViewLifecycleOwner(), movies -> {
            favList.clear();
            favList.addAll(movies);
            favAdapter.notifyDataSetChanged();
        });

        return binding.getRoot();
    }

    // -------- click fav item listener
    @Override
    public void onFavClick(Movie movie) {
        Intent intent = new Intent(getContext(), FavDetailActivity.class);
        intent.putExtra("imdbID", movie.getImdbID());
        startActivity(intent);
    }
}