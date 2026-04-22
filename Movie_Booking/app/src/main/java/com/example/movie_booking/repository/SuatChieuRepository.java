package com.example.movie_booking.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.movie_booking.dao.SuatChieuDao;
import com.example.movie_booking.database.AppDatabase;
import com.example.movie_booking.object.SuatChieu;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SuatChieuRepository {
    private SuatChieuDao suatChieuDao;
    private ExecutorService executorService;

    public SuatChieuRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        suatChieuDao = db.suatChieuDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(SuatChieu suatChieu) {
        executorService.execute(() -> suatChieuDao.insert(suatChieu));
    }

    public LiveData<List<SuatChieuDao.SuatChieuWithTheater>> getSuatChieuWithTheaterByPhim(int idPhim) {
        MutableLiveData<List<SuatChieuDao.SuatChieuWithTheater>> data = new MutableLiveData<>();
        executorService.execute(() -> data.postValue(suatChieuDao.getSuatChieuWithTheaterByPhim(idPhim)));
        return data;
    }

    public LiveData<String> getTenRapBySuatChieu(int idSuatChieu) {
        MutableLiveData<String> data = new MutableLiveData<>();
        executorService.execute(() -> data.postValue(suatChieuDao.getTenRapBySuatChieu(idSuatChieu)));
        return data;
    }

    public LiveData<List<SuatChieu>> getSuatChieuByPhim(int idPhim) {
        MutableLiveData<List<SuatChieu>> data = new MutableLiveData<>();
        executorService.execute(() -> data.postValue(suatChieuDao.getSuatChieuByPhim(idPhim)));
        return data;
    }
}
