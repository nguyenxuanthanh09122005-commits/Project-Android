package com.cinema.movie_booking.models;

public class CinemaResponse {

    private Long cinemaId;

    private String cinemaName;

    private String address;

    private String city;

    public Long getCinemaId() {
        return cinemaId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }
}