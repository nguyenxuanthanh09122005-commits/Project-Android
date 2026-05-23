package com.cinema.movie_booking.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.adapters.CinemaMovieAdapter;
import com.cinema.movie_booking.adapters.DateAdapter;
import com.cinema.movie_booking.viewmodels.CinemaViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CinemaDetailActivity
        extends AppCompatActivity {

    private TextView btnBack;
    private TextView txtCinemaName;
    private TextView txtAddress;

    private RecyclerView recyclerMovies;
    private RecyclerView recyclerDate;

    private CinemaMovieAdapter movieAdapter;
    private DateAdapter dateAdapter;

    private CinemaViewModel cinemaViewModel;

    private Long cinemaId;
    private String selectedDate;

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
            );

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_cinema_detail
        );

        cinemaId =
                getIntent()
                        .getLongExtra(
                                "cinemaId",
                                -1
                        );

        setupViewModel();

        initViews();
        // Set up observation for movies; will react to ViewModel changes
        observeMovies();
        observeDates();
    }

    private void initViews() {

        btnBack =
                findViewById(
                        R.id.btnBack
                );

        txtCinemaName =
                findViewById(
                        R.id.txtCinemaName
                );

        txtAddress =
                findViewById(
                        R.id.txtAddress
                );

        recyclerMovies =
                findViewById(
                        R.id.recyclerShowtime
                );

        recyclerDate =
                findViewById(
                        R.id.recyclerDate
                );

        btnBack.setOnClickListener(
                v -> finish()
        );

        txtCinemaName.setText(
                getIntent()
                        .getStringExtra(
                                "cinemaName"
                        )
        );

        txtAddress.setText(
                getIntent()
                        .getStringExtra(
                                "address"
                        )
        );

        movieAdapter =
                new CinemaMovieAdapter();

        movieAdapter
                .setOnShowtimeClickListener(
                        (movie, showtime) -> {

                            Intent intent =
                                    new Intent(
                                            this,
                                            SeatSelectionActivity.class
                                    );

                            intent.putExtra(
                                    "showtimeId",
                                    showtime.getShowtimeId()
                            );

                            intent.putExtra(
                                    "movieName",
                                    movie.getMovieName()
                            );

                            intent.putExtra(
                                    "cinemaName",
                                    txtCinemaName.getText()
                                            .toString()
                            );

                            intent.putExtra(
                                    "basePrice",
                                    showtime.getPrice()
                            );

                            startActivity(
                                    intent
                            );
                        }
                );

        recyclerMovies.setLayoutManager(
                new LinearLayoutManager(
                        this
                )
        );

        recyclerMovies.setAdapter(
                movieAdapter
        );

        setupDateSelection();
    }

    private void setupViewModel() {

        cinemaViewModel =
                new ViewModelProvider(this)
                        .get(
                                CinemaViewModel.class
                        );
    }

    private void setupDateSelection() {

        dateAdapter =
                new DateAdapter(date -> {
                    selectedDate = date;
                    cinemaViewModel.setSelectedCinemaAndDate(cinemaId, selectedDate);
                });

        recyclerDate.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        recyclerDate.setAdapter(
                dateAdapter
        );

        cinemaViewModel.loadAvailableDates(
                14
        );
    }

    private void observeDates() {

        cinemaViewModel
                .getAvailableDates()
                .observe(this, dates -> {

                    if (dates == null ||
                            dates.isEmpty())
                        return;

                    dateAdapter.submitList(
                            dates
                    );

                    if (selectedDate == null) {
                        selectedDate = dateFormat.format(dates.get(0).getTime());
                        cinemaViewModel.setSelectedCinemaAndDate(cinemaId, selectedDate);
                    }
                });
    }

    private void observeMovies() {
        // Observe movies LiveData from ViewModel which updates based on selected cinema and date
        cinemaViewModel.getMoviesLiveData().observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    // TODO: show loading indicator
                    break;
                case SUCCESS:
                    movieAdapter.setSelectedDate(selectedDate);
                    movieAdapter.submitList(resource.data != null ? resource.data : new ArrayList<>());
                    break;
                case ERROR:
                    // TODO: handle error UI
                    break;
            }
        });
    }
}