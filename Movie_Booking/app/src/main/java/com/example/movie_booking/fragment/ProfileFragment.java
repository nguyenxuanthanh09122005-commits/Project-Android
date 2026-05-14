package com.example.movie_booking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.movie_booking.R;
import com.example.movie_booking.activity.TrangChuActivity;
import com.example.movie_booking.database.SharedPrefManager;

public class ProfileFragment extends Fragment {

    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            // Xóa phiên đăng nhập
            SharedPrefManager.getInstance(getContext()).logout();
            
            // Quay lại màn hình đăng nhập
            if (getActivity() instanceof TrangChuActivity) {
                ((TrangChuActivity) getActivity()).hienThiManHinhAuth();
            }
        });

        return view;
    }
}
