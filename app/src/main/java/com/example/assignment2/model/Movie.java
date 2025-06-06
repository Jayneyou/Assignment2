package com.example.assignment2.model;

import android.os.Parcelable;

import java.io.Serializable;

public class Movie{
    private String title;
    private String year;
    private String plot;
    private String imdbRating;
    private String production;
    private String imdbID;
    private String posterUrl;
    private String genre;
    private String runtime;

    public Movie(String title, String year, String plot, String imdbRating, String production, String imdbID, String posterUrl, String genre, String runtime) {
        this.title = title;
        this.year = year;
        this.plot = plot;
        this.imdbRating = imdbRating;
        this.production = production;
        this.imdbID = imdbID;
        this.posterUrl = posterUrl;
        this.genre = genre;
        this.runtime = runtime;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getPlot() {
        return plot;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getProduction() {
        return production;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getGenre() {
        return genre;
    }

    public String getRuntime() {
        return runtime;
    }
}