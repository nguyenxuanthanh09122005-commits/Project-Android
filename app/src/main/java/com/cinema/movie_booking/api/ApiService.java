package com.cinema.movie_booking.api;

import com.cinema.movie_booking.models.CinemaMovieResponse;
import com.cinema.movie_booking.models.CinemaResponse;
import com.cinema.movie_booking.models.Genre;
import com.cinema.movie_booking.models.Movie;
import com.cinema.movie_booking.models.MovieShowtimeResponse;
import com.cinema.movie_booking.models.SeatLayoutResponse;
import com.cinema.movie_booking.models.Showtime;
import com.cinema.movie_booking.models.SeatLockRequest;
import com.cinema.movie_booking.models.SeatLockResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ================= MOVIE =================

    @GET("api/movies")
    Call<List<Movie>> getMovies(
            @Query("status") String status
    );

    @GET("api/movies/{movieId}")
    Call<Movie> getMovieById(
            @Path("movieId") Long movieId
    );

    @GET("api/movies/{movieId}/showtimes")
    Call<List<MovieShowtimeResponse>> getMovieShowtimes(
            @Path("movieId") Long movieId,
            @Query("city") String city,
            @Query("date") String date
    );

    // ================= GENRE =================

    @GET("api/genres")
    Call<List<Genre>> getAllGenres();

    // ================= CINEMA =================

    @GET("api/cinemas")
    Call<List<CinemaResponse>> getCinemas(
            @Query("city") String city
    );

    @GET("api/cinemas/{cinemaId}")
    Call<CinemaResponse> getCinemaById(
            @Path("cinemaId") Long cinemaId
    );

    @GET("api/cinemas/{cinemaId}/movies")
    Call<List<CinemaMovieResponse>> getMoviesByCinema(
            @Path("cinemaId") Long cinemaId,
            @Query("date") String date
    );

    // ================= SHOWTIME =================

    @GET("api/showtimes")
    Call<List<Showtime>> getShowtimes(
            @Query("movieId") Long movieId,
            @Query("cinemaId") Long cinemaId,
            @Query("date") String date
    );

    @GET("api/showtimes/{showtimeId}")
    Call<Showtime> getShowtimeById(
            @Path("showtimeId") Long showtimeId
    );

    @GET("api/showtimes/{showtimeId}/seat-layout")
    Call<SeatLayoutResponse> getSeatLayout(
            @Path("showtimeId") Long showtimeId
    );

    @POST("api/showtimes/{showtimeId}/seats-lock")
    Call<SeatLockResponse> lockSeats(
            @Path("showtimeId") Long showtimeId,
            @Body SeatLockRequest request
    );

}
