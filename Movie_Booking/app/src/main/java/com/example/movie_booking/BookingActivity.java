package com.example.movie_booking;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import adapter.TheaterAdapter;
import database.DatabaseHelper;

public class BookingActivity extends AppCompatActivity {

    private ImageView imgBanner, btnBack;
    private TextView tvMovieTitle, tvMovieInfo;
    private RecyclerView rvShowtimes;
    private DatabaseHelper dbHelper;
    private TheaterAdapter theaterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        dbHelper = new DatabaseHelper(this);
        initViews();

        // Nhận dữ liệu Phim từ Intent
        Phim movie = (Phim) getIntent().getSerializableExtra("movie_data");

        if (movie != null) {
            displayMovieData(movie);
            loadShowtimes(movie.getId_phim());
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        imgBanner = findViewById(R.id.imgBanner);
        btnBack = findViewById(R.id.btnBack);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvMovieInfo = findViewById(R.id.tvMovieInfo);
        rvShowtimes = findViewById(R.id.rvShowtimes);
        
        rvShowtimes.setLayoutManager(new LinearLayoutManager(this));
    }

    private void displayMovieData(Phim movie) {
        tvMovieTitle.setText(movie.getTen_phim());
        String info = movie.getThe_loai() + " | " + movie.getDo_tuoi_quy_dinh() + " | " + movie.getThoi_luong() + " phút";
        tvMovieInfo.setText(info);

        String imageName = movie.getAnh_poster();
        if (imageName != null && !imageName.isEmpty()) {
            if (imageName.contains(".")) {
                imageName = imageName.substring(0, imageName.lastIndexOf("."));
            }
            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (resId != 0) {
                imgBanner.setImageResource(resId);
            }
        }
    }

    private void loadShowtimes(int movieId) {
        // Lấy dữ liệu suất chiếu đã được nhóm theo tên rạp từ Database
        Map<String, List<SuatChieu>> groupedShowtimes = dbHelper.getShowtimesGroupedByTheater(movieId);
        
        if (groupedShowtimes != null && !groupedShowtimes.isEmpty()) {
            theaterAdapter = new TheaterAdapter(groupedShowtimes);
            rvShowtimes.setAdapter(theaterAdapter);
            Log.d("BookingActivity", "Loaded showtimes for " + groupedShowtimes.size() + " theaters.");
        } else {
            Log.w("BookingActivity", "No showtimes found for movie ID: " + movieId);
        }
    }
}
