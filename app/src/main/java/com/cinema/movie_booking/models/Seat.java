package com.cinema.movie_booking.models;

public class Seat {
    private Long seatId;
    private String seatName;
    private String rowLetter;
    private int seatNumber;
    private String seatType;
    private Long pairId;
    private String status;
    private boolean selected=false;

    public Long getSeatId() { return seatId; }
    public String getSeatName() { return seatName; }
    public String getRowLetter() { return rowLetter; }
    public int getSeatNumber() { return seatNumber; }
    public String getSeatType() { return seatType; }
    public Long getPairId() { return pairId; }

    public String getStatus() { return status; }

    public boolean isCouple(){

        return "COUPLE"
                .equalsIgnoreCase(seatType);
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected=selected;
    }

    public void setSeatName(String seatName) { this.seatName = seatName; }
}
