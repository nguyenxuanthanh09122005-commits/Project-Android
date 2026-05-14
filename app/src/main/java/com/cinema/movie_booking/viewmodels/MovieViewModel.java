package com.cinema.movie_booking.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cinema.movie_booking.models.Movie;
import com.cinema.movie_booking.repositories.MovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private final MovieRepository repository;

    public MovieViewModel(){

        repository = new MovieRepository();
    }

    public LiveData<List<Movie>> getMovies(){

        return repository.getMovies();
    }
}
