package com.cinema.movie_booking.models;

import com.google.gson.annotations.SerializedName;

public class Seat {
    @SerializedName(value = "id", alternate = {"seatId", "seat_id"})
    private Long seatId;

    @SerializedName(value = "seatName", alternate = {"name", "seat_name"})
    private String seatName;

    @SerializedName(value = "rowLetter", alternate = {"row_letter", "row"})
    private String rowLetter;

    @SerializedName(value = "seatNumber", alternate = {"seat_number", "number"})
    private int seatNumber;

    @SerializedName(value = "seatType", alternate = {"seat_type", "type"})
    private String seatType;

    @SerializedName(value = "pairId", alternate = {"pair_id"})
    private Long pairId;

    @SerializedName("status")
    private String status;

    @SerializedName(value = "locked", alternate = {"isLocked", "is_locked"})
    private boolean locked;

    private boolean selected = false;

    public Long getSeatId() { return seatId; }

    public boolean isLocked() {
        // Ghế bị khóa nếu trường locked=true HOẶC status là LOCKED/BOOKED
        return locked || "LOCKED".equalsIgnoreCase(status) || "BOOKED".equalsIgnoreCase(status);
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getSeatName() {
        // Nếu seatName bị null, tự động ghép từ Row và Number (ví dụ: A + 1 = A1)
        if (seatName == null || seatName.isEmpty() || "null".equals(seatName)) {
            if (rowLetter != null && seatNumber > 0) {
                return rowLetter + seatNumber;
            }
            return "??";
        }
        return seatName;
    }

    public String getRowLetter() { return rowLetter; }
    public int getSeatNumber() { return seatNumber; }
    public String getSeatType() { return seatType; }
    public Long getPairId() { return pairId; }
    public String getStatus() { return status; }

    public boolean isCouple(){
        return "COUPLE".equalsIgnoreCase(seatType);
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public void setSeatName(String seatName) { this.seatName = seatName; }
}
