package com.cinema.movie_booking.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cinema.movie_booking.api.RetrofitClient;
import com.cinema.movie_booking.models.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    public LiveData<List<Movie>> getMovies(){

        MutableLiveData<List<Movie>> data =
                new MutableLiveData<>();

        RetrofitClient
                .getApiService()
                .getMovies()
                .enqueue(new Callback<List<Movie>>() {

                    @Override
                    public void onResponse(
                            Call<List<Movie>> call,
                            Response<List<Movie>> response) {

                        if(response.isSuccessful()
                                && response.body() != null){

                            data.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Movie>> call,
                            Throwable t) {

                        data.setValue(null);
                    }
                });

        return data;
    }
}
