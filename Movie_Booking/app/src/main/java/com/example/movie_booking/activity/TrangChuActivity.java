package com.example.movie_booking.activity;

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

import com.example.movie_booking.R;
import com.example.movie_booking.database.AppDatabase;
import com.example.movie_booking.object.Phim;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import adapter.PhimAdapter;

public class TrangChuActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView rvDangChieu, rvSapChieu, rvPhimHot;
    private PhimAdapter adapterDangChieu, adapterSapChieu, adapterPhimHot;
    private static final String TAG = "TrangChuActivity";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Movie_Booking);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_chu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.man_hinh_chinh), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getInstance(this);
        khoiTaoGiaoDien();
        taiDuLieuPhim();
    }

    private void khoiTaoGiaoDien() {
        rvDangChieu = findViewById(R.id.rvDangChieu);
        rvSapChieu = findViewById(R.id.rvSapChieu);
        rvPhimHot = findViewById(R.id.rvPhimHot);

        adapterDangChieu = new PhimAdapter(new ArrayList<>());
        adapterSapChieu = new PhimAdapter(new ArrayList<>());
        adapterPhimHot = new PhimAdapter(new ArrayList<>());

        thietLapRecyclerView(rvDangChieu, adapterDangChieu);
        thietLapRecyclerView(rvSapChieu, adapterSapChieu);
        thietLapRecyclerView(rvPhimHot, adapterPhimHot);
    }

    private void thietLapRecyclerView(RecyclerView recyclerView, PhimAdapter adapter) {
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
        }
    }

    private void taiDuLieuPhim() {
        executorService.execute(() -> {
            try {
                List<Phim> tatCaPhim = db.phimDao().getAllPhim();

                if (tatCaPhim == null || tatCaPhim.isEmpty()) {
                    Log.w(TAG, "Database rỗng hoặc không tải được.");
                } else {
                    List<Phim> dangChieu = new ArrayList<>();
                    List<Phim> sapChieu = new ArrayList<>();
                    List<Phim> phimHot = new ArrayList<>();

                    Date ngayHienTai = new Date();

                    for (Phim phim : tatCaPhim) {
                        Date ngayKhoiChieu = phim.getNgayKhoiChieuObject();
                        if (ngayKhoiChieu != null && ngayKhoiChieu.after(ngayHienTai)) {
                            sapChieu.add(phim);
                        } else {
                            dangChieu.add(phim);
                        }

                        // Sửa lỗi check null cho kiểu int
                        if (phim.getId_phim() % 2 == 0) {
                            phimHot.add(phim);
                        }
                    }

                    runOnUiThread(() -> {
                        adapterDangChieu.updateData(dangChieu);
                        adapterSapChieu.updateData(sapChieu);
                        adapterPhimHot.updateData(phimHot);
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "Lỗi khi tải dữ liệu phim: ", e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
