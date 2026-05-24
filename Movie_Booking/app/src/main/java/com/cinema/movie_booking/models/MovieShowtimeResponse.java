package com.cinema.movie_booking.models;

import java.util.List;

public class MovieShowtimeResponse {
    private Long cinemaId;
    private String cinemaName;
    private String roomName;
    private List<ShowtimeInfo> showtimes;

    public Long getCinemaId() {
        return cinemaId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<ShowtimeInfo> getShowtimes() {
        return showtimes;
    }

    public static class ShowtimeInfo {
        private Long id;
        private String startTime;
        private String endTime;
        private Double basePrice;

        public Long getId() {
            return id;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public Double getBasePrice() {
            return basePrice;
        }
    }
}
