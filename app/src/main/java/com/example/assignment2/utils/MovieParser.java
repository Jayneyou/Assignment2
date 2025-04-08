package com.example.assignment2.utils;

import com.example.assignment2.model.Movie;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieParser {
    // ---------- parse single movie details JSON string
    public static Movie parseMovieDetail(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String title = jsonObject.getString("Title");
        String year = jsonObject.getString("Year");
        String plot = jsonObject.getString("Plot");
        String imdbRating = jsonObject.getString("imdbRating");
        String imdbID = jsonObject.getString("imdbID");
        String production = jsonObject.optString("Production", "N/A");
        String posterUrl = jsonObject.getString("Poster");
        String genre = jsonObject.getString("Genre");
        String runtime = jsonObject.getString("Runtime");

        return new Movie(title, year, plot, imdbRating, production, imdbID, posterUrl, genre, runtime);
    }
}
