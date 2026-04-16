package com.example.movie_booking.activity;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.RapAdapter;

public class DatVeActivity extends AppCompatActivity {

    private ImageView imgBanner, btnQuayLai;
    private TextView tvTenPhim, tvThongTinPhim;
    private RecyclerView rvLichChieu;
    private AppDatabase db;
    private RapAdapter rapAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_ve);

        db = AppDatabase.getInstance(this);
        khoiTaoGiaoDien();

        // Nhận dữ liệu Phim từ Intent
        Phim phim = (Phim) getIntent().getSerializableExtra("movie_data");

        if (phim != null) {
            hienThiDuLieuPhim(phim);
            taiLichChieu(phim.getId_phim());
        }

        btnQuayLai.setOnClickListener(v -> finish());
    }

    private void khoiTaoGiaoDien() {
        imgBanner = findViewById(R.id.imgBanner);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        tvTenPhim = findViewById(R.id.tvTenPhim);
        tvThongTinPhim = findViewById(R.id.tvThongTinPhim);
        rvLichChieu = findViewById(R.id.rvLichChieu);
        
        rvLichChieu.setLayoutManager(new LinearLayoutManager(this));
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

    private void taiLichChieu(int idPhim) {
        // Lấy dữ liệu suất chiếu từ Room và nhóm theo tên rạp
        List<SuatChieuDao.SuatChieuWithTheater> listWithTheater = db.suatChieuDao().getSuatChieuWithTheaterByPhim(idPhim);
        
        Map<String, List<SuatChieu>> lichChieuTheoRap = new HashMap<>();
        
        if (listWithTheater != null && !listWithTheater.isEmpty()) {
            for (SuatChieuDao.SuatChieuWithTheater item : listWithTheater) {
                if (!lichChieuTheoRap.containsKey(item.ten_rap)) {
                    lichChieuTheoRap.put(item.ten_rap, new ArrayList<>());
                }
                lichChieuTheoRap.get(item.ten_rap).add(item.toSuatChieu());
            }
            
            rapAdapter = new RapAdapter(lichChieuTheoRap);
            rvLichChieu.setAdapter(rapAdapter);
            Log.d("DatVeActivity", "Loaded showtimes from Room for " + lichChieuTheoRap.size() + " theaters.");
        } else {
            Log.w("DatVeActivity", "No showtimes found in Room for movie ID: " + idPhim);
        }
    }
}
