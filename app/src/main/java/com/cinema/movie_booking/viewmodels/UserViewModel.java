package com.cinema.movie_booking.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cinema.movie_booking.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    private final LiveData<String> userFullName;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        userFullName = userRepository.getUserFullName();
    }

    public LiveData<String> getUserFullName() {
        return userFullName;
    }

    // Optional refresh; no-op as LiveData updates automatically.
    public void refreshUser() {
        // No action needed.
    }
}