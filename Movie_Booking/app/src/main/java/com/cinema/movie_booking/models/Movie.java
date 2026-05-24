package com.cinema.movie_booking.models;

import java.util.List;

public class Movie {
    private Long movieId;
    private String movieName;
    private String description;
    private int duration;
    private String releaseDate;
    private String posterImage;
    private String trailerUrl;
    private String status;
    private List<Genre> genres;
    private String ageRating;

    public Long getMovieId() { return movieId; }
    public String getMovieName() { return movieName; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public String getReleaseDate() { return releaseDate; }
    public String getPosterImage() { return posterImage; }
    public String getTrailerUrl() { return trailerUrl; }
    public String getStatus() { return status; }
    public List<Genre> getGenres() { return genres; }
    public String getAgeRating() { return ageRating; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return duration == movie.duration &&
                java.util.Objects.equals(movieId, movie.movieId) &&
                java.util.Objects.equals(movieName, movie.movieName) &&
                java.util.Objects.equals(description, movie.description) &&
                java.util.Objects.equals(releaseDate, movie.releaseDate) &&
                java.util.Objects.equals(posterImage, movie.posterImage) &&
                java.util.Objects.equals(trailerUrl, movie.trailerUrl) &&
                java.util.Objects.equals(status, movie.status) &&
                java.util.Objects.equals(genres, movie.genres) &&
                java.util.Objects.equals(ageRating, movie.ageRating);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(movieId, movieName, description, duration, releaseDate, posterImage, trailerUrl, status, genres, ageRating);
    }
}
