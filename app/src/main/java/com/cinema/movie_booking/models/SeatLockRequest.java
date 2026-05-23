package com.cinema.movie_booking.models;

import java.util.List;

public class SeatLockRequest {
    private List<Long> seatIds;
    private String userId;

    public SeatLockRequest(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public List<Long> getSeatIds() { return seatIds; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
