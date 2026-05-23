package com.cinema.movie_booking.models;

import java.util.ArrayList;
import java.util.List;

public class SeatLayoutResponse {
    private Long showtimeId;
    private String roomName;
    private List<Seat> seats;

    public Long getShowtimeId() { return showtimeId; }
    public String getRoomName() { return roomName; }
    public List<Seat> getSeats() {
        if (seats == null) return null;
        List<Seat> processedSeats = new ArrayList<>();
        List<Long> addedPairIds = new ArrayList<>();

        for (Seat seat : seats) {
            if (seat.isCouple() && seat.getPairId() != null) {
                if (!addedPairIds.contains(seat.getPairId())) {
                    processedSeats.add(seat);
                    addedPairIds.add(seat.getPairId());
                }
            } else {
                processedSeats.add(seat);
            }
        }
        return processedSeats;
    }

    public List<Seat> getRawSeats() {
        return seats;
    }

    public int getTotalColumns() {
        if (seats == null || seats.isEmpty()) return 0;
        int maxColumn = 0;
        for (Seat seat : seats) {
            if (seat.getSeatNumber() > maxColumn) {
                maxColumn = seat.getSeatNumber();
            }
        }
        return maxColumn;
    }
}
