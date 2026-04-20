package com.example.movie_booking.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_booking.R;
import com.example.movie_booking.database.AppDatabase;
import com.example.movie_booking.dao.SuatChieuDao;
import com.example.movie_booking.object.Phim;
import com.example.movie_booking.object.SuatChieu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import adapter.NgayAdapter;
import adapter.RapAdapter;

public class DatVeActivity extends AppCompatActivity {

    private ImageView imgBanner, btnQuayLai;
    private TextView tvTenPhim, tvThongTinPhim, tvChonKhuVuc, tvChiTietPhim;
    private RecyclerView rvLichChieu, rvNgay;
    private AppDatabase db;
    private RapAdapter rapAdapter;
    private NgayAdapter ngayAdapter;
    private List<SuatChieuDao.SuatChieuWithTheater> allSuatChieu = new ArrayList<>();
    private String selectedThanhPho = "Tất cả";
    private String selectedFullDate = "";
    private String currentMovieAgeRating = "P";
    private Phim currentPhim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_ve);

        db = AppDatabase.getInstance(this);
        khoiTaoGiaoDien();

        currentPhim = (Phim) getIntent().getSerializableExtra("movie_data");

        if (currentPhim != null) {
            currentMovieAgeRating = currentPhim.getDo_tuoi_quy_dinh();
            hienThiDuLieuPhim(currentPhim);
            taiTatCaSuatChieu(currentPhim.getId_phim());
        }

        btnQuayLai.setOnClickListener(v -> finish());
        tvChonKhuVuc.setOnClickListener(v -> hienThiDialogChonKhuVuc());
        
        // Sự kiện click nút Chi tiết phim
        tvChiTietPhim.setOnClickListener(v -> {
            if (currentPhim != null) {
                Intent intent = new Intent(DatVeActivity.this, ChiTietPhimActivity.class);
                intent.putExtra("phim_data", currentPhim);
                startActivity(intent);
            }
        });
    }

    private void khoiTaoGiaoDien() {
        imgBanner = findViewById(R.id.imgBanner);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        tvTenPhim = findViewById(R.id.tvTenPhim);
        tvThongTinPhim = findViewById(R.id.tvThongTinPhim);
        tvChonKhuVuc = findViewById(R.id.tvChonKhuVuc);
        tvChiTietPhim = findViewById(R.id.tvChiTietPhim);
        rvLichChieu = findViewById(R.id.rvLichChieu);
        rvNgay = findViewById(R.id.rvNgay);
        
        rvLichChieu.setLayoutManager(new LinearLayoutManager(this));
        rvNgay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void hienThiDuLieuPhim(Phim phim) {
        tvTenPhim.setText(phim.getTen_phim());
        String thongTin = phim.getThe_loai() + " | " + phim.getDo_tuoi_quy_dinh() + " | " + phim.getThoi_luong() + " phút";
        tvThongTinPhim.setText(thongTin);

        String tenHinhAnh = phim.getAnh_poster();
        if (tenHinhAnh != null && !tenHinhAnh.isEmpty()) {
            if (tenHinhAnh.contains(".")) {
                tenHinhAnh = tenHinhAnh.substring(0, tenHinhAnh.lastIndexOf("."));
            }
            int resId = getResources().getIdentifier(tenHinhAnh, "drawable", getPackageName());
            if (resId != 0) {
                imgBanner.setImageResource(resId);
            }
        }
    }

    private void taiTatCaSuatChieu(int idPhim) {
        List<SuatChieuDao.SuatChieuWithTheater> rawSuatChieu = db.suatChieuDao().getSuatChieuWithTheaterByPhim(idPhim);
        if (rawSuatChieu == null || rawSuatChieu.isEmpty()) {
            Log.w("DatVeActivity", "No showtimes found for movie ID: " + idPhim);
            return;
        }

        allSuatChieu = new ArrayList<>();
        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDateTime = new Date();

        for (SuatChieuDao.SuatChieuWithTheater sc : rawSuatChieu) {
            try {
                Date showtimeDate = sdfFull.parse(sc.thoi_gian_bat_dau);
                if (showtimeDate != null && showtimeDate.after(currentDateTime)) {
                    allSuatChieu.add(sc);
                }
            } catch (ParseException e) {
                Log.e("DatVeActivity", "Lỗi định dạng ngày giờ: " + sc.thoi_gian_bat_dau, e);
            }
        }

        if (allSuatChieu.isEmpty()) {
            rvNgay.setAdapter(null);
            rvLichChieu.setAdapter(null);
            return;
        }

        Map<String, NgayAdapter.NgayItem> mapNgay = new TreeMap<>();
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat sdfMonth = new SimpleDateFormat("'Th' MM", Locale.getDefault());
        SimpleDateFormat sdfThu = new SimpleDateFormat("EEE", new Locale("vi", "VN"));

        for (SuatChieuDao.SuatChieuWithTheater sc : allSuatChieu) {
            try {
                String dateStr = sc.thoi_gian_bat_dau.split(" ")[0];
                if (!mapNgay.containsKey(dateStr)) {
                    Date date = sdfInput.parse(dateStr);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    
                    String thu = sdfThu.format(date);
                    String ngay = sdfDay.format(date);
                    String thang = sdfMonth.format(date);
                    
                    mapNgay.put(dateStr, new NgayAdapter.NgayItem(thu, ngay, thang, dateStr));
                }
            } catch (Exception e) {
                Log.e("DatVeActivity", "Lỗi parse ngày: " + sc.thoi_gian_bat_dau, e);
            }
        }

        List<NgayAdapter.NgayItem> listNgay = new ArrayList<>(mapNgay.values());
        
        ngayAdapter = new NgayAdapter(listNgay, ngayItem -> {
            selectedFullDate = ngayItem.getFullDate();
            locLichChieuTheoDieuKien();
        });
        rvNgay.setAdapter(ngayAdapter);

        if (!listNgay.isEmpty()) {
            selectedFullDate = listNgay.get(0).getFullDate();
            locLichChieuTheoDieuKien();
        }
    }

    private void hienThiDialogChonKhuVuc() {
        List<String> listThanhPho = db.rapDao().getAllThanhPho();
        listThanhPho.add(0, "Tất cả");
        
        String[] arrays = listThanhPho.toArray(new String[0]);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thành phố");
        builder.setItems(arrays, (dialog, which) -> {
            selectedThanhPho = arrays[which];
            tvChonKhuVuc.setText(selectedThanhPho + " >");
            locLichChieuTheoDieuKien();
        });
        builder.show();
    }

    private void locLichChieuTheoDieuKien() {
        Map<String, List<SuatChieuDao.SuatChieuWithTheater>> lichChieuTheoRap = new HashMap<>();
        
        for (SuatChieuDao.SuatChieuWithTheater item : allSuatChieu) {
            String thanhPhoCuaRap = db.suatChieuDao().getThanhPhoByRap(item.ten_rap);
            
            boolean matchesDate = item.thoi_gian_bat_dau.startsWith(selectedFullDate);
            boolean matchesCity = selectedThanhPho.equals("Tất cả") || (thanhPhoCuaRap != null && thanhPhoCuaRap.equals(selectedThanhPho));

            if (matchesDate && matchesCity) {
                if (!lichChieuTheoRap.containsKey(item.ten_rap)) {
                    lichChieuTheoRap.put(item.ten_rap, new ArrayList<>());
                }
                lichChieuTheoRap.get(item.ten_rap).add(item);
            }
        }
        
        rapAdapter = new RapAdapter(lichChieuTheoRap);
        rvLichChieu.setAdapter(rapAdapter);
    }
}
