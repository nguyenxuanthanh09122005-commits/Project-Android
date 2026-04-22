package com.example.movie_booking.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_booking.R;
import com.example.movie_booking.object.Phim;
import com.example.movie_booking.viewmodel.PhimViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import adapter.PhimAdminAdapter;

public class QuanLyPhimActivity extends AppCompatActivity {

    private RecyclerView rvDanhSachPhim;
    private FloatingActionButton fabAddPhim;
    private PhimViewModel phimViewModel;
    private PhimAdminAdapter adapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_phim);

        rvDanhSachPhim = findViewById(R.id.rvDanhSachPhim);
        fabAddPhim = findViewById(R.id.fabAddPhim);
        rvDanhSachPhim.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PhimAdminAdapter(new ArrayList<>(), new PhimAdminAdapter.OnPhimClickListener() {
            @Override
            public void onEdit(Phim phim) {
                showAddEditDialog(phim);
            }

            @Override
            public void onDelete(Phim phim) {
                confirmDelete(phim);
            }
        });
        rvDanhSachPhim.setAdapter(adapter);

        phimViewModel = new ViewModelProvider(this).get(PhimViewModel.class);
        phimViewModel.getAllPhims().observe(this, phims -> {
            if (phims != null) {
                adapter.updateData(phims);
            }
        });

        fabAddPhim.setOnClickListener(v -> showAddEditDialog(null));
    }

    private void showAddEditDialog(Phim phim) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_edit_phim);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvTitle = dialog.findViewById(R.id.tvDialogTitle);
        EditText etTen = dialog.findViewById(R.id.etTenPhim);
        EditText etTheLoai = dialog.findViewById(R.id.etTheLoai);
        EditText etThoiLuong = dialog.findViewById(R.id.etThoiLuong);
        EditText etNgay = dialog.findViewById(R.id.etNgayKhoiChieu);
        EditText etDoTuoi = dialog.findViewById(R.id.etDoTuoi);
        EditText etAnh = dialog.findViewById(R.id.etAnhPoster);
        EditText etTrailer = dialog.findViewById(R.id.etTrailerUrl);
        EditText etMoTa = dialog.findViewById(R.id.etMoTa);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

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

        btnCancel.setOnClickListener(v -> dialog.dismiss());

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
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            } else {
                phimViewModel.update(p);
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
            
            dialog.dismiss();
        });

        dialog.show();
        
        // Tự động hiện bàn phím và focus
        etTen.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void confirmDelete(Phim phim) {
        executorService.execute(() -> {
            int count = phimViewModel.getFutureShowtimesCount(phim.getId_phim());
            
            runOnUiThread(() -> {
                if (count > 0) {
                    new AlertDialog.Builder(this)
                            .setTitle("Không thể xóa")
                            .setMessage("Phim này đang có " + count + " suất chiếu sắp tới. Vui lòng xóa suất chiếu trước.")
                            .setPositiveButton("Đã hiểu", null)
                            .show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa phim " + phim.getTen_phim() + "?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                phimViewModel.delete(phim);
                                Toast.makeText(this, "Đã xóa phim", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
