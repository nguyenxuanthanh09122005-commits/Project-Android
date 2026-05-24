package com.cinema.movie_booking.models;

import java.util.List;

public class GroupedShowtime {
    private String cinemaName;
    private String roomName;
    private List<Showtime> showtimes;

    public GroupedShowtime(String cinemaName, String roomName, List<Showtime> showtimes) {
        this.cinemaName = cinemaName;
        this.roomName = roomName;
        this.showtimes = showtimes;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }
}
