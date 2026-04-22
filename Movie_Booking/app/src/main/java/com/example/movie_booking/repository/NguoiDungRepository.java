package com.example.movie_booking.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.movie_booking.dao.NguoiDungDao;
import com.example.movie_booking.database.AppDatabase;
import com.example.movie_booking.object.NguoiDung;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NguoiDungRepository {
    private NguoiDungDao nguoiDungDao;
    private ExecutorService executorService;

    public NguoiDungRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        nguoiDungDao = db.nguoiDungDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<NguoiDung>> getAllUsers() {
        MutableLiveData<List<NguoiDung>> data = new MutableLiveData<>();
        executorService.execute(() -> data.postValue(nguoiDungDao.getAll()));
        return data;
    }

    public void login(String email, String password, OnLoginResultListener listener) {
        executorService.execute(() -> {
            NguoiDung user = nguoiDungDao.login(email, password);
            if (listener != null) listener.onResult(user);
        });
    }

    public interface OnLoginResultListener {
        void onResult(NguoiDung user);
    }
}
