package com.cinema.movie_booking.models;

public class Showtime {

    private Long showtimeId;

    private Long movieId;

    private String movieName;

    private Long roomId;

    private String roomName;

    private Long cinemaId;

    private String cinemaName;

    private String startTime;

    private String endTime;

    private double baseTicketPrice;

    public Showtime() {}

    public Showtime(Long showtimeId, String startTime, String endTime, String cinemaName, String roomName, Long cinemaId) {
        this.showtimeId = showtimeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cinemaName = cinemaName;
        this.roomName = roomName;
        this.cinemaId = cinemaId;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public String getStartTime() {
        return startTime;
    }

    public double getBaseTicketPrice() {
        return baseTicketPrice;
    }

    public Long getMovieId() {
        return movieId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getCinemaId() {
        return cinemaId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setBaseTicketPrice(double baseTicketPrice) {
        this.baseTicketPrice = baseTicketPrice;
    }

    public void setCinemaId(Long cinemaId) {
        this.cinemaId = cinemaId;
    }
}