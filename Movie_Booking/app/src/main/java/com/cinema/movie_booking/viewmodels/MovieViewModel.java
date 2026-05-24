package com.cinema.movie_booking.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cinema.movie_booking.models.CinemaGroup;
import com.cinema.movie_booking.models.Movie;
import com.cinema.movie_booking.repositories.MovieRepository;
import com.cinema.movie_booking.utils.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieViewModel extends ViewModel {

    private final MovieRepository repository =
            new MovieRepository();

    private final Map<String,
            LiveData<Resource<List<Movie>>>> movieCache =
            new HashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LiveData<Resource<List<Movie>>> getMovies(
            String status
    ) {

        if (!movieCache.containsKey(status)) {

            movieCache.put(
                    status,
                    repository.getMovies(status)
            );
        }

        return movieCache.get(status);
    }

    public LiveData<Resource<Movie>>
    getMovieById(Long movieId) {

        return repository.getMovieById(movieId);
    }

    private final MutableLiveData<MovieRepository.MovieMetadata>
            movieMetadata =
            new MutableLiveData<>();

    public LiveData<MovieRepository.MovieMetadata>
    getMovieMetadata() {

        return movieMetadata;
    }

    public void processMovieMetadata(
            Movie movie
    ) {

        movieMetadata.setValue(
                repository.formatMovieMetadata(movie)
        );
    }

    private final MutableLiveData<ShowtimeQuery>
        showtimeQuery = new MutableLiveData<>();

    private LiveData<Resource<List<CinemaGroup>>> groupedShowtimes;

    private static class ShowtimeQuery {
        final Long movieId;
        final String city;
        final String date;
        final String selectedCinema;
        final String movieName;
        ShowtimeQuery(Long movieId, String city, String date, String selectedCinema, String movieName) {
            this.movieId = movieId;
            this.city = city;
            this.date = date;
            this.selectedCinema = selectedCinema;
            this.movieName = movieName;
        }
    }

    public void loadGroupedShowtimes(Long movieId, String city, String date, String selectedCinema, String movieName) {
        showtimeQuery.setValue(new ShowtimeQuery(movieId, city, date, selectedCinema, movieName));
    }

    public LiveData<Resource<List<CinemaGroup>>> getGroupedShowtimes() {
        if (groupedShowtimes == null) {
            MediatorLiveData<Resource<List<CinemaGroup>>> result = new MediatorLiveData<>();
            result.addSource(
                Transformations.switchMap(showtimeQuery,
                    query -> repository.getMovieShowtimes(query.movieId, query.city, query.date)),
                resource -> {
                    if (resource == null) return;
                    if (resource.status == Resource.Status.LOADING) {
                        result.setValue(Resource.loading(null));
                        return;
                    }
                    if (resource.status == Resource.Status.ERROR) {
                        result.setValue(Resource.error(resource.message, null));
                        return;
                    }
                    ShowtimeQuery query = showtimeQuery.getValue();
                    if (query == null) return;
                    result.setValue(Resource.loading(null));
                    executor.execute(() -> {
                        List<CinemaGroup> groups = repository.processAndGroupShowtimes(
                            resource.data,
                            query.date,
                            query.selectedCinema,
                            query.movieName
                        );
                        result.postValue(Resource.success(groups));
                    });
                }
            );
            groupedShowtimes = result;
        }
        return groupedShowtimes;
    }

    public void loadAndGroupShowtimes(
            Long movieId,
            String city,
            String date,
            String selectedCinema,
            String movieName
    ) {

        loadGroupedShowtimes(
                movieId,
                city,
                date,
                selectedCinema,
                movieName
        );
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
        repository.clear();
        super.onCleared();
    }
}