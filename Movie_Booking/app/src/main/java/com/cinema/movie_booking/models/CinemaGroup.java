package com.cinema.movie_booking.models;

import java.util.List;

public class CinemaGroup {
    private String cinemaName;
    private List<RoomGroup> rooms;

    public CinemaGroup(String cinemaName, List<RoomGroup> rooms) {
        this.cinemaName = cinemaName;
        this.rooms = rooms;
    }

    public String getCinemaName() { return cinemaName; }
    public List<RoomGroup> getRooms() { return rooms; }

    public static class RoomGroup {
        private String roomName;
        private List<Showtime> showtimes;

        public RoomGroup(String roomName, List<Showtime> showtimes) {
            this.roomName = roomName;
            this.showtimes = showtimes;
        }

        public String getRoomName() { return roomName; }
        public List<Showtime> getShowtimes() { return showtimes; }
    }
}
