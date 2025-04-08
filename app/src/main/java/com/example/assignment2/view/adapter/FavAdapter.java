package com.example.assignment2.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment2.R;
import com.example.assignment2.databinding.FavItemBinding;
import com.example.assignment2.databinding.MovieItemBinding;
import com.example.assignment2.model.Movie;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavViewHolder> {

    private List<Movie> favorites;
    private static OnFavClickListener onFavClickListener;

    public interface OnFavClickListener {
        void onFavClick(Movie movie);
    }

    public FavAdapter(List<Movie> favorites, OnFavClickListener onFavClickListener) {
        this.favorites = favorites;
        FavAdapter.onFavClickListener = onFavClickListener;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FavItemBinding binding = FavItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FavViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        Movie movie = favorites.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    static class FavViewHolder extends RecyclerView.ViewHolder {
        private final FavItemBinding binding;
        public FavViewHolder(FavItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Movie movie){
            binding.title.setText(movie.getTitle());
            binding.studio.setText(movie.getProduction());
            binding.rating.setText(movie.getImdbRating());

            Glide.with(binding.poster.getContext())
                    .load(movie.getPosterUrl())
                    .into(binding.poster);

            binding.getRoot().setOnClickListener(v -> {
                if (onFavClickListener != null){
                    onFavClickListener.onFavClick(movie);
                }
            });
        }
    }
}