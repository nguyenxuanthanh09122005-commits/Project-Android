package com.example.movie_booking;

import java.io.Serializable;

public class Seat implements Serializable {
    private int id;
    private int roomId;
    private String row;
    private int number;
    private String type;

    public Seat(int id, int roomId, String row, int number, String type) {
        this.id = id;
        this.roomId = roomId;
        this.row = row;
        this.number = number;
        this.type = type;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getRow() { return row; }
    public void setRow(String row) { this.row = row; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
