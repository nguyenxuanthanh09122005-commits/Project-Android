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
import com.example.movie_booking.request.RegisterRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    private EditText edtName, edtEmail, edtPass, edtPhone;
    private Button btnRegister;
    private TextView txtBackToLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtName = view.findViewById(R.id.edtRegName);
        edtEmail = view.findViewById(R.id.edtRegEmail);
        edtPass = view.findViewById(R.id.edtRegPass);
        edtPhone = view.findViewById(R.id.edtRegPhone);
        btnRegister = view.findViewById(R.id.btnRegister);
        txtBackToLogin = view.findViewById(R.id.txtBackToLogin);

        txtBackToLogin.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        btnRegister.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Tạo request để gửi lên Server
            RegisterRequest request = new RegisterRequest(name, email, pass, phone);

            // 2. Gửi dữ liệu qua API thay vì Room DB
            RetrofitClient.getApiService().register(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Đăng ký Server thành công!", Toast.LENGTH_SHORT).show();
                        // Quay lại màn hình đăng nhập
                        getParentFragmentManager().popBackStack();
                    } else {
                        // Server trả về lỗi (Ví dụ: Email đã tồn tại trên Server)
                        Log.e("API_ERROR", "Code: " + response.code());
                        Toast.makeText(getContext(), "Đăng ký thất bại: Email đã tồn tại trên Server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Lỗi kết nối (Server chưa bật hoặc sai IP)
                    Log.e("API_ERROR", "Fail: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi kết nối Server! Hãy kiểm tra Node.js đã bật chưa.", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
