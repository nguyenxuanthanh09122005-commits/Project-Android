package com.cinema.movie_booking.views.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.adapters.MovieDetailPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MovieDetailActivity
        extends AppCompatActivity {

    private TextView backButton;
    private TextView movieTitle;

    private TabLayout movieTabs;
    private ViewPager2 viewPager;

    private MovieDetailPagerAdapter pagerAdapter;

    private Long movieId;
    private String movieName;
    private Long cinemaId;
    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(
                savedInstanceState
        );

        setContentView(
                R.layout.activity_movie_detail
        );

        initViews();

        getIntentData();

        if(movieId == null || movieId <= 0){

            finish();

            return;
        }

        setupTopBar();

        setupViewPager();
    }

    private void initViews(){

        backButton =
                findViewById(
                        R.id.backButton
                );

        movieTitle =
                findViewById(
                        R.id.movieTitle
                );

        movieTabs =
                findViewById(
                        R.id.movieTabs
                );

        viewPager =
                findViewById(
                        R.id.viewPager
                );
    }

    private void getIntentData() {
        movieId = getIntent().hasExtra("movieId")
                ? getIntent().getLongExtra("movieId", -1)
                : null;

        movieName = getIntent().getStringExtra("movieName");

        // Ưu tiên lấy cinemaId từ Intent nếu có
        if (getIntent().hasExtra("cinemaId")) {
            cinemaId = getIntent().getLongExtra("cinemaId", -1);
        } else {
            // Nếu không có trong Intent, kiểm tra SharedPreferences nhưng không để mặc định là 1L
            android.content.SharedPreferences prefs = getSharedPreferences("CinemaPrefs", MODE_PRIVATE);
            if (prefs.contains("selected_cinema_id")) {
                cinemaId = prefs.getLong("selected_cinema_id", -1L);
            } else {
                cinemaId = null;
            }
        }

        // Nếu cinemaId là -1 thì coi như là "Tất cả rạp"
        if (cinemaId != null && cinemaId == -1) {
            cinemaId = null;
        }
    }

    private void setupTopBar(){

        movieTitle.setText(

                movieName != null
                        ? movieName
                        : "Chi tiết phim"
        );

        backButton.setOnClickListener(
                v -> finish()
        );
    }

    private void setupViewPager(){

        // CHỈ GIỮ LẠI KHỞI TẠO NÀY (Đã truyền đầy đủ cinemaId)
        pagerAdapter = new MovieDetailPagerAdapter(this, movieId, movieName, cinemaId);
        viewPager.setAdapter(pagerAdapter);

        // Defer setting offscreen page limit to avoid blocking the main thread during transition/rendering
        viewPager.post(() -> {
            if (!isFinishing() && viewPager != null) {
                viewPager.setOffscreenPageLimit(2);
            }
        });

        new TabLayoutMediator(
                movieTabs,
                viewPager,
                (tab,position)->{
                    switch(position){
                        case 0:
                            tab.setText("Suất chiếu");
                            break;
                        case 1:
                            tab.setText("Thông tin");
                            break;
                    }
                }
        ).attach();
    }
}