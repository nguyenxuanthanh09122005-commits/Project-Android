package com.example.movie_booking.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class HomeFragment extends Fragment {

    private RecyclerView rvDangChieu, rvSapChieu, rvPhimHot;
    private PhimAdapter adapterDangChieu, adapterSapChieu, adapterPhimHot;
    private EditText etSearch;
    private List<Phim> allPhimList = new ArrayList<>();
    private PhimViewModel phimViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        khoiTaoGiaoDien(view);
        thietLapViewModel();
        thietLapTimKiem();
        thietLapAnBanPhim(view);
        
        return view;
    }

    private void khoiTaoGiaoDien(View view) {
        rvDangChieu = view.findViewById(R.id.rvDangChieu);
        rvSapChieu = view.findViewById(R.id.rvSapChieu);
        // Lưu ý: rvPhimHot có thể không có trong fragment_home.xml mới nếu bạn quên copy, 
        // hãy đảm bảo fragment_home.xml có đầy đủ các ID này.
        rvPhimHot = view.findViewById(R.id.rvPhimHot);
        etSearch = view.findViewById(R.id.etSearch);

        adapterDangChieu = new PhimAdapter(new ArrayList<>());
        adapterSapChieu = new PhimAdapter(new ArrayList<>());
        adapterPhimHot = new PhimAdapter(new ArrayList<>());

        thietLapRecyclerView(rvDangChieu, adapterDangChieu);
        thietLapRecyclerView(rvSapChieu, adapterSapChieu);
        thietLapRecyclerView(rvPhimHot, adapterPhimHot);
    }

    private void thietLapRecyclerView(RecyclerView recyclerView, PhimAdapter adapter) {
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
        }
    }

    private void thietLapViewModel() {
        phimViewModel = new ViewModelProvider(this).get(PhimViewModel.class);
        phimViewModel.getAllPhims().observe(getViewLifecycleOwner(), phims -> {
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
                (phim.getThe_loai() != null && phim.getThe_loai().toLowerCase().contains(query.toLowerCase()))) {
                filteredList.add(phim);
            }
        }
        hienThiPhim(filteredList);
    }

    private void thietLapAnBanPhim(View view) {
        view.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                anBanPhim();
            }
            return false;
        });

        View scrollView = view.findViewById(R.id.scrollView);
        if (scrollView != null) {
            scrollView.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    anBanPhim();
                }
                return false;
            });
        }
    }

    private void anBanPhim() {
        if (getActivity() != null) {
            View focusView = getActivity().getCurrentFocus();
            if (focusView instanceof EditText) {
                focusView.clearFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                }
            }
        }
    }
}
