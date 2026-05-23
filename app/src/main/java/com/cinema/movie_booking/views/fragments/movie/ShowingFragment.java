package com.cinema.movie_booking.views.fragments.movie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.adapters.MovieAdapter;
import com.cinema.movie_booking.viewmodels.MovieViewModel;
import com.cinema.movie_booking.utils.Resource;

public class ShowingFragment extends Fragment {

    private RecyclerView rcvMovie;
    private MovieAdapter adapter;
    private MovieViewModel movieViewModel;

    private boolean dataLoaded=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
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

        initViews(view);

        setupRecyclerView();

        setupViewModel();
    }

    private void initViews(View view) {
        rcvMovie = view.findViewById(R.id.rcvMovie);
    }

    private void setupRecyclerView() {
        adapter = new MovieAdapter();
        rcvMovie.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rcvMovie.setHasFixedSize(true);
        rcvMovie.setAdapter(adapter);
    }

    private void setupViewModel() {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
    }

    private void observeMovies() {
        movieViewModel.getMovies("SHOWING").observe(getViewLifecycleOwner(), resource -> {
            if (!isAdded() || getView() == null || resource == null) return;

            switch (resource.status) {
                case LOADING:
                    // Show progress bar
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
    @Override
    public void onResume() {

        super.onResume();

        if(!dataLoaded){

            observeMovies();

            dataLoaded=true;
        }
    }
}
