package com.example.movie_booking.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.movie_booking.R;
import com.example.movie_booking.api.RetrofitClient;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends Fragment {
    private EditText edtForgotEmail;
    private Button btnSendOTP;
    private TextView txtBackToLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtForgotEmail = view.findViewById(R.id.edtForgotEmail);
        btnSendOTP = view.findViewById(R.id.btnSendOTP);
        txtBackToLogin = view.findViewById(R.id.txtBackToLogin);

        txtBackToLogin.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        btnSendOTP.setOnClickListener(v -> {
            String email = edtForgotEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> map = new HashMap<>();
            map.put("email", email);

            RetrofitClient.getApiService().forgotPassword(map).enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Mã OTP đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show();
                        // Chuyển sang màn hình Reset Password
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.auth_container, ResetPasswordFragment.newInstance(email))
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    Log.e("API_ERROR", "Fail: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi kết nối Server!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
