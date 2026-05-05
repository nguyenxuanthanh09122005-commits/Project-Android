package com.example.movie_booking.fragment;

import android.content.Intent;
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
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.movie_booking.R;
import com.example.movie_booking.object.Phim;

public class ChiTietPhimFragment extends Fragment {

    private ImageView imgBackdrop, imgPoster, btnBack, btnPlayTrailer;
    private TextView tvMovieTitle, tvAgeRating, tvGenre, tvDuration, tvReleaseDate, tvDescription;
    private Button btnShare, btnDatVe;
    private Phim phim;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chi_tiet_phim, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            phim = (Phim) getArguments().getSerializable("phim_data");
        }

        initViews(view);

        if (phim != null) {
            displayMovieDetails();
        }

        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        btnPlayTrailer.setOnClickListener(v -> {
            if (phim.getTrailer_url() != null && !phim.getTrailer_url().isEmpty()) {
                TrailerDialogFragment.newInstance(phim.getTrailer_url())
                        .show(getChildFragmentManager(), "TrailerDialog");
            }
        });

        btnDatVe.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("movie_data", phim);
            Navigation.findNavController(v).navigate(R.id.action_chiTietPhimFragment_to_datVeFragment, bundle);
        });
    }

    private void initViews(View view) {
        imgBackdrop = view.findViewById(R.id.imgBackdrop);
        imgPoster = view.findViewById(R.id.imgPoster);
        btnBack = view.findViewById(R.id.btnBack);
        btnPlayTrailer = view.findViewById(R.id.btnPlayTrailer);
        tvMovieTitle = view.findViewById(R.id.tvMovieTitle);
        tvAgeRating = view.findViewById(R.id.tvAgeRating);
        tvGenre = view.findViewById(R.id.tvGenre);
        tvDuration = view.findViewById(R.id.tvDuration);
        tvReleaseDate = view.findViewById(R.id.tvReleaseDate);
        tvDescription = view.findViewById(R.id.tvDescription);
        btnShare = view.findViewById(R.id.btnShare);
        btnDatVe = view.findViewById(R.id.btnDatVe);
    }

    private void displayMovieDetails() {
        tvMovieTitle.setText(phim.getTen_phim());
        tvAgeRating.setText("Phân loại: " + phim.getDo_tuoi_quy_dinh());
        tvGenre.setText(phim.getThe_loai());
        tvDuration.setText(phim.getThoi_luong() + " phút");
        tvReleaseDate.setText(phim.getNgay_khoi_chieu());
        tvDescription.setText(phim.getMo_ta());

        String tenHinhAnh = phim.getAnh_poster();
        if (tenHinhAnh != null && tenHinhAnh.contains(".")) {
            tenHinhAnh = tenHinhAnh.substring(0, tenHinhAnh.lastIndexOf("."));
        }
        int resId = getResources().getIdentifier(tenHinhAnh, "drawable", requireContext().getPackageName());

        Glide.with(this).load(resId != 0 ? resId : R.drawable.logo_beta).into(imgBackdrop);
        Glide.with(this).load(resId != 0 ? resId : R.drawable.logo_beta).into(imgPoster);
    }
}
