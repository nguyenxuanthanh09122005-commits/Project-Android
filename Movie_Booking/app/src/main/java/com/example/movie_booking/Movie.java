package com.example.movie_booking;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Movie implements Serializable {
    private int id;
    private String title;
    private String description;
    private int duration;
    private String releaseDate;
    private String imageName;
    private String trailerUrl;
    private String genre;
    private String ageRating;

    public Movie() {}

    public Movie(int id, String title, String description, int duration, String releaseDate, String imageName, String trailerUrl, String genre, String ageRating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.imageName = imageName;
        this.trailerUrl = trailerUrl;
        this.genre = genre;
        this.ageRating = ageRating;
    }

    public Date getReleaseDateObject() {
        String[] formats = {"yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy"};
        for (String format : formats) {
            try {
                return new SimpleDateFormat(format, Locale.getDefault()).parse(releaseDate);
            } catch (ParseException ignored) {}
        }
        return null;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getAgeRating() { return ageRating; }
    public void setAgeRating(String ageRating) { this.ageRating = ageRating; }
}
