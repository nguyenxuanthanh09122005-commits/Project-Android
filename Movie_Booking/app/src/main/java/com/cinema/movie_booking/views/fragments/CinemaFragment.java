package com.cinema.movie_booking.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.adapters.CinemaAdapter;
import com.cinema.movie_booking.models.CinemaResponse;
import com.cinema.movie_booking.viewmodels.CinemaViewModel;
import com.cinema.movie_booking.views.activities.CinemaDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class CinemaFragment extends Fragment {

    private Spinner spinnerCity;
    private RecyclerView recyclerCinema;

    private CinemaAdapter adapter;
    private CinemaViewModel viewModel;
    private ArrayAdapter<String> cityAdapter;

    private final List<String> cities = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cinema, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerCity = view.findViewById(R.id.spinnerCity);
        recyclerCinema = view.findViewById(R.id.recyclerCinema);

        recyclerCinema.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new CinemaAdapter(cinema -> {
            if (!isAdded()) return;
            Intent intent = new Intent(requireContext(), CinemaDetailActivity.class);
            intent.putExtra("cinemaId", cinema.getCinemaId());
            intent.putExtra("cinemaName", cinema.getCinemaName());
            intent.putExtra("address", cinema.getAddress());
            startActivity(intent);
        });

        recyclerCinema.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(CinemaViewModel.class);

        observeCinemas();
        observeCities();

        // Initial load
        viewModel.setCity(null);
    }

    private void observeCinemas() {
        viewModel.getCinemas().observe(getViewLifecycleOwner(), resource -> {
            if (!isAdded() || getView() == null || resource == null) return;

            switch (resource.status) {
                case LOADING:
                    // Show progress bar if available
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        adapter.submitList(resource.data);
                    }
                    break;
                case ERROR:
                    // Show error message
                    break;
            }
        });
    }

    private void observeCities() {
        viewModel.getUniqueCities().observe(getViewLifecycleOwner(), uniqueCities -> {
            if (!isAdded() || getView() == null) return;
            if (uniqueCities != null) {
                cities.clear();
                cities.addAll(uniqueCities);
                if (cityAdapter == null) {
                    setupCityFilter();
                } else {
                    cityAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setupCityFilter() {
        if (!isAdded() || getView() == null) return;

        cityAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                cities
        );

        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private String lastSelected = null;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < cities.size()) {
                    String selectedCity = cities.get(position);
                    if (selectedCity.equals(lastSelected)) return;
                    
                    lastSelected = selectedCity;
                    String cityParam = selectedCity.equals("Tất cả") ? null : selectedCity;
                    viewModel.setCity(cityParam);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clear references if needed
    }
}
