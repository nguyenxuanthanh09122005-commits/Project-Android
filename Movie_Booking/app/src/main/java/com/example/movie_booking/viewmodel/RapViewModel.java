package com.example.movie_booking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.movie_booking.object.Rap;
import com.example.movie_booking.repository.RapRepository;
import java.util.List;

public class RapViewModel extends AndroidViewModel {
    private RapRepository repository;

    public RapViewModel(@NonNull Application application) {
        super(application);
        repository = new RapRepository(application);
    }

    public void insert(Rap rap) {
        repository.insert(rap);
    }

    public void update(Rap rap) {
        repository.update(rap);
    }

    public void delete(Rap rap) {
        repository.delete(rap);
    }

    public LiveData<List<Rap>> getAllRap() {
        return repository.getAllRap();
    }

    public LiveData<List<String>> getAllThanhPho() {
        return repository.getAllThanhPho();
    }
}
