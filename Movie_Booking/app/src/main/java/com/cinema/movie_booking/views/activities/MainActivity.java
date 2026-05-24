package com.cinema.movie_booking.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.views.fragments.AccountFragment;
import com.cinema.movie_booking.views.fragments.CinemaFragment;
import com.cinema.movie_booking.views.fragments.HomeFragment;
import com.cinema.movie_booking.views.fragments.MyTicketFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            String tag = "";
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                tag = "HOME";
            } else if (itemId == R.id.nav_cinema) {
                tag = "CINEMA";
            } else if (itemId == R.id.nav_ticket) {
                tag = "TICKET";
            } else {
                tag = "ACCOUNT";
            }

            // Safe recovery: find by tag first
            fragment = getSupportFragmentManager().findFragmentByTag(tag);

            if (fragment == null) {
                if (itemId == R.id.nav_home) fragment = new HomeFragment();
                else if (itemId == R.id.nav_cinema) fragment = new CinemaFragment();
                else if (itemId == R.id.nav_ticket) fragment = new MyTicketFragment();
                else fragment = new AccountFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, fragment, tag)
                    .commit();

            return true;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }
}
