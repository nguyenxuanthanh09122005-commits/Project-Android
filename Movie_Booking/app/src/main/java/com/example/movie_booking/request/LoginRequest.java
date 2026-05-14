package com.example.movie_booking.request;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("mat_khau") // Ép buộc gửi đi với tên field là 'mat_khau'
    private String mat_khau;

    public LoginRequest(String email, String mat_khau) {
        this.email = email;
        this.mat_khau = mat_khau;
    }
}