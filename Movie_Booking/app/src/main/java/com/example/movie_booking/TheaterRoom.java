package com.example.movie_booking;

import java.io.Serializable;

public class TheaterRoom implements Serializable {
    private int id;
    private int cinemaId;
    private String roomName;
    private int totalSeats;

    public TheaterRoom(int id, int cinemaId, String roomName, int totalSeats) {
        this.id = id;
        this.cinemaId = cinemaId;
        this.roomName = roomName;
        this.totalSeats = totalSeats;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCinemaId() { return cinemaId; }
    public void setCinemaId(int cinemaId) { this.cinemaId = cinemaId; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
}
