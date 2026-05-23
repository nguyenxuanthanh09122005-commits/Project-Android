package com.cinema.movie_booking.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cinema.movie_booking.models.Showtime;
import com.cinema.movie_booking.repositories.ShowtimeRepository;
import com.cinema.movie_booking.utils.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowtimeViewModel
        extends ViewModel {

    private final ShowtimeRepository repository =
            new ShowtimeRepository();

    private final Map<String,
            LiveData<Resource<List<Showtime>>>>
            cache =
            new HashMap<>();

    public LiveData<Resource<List<Showtime>>>
    getShowtimes(
            Long movieId,
            Long cinemaId,
            String date
    ) {

        String key =
                movieId + "_"
                        + cinemaId + "_"
                        + date;

        if (!cache.containsKey(key)) {

            cache.put(
                    key,
                    repository.getShowtimes(
                            movieId,
                            cinemaId,
                            date
                    )
            );
        }

        return cache.get(key);
    }

    public LiveData<Resource<Showtime>>
    getShowtimeById(
            Long showtimeId
    ) {

        return repository.getShowtimeById(
                showtimeId
        );
    }

    @Override
    protected void onCleared() {

        repository.clear();

        super.onCleared();
    }
}