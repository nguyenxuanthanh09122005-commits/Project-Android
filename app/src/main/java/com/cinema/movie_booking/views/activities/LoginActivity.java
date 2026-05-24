package com.cinema.movie_booking.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.api.RetrofitClient;
import com.cinema.movie_booking.models.LoginRequest;
import com.cinema.movie_booking.models.LoginResponse;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            performLogin(email, password);
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);
    }

    private void performLogin(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        LoginRequest loginRequest = new LoginRequest(email, password);
        RetrofitClient.getApiService().login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    // Lưu token và thông tin user vào SharedPreferences
                    LoginResponse loginResponse = response.body();
                    
                    // Lưu vào auth_prefs (thường dùng cho Retrofit Interceptor)
                    getSharedPreferences("auth_prefs", MODE_PRIVATE)
                            .edit()
                            .putString("auth_token", loginResponse.getToken())
                            .putString("user_email", loginResponse.getEmail())
                            .apply();

                    // Lưu vào USER_FILE (thường dùng cho UI như HomeFragment)
                    getSharedPreferences("USER_FILE", MODE_PRIVATE)
                            .edit()
                            .putString("FULL_NAME", loginResponse.getEmail()) // Tạm thời dùng email nếu server không trả về name
                            .apply();

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    
                    // Nếu LoginActivity được mở từ màn hình khác (startActivity), 
                    // chỉ cần finish() để quay lại màn hình đó.
                    // Nếu là màn hình khởi đầu (không còn activity nào phía dưới), mới mở MainActivity.
                    if (isTaskRoot()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
