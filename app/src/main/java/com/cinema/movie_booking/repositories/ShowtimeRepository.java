package com.cinema.movie_booking.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cinema.movie_booking.api.RetrofitClient;
import com.cinema.movie_booking.models.Showtime;
import com.cinema.movie_booking.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowtimeRepository {
    private final List<Call<?>> runningCalls =
            new ArrayList<>();

    public LiveData<Resource<List<Showtime>>> getShowtimes(Long movieId, Long cinemaId, String date) {
        MutableLiveData<Resource<List<Showtime>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        Call<List<Showtime>> call = RetrofitClient.getApiService().getShowtimes(movieId, cinemaId, date);
        runningCalls.add(call);
        call.enqueue(new Callback<List<Showtime>>() {
            @Override
            public void onResponse(@NonNull Call<List<Showtime>> call, @NonNull Response<List<Showtime>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(Resource.success(response.body()));
                } else {
                    data.postValue(Resource.error("Failed to load showtimes", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Showtime>> call, @NonNull Throwable t) {
                data.postValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<Showtime>> getShowtimeById(Long showtimeId) {
        MutableLiveData<Resource<Showtime>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        Call<Showtime> call = RetrofitClient.getApiService().getShowtimeById(showtimeId);
        runningCalls.add(call);
        call.enqueue(new Callback<Showtime>() {
            @Override
            public void onResponse(@NonNull Call<Showtime> call, @NonNull Response<Showtime> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(Resource.success(response.body()));
                } else {
                    data.postValue(Resource.error("Showtime not found", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Showtime> call, @NonNull Throwable t) {
                data.postValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public void clear(){

        for(Call<?> call : runningCalls){

            if(!call.isCanceled()){

                call.cancel();
            }
        }

        runningCalls.clear();
    }
}
