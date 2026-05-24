package com.cinema.movie_booking.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cinema.movie_booking.api.RetrofitClient;
import com.cinema.movie_booking.models.BookingRequest;
import com.cinema.movie_booking.models.BookingResponse;
import com.cinema.movie_booking.models.Seat;
import com.cinema.movie_booking.utils.Resource;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeatRepository {

    public LiveData<Resource<List<Seat>>> getSeatLayout(Long showtimeId) {
        MutableLiveData<Resource<List<Seat>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        RetrofitClient.getApiService().getSeatLayout(showtimeId).enqueue(new Callback<List<Seat>>() {
            @Override
            public void onResponse(@NonNull Call<List<Seat>> call, @NonNull Response<List<Seat>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(Resource.success(response.body()));
                } else {
                    String errorMsg = "Lỗi hệ thống";
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string();
                        }
                    } catch (Exception ignored) {}
                    
                    if (response.code() == 500) {
                        data.postValue(Resource.error("Lỗi Server (500): Có thể Suất chiếu này chưa được gắn Phòng hoặc chưa có Ghế trong DB.", null));
                    } else {
                        data.postValue(Resource.error("Lỗi " + response.code() + ": " + errorMsg, null));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Seat>> call, @NonNull Throwable t) {
                data.postValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<BookingResponse>> createBooking(Long showtimeId, List<Long> seatIds) {
        MutableLiveData<Resource<BookingResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        BookingRequest request = new BookingRequest(showtimeId, seatIds);
        RetrofitClient.getApiService().createBooking(request).enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(@NonNull Call<BookingResponse> call, @NonNull Response<BookingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(Resource.success(response.body()));
                } else {
                    data.postValue(Resource.error("Đặt vé thất bại: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingResponse> call, @NonNull Throwable t) {
                data.postValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
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
//                    case "COUPLE": seatPrice *= 2.0; break;
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
