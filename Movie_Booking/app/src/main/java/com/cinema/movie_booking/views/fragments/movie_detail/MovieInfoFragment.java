package com.cinema.movie_booking.views.fragments.movie_detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.cinema.movie_booking.BuildConfig;
import com.cinema.movie_booking.R;
import com.cinema.movie_booking.models.Movie;
import com.cinema.movie_booking.repositories.MovieRepository;
import com.cinema.movie_booking.viewmodels.MovieViewModel;

public class MovieInfoFragment
        extends Fragment {

    private static final String ARG_MOVIE_ID =
            "movie_id";

    private static final String IMAGE_URL = BuildConfig.IMAGE_URL;

    private Long movieId;

    private ImageView imgTrailer;
    private ImageView imgPoster;

    private TextView txtMovieName;
    private TextView txtAge;
    private TextView txtDuration;
    private TextView txtReleaseDate;
    private TextView txtDescription;

    private MovieViewModel movieViewModel;

    public static MovieInfoFragment newInstance(
            Long movieId
    ){

        MovieInfoFragment fragment =
                new MovieInfoFragment();

        Bundle bundle =
                new Bundle();

        bundle.putLong(
                ARG_MOVIE_ID,
                movieId
        );

        fragment.setArguments(
                bundle
        );

        return fragment;
    }

    @Override
    public void onCreate(
            @Nullable Bundle savedInstanceState
    ) {

        super.onCreate(
                savedInstanceState
        );

        if(getArguments()!=null){

            movieId =
                    getArguments()
                            .getLong(
                                    ARG_MOVIE_ID
                            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view =
                inflater.inflate(
                        R.layout.fragment_movie_info,
                        container,
                        false
                );

        initViews(view);

        setupViewModel();

        observeMovie();

        return view;
    }

    private void initViews(
            View view
    ){

        imgTrailer =
                view.findViewById(
                        R.id.imgTrailer
                );

        imgPoster =
                view.findViewById(
                        R.id.imgPoster
                );

        txtMovieName =
                view.findViewById(
                        R.id.txtMovieName
                );

        txtAge =
                view.findViewById(
                        R.id.txtAge
                );

        txtDuration =
                view.findViewById(
                        R.id.txtDuration
                );

        txtReleaseDate =
                view.findViewById(
                        R.id.txtReleaseDate
                );

        txtDescription =
                view.findViewById(
                        R.id.txtDescription
                );
    }

    private void setupViewModel(){

        movieViewModel =
                new ViewModelProvider(
                        this
                )
                        .get(
                                MovieViewModel.class
                        );
    }

    private void observeMovie() {
        movieViewModel.getMovieById(movieId).observe(getViewLifecycleOwner(), resource -> {
            if (!isAdded() || getView() == null || resource == null) return;

            if (resource.status == com.cinema.movie_booking.utils.Resource.Status.SUCCESS && resource.data != null) {
                bindMovie(resource.data);
                movieViewModel.processMovieMetadata(resource.data);
            }
        });

        movieViewModel.getMovieMetadata().observe(getViewLifecycleOwner(), metadata -> {
            if (!isAdded() || getView() == null || metadata == null) return;
            updateMetadata(metadata);
        });
    }

    private void updateMetadata(MovieRepository.MovieMetadata metadata) {
        txtDuration.setText(metadata.getFormattedDuration());
        txtReleaseDate.setText(metadata.getFormattedReleaseDate());

        if (metadata.getYoutubeThumbnail() != null && !metadata.getYoutubeThumbnail().isEmpty()) {
            Glide.with(this)
                    .load(metadata.getYoutubeThumbnail())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imgTrailer);
            imgTrailer.setVisibility(View.VISIBLE);
        } else {
            imgTrailer.setVisibility(View.GONE);
        }
    }

    private void bindMovie(Movie movie) {
        txtMovieName.setText(movie.getMovieName());
        txtAge.setText(movie.getAgeRating());
        txtDescription.setText(movie.getDescription());

        Glide.with(this)
                .load(IMAGE_URL + movie.getPosterImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imgPoster);

        if (!TextUtils.isEmpty(movie.getTrailerUrl())) {
            imgTrailer.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerUrl()));
                startActivity(intent);
            });
        }
    }
}