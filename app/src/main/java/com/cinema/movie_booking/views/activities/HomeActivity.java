package com.cinema.movie_booking.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.views.fragments.AccountFragment;
import com.cinema.movie_booking.views.fragments.HomeFragment;
import com.cinema.movie_booking.views.fragments.CinemaFragment;
import com.cinema.movie_booking.views.fragments.MyTicketFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView =
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            Fragment fragment = null;

            if(item.getItemId() == R.id.nav_home){
                fragment = new HomeFragment();
            }
            else if(item.getItemId() == R.id.nav_cinema){
                fragment = new CinemaFragment();
            }
            else if(item.getItemId() == R.id.nav_ticket){
                fragment = new MyTicketFragment();
            }
            else if(item.getItemId() == R.id.nav_account){
                fragment = new AccountFragment();
            }

            assert fragment != null;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();

            return true;
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
}