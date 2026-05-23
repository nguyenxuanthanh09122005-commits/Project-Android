package com.cinema.movie_booking;

import android.app.Application;
import android.os.StrictMode;

import java.util.concurrent.Executors;

public class MovieApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Retrofit API client asynchronously on startup to avoid blocking the main thread
        Executors.newSingleThreadExecutor().execute(
                com.cinema.movie_booking.api.RetrofitClient::getApiService
        );

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
    }
}
