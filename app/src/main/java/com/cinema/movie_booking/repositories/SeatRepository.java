package com.cinema.movie_booking.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cinema.movie_booking.api.RetrofitClient;
import com.cinema.movie_booking.models.Seat;
import com.cinema.movie_booking.models.SeatLayoutResponse;
import com.cinema.movie_booking.models.SeatLockRequest;
import com.cinema.movie_booking.models.SeatLockResponse;
import com.cinema.movie_booking.utils.Resource;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeatRepository {

    public LiveData<Resource<SeatLayoutResponse>> getSeatLayout(Long showtimeId) {
        MutableLiveData<Resource<SeatLayoutResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        RetrofitClient.getApiService().getSeatLayout(showtimeId).enqueue(new Callback<SeatLayoutResponse>() {
            @Override
            public void onResponse(@NonNull Call<SeatLayoutResponse> call, @NonNull Response<SeatLayoutResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(Resource.success(response.body()));
                } else {
                    data.postValue(Resource.error("Failed to load seat layout", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SeatLayoutResponse> call, @NonNull Throwable t) {
                data.postValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<SeatLockResponse>> lockSeats(Long showtimeId, List<Long> seatIds) {
        MutableLiveData<Resource<SeatLockResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        SeatLockRequest request = new SeatLockRequest(seatIds);
        RetrofitClient.getApiService().lockSeats(showtimeId, request).enqueue(new Callback<SeatLockResponse>() {
            @Override
            public void onResponse(@NonNull Call<SeatLockResponse> call, @NonNull Response<SeatLockResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(Resource.success(response.body()));
                } else {
                    data.postValue(Resource.error("Failed to lock seats", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SeatLockResponse> call, @NonNull Throwable t) {
                data.postValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public BookingSummary calculateBookingSummary(List<Seat> selectedSeats, double basePrice) {
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            return new BookingSummary("Chưa chọn ghế", "0đ", new ArrayList<>());
        }

        StringBuilder names = new StringBuilder();
        double total = 0;
        List<Long> ids = new ArrayList<>();

        for (Seat seat : selectedSeats) {
            names.append(seat.getSeatName()).append(", ");
            ids.add(seat.getSeatId());
            
            double seatPrice = basePrice;
            String type = seat.getSeatType();
            if (type != null) {
                switch (type.toUpperCase(Locale.ROOT)) {
                    case "VIP": seatPrice *= 1.2; break;
                    default: break;
                }
            }
            total += seatPrice;
        }

        String seatNames = names.substring(0, names.length() - 2);
        DecimalFormat formatter = new DecimalFormat("#,###đ");
        String totalText = formatter.format(total);

        return new BookingSummary(seatNames, totalText, ids);
    }

    public static class BookingSummary {
        public final String seatNames;
        public final String totalPrice;
        public final List<Long> seatIds;

        public BookingSummary(String seatNames, String totalPrice, List<Long> seatIds) {
            this.seatNames = seatNames;
            this.totalPrice = totalPrice;
            this.seatIds = seatIds;
        }
    }
}
