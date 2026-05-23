package com.cinema.movie_booking.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private final Context context;
    private final ExecutorService executorService;

    public UserRepository(Context context) {
        this.context = context.getApplicationContext();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<String> getUserFullName() {

        MutableLiveData<String> liveData =
                new MutableLiveData<>();

        executorService.execute(() -> {

            SharedPreferences sharedPreferences =
                    context.getSharedPreferences(
                            "USER_FILE",
                            Context.MODE_PRIVATE
                    );

            String fullName =
                    sharedPreferences.getString(
                            "FULL_NAME",
                            ""
                    );

            liveData.postValue(fullName);

        });

        return liveData;
    }
}