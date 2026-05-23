package com.cinema.movie_booking.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cinema.movie_booking.api.RetrofitClient;
import com.cinema.movie_booking.models.CinemaMovieResponse;
import com.cinema.movie_booking.models.CinemaResponse;
import com.cinema.movie_booking.utils.Resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CinemaRepository {

    public LiveData<Resource<List<CinemaResponse>>> getCinemas(String city) {
        MutableLiveData<Resource<List<CinemaResponse>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        RetrofitClient.getApiService().getCinemas(city).enqueue(new Callback<List<CinemaResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CinemaResponse>> call, @NonNull Response<List<CinemaResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(Resource.success(response.body()));
                } else {
                    data.postValue(Resource.error("Error fetching cinemas", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CinemaResponse>> call, @NonNull Throwable t) {
                data.postValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<CinemaResponse>> getCinemaById(Long cinemaId) {
        MutableLiveData<Resource<CinemaResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        RetrofitClient.getApiService().getCinemaById(cinemaId).enqueue(new Callback<CinemaResponse>() {
            @Override
            public void onResponse(@NonNull Call<CinemaResponse> call, @NonNull Response<CinemaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(Resource.success(response.body()));
                } else {
                    data.postValue(Resource.error("Cinema not found", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CinemaResponse> call, @NonNull Throwable t) {
                data.postValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<List<CinemaMovieResponse>>> getMoviesByCinema(Long cinemaId, String date) {
        MutableLiveData<Resource<List<CinemaMovieResponse>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        RetrofitClient.getApiService().getMoviesByCinema(cinemaId, date).enqueue(new Callback<List<CinemaMovieResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CinemaMovieResponse>> call, @NonNull Response<List<CinemaMovieResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(Resource.success(response.body()));
                } else {
                    data.postValue(Resource.error("Error fetching movies for cinema", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CinemaMovieResponse>> call, @NonNull Throwable t) {
                data.postValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public List<String> processUniqueCities(List<CinemaResponse> cinemas) {
        List<String> uniqueCities = new ArrayList<>();
        uniqueCities.add("Tất cả");
        if (cinemas != null) {
            for (CinemaResponse c : cinemas) {
                if (c.getCity() != null && !uniqueCities.contains(c.getCity())) {
                    uniqueCities.add(c.getCity());
                }
            }
        }
        return uniqueCities;
    }

    public List<Calendar> generateNextDates(int days) {
        List<Calendar> dates = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, i);
            dates.add(calendar);
        }
        return dates;
    }
}
