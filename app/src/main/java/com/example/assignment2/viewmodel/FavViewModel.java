package com.example.assignment2.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment2.model.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FavViewModel extends ViewModel {
    private final MutableLiveData<List<Movie>> favoritesLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Movie> selectedMovie = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final String uid = user.getUid();

    public LiveData<List<Movie>> getFavorites() {
        return favoritesLiveData;
    }

    public LiveData<Movie> getSelectedMovie() {
        return selectedMovie;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public void loadFavorites() {
        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Movie> favoritesList = new ArrayList<>();
                        List<HashMap<String, Object>> favorites = (List<HashMap<String, Object>>) documentSnapshot.get("favorites");

                        if (favorites != null) {
                            // ------ only display the first 20
                            int limit = Math.min(favorites.size(), 20);
                            for (int i = 0; i < limit; i++) {
                                HashMap<String, Object> favorite = favorites.get(i);
                                String title = (String) favorite.get("title");
                                String imdbRating = (String) favorite.get("imdbRating");
                                String poster = (String) favorite.get("posterUrl");
                                String production = (String) favorite.get("production");
                                String imdbID = (String) favorite.get("imdbID");
                                favoritesList.add(new Movie(title, null, null, imdbRating, production, imdbID, poster, null, null));
                            }
                        }
                        favoritesLiveData.setValue(favoritesList);
                    }

                })
                .addOnFailureListener(Throwable::printStackTrace);
    }

    public void addToFavorites(Movie movie) {
        DocumentReference userRef = db.collection("users").document(user.getUid());
        userRef.update("favorites", FieldValue.arrayUnion(movie))
                .addOnSuccessListener(aVoid -> {
                    toastMessage.postValue("Movie added to favorites!");
                    loadFavorites();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    toastMessage.postValue("Failed to add movie to favorites.");
                });
    }

    // ---------- load fav item
    public void loadMovieByImdbID(String imdbID) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<Map<String, Object>> favorites = (List<Map<String, Object>>) documentSnapshot.get("favorites");

                    if (favorites != null) {
                        for (Map<String, Object> favorite : favorites) {
                            String movieImdbID = (String) favorite.get("imdbID");
                            if (imdbID.equals(movieImdbID)) {
                                String title = (String) favorite.get("title");
                                String year = (String) favorite.get("year");
                                String runtime = (String) favorite.get("runtime");
                                String imdbRating = (String) favorite.get("imdbRating");
                                String genre = (String) favorite.get("genre");
                                String plot = (String) favorite.get("plot");
                                String posterUrl = (String) favorite.get("posterUrl");

                                Movie movie = new Movie(title, year, plot, imdbRating, null, imdbID, posterUrl, genre, runtime);
                                selectedMovie.setValue(movie);
                                break;
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("FavoriteViewModel", "Error fetching movie", e));
    }

    // ---------- delete movie from list
    public void deleteMovie(String imdbID, Runnable onSuccess, Consumer<Exception> onFailure) {
        DocumentReference userDoc = db.collection("users").document(uid);

        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            List<Map<String, Object>> favorites = (List<Map<String, Object>>) documentSnapshot.get("favorites");

            if (favorites != null) {
                List<Map<String, Object>> updatedFavorites = new ArrayList<>();
                for (Map<String, Object> movieMap : favorites) {
                    if (!imdbID.equals(movieMap.get("imdbID"))) {
                        updatedFavorites.add(movieMap);
                    }
                }

                userDoc.update("favorites", updatedFavorites)
                        .addOnSuccessListener(aVoid -> {
                            if (onSuccess != null) onSuccess.run();
                            loadFavorites();
                        })
                        .addOnFailureListener(e -> {
                            if (onFailure != null) onFailure.accept(e);
                        });
            }
        });
    }

    // ---------- update plot method
    public void updatePlot(String imdbID, String newPlot, Runnable onSuccess, Consumer<Exception> onFailure) {
        DocumentReference userDoc = db.collection("users").document(uid);

        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            List<Map<String, Object>> favorites = (List<Map<String, Object>>) documentSnapshot.get("favorites");

            if (favorites != null) {
                List<Map<String, Object>> updatedFavorites = new ArrayList<>();
                for (Map<String, Object> movieMap : favorites) {
                    if (imdbID.equals(movieMap.get("imdbID"))) {
                        movieMap.put("plot", newPlot);
                    }
                    updatedFavorites.add(movieMap);
                }

                userDoc.update("favorites", updatedFavorites)
                        .addOnSuccessListener(aVoid -> {
                            if (onSuccess != null) onSuccess.run();
                            loadFavorites();
                        })
                        .addOnFailureListener(e -> {
                            if (onFailure != null) onFailure.accept(e);
                        });
            }
        });
    }
}
