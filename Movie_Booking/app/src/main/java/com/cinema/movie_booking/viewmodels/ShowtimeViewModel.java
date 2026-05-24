// Trong ShowtimeViewModel.java
package com.cinema.movie_booking.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;
import com.cinema.movie_booking.models.Showtime;
import com.cinema.movie_booking.repositories.ShowtimeRepository;
import com.cinema.movie_booking.utils.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowtimeViewModel extends ViewModel {

    private final ShowtimeRepository repository = new ShowtimeRepository();

    // SỬA TẠI ĐÂY: Đổi kiểu dữ liệu trong Map Cache
    private final Map<String, LiveData<Resource<List<Showtime>>>> cache = new HashMap<>();

    public LiveData<Resource<List<Showtime>>> getShowtimes(Long movieId, Long cinemaId, String date) {
        // Chặn gọi API nếu thiếu thông tin rạp phim để bảo vệ không bị lỗi 500
        if (movieId == null || cinemaId == null || cinemaId <= 0 || date == null) {
            MutableLiveData<Resource<List<Showtime>>> emptyData = new MutableLiveData<>();
            emptyData.setValue(Resource.error("Vui lòng chọn rạp phim hợp lệ", null));
            return emptyData;
        }

        String key = movieId + "_" + cinemaId + "_" + date;

        if (!cache.containsKey(key)) {
            cache.put(key, repository.getShowtimes(movieId, cinemaId, date));
        }

        return cache.get(key);
    }

    // Hàm này giữ nguyên dữ liệu gốc Showtime đơn lẻ
    public LiveData<Resource<com.cinema.movie_booking.models.Showtime>> getShowtimeById(Long showtimeId) {
        return repository.getShowtimeById(showtimeId);
    }

    @Override
    protected void onCleared() {
        repository.clear();
        super.onCleared();
    }
}