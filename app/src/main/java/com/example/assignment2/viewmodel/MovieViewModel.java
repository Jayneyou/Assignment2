package com.example.assignment2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.assignment2.model.Movie;
import java.util.List;

public class MovieViewModel extends ViewModel {
    private final MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private static final MutableLiveData<Movie> movieDetail = new MutableLiveData<>();

    // ----- movie list
    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movieList) {
        movies.setValue(movieList);
    }

    // ----- movie details
    public LiveData<Movie> getMovieDetail() {
        return movieDetail;
    }

    public static void setMovieDetail(Movie movie) {
        movieDetail.setValue(movie);
    }
}