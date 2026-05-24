package com.cinema.movie_booking.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cinema.movie_booking.models.BookingResponse;
import com.cinema.movie_booking.models.Seat;
import com.cinema.movie_booking.repositories.SeatRepository;
import com.cinema.movie_booking.utils.Resource;

import java.util.List;

public class SeatViewModel extends ViewModel {
    private final SeatRepository repository = new SeatRepository();

    public LiveData<Resource<List<Seat>>> getSeatLayout(Long showtimeId) {
        return repository.getSeatLayout(showtimeId);
    }

    private final MutableLiveData<SeatRepository.BookingSummary> bookingSummary = new MutableLiveData<>();
    private final MutableLiveData<Resource<BookingResponse>> bookingResult = new MutableLiveData<>();

    public LiveData<SeatRepository.BookingSummary> getBookingSummary() {
        return bookingSummary;
    }

    public void updateSelectedSeats(List<Seat> selectedSeats, double basePrice) {
        bookingSummary.setValue(repository.calculateBookingSummary(selectedSeats, basePrice));
    }

    public void createBooking(Long showtimeId, List<Long> seatIds) {
        repository.createBooking(showtimeId, seatIds).observeForever(bookingResult::setValue);
    }

    public LiveData<Resource<BookingResponse>> getBookingResult() {
        return bookingResult;
    }
}
