package com.example.movie_booking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.movie_booking.dao.SuatChieuDao;
import com.example.movie_booking.object.SuatChieu;
import com.example.movie_booking.repository.SuatChieuRepository;
import java.util.List;

public class SuatChieuViewModel extends AndroidViewModel {
    private SuatChieuRepository repository;

    public SuatChieuViewModel(@NonNull Application application) {
        super(application);
        repository = new SuatChieuRepository(application);
    }

    public LiveData<List<SuatChieuDao.SuatChieuWithTheater>> getSuatChieuWithTheaterByPhim(int idPhim) {
        return repository.getSuatChieuWithTheaterByPhim(idPhim);
    }

    public LiveData<String> getTenRapBySuatChieu(int idSuatChieu) {
        return repository.getTenRapBySuatChieu(idSuatChieu);
    }

    public LiveData<List<SuatChieu>> getSuatChieuByPhim(int idPhim) {
        return repository.getSuatChieuByPhim(idPhim);
    }

    public void insert(SuatChieu suatChieu) {
        repository.insert(suatChieu);
    }
}
