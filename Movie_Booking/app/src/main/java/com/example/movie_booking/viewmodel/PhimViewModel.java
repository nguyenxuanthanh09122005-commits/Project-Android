package com.example.movie_booking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.movie_booking.object.Phim;
import com.example.movie_booking.repository.PhimRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhimViewModel extends AndroidViewModel {
    private PhimRepository repository;
    private LiveData<List<Phim>> allPhims;

    public PhimViewModel(@NonNull Application application) {
        super(application);
        repository = new PhimRepository(application);
        allPhims = repository.getAllPhim();
    }

    public LiveData<List<Phim>> getAllPhims() {
        return allPhims;
    }

    public void insert(Phim phim) {
        repository.insert(phim);
    }

    public void update(Phim phim) {
        repository.update(phim);
    }

    public void delete(Phim phim) {
        repository.delete(phim);
    }

    public int getFutureShowtimesCount(int idPhim) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        return repository.countFutureShowtimes(idPhim, currentTime);
    }
}
