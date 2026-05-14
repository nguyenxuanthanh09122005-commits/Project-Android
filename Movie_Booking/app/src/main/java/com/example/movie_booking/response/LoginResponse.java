package com.example.movie_booking.response;

public class LoginResponse {
    private String token;
    private String role; // "Admin" hoặc "KhachHang"
    private String message;

    // Getter
    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getMessage() { return message; }
}