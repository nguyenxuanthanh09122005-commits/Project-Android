package com.cinema.movie_booking.views.fragments.movie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.adapters.MovieAdapter;
import com.cinema.movie_booking.models.Movie;
import com.cinema.movie_booking.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class EarlyAccessFragment extends Fragment {

    private RecyclerView rcvMovie;

    private MovieAdapter adapter;

    private List<Movie> movieList;

    private MovieViewModel movieViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_movie_list,
                container,
                false);

        initViews(view);

        setupRecyclerView();

        setupViewModel();

        observeMovies();

        return view;
    }

    private void initViews(View view){

        rcvMovie = view.findViewById(R.id.rcvMovie);
    }

    private void setupRecyclerView(){

        movieList = new ArrayList<>();

        adapter = new MovieAdapter(
                requireContext(),
                movieList
        );

        rcvMovie.setLayoutManager(
                new GridLayoutManager(
                        requireContext(),
                        2
                ));

        rcvMovie.setAdapter(adapter);
    }

    private void setupViewModel(){

        movieViewModel =
                new ViewModelProvider(this)
                        .get(MovieViewModel.class);
    }

    private void observeMovies(){

        movieViewModel
                .getMovies()
                .observe(getViewLifecycleOwner(), movies -> {

                    movieList.clear();

                    if(movies != null){

                        for(Movie movie : movies){

                            if(movie.getStatus()
                                    .equals("EARLY_ACCESS")){

                                movieList.add(movie);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}