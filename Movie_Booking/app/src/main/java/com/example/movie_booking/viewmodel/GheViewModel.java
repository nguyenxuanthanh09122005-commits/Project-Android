package com.example.movie_booking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.movie_booking.object.Ghe;
import com.example.movie_booking.repository.GheRepository;
import java.util.List;

public class GheViewModel extends AndroidViewModel {
    private GheRepository repository;

    public GheViewModel(@NonNull Application application) {
        super(application);
        repository = new GheRepository(application);
    }

    public LiveData<List<Ghe>> getGheByPhong(int idPhong) {
        return repository.getGheByPhong(idPhong);
    }

    public LiveData<List<Ghe>> getGheDaDat(int idSuatChieu) {
        return repository.getGheDaDat(idSuatChieu);
    }

    public void insert(Ghe ghe) {
        repository.insert(ghe);
    }

    public void update(Ghe ghe) {
        repository.update(ghe);
    }

    public void delete(Ghe ghe) {
        repository.delete(ghe);
    }
}
