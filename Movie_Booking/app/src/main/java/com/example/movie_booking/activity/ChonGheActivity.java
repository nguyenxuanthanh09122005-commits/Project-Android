package com.example.movie_booking.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_booking.R;
import com.example.movie_booking.database.AppDatabase;
import com.example.movie_booking.object.Ghe;
import com.example.movie_booking.object.Phim;
import com.example.movie_booking.object.SuatChieu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import adapter.GheAdapter;

public class ChonGheActivity extends AppCompatActivity {

    private RecyclerView rvGhe;
    private TextView tvGheDaChon, tvTongTien;
    private Button btnTiepTuc;
    private ImageView btnBack;
    private AppDatabase db;
    private SuatChieu suatChieu;
    private GheAdapter gheAdapter;
    private DecimalFormat formatter = new DecimalFormat("###,###,###");
    private double currentTongTien = 0;
    private String doTuoiQuyDinh = "P";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_ghe);

        db = AppDatabase.getInstance(this);
        suatChieu = (SuatChieu) getIntent().getSerializableExtra("suat_chieu_data");

        khoiTaoGiaoDien();
        
        if (suatChieu != null) {
            taiDanhSachGhe(suatChieu.getId_phong(), suatChieu.getId_suat_chieu());
            layThongTinDoTuoiPhim(suatChieu.getId_phim());
        }

        btnBack.setOnClickListener(v -> finish());
        
        btnTiepTuc.setOnClickListener(v -> {
            List<Ghe> selectedGhes = gheAdapter.getSelectedGhes();
            if (selectedGhes.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ghế!", Toast.LENGTH_SHORT).show();
            } else {
                checkAgeAndProceed(selectedGhes);
            }
        });
    }

    private void layThongTinDoTuoiPhim(int idPhim) {
        executorService.execute(() -> {
            Phim phim = db.phimDao().getPhimById(idPhim);
            if (phim != null) {
                doTuoiQuyDinh = phim.getDo_tuoi_quy_dinh();
            }
        });
    }

    private void checkAgeAndProceed(List<Ghe> selectedGhes) {
        if (doTuoiQuyDinh == null || doTuoiQuyDinh.equalsIgnoreCase("P")) {
            navigateToPayment(selectedGhes);
            return;
        }

        int requiredAge = 0;
        try {
            if (doTuoiQuyDinh.length() > 1) {
                requiredAge = Integer.parseInt(doTuoiQuyDinh.substring(1));
            }
        } catch (Exception e) {
            requiredAge = 0;
        }

        if (requiredAge == 0) {
            navigateToPayment(selectedGhes);
            return;
        }

        // Tạo giao diện DatePickerDialog đẹp hơn với Holo Light hoặc Theme mặc định của hệ thống
        Calendar calendar = Calendar.getInstance();
        int finalRequiredAge = requiredAge;
        
        // Sử dụng style Theme_Holo_Light_Dialog_MinWidth hoặc để hệ thống tự chọn bản hiện đại nhất
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, 
                AlertDialog.THEME_HOLO_LIGHT, // Tạo giao diện kiểu vòng xoay (spinner) dễ nhìn hơn
                (view, year, month, dayOfMonth) -> {
                    Calendar birthDate = Calendar.getInstance();
                    birthDate.set(year, month, dayOfMonth);
                    
                    int age = calendar.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
                    if (calendar.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                        age--;
                    }

                    if (age >= finalRequiredAge) {
                        navigateToPayment(selectedGhes);
                    } else {
                        Toast.makeText(this, "Bạn chưa đủ " + finalRequiredAge + " tuổi để xem phim này!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, TrangChuActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }, 
                calendar.get(Calendar.YEAR) - finalRequiredAge, // Mặc định nhảy tới năm vừa đủ tuổi
                calendar.get(Calendar.MONTH), 
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.setTitle("Xác nhận ngày sinh (Phim " + doTuoiQuyDinh + ")");
        // Giới hạn không cho chọn ngày ở tương lai
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void navigateToPayment(List<Ghe> selectedGhes) {
        Intent intent = new Intent(this, ThanhToanActivity.class);
        intent.putExtra("tong_tien", currentTongTien);
        intent.putExtra("suat_chieu_data", suatChieu);
        intent.putExtra("selected_ghes", new ArrayList<>(selectedGhes));
        startActivity(intent);
    }

    private void khoiTaoGiaoDien() {
        rvGhe = findViewById(R.id.rvGhe);
        tvGheDaChon = findViewById(R.id.tvGheDaChon);
        tvTongTien = findViewById(R.id.tvTongTien);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        btnBack = findViewById(R.id.btnBack);

        rvGhe.setLayoutManager(new GridLayoutManager(this, 8));
    }

    private void taiDanhSachGhe(int idPhong, int idSuatChieu) {
        executorService.execute(() -> {
            List<Ghe> listGhe = db.gheDao().getGheByPhong(idPhong);
            List<Ghe> listGheDaDat = db.gheDao().getGheDaDat(idSuatChieu);
            
            List<Integer> bookedIds = new ArrayList<>();
            for (Ghe g : listGheDaDat) {
                bookedIds.add(g.getId_ghe());
            }
            
            runOnUiThread(() -> {
                gheAdapter = new GheAdapter(listGhe, bookedIds, selectedGhes -> {
                    capNhatThongTin(selectedGhes);
                });
                rvGhe.setAdapter(gheAdapter);
            });
        });
    }

    private void capNhatThongTin(List<Ghe> selectedGhes) {
        if (selectedGhes.isEmpty()) {
            tvGheDaChon.setText("Ghế: chưa chọn");
            tvTongTien.setText("Tổng: 0đ");
            currentTongTien = 0;
            return;
        }

        StringBuilder sb = new StringBuilder("Ghế: ");
        double tongTien = 0;
        double giaCoBan = suatChieu.getGia_ve_co_ban();

        for (int i = 0; i < selectedGhes.size(); i++) {
            Ghe g = selectedGhes.get(i);
            sb.append(g.getHang_ghe()).append(g.getSo_ghe());
            if (i < selectedGhes.size() - 1) sb.append(", ");

            if ("VIP".equalsIgnoreCase(g.getLoai_ghe())) {
                tongTien += (giaCoBan + 10000);
            } else {
                tongTien += giaCoBan;
            }
        }

        currentTongTien = tongTien;
        tvGheDaChon.setText(sb.toString());
        tvTongTien.setText("Tổng: " + formatter.format(tongTien) + "đ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (suatChieu != null) {
            taiDanhSachGhe(suatChieu.getId_phong(), suatChieu.getId_suat_chieu());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
