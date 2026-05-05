package com.example.movie_booking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.movie_booking.R;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Manage Movies Click
        View lnQuanLyPhim = view.findViewById(R.id.lnQuanLyPhim);
        if (lnQuanLyPhim != null) {
            lnQuanLyPhim.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_quanLyPhimFragment);
            });
        }

        // Manage Showtimes Click
        View lnQuanLySuatChieu = view.findViewById(R.id.lnQuanLySuatChieu);
        if (lnQuanLySuatChieu != null) {
            lnQuanLySuatChieu.setOnClickListener(v -> {
                // Navigate to showtime management when ready
            });
        }

        // Logout Click
        View btnLogOut = view.findViewById(R.id.btnLogOut);
        if (btnLogOut != null) {
            btnLogOut.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            });
        }
    }
}
