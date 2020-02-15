package com.nynstd.moviesadmin;

public class MovieModel {
    String movieTitle;
    String movieImageLink;
    String movieVideo;
    String movieCat;
    String MovieSeries;

    public MovieModel() {
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieImageLink() {
        return movieImageLink;
    }

    public void setMovieImageLink(String movieImageLink) {
        this.movieImageLink = movieImageLink;
    }

    public String getMovieVideo() {
        return movieVideo;
    }

    public void setMovieVideo(String movieVideo) {
        this.movieVideo = movieVideo;
    }

    public String getMovieCat() {
        return movieCat;
    }

    public void setMovieCat(String movieCat) {
        this.movieCat = movieCat;
    }

    public String getMovieSeries() {
        return MovieSeries;
    }

    public void setMovieSeries(String movieSeries) {
        MovieSeries = movieSeries;
    }

    public MovieModel(String movieTitle, String movieImageLink, String movieVideo, String movieCat, String movieSeries) {
        this.movieTitle = movieTitle;
        this.movieImageLink = movieImageLink;
        this.movieVideo = movieVideo;
        this.movieCat = movieCat;
        MovieSeries = movieSeries;
    }
}
