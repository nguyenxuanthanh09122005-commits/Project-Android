package com.example.movie_booking.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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

public class ChonGheFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chon_ghe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            suatChieu = (SuatChieu) getArguments().getSerializable("suat_chieu_data");
        }

        khoiTaoGiaoDien(view);
        thietLapViewModel();

        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        btnTiepTuc.setOnClickListener(v -> {
            if (gheAdapter == null) return;
            List<Ghe> selectedGhes = gheAdapter.getSelectedGhes();
            if (selectedGhes.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng chọn ghế!", Toast.LENGTH_SHORT).show();
            } else {
                checkAgeAndProceed(selectedGhes, v);
            }
        });
    }

    private void khoiTaoGiaoDien(View view) {
        rvGhe = view.findViewById(R.id.rvGhe);
        tvGheDaChon = view.findViewById(R.id.tvGheDaChon);
        tvTongTien = view.findViewById(R.id.tvTongTien);
        btnTiepTuc = view.findViewById(R.id.btnTiepTuc);
        btnBack = view.findViewById(R.id.btnBack);

        rvGhe.setLayoutManager(new GridLayoutManager(getContext(), 8));
    }

    private void thietLapViewModel() {
        gheViewModel = new ViewModelProvider(this).get(GheViewModel.class);
        phimViewModel = new ViewModelProvider(this).get(PhimViewModel.class);

        if (suatChieu != null) {
            phimViewModel.getAllPhims().observe(getViewLifecycleOwner(), phims -> {
                if (phims != null) {
                    for (com.example.movie_booking.object.Phim p : phims) {
                        if (p.getId_phim() != null && p.getId_phim() == suatChieu.getId_phim()) {
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
        gheViewModel.getGheByPhong(suatChieu.getId_phong()).observe(getViewLifecycleOwner(), tatCaGhe -> {
            if (tatCaGhe != null) {
                gheViewModel.getGheDaDat(suatChieu.getId_suat_chieu()).observe(getViewLifecycleOwner(), gheDaDat -> {
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

    private void checkAgeAndProceed(List<Ghe> selectedGhes, View view) {
        if (doTuoiQuyDinh == null || doTuoiQuyDinh.equalsIgnoreCase("P")) {
            navigateToPayment(selectedGhes, view);
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
            navigateToPayment(selectedGhes, view);
            return;
        }

        Calendar calendar = Calendar.getInstance();
        int finalRequiredAge = requiredAge;

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                AlertDialog.THEME_HOLO_LIGHT,
                (v, year, month, dayOfMonth) -> {
                    Calendar birthDate = Calendar.getInstance();
                    birthDate.set(year, month, dayOfMonth);

                    int age = calendar.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
                    if (calendar.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                        age--;
                    }

                    if (age >= finalRequiredAge) {
                        navigateToPayment(selectedGhes, view);
                    } else {
                        Toast.makeText(getContext(), "Bạn chưa đủ " + finalRequiredAge + " tuổi để xem phim này!", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(view).popBackStack(R.id.homeFragment, false);
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

    private void navigateToPayment(List<Ghe> selectedGhes, View view) {
        Bundle bundle = new Bundle();
        bundle.putFloat("tong_tien", (float) currentTongTien);
        bundle.putSerializable("suat_chieu_data", suatChieu);
        bundle.putSerializable("selected_ghes", selectedGhes.toArray(new Ghe[0]));
        Navigation.findNavController(view).navigate(R.id.action_chonGheFragment_to_thanhToanFragment, bundle);
    }
}
