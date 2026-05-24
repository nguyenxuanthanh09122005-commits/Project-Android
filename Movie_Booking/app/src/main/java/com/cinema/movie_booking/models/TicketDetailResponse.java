package com.cinema.movie_booking.models;

import com.google.gson.annotations.SerializedName;

public class TicketDetailResponse {
    private Long seatId;
    private String rowLetter;
    private Integer seatNumber;
    private String seatType;
    private double purchasePrice;

    public Long getSeatId() { return seatId; }
    public String getRowLetter() { return rowLetter; }
    public Integer getSeatNumber() { return seatNumber; }
    public String getSeatType() { return seatType; }
    public double getPurchasePrice() { return purchasePrice; }
}
