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
import com.example.movie_booking.request.LoginRequest;
import com.example.movie_booking.response.LoginResponse;
import com.example.movie_booking.activity.TrangChuActivity;
import com.example.movie_booking.database.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private EditText edtEmail, edtPass;
    private Button btnLogin;
    private TextView txtGoToRegister, txtForgotPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        edtEmail = view.findViewById(R.id.edtEmail);
        edtPass = view.findViewById(R.id.edtPass);
        btnLogin = view.findViewById(R.id.btnLogin);
        txtGoToRegister = view.findViewById(R.id.txtGoToRegister);
        txtForgotPassword = view.findViewById(R.id.txtForgotPassword);

        // Chuyển sang màn hình Đăng ký
        txtGoToRegister.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.auth_container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Chuyển sang màn hình Quên mật khẩu
        txtForgotPassword.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.auth_container, new ForgotPasswordFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest loginRequest = new LoginRequest(email, pass);
            RetrofitClient.getApiService().login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String role = response.body().getRole();
                        SharedPrefManager.getInstance(getContext()).saveUser(response.body().getToken(), role);
                        Toast.makeText(getContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        vaoApp();
                    } else {
                        Toast.makeText(getContext(), "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e("API_ERROR", "Fail: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi kết nối Server!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        return view;
    }

    private void vaoApp() {
        if (getActivity() instanceof TrangChuActivity) {
            ((TrangChuActivity) getActivity()).vaoTrangChu();
        }
    }
}
