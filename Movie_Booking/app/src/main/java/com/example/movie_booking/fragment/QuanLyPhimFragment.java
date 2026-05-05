package com.example.movie_booking.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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

public class QuanLyPhimFragment extends Fragment {

    private RecyclerView rvDanhSachPhim;
    private FloatingActionButton fabAddPhim;
    private PhimViewModel phimViewModel;
    private PhimAdminAdapter adapter;
    private Toolbar toolbar;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quan_ly_phim, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        rvDanhSachPhim = view.findViewById(R.id.rvDanhSachPhim);
        fabAddPhim = view.findViewById(R.id.fabAddPhim);
        rvDanhSachPhim.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PhimAdminAdapter(new ArrayList<>(), new PhimAdminAdapter.OnPhimClickListener() {
            @Override
            public void onEdit(Phim phim) {
                showAddEditDialogFragment(phim);
            }

            @Override
            public void onDelete(Phim phim) {
                confirmDelete(phim);
            }
        });
        rvDanhSachPhim.setAdapter(adapter);

        phimViewModel = new ViewModelProvider(this).get(PhimViewModel.class);
        phimViewModel.getAllPhims().observe(getViewLifecycleOwner(), phims -> {
            if (phims != null) {
                adapter.updateData(phims);
            }
        });

        fabAddPhim.setOnClickListener(v -> showAddEditDialogFragment(null));
    }

    private void showAddEditDialogFragment(Phim phim) {
        AddEditPhimDialogFragment dialogFragment = AddEditPhimDialogFragment.newInstance(phim);
        dialogFragment.show(getChildFragmentManager(), "AddEditPhimDialog");
    }

    private void confirmDelete(Phim phim) {
        executorService.execute(() -> {
            int count = phimViewModel.getFutureShowtimesCount(phim.getId_phim());
            
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (count > 0) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Không thể xóa")
                                .setMessage("Phim này đang có " + count + " suất chiếu sắp tới. Vui lòng xóa suất chiếu trước.")
                                .setPositiveButton("Đã hiểu", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Xác nhận xóa")
                                .setMessage("Bạn có chắc chắn muốn xóa phim " + phim.getTen_phim() + "?")
                                .setPositiveButton("Xóa", (dialog, which) -> {
                                    phimViewModel.delete(phim);
                                    Toast.makeText(getContext(), "Đã xóa phim", Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("Hủy", null)
                                .show();
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        executorService.shutdown();
    }
}
