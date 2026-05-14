package com.cinema.movie_booking.models;

public class Movie{
    private Long movieId;
    private String movieName;
    private String ageRating;
    private String description;
    private int duration;
    private String posterImage;
    private String releaseDate;
    private String status;
    private String trailerUrl;
    public Long getMovieId() { return movieId; }
    public String getMovieName() { return movieName; }
    public String getPosterImage() { return posterImage; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }

    public String getAgeRating() { return ageRating; }
}
