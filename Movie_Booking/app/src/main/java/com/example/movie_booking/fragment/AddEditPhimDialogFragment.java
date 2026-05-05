package com.example.movie_booking.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.movie_booking.R;
import com.example.movie_booking.object.Phim;
import com.example.movie_booking.viewmodel.PhimViewModel;

public class AddEditPhimDialogFragment extends DialogFragment {

    private Phim phim;
    private PhimViewModel phimViewModel;

    // Sử dụng static factory method để truyền dữ liệu
    public static AddEditPhimDialogFragment newInstance(Phim phim) {
        AddEditPhimDialogFragment fragment = new AddEditPhimDialogFragment();
        if (phim != null) {
            Bundle args = new Bundle();
            args.putSerializable("phim_data", phim);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phim = (Phim) getArguments().getSerializable("phim_data");
        }
        // Lấy ViewModel từ Activity để đồng bộ dữ liệu
        phimViewModel = new ViewModelProvider(requireActivity()).get(PhimViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_edit_phim, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitle = view.findViewById(R.id.tvDialogTitle);
        EditText etTen = view.findViewById(R.id.etTenPhim);
        EditText etTheLoai = view.findViewById(R.id.etTheLoai);
        EditText etThoiLuong = view.findViewById(R.id.etThoiLuong);
        EditText etNgay = view.findViewById(R.id.etNgayKhoiChieu);
        EditText etDoTuoi = view.findViewById(R.id.etDoTuoi);
        EditText etAnh = view.findViewById(R.id.etAnhPoster);
        EditText etTrailer = view.findViewById(R.id.etTrailerUrl);
        EditText etMoTa = view.findViewById(R.id.etMoTa);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        if (phim != null) {
            tvTitle.setText("Chỉnh sửa phim");
            etTen.setText(phim.getTen_phim());
            etTheLoai.setText(phim.getThe_loai());
            etThoiLuong.setText(String.valueOf(phim.getThoi_luong()));
            etNgay.setText(phim.getNgay_khoi_chieu());
            etDoTuoi.setText(phim.getDo_tuoi_quy_dinh());
            etAnh.setText(phim.getAnh_poster());
            etTrailer.setText(phim.getTrailer_url());
            etMoTa.setText(phim.getMo_ta());
        }

        btnCancel.setOnClickListener(v -> dismiss());

        btnSave.setOnClickListener(v -> {
            String ten = etTen.getText().toString().trim();
            if (ten.isEmpty()) {
                etTen.setError("Không được để trống");
                return;
            }

            Phim p = (phim == null) ? new Phim() : phim;
            p.setTen_phim(ten);
            p.setThe_loai(etTheLoai.getText().toString());
            p.setThoi_luong(Integer.parseInt(etThoiLuong.getText().toString().isEmpty() ? "0" : etThoiLuong.getText().toString()));
            p.setNgay_khoi_chieu(etNgay.getText().toString());
            p.setDo_tuoi_quy_dinh(etDoTuoi.getText().toString());
            p.setAnh_poster(etAnh.getText().toString());
            p.setTrailer_url(etTrailer.getText().toString());
            p.setMo_ta(etMoTa.getText().toString());

            if (phim == null) {
                phimViewModel.insert(p);
                Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
            } else {
                phimViewModel.update(p);
                Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });

        // Tự động hiện bàn phím
        etTen.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
