package com.example.movie_booking.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.example.movie_booking.database.SharedPrefManager;
import com.example.movie_booking.fragment.LoginFragment;
import com.example.movie_booking.fragment.ProfileFragment;
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
    private View authContainer;
    private ImageView navHome, navProfile;
    private View layoutUser, layoutAdmin;
    private Button btnManageMovies, btnManageSchedules, btnManageTickets, btnManageUsers, btnStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Movie_Booking);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_chu);

        // Ánh xạ View
        authContainer = findViewById(R.id.auth_container);
        navHome = findViewById(R.id.navHome);
        navProfile = findViewById(R.id.navProfile);
        
        layoutUser = findViewById(R.id.layout_user);
        layoutAdmin = findViewById(R.id.layout_admin);
        
        btnManageMovies = findViewById(R.id.btnManageMovies);
        btnManageSchedules = findViewById(R.id.btnManageSchedules);
        btnManageTickets = findViewById(R.id.btnManageTickets);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnStatistics = findViewById(R.id.btnStatistics);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.man_hinh_chinh), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getInstance(this);
        khoiTaoGiaoDien();
        taiDuLieuPhim();

        // Xử lý chuyển hướng điều hướng
        navHome.setOnClickListener(v -> vaoTrangChu());
        
        navProfile.setOnClickListener(v -> {
            if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                hienThiProfile();
            } else {
                hienThiManHinhAuth();
            }
        });

        // Kiểm tra quyền hạn ngay khi mở app
        capNhatGiaoDienTheoQuyen();

        // Kiểm tra trạng thái đăng nhập
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            hienThiManHinhAuth();
        } else {
            authContainer.setVisibility(View.GONE);
        }
    }

    // HÀM QUAN TRỌNG: Phân quyền Admin/Khách hàng
    private void capNhatGiaoDienTheoQuyen() {
        String role = SharedPrefManager.getInstance(this).getRole();
        
        if ("Admin".equals(role)) {
            // Hiển thị giao diện Admin, ẩn giao diện User
            layoutUser.setVisibility(View.GONE);
            layoutAdmin.setVisibility(View.VISIBLE);
            
            // Cài đặt sự kiện cho các nút quản lý của Admin
            btnManageMovies.setOnClickListener(v -> Toast.makeText(this, "Quản lý Phim & Nội dung", Toast.LENGTH_SHORT).show());
            btnManageSchedules.setOnClickListener(v -> Toast.makeText(this, "Quản lý Lịch chiếu", Toast.LENGTH_SHORT).show());
            btnManageTickets.setOnClickListener(v -> Toast.makeText(this, "Tra cứu & Hủy vé", Toast.LENGTH_SHORT).show());
            btnManageUsers.setOnClickListener(v -> Toast.makeText(this, "Quản lý Người dùng", Toast.LENGTH_SHORT).show());
            btnStatistics.setOnClickListener(v -> Toast.makeText(this, "Dashboard Thống kê Doanh thu", Toast.LENGTH_SHORT).show());
            
        } else {
            // Hiển thị giao diện User, ẩn giao diện Admin
            layoutUser.setVisibility(View.VISIBLE);
            layoutAdmin.setVisibility(View.GONE);
        }
    }

    public void hienThiManHinhAuth() {
        authContainer.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_container, new LoginFragment())
                .commit();
    }

    public void hienThiProfile() {
        authContainer.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_container, new ProfileFragment())
                .commit();
    }

    public void vaoTrangChu() {
        authContainer.setVisibility(View.GONE);
        // Cập nhật lại giao diện ngay sau khi login/logout
        capNhatGiaoDienTheoQuyen();
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
