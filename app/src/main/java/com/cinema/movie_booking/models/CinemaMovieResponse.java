package com.cinema.movie_booking.models;

import java.util.List;

public class CinemaMovieResponse {
    private Long movieId;
    private String movieName;
    private String posterImage;
    private int duration;
    private String ageRating;
    private List<Room> rooms;

    private transient List<ShowtimeInfo> filteredShowtimes;

    public List<ShowtimeInfo> getFilteredShowtimes(String date) {
        java.util.List<ShowtimeInfo> filtered = new java.util.ArrayList<>();
        if (rooms != null && date != null) {
            for (Room room : rooms) {
                if (room.getShowtimes() != null) {
                    for (ShowtimeInfo info : room.getShowtimes()) {
                        if (info.getStartTime() != null && info.getStartTime().startsWith(date)) {
                            filtered.add(info);
                        }
                    }
                }
            }
        }
        return filtered;
    }

    public Long getMovieId() { return movieId; }
    public String getMovieName() { return movieName; }
    public String getPosterImage() { return posterImage; }
    public int getDuration() { return duration; }
    public String getAgeRating() { return ageRating; }
    public List<Room> getRooms() { return rooms; }

    public static class Room {
        private Long roomId;
        private String roomName;
        private List<ShowtimeInfo> showtimes;

        public Long getRoomId() { return roomId; }
        public String getRoomName() { return roomName; }
        public List<ShowtimeInfo> getShowtimes() { return showtimes; }
    }

    public static class ShowtimeInfo {
        private Long showtimeId;
        private String startTime;
        private String endTime;
        private double price;

        public Long getShowtimeId() { return showtimeId; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
        public double getPrice() { return price; }
    }
}
