package com.cinema.movie_booking.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.cinema.movie_booking.views.fragments.movie_detail.MovieInfoFragment;
import com.cinema.movie_booking.views.fragments.movie_detail.ShowtimeFragment;

public class MovieDetailPagerAdapter extends FragmentStateAdapter {
    private final Long movieId;
    private final String movieName;

    private final Long cinemaId;
    public MovieDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity, Long movieId, String movieName, Long cinemaId) {
        super(fragmentActivity);
        this.movieId = movieId;
        this.movieName = movieName;
        this.cinemaId = cinemaId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return ShowtimeFragment.newInstance(movieId, movieName, cinemaId);
        }
        return MovieInfoFragment.newInstance(movieId);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}