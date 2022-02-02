package com.example.movierecommendation;

public class Movies {
    private String movieName;
    private String movieCategory;
    private String movieImageURL;

    public Movies(String movieName,String movieCategory, String movieImageURL){
        this.movieName = movieName;
        this.movieCategory = movieCategory;
        this.movieImageURL = movieImageURL;
    }

    public String getMovieCategory() {
        return movieCategory;
    }

    public String getMovieImageURL() {
        return movieImageURL;
    }

    public String getMovieName() {
        return movieName;
    }
}
