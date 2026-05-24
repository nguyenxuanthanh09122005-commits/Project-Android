package com.cinema.movie_booking.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.cinema.movie_booking.views.fragments.movie.ComingSoonFragment;
import com.cinema.movie_booking.views.fragments.movie.EarlyAccessFragment;
import com.cinema.movie_booking.views.fragments.movie.ShowingFragment;

public class ViewPagerMovieAdapter extends FragmentStateAdapter {
    public ViewPagerMovieAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new ComingSoonFragment();
            case 2: return new EarlyAccessFragment();
            case 1:
            default: return new ShowingFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}