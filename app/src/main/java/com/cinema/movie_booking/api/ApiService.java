package com.cinema.movie_booking.api;

import com.cinema.movie_booking.models.BookingRequest;
import com.cinema.movie_booking.models.BookingResponse;
import com.cinema.movie_booking.models.CinemaMovieResponse;
import com.cinema.movie_booking.models.CinemaResponse;
import com.cinema.movie_booking.models.Genre;
import com.cinema.movie_booking.models.LoginRequest;
import com.cinema.movie_booking.models.LoginResponse;
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

    // ================= AUTH =================

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);


    @POST("api/auth/register")
    Call<Void> register(@Body com.cinema.movie_booking.models.RegisterRequest request);



    // ================= MOVIE =================

    @GET("api/movies")
    Call<List<Movie>> getMovies(
            @Query("status") String status
    );

    @GET("api/movies/{movieId}")
    Call<Movie> getMovieById(
            @Path("movieId") Long movieId
    );

    @GET("api/showtimes")
    Call<List<Showtime>> getMovieShowtimes(
            @Query("movieId") Long movieId,
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

    @GET("api/showtimes/{showtimeId}/seats")
    Call<List<com.cinema.movie_booking.models.Seat>> getSeatLayout(
            @Path("showtimeId") Long showtimeId
    );

    @POST("api/showtimes/{showtimeId}/seats-lock")
    Call<SeatLockResponse> lockSeats(
            @Path("showtimeId") Long showtimeId,
            @Body SeatLockRequest request
    );

    @POST("api/bookings")
    Call<BookingResponse> createBooking(
            @Body BookingRequest request
    );

    @GET("api/bookings")
    Call<List<BookingResponse>> getMyBookings();

}
