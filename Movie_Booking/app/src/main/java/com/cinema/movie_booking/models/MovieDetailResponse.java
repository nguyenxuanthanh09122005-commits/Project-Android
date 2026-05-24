package com.cinema.movie_booking.models;

import java.util.List;

public class MovieDetailResponse {

    private Long movieId;

    private String movieName;

    private String description;

    private Integer duration;

    private String releaseDate;

    private String posterImage;

    private String trailerUrl;

    private String status;

    private String ageRating;

    private List<Genre> genres;

    public Long getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public String getStatus() {
        return status;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public List<Genre> getGenres() {
        return genres;
    }
}