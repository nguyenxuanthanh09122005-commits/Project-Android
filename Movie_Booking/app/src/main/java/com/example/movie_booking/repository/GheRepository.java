package com.example.movie_booking.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.movie_booking.dao.GheDao;
import com.example.movie_booking.database.AppDatabase;
import com.example.movie_booking.object.Ghe;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GheRepository {
    private GheDao gheDao;
    private ExecutorService executorService;

    public GheRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        gheDao = db.gheDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Ghe>> getGheByPhong(int idPhong) {
        MutableLiveData<List<Ghe>> data = new MutableLiveData<>();
        executorService.execute(() -> {
            List<Ghe> list = gheDao.getGheByPhong(idPhong);
            data.postValue(list);
        });
        return data;
    }

    public LiveData<List<Ghe>> getGheDaDat(int idSuatChieu) {
        MutableLiveData<List<Ghe>> data = new MutableLiveData<>();
        executorService.execute(() -> {
            List<Ghe> list = gheDao.getGheDaDat(idSuatChieu);
            data.postValue(list);
        });
        return data;
    }

    public void insert(Ghe ghe) {
        executorService.execute(() -> gheDao.insert(ghe));
    }

    public void update(Ghe ghe) {
        executorService.execute(() -> gheDao.update(ghe));
    }

    public void delete(Ghe ghe) {
        executorService.execute(() -> gheDao.delete(ghe));
    }
}
