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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_booking.R;
import com.example.movie_booking.object.Ghe;
import com.example.movie_booking.object.SuatChieu;
import com.example.movie_booking.viewmodel.GheViewModel;
import com.example.movie_booking.viewmodel.PhimViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapter.GheAdapter;

public class ChonGheActivity extends AppCompatActivity {

    private RecyclerView rvGhe;
    private TextView tvGheDaChon, tvTongTien;
    private Button btnTiepTuc;
    private ImageView btnBack;
    private SuatChieu suatChieu;
    private GheAdapter gheAdapter;
    private DecimalFormat formatter = new DecimalFormat("###,###,###");
    private double currentTongTien = 0;
    private String doTuoiQuyDinh = "P";
    
    private GheViewModel gheViewModel;
    private PhimViewModel phimViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_ghe);

        suatChieu = (SuatChieu) getIntent().getSerializableExtra("suat_chieu_data");

        khoiTaoGiaoDien();
        thietLapViewModel();

        btnBack.setOnClickListener(v -> finish());
        
        btnTiepTuc.setOnClickListener(v -> {
            if (gheAdapter == null) return;
            List<Ghe> selectedGhes = gheAdapter.getSelectedGhes();
            if (selectedGhes.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ghế!", Toast.LENGTH_SHORT).show();
            } else {
                checkAgeAndProceed(selectedGhes);
            }
        });
    }

    private void thietLapViewModel() {
        gheViewModel = new ViewModelProvider(this).get(GheViewModel.class);
        phimViewModel = new ViewModelProvider(this).get(PhimViewModel.class);

        if (suatChieu != null) {
            // Lấy độ tuổi quy định của phim
            phimViewModel.getAllPhims().observe(this, phims -> {
                if (phims != null) {
                    for (com.example.movie_booking.object.Phim p : phims) {
                        if (p.getId_phim() == suatChieu.getId_phim()) {
                            doTuoiQuyDinh = p.getDo_tuoi_quy_dinh();
                            break;
                        }
                    }
                }
            });

            taiDuLieuGhe();
        }
    }

    private void taiDuLieuGhe() {
        gheViewModel.getGheByPhong(suatChieu.getId_phong()).observe(this, tatCaGhe -> {
            if (tatCaGhe != null) {
                gheViewModel.getGheDaDat(suatChieu.getId_suat_chieu()).observe(this, gheDaDat -> {
                    List<Integer> bookedIds = new ArrayList<>();
                    if (gheDaDat != null) {
                        for (Ghe g : gheDaDat) {
                            bookedIds.add(g.getId_ghe());
                        }
                    }
                    
                    gheAdapter = new GheAdapter(tatCaGhe, bookedIds, selectedGhes -> {
                        capNhatThongTin(selectedGhes);
                    });
                    rvGhe.setAdapter(gheAdapter);
                });
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

        Calendar calendar = Calendar.getInstance();
        int finalRequiredAge = requiredAge;
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, 
                AlertDialog.THEME_HOLO_LIGHT,
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
                calendar.get(Calendar.YEAR) - finalRequiredAge,
                calendar.get(Calendar.MONTH), 
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.setTitle("Xác nhận ngày sinh (Phim " + doTuoiQuyDinh + ")");
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
}
