package com.example.movie_booking.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_booking.R;
import com.example.movie_booking.dao.SuatChieuDao;
import com.example.movie_booking.object.Phim;
import com.example.movie_booking.viewmodel.RapViewModel;
import com.example.movie_booking.viewmodel.SuatChieuViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import adapter.NgayAdapter;
import adapter.RapAdapter;

public class DatVeFragment extends Fragment {

    private ImageView imgBanner, btnQuayLai;
    private TextView tvTenPhim, tvThongTinPhim, tvChonKhuVuc, tvChiTietPhim;
    private RecyclerView rvLichChieu, rvNgay;

    private RapAdapter rapAdapter;
    private NgayAdapter ngayAdapter;
    private List<SuatChieuDao.SuatChieuWithTheater> allSuatChieu = new ArrayList<>();
    private String selectedThanhPho = "Tất cả";
    private String selectedFullDate = "";
    private Phim currentPhim;

    private RapViewModel rapViewModel;
    private SuatChieuViewModel suatChieuViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dat_ve, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            currentPhim = (Phim) getArguments().getSerializable("movie_data");
        }

        khoiTaoGiaoDien(view);
        thietLapViewModel();

        if (currentPhim != null) {
            hienThiDuLieuPhim(currentPhim);
            taiTatCaSuatChieu(currentPhim.getId_phim());
        }

        btnQuayLai.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        tvChonKhuVuc.setOnClickListener(v -> hienThiDialogChonKhuVuc());

        tvChiTietPhim.setOnClickListener(v -> {
            if (currentPhim != null) {
                // Chuyển sang ChiTietPhimFragment bằng Navigation Component
                Bundle bundle = new Bundle();
                bundle.putSerializable("phim_data", currentPhim);
                Navigation.findNavController(v).navigate(R.id.action_datVeFragment_to_chiTietPhimFragment, bundle);
            }
        });
    }

    private void khoiTaoGiaoDien(View view) {
        imgBanner = view.findViewById(R.id.imgBanner);
        btnQuayLai = view.findViewById(R.id.btnQuayLai);
        tvTenPhim = view.findViewById(R.id.tvTenPhim);
        tvThongTinPhim = view.findViewById(R.id.tvThongTinPhim);
        tvChonKhuVuc = view.findViewById(R.id.tvChonKhuVuc);
        tvChiTietPhim = view.findViewById(R.id.tvChiTietPhim);
        rvLichChieu = view.findViewById(R.id.rvLichChieu);
        rvNgay = view.findViewById(R.id.rvNgay);

        rvLichChieu.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNgay.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void thietLapViewModel() {
        rapViewModel = new ViewModelProvider(this).get(RapViewModel.class);
        suatChieuViewModel = new ViewModelProvider(this).get(SuatChieuViewModel.class);
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
            int resId = getResources().getIdentifier(tenHinhAnh, "drawable", requireContext().getPackageName());
            if (resId != 0) {
                imgBanner.setImageResource(resId);
            }
        }
    }

    private void taiTatCaSuatChieu(int idPhim) {
        suatChieuViewModel.getSuatChieuWithTheaterByPhim(idPhim).observe(getViewLifecycleOwner(), rawSuatChieu -> {
            if (rawSuatChieu == null || rawSuatChieu.isEmpty()) {
                rvNgay.setAdapter(null);
                rvLichChieu.setAdapter(null);
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
                    Log.e("DatVeFragment", "Lỗi định dạng ngày giờ", e);
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
                        String thu = sdfThu.format(date);
                        String ngay = sdfDay.format(date);
                        String thang = sdfMonth.format(date);
                        mapNgay.put(dateStr, new NgayAdapter.NgayItem(thu, ngay, thang, dateStr));
                    }
                } catch (Exception ignored) {}
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
        });
    }

    private void hienThiDialogChonKhuVuc() {
        rapViewModel.getAllThanhPho().observe(getViewLifecycleOwner(), cities -> {
            if (cities == null) return;
            List<String> listThanhPho = new ArrayList<>(cities);
            listThanhPho.add(0, "Tất cả");

            String[] arrays = listThanhPho.toArray(new String[0]);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Chọn thành phố");
            builder.setItems(arrays, (dialog, which) -> {
                selectedThanhPho = arrays[which];
                tvChonKhuVuc.setText(selectedThanhPho + " >");
                locLichChieuTheoDieuKien();
            });
            builder.show();
        });
    }

    private void locLichChieuTheoDieuKien() {
        if (allSuatChieu == null || allSuatChieu.isEmpty()) return;

        Map<String, List<SuatChieuDao.SuatChieuWithTheater>> lichChieuTheoRap = new HashMap<>();

        for (SuatChieuDao.SuatChieuWithTheater item : allSuatChieu) {
            boolean matchesDate = item.thoi_gian_bat_dau.startsWith(selectedFullDate);
            if (matchesDate) {
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
