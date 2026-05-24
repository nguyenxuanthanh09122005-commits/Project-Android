package com.cinema.movie_booking.views.fragments;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.views.activities.LoginActivity;

public class AccountFragment extends Fragment {

    private TextView txtUserName, txtUserEmail;
    private Button btnLogout, btnGoToLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        checkLoginStatus();
    }

    private void initViews(View view) {
        txtUserName = view.findViewById(R.id.txtUserName);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnGoToLogin = view.findViewById(R.id.btnGoToLogin);

        btnLogout.setOnClickListener(v -> logout());
        btnGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
        });
    }

    private void checkLoginStatus() {
        SharedPreferences authPrefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        String token = authPrefs.getString("auth_token", null);
        String email = authPrefs.getString("user_email", "");

        SharedPreferences userPrefs = requireContext().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        String fullName = userPrefs.getString("FULL_NAME", "");

        boolean isLoggedIn = token != null && !token.isEmpty();

        if (isLoggedIn) {
            txtUserName.setText(fullName.isEmpty() ? "Người dùng" : fullName);
            txtUserEmail.setText(email);
            btnLogout.setVisibility(View.VISIBLE);
            btnGoToLogin.setVisibility(View.GONE);
        } else {
            txtUserName.setText("Khách");
            txtUserEmail.setText("Bạn chưa đăng nhập");
            btnLogout.setVisibility(View.GONE);
            btnGoToLogin.setVisibility(View.VISIBLE);
        }
    }

    private void logout() {
        // Xóa thông tin auth
        requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                .edit().clear().apply();
        
        // Xóa thông tin user profile
        requireContext().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE)
                .edit().clear().apply();

        // Cập nhật giao diện
        checkLoginStatus();
        
        // Có thể quay lại màn hình Home
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLoginStatus();
    }
}
