package com.example.movie_booking.api;

import com.example.movie_booking.request.LoginRequest;
import com.example.movie_booking.request.RegisterRequest;
import com.example.movie_booking.response.LoginResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<Void> register(@Body RegisterRequest request);

    @POST("auth/forgot-password")
    Call<Map<String, String>> forgotPassword(@Body Map<String, String> email);

    @POST("auth/reset-password")
    Call<Map<String, String>> resetPassword(@Body Map<String, String> data);
}
