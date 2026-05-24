package com.cinema.movie_booking.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cinema.movie_booking.models.CinemaMovieResponse;
import com.cinema.movie_booking.models.CinemaResponse;
import com.cinema.movie_booking.repositories.CinemaRepository;
import com.cinema.movie_booking.utils.Resource;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CinemaViewModel extends ViewModel {

    private final CinemaRepository repository = new CinemaRepository();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<String> selectedCity = new MutableLiveData<>();
    private final MutableLiveData<Long> selectedCinemaId = new MutableLiveData<>();
    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();

    // LiveData cho danh sách tất cả rạp để lấy danh sách thành phố cố định
    private final LiveData<Resource<List<CinemaResponse>>> allCinemas = repository.getCinemas(null);

    // LiveData for unique cities, derived from allCinemas data (không đổi khi lọc)
    private final LiveData<List<String>> uniqueCities = Transformations.map(allCinemas, resource -> {
        if (resource != null && resource.status == Resource.Status.SUCCESS && resource.data != null) {
            return repository.processUniqueCities(resource.data);
        }
        return null;
    });

    // LiveData for filtered cinemas list based on selected city
    private final LiveData<Resource<List<CinemaResponse>>> cinemas = Transformations.switchMap(
            selectedCity,
            repository::getCinemas
    );

    public void setCity(String city) {
        // Luôn set value để kích hoạt Transformations.switchMap lần đầu
        selectedCity.setValue(city);
    }

    public LiveData<Resource<List<CinemaResponse>>> getCinemas() {
        return cinemas;
    }

    public LiveData<List<String>> getUniqueCities() {
        return uniqueCities;
    }

    public LiveData<Resource<CinemaResponse>> getCinemaById(Long cinemaId) {
        return repository.getCinemaById(cinemaId);
    }

    // Sets selected cinema and date, triggering moviesLiveData via switchMap on selectedDate
    // Public method to set selected cinema and date without returning LiveData
    public void setSelectedCinemaAndDate(Long cinemaId, String date) {
        selectedCinemaId.setValue(cinemaId);
        selectedDate.setValue(date);
    }

    // Expose moviesLiveData for observation
    public LiveData<Resource<List<CinemaMovieResponse>>> getMoviesLiveData() {
        return moviesLiveData;
    }

    // Retained for compatibility; sets values and returns LiveData (can be used if needed)
    public LiveData<Resource<List<CinemaMovieResponse>>> getMoviesByCinema(Long cinemaId, String date) {
        selectedCinemaId.setValue(cinemaId);
        selectedDate.setValue(date);
        return moviesLiveData;
    }

    private final MutableLiveData<List<Calendar>> availableDates = new MutableLiveData<>();
    // LiveData for movies based on selectedDate switchMap
    private final LiveData<Resource<List<CinemaMovieResponse>>> moviesLiveData = Transformations.switchMap(selectedDate, date -> {
        Long cinemaId = selectedCinemaId.getValue();
        if (cinemaId == null || date == null) {
            return new MutableLiveData<>();
        }
        return repository.getMoviesByCinema(cinemaId, date);
    });

    public LiveData<List<Calendar>> getAvailableDates() {
        return availableDates;
    }

    // Expose selectedDate LiveData for UI observation
    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    // Expose selectedCinemaId LiveData for UI observation if needed
    public LiveData<Long> getSelectedCinemaId() {
        return selectedCinemaId;
    }

    public void loadAvailableDates(int days) {
        executor.execute(() -> {
            List<Calendar> dates = repository.generateNextDates(days);
            availableDates.postValue(dates); // postValue cho background thread
        });
    }
    @Override
    protected void onCleared() {
        executor.shutdown();
        super.onCleared();
    }
private static class CinemaMoviesQuery {
        final Long cinemaId;
        final String date;
        CinemaMoviesQuery(Long cinemaId, String date) {
            this.cinemaId = cinemaId;
            this.date = date;
        }
    }
}

