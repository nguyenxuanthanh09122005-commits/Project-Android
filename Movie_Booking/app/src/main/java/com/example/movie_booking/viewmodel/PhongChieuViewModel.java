package com.example.movie_booking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.movie_booking.object.PhongChieu;
import com.example.movie_booking.repository.PhongChieuRepository;

public class PhongChieuViewModel extends AndroidViewModel {
    private PhongChieuRepository repository;

    public PhongChieuViewModel(@NonNull Application application) {
        super(application);
        repository = new PhongChieuRepository(application);
    }

    public LiveData<PhongChieu> getPhongById(int idPhong) {
        return repository.getPhongById(idPhong);
    }
}
