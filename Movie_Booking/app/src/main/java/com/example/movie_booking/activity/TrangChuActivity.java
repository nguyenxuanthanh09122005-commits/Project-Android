package com.example.movie_booking.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_booking.R;
import com.example.movie_booking.object.Phim;
import com.example.movie_booking.viewmodel.PhimViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import adapter.PhimAdapter;

public class TrangChuActivity extends AppCompatActivity {

    private RecyclerView rvDangChieu, rvSapChieu, rvPhimHot;
    private PhimAdapter adapterDangChieu, adapterSapChieu, adapterPhimHot;
    private EditText etSearch;
    private ImageView navGrid;
    private List<Phim> allPhimList = new ArrayList<>();
    private PhimViewModel phimViewModel;

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

        khoiTaoGiaoDien();
        thietLapViewModel();
        thietLapTimKiem();
        thietLapAnBanPhim();
        
        navGrid.setOnClickListener(v -> {
            startActivity(new Intent(this, QuanLyPhimActivity.class));
        });
    }

    private void khoiTaoGiaoDien() {
        rvDangChieu = findViewById(R.id.rvDangChieu);
        rvSapChieu = findViewById(R.id.rvSapChieu);
        rvPhimHot = findViewById(R.id.rvPhimHot);
        etSearch = findViewById(R.id.etSearch);
        navGrid = findViewById(R.id.navGrid);

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

    private void thietLapViewModel() {
        phimViewModel = new ViewModelProvider(this).get(PhimViewModel.class);
        phimViewModel.getAllPhims().observe(this, phims -> {
            if (phims != null) {
                allPhimList = phims;
                hienThiPhim(allPhimList);
            }
        });
    }

    private void hienThiPhim(List<Phim> listPhim) {
        List<Phim> dangChieu = new ArrayList<>();
        List<Phim> sapChieu = new ArrayList<>();

        Date ngayHienTai = new Date();

        for (Phim phim : listPhim) {
            Date ngayKhoiChieu = phim.getNgayKhoiChieuObject();
            if (ngayKhoiChieu != null && ngayKhoiChieu.after(ngayHienTai)) {
                sapChieu.add(phim);
            } else {
                dangChieu.add(phim);
            }
        }

        List<Phim> phimHotTemp = new ArrayList<>(dangChieu);
        Collections.sort(phimHotTemp, (p1, p2) -> p2.getThoi_luong() - p1.getThoi_luong());
        List<Phim> phimHotFinal = (phimHotTemp.size() > 3) ? new ArrayList<>(phimHotTemp.subList(0, 3)) : phimHotTemp;

        adapterDangChieu.updateData(dangChieu);
        adapterSapChieu.updateData(sapChieu);
        adapterPhimHot.updateData(phimHotFinal);
    }

    private void thietLapTimKiem() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locPhim(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void locPhim(String query) {
        if (query.isEmpty()) {
            hienThiPhim(allPhimList);
            return;
        }

        List<Phim> filteredList = new ArrayList<>();
        for (Phim phim : allPhimList) {
            if (phim.getTen_phim().toLowerCase().contains(query.toLowerCase()) ||
                phim.getThe_loai().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(phim);
            }
        }
        hienThiPhim(filteredList);
    }

    private void thietLapAnBanPhim() {
        findViewById(R.id.man_hinh_chinh).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View view = getCurrentFocus();
                    if (view instanceof EditText) {
                        view.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });

        findViewById(R.id.scrollView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View view = getCurrentFocus();
                    if (view instanceof EditText) {
                        view.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });
    }
}
