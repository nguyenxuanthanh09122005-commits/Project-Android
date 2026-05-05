package com.example.movie_booking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.movie_booking.R;

public class ManagementFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout btnManageMovies = view.findViewById(R.id.btnManageMovies);
        LinearLayout btnManageTickets = view.findViewById(R.id.btnManageTickets);
        LinearLayout btnManageShowtimes = view.findViewById(R.id.btnManageShowtimes);

        btnManageMovies.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_managementFragment_to_quanLyPhimFragment);
        });

        btnManageTickets.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chức năng Quản lý Vé đang phát triển", Toast.LENGTH_SHORT).show();
        });

        btnManageShowtimes.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chức năng Quản lý Suất Chiếu đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }
}
