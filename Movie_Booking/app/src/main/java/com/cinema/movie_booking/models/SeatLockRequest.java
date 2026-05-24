package com.cinema.movie_booking.models;

import java.util.List;

public class SeatLockRequest {
    private Long showtimeId;
    private List<Long> seatIds;

    public SeatLockRequest(Long showtimeId, List<Long> seatIds) {
        this.showtimeId = showtimeId;
        this.seatIds = seatIds;
    }

    public Long getShowtimeId() { return showtimeId; }
    public List<Long> getSeatIds() { return seatIds; }
}
