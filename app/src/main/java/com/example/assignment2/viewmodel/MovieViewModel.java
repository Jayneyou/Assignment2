package com.example.assignment2.viewmodel;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.assignment2.model.Movie;
import com.example.assignment2.utils.ApiClient;

import java.util.List;

public class MovieViewModel extends ViewModel {
    private final MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private static final MutableLiveData<Movie> movieDetail = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    // ----- movie list
    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movieList) {
        movies.setValue(movieList);
    }

    // ----- movie details
    public static LiveData<Movie> getMovieDetail() {
        return movieDetail;
    }

    // ------ make api calls
    public void fetchMovieDetail(String imdbID) {
        ApiClient.getMovieDetail(imdbID, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(List<Movie> movieList) {}

            @Override
            public void onSuccess(Movie movie) {
                movieDetail.postValue(movie);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }

    public void searchMovies(String query, Context context) {
        ApiClient.getMovieList(query, (Activity) context, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                setMovies(movies);
            }

            @Override
            public void onSuccess(Movie movieDetail) {
            }

            @Override
            public void onError(String errorMessage) {
                toastMessage.setValue("Error: " + errorMessage);
            }
        });
    }


    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }
}