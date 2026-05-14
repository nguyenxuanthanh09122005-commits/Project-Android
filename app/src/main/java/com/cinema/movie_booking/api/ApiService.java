package com.cinema.movie_booking.api;

import com.cinema.movie_booking.models.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("api/movies")
    Call<List<Movie>> getMovies();

}
