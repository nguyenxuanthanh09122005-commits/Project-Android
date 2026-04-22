package com.example.movie_booking.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.movie_booking.dao.PhongChieuDao;
import com.example.movie_booking.database.AppDatabase;
import com.example.movie_booking.object.PhongChieu;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhongChieuRepository {
    private PhongChieuDao phongChieuDao;
    private ExecutorService executorService;

    public PhongChieuRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        phongChieuDao = db.phongChieuDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<PhongChieu> getPhongById(int idPhong) {
        MutableLiveData<PhongChieu> data = new MutableLiveData<>();
        executorService.execute(() -> data.postValue(phongChieuDao.getPhongById(idPhong)));
        return data;
    }
}
