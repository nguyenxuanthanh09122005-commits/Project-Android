package com.example.movie_booking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.movie_booking.object.NguoiDung;
import com.example.movie_booking.repository.NguoiDungRepository;
import java.util.List;

public class NguoiDungViewModel extends AndroidViewModel {
    private NguoiDungRepository repository;

    public NguoiDungViewModel(@NonNull Application application) {
        super(application);
        repository = new NguoiDungRepository(application);
    }

    public LiveData<List<NguoiDung>> getAllUsers() {
        return repository.getAllUsers();
    }

    public void login(String email, String password, NguoiDungRepository.OnLoginResultListener listener) {
        repository.login(email, password, listener);
    }
}
