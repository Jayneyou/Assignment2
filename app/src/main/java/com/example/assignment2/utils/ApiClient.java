package com.example.assignment2.utils;

import android.app.Activity;

import com.example.assignment2.model.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiClient {
    private static final String BASE_URL = "https://www.omdbapi.com/";
    private static final String API_KEY = "27ebce7a";
    private static OkHttpClient client = new OkHttpClient();

    // ---------- method to get a movie list for movie search screen
    public static void getMovieList(String query, final Activity activity, final ApiCallback callback) {
        String url = BASE_URL + "?apikey=" + API_KEY + "&s=" + query;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray searchArray = jsonObject.getJSONArray("Search");

                        List<Movie> movieList = new ArrayList<>();

                        // ----- loop through list to get id
                        for (int i = 0; i < searchArray.length(); i++) {
                            JSONObject movieObject = searchArray.getJSONObject(i);
                            String imdbID = movieObject.getString("imdbID");

                            // ----- call detail method to fetch other needed data
                            getMovieDetail(imdbID, new ApiCallback() {
                                @Override
                                public void onSuccess(List<Movie> movieList) {
                                }

                                @Override
                                public void onSuccess(Movie movieDetail) {
                                    movieList.add(movieDetail);

                                    // ----- check if all movie details have been fetched
                                    if (movieList.size() == searchArray.length()) {
                                        activity.runOnUiThread(() -> callback.onSuccess(movieList));
                                    }
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    callback.onError(errorMessage);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        callback.onError("JSON parsing error: " + e.getMessage());
                    }
                } else {
                    callback.onError("Request failed with status: " + response.code());
                }
            }
        });
    }

    // ---------- method to get movie specific details
    public static void getMovieDetail(String imdbID, final ApiCallback callback) {
        String url = BASE_URL + "?apikey=" + API_KEY + "&i=" + imdbID;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    // ----- parse data
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);

                        String title = jsonObject.getString("Title");
                        String year = jsonObject.getString("Year");
                        String plot = jsonObject.getString("Plot");
                        String imdbRating = jsonObject.getString("imdbRating");
                        String imdbID = jsonObject.getString("imdbID");
                        String production = jsonObject.optString("Production", "N/A");
                        String postUrl = jsonObject.getString("Poster");

                        Movie movieDetail = new Movie(title, year, plot, imdbRating, production, imdbID, postUrl);

                        callback.onSuccess(movieDetail);
                    } catch (JSONException e) {
                        callback.onError("JSON parsing error: " + e.getMessage());
                    }
                } else {
                    callback.onError("Request failed with status: " + response.code());
                }
            }
        });
    }

    public interface ApiCallback {
        void onSuccess(List<Movie> movieList);
        void onSuccess(Movie movieDetail);
        void onError(String errorMessage);
    }
}