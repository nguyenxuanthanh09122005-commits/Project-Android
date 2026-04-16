package com.example.movie_booking;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapter.MovieAdapter;
import database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView rvNowPlaying, rvComingSoon, rvTopMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        
        super.onCreate(savedInstanceState);
        
        setTheme(R.style.Theme_Movie_Booking);
        
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadMovies();
    }

    private void initViews() {
        rvNowPlaying = findViewById(R.id.rvNowPlaying);
        rvComingSoon = findViewById(R.id.rvComingSoon);
        rvTopMovies = findViewById(R.id.rvTopMovies);
    }

    private void loadMovies() {
        try {
            List<Phim> allMovies = dbHelper.getAllMovies();
            
            if (allMovies != null && !allMovies.isEmpty()) {
                List<Phim> nowPlaying = new ArrayList<>();
                List<Phim> comingSoon = new ArrayList<>();
                List<Phim> topMovies = new ArrayList<>();

                Date currentDate = new Date();

                for (Phim movie : allMovies) {
                    Date releaseDate = movie.getNgayKhoiChieuObject();
                    if (releaseDate != null) {
                        if (releaseDate.after(currentDate)) {
                            comingSoon.add(movie);
                        } else {
                            nowPlaying.add(movie);
                        }
                    } else {
                        nowPlaying.add(movie);
                    }
                }

                setupRecyclerView(rvNowPlaying, nowPlaying);
                setupRecyclerView(rvComingSoon, comingSoon);
                setupRecyclerView(rvTopMovies, topMovies);
                
                Log.d("MainActivity", "Successfully loaded " + allMovies.size() + " movies.");
            } else {
                Log.w("MainActivity", "No movies found in database.");
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error loading movies", e);
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Phim> movies) {
        if (recyclerView != null) {
            MovieAdapter adapter = new MovieAdapter(movies);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
        }
    }
}
