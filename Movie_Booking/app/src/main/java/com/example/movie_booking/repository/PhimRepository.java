package com.example.movie_booking.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.movie_booking.dao.PhimDao;
import com.example.movie_booking.dao.SuatChieuDao;
import com.example.movie_booking.database.AppDatabase;
import com.example.movie_booking.object.Phim;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PhimRepository {
    private PhimDao phimDao;
    private SuatChieuDao suatChieuDao;
    private LiveData<List<Phim>> allPhims;
    private ExecutorService executorService;

    public PhimRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        phimDao = db.phimDao();
        suatChieuDao = db.suatChieuDao();
        allPhims = phimDao.getAllPhim();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Phim>> getAllPhim() {
        return allPhims;
    }

    public void insert(Phim phim) {
        executorService.execute(() -> phimDao.insert(phim));
    }

    public void update(Phim phim) {
        executorService.execute(() -> phimDao.update(phim));
    }

    public void delete(Phim phim) {
        executorService.execute(() -> phimDao.delete(phim));
    }

    public int countFutureShowtimes(int idPhim, String currentTime) {
        Future<Integer> future = executorService.submit(() -> suatChieuDao.countFutureShowtimes(idPhim, currentTime));
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
