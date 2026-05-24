package com.cinema.movie_booking.models;

import java.util.List;

public class BookingResponse {
    private Long bookingId;
    private Long showtimeId;
    private double totalAmount;
    private String status;
    private String bookingDate;
    private List<TicketDetailResponse> tickets;

    public Long getBookingId() { return bookingId; }
    public Long getShowtimeId() { return showtimeId; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getBookingDate() { return bookingDate; }
    public List<TicketDetailResponse> getTickets() { return tickets; }
}
