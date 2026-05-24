package com.cinema.movie_booking.models;

import java.util.List;

public class SeatLockResponse {
    private Long showtimeId;
    private List<Long> seatIds;
    private String expiresAt;

    public Long getShowtimeId() { return showtimeId; }
    public List<Long> getSeatIds() { return seatIds; }
    public String getExpiresAt() { return expiresAt; }
}
