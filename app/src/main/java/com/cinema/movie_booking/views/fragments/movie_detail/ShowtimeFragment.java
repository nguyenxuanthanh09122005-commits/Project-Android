package com.cinema.movie_booking.views.fragments.movie_detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.adapters.DateAdapter;
import com.cinema.movie_booking.adapters.MovieShowtimeCinemaAdapter;
import com.cinema.movie_booking.models.CinemaGroup;
import com.cinema.movie_booking.models.CinemaResponse;
import com.cinema.movie_booking.utils.Resource;
import com.cinema.movie_booking.viewmodels.CinemaViewModel;
import com.cinema.movie_booking.viewmodels.MovieViewModel;
import com.cinema.movie_booking.views.activities.SeatSelectionActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShowtimeFragment extends Fragment {

    private static final String KEY_ID = "movieId";

    private Long movieId;
    private String movieName;

    private RecyclerView cinemaList;
    private RecyclerView dateSelector;
    private Spinner cityFilter;
    private Spinner cinemaFilter;
    private ProgressBar progressBar;
    private TextView emptyState;

    private MovieShowtimeCinemaAdapter cinemaAdapter;
    private DateAdapter dateAdapter;

    private MovieViewModel movieViewModel;
    private CinemaViewModel cinemaViewModel;

    private String selectedDate;
    private String selectedCity = "Tất cả";
    private String selectedCinema = "Tất cả rạp";

    private List<CinemaResponse> allCinemas = new ArrayList<>();

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
            );

    public static ShowtimeFragment newInstance(
            Long movieId,
            String movieName
    ) {
        ShowtimeFragment fragment =
                new ShowtimeFragment();

        Bundle bundle = new Bundle();

        bundle.putLong(KEY_ID, movieId);
        bundle.putString(
                "movieName",
                movieName
        );

        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(
                R.layout.fragment_showtime,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(
                view,
                savedInstanceState
        );

        if (getArguments() != null) {
            movieId =
                    getArguments().getLong(KEY_ID);

            movieName =
                    getArguments().getString(
                            "movieName"
                    );
        }

        initViews(view);
        initRecyclerViews();
        initViewModels();

        setupDateSelection();
        setupFilters();

        observeCinemas();
        observeGroupedShowtimes();

        cinemaViewModel.setCity(null);
    }

    private void initViews(View view) {

        cinemaList =
                view.findViewById(
                        R.id.cinemaList
                );

        dateSelector =
                view.findViewById(
                        R.id.dateSelector
                );

        cityFilter =
                view.findViewById(
                        R.id.cityFilter
                );

        cinemaFilter =
                view.findViewById(
                        R.id.cinemaFilter
                );

        progressBar =
                view.findViewById(
                        R.id.progressBar
                );

        emptyState =
                view.findViewById(
                        R.id.emptyState
                );
    }

    private void initRecyclerViews() {

        cinemaAdapter =
                new MovieShowtimeCinemaAdapter(
                        showtime -> {

                            if (!isAdded())
                                return;

                            Intent intent =
                                    new Intent(
                                            requireContext(),
                                            SeatSelectionActivity.class
                                    );

                            intent.putExtra(
                                    "showtimeId",
                                    showtime.getShowtimeId()
                            );

                            intent.putExtra(
                                    "movieName",
                                    showtime.getMovieName()
                            );

                            intent.putExtra(
                                    "cinemaName",
                                    showtime.getCinemaName()
                            );

                            intent.putExtra(
                                    "basePrice",
                                    showtime.getBaseTicketPrice()
                            );

                            startActivity(intent);
                        }
                );

        cinemaList.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        cinemaList.setAdapter(
                cinemaAdapter
        );

        dateSelector.setLayoutManager(
                new LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );
    }

    private void initViewModels() {

        movieViewModel =
                new ViewModelProvider(this)
                        .get(MovieViewModel.class);

        cinemaViewModel =
                new ViewModelProvider(this)
                        .get(CinemaViewModel.class);
    }

    private void setupDateSelection() {

        dateAdapter =
                new DateAdapter(
                        dateQuery -> {

                            selectedDate =
                                    dateQuery;

                            loadShowtimes();
                        }
                );

        dateSelector.setAdapter(
                dateAdapter
        );

        cinemaViewModel
                .getAvailableDates()
                .observe(
                        getViewLifecycleOwner(),
                        dates -> {

                            if (!isAdded()
                                    || dates == null
                                    || dates.isEmpty())
                                return;

                            dateAdapter.submitList(
                                    dates
                            );

                            if (selectedDate == null) {

                                selectedDate =
                                        dateFormat.format(
                                                dates.get(0).getTime()
                                        );

                                loadShowtimes();
                            }
                        }
                );

        cinemaViewModel.loadAvailableDates(
                14
        );
    }

    private void setupFilters() {

        cityFilter.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        String city =
                                (String)
                                        parent.getItemAtPosition(
                                                position
                                        );

                        if (city == null)
                            return;

                        boolean changed =
                                !city.equals(
                                        selectedCity
                                );

                        selectedCity = city;

                        if (changed) {
                            updateCinemaFilterOptions();
                        }
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {
                    }
                }
        );

        cinemaFilter.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        selectedCinema =
                                (String)
                                        parent.getItemAtPosition(
                                                position
                                        );

                        loadShowtimes();
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {
                    }
                }
        );
    }

    private void observeCinemas() {

        cinemaViewModel
                .getCinemas()
                .observe(
                        getViewLifecycleOwner(),
                        resource -> {

                            if (resource == null)
                                return;

                            if (resource.status ==
                                    Resource.Status.SUCCESS
                                    && resource.data != null) {

                                allCinemas =
                                        resource.data;

                                updateCityFilterOptions();
                                updateCinemaFilterOptions();
                            }
                        }
                );
    }

    private void observeGroupedShowtimes() {

        movieViewModel
                .getGroupedShowtimes()
                .observe(
                        getViewLifecycleOwner(),
                        resource -> {

                            if (resource == null)
                                return;

                            switch (resource.status) {

                                case LOADING:

                                    progressBar.setVisibility(
                                            View.VISIBLE
                                    );

                                    emptyState.setVisibility(
                                            View.GONE
                                    );

                                    break;

                                case SUCCESS:

                                    progressBar.setVisibility(
                                            View.GONE
                                    );

                                    List<CinemaGroup> groups =
                                            resource.data;

                                    if (groups == null
                                            || groups.isEmpty()) {

                                        emptyState.setText(
                                                "Không có suất chiếu"
                                        );

                                        emptyState.setVisibility(
                                                View.VISIBLE
                                        );

                                        cinemaList.setVisibility(
                                                View.GONE
                                        );

                                    } else {

                                        emptyState.setVisibility(
                                                View.GONE
                                        );

                                        cinemaList.setVisibility(
                                                View.VISIBLE
                                        );

                                        cinemaAdapter.submitList(
                                                groups
                                        );
                                    }

                                    break;

                                case ERROR:

                                    progressBar.setVisibility(
                                            View.GONE
                                    );

                                    emptyState.setText(
                                            resource.message
                                    );

                                    emptyState.setVisibility(
                                            View.VISIBLE
                                    );

                                    break;
                            }
                        }
                );
    }

    private void updateCityFilterOptions() {

        List<String> cities =
                new ArrayList<>();

        cities.add("Tất cả");

        for (CinemaResponse cinema : allCinemas) {

            if (cinema.getCity() != null
                    && !cities.contains(
                    cinema.getCity()
            )) {

                cities.add(
                        cinema.getCity()
                );
            }
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        cities
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        cityFilter.setAdapter(adapter);

        int pos =
                cities.indexOf(selectedCity);

        cityFilter.setSelection(
                pos >= 0 ? pos : 0,
                false
        );
    }

    private void updateCinemaFilterOptions() {

        List<String> names =
                new ArrayList<>();

        names.add("Tất cả rạp");

        for (CinemaResponse cinema : allCinemas) {

            if ("Tất cả".equals(selectedCity)
                    || selectedCity.equals(
                    cinema.getCity()
            )) {

                names.add(
                        cinema.getCinemaName()
                );
            }
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        names
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        cinemaFilter.setAdapter(
                adapter
        );

        selectedCinema =
                "Tất cả rạp";

        cinemaFilter.setSelection(
                0,
                false
        );
    }

    private void loadShowtimes() {

        if (movieId == null
                || selectedDate == null)
            return;

        String cityParam =
                "Tất cả".equals(selectedCity)
                        ? null
                        : selectedCity;

        String cinemaParam =
                "Tất cả rạp".equals(selectedCinema)
                        ? null
                        : selectedCinema;

        movieViewModel.loadAndGroupShowtimes(
                movieId,
                cityParam,
                selectedDate,
                cinemaParam,
                movieName
        );
    }
}