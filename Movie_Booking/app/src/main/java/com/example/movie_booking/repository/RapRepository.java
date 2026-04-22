package com.example.movie_booking.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.movie_booking.dao.RapDao;
import com.example.movie_booking.database.AppDatabase;
import com.example.movie_booking.object.Rap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RapRepository {
    private RapDao rapDao;
    private ExecutorService executorService;

    public RapRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        rapDao = db.rapDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Rap rap) {
        executorService.execute(() -> rapDao.insert(rap));
    }

    public void update(Rap rap) {
        executorService.execute(() -> rapDao.update(rap));
    }

    public void delete(Rap rap) {
        executorService.execute(() -> rapDao.delete(rap));
    }

    public LiveData<List<Rap>> getAllRap() {
        MutableLiveData<List<Rap>> data = new MutableLiveData<>();
        executorService.execute(() -> data.postValue(rapDao.getAllRap()));
        return data;
    }

    public LiveData<List<String>> getAllThanhPho() {
        MutableLiveData<List<String>> data = new MutableLiveData<>();
        executorService.execute(() -> data.postValue(rapDao.getAllThanhPho()));
        return data;
    }
}
