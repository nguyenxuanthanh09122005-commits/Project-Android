package com.cinema.movie_booking.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.adapters.SeatAdapter;
import com.cinema.movie_booking.models.Seat;
import com.cinema.movie_booking.repositories.SeatRepository;
import com.cinema.movie_booking.utils.Resource;
import com.cinema.movie_booking.viewmodels.SeatViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionActivity extends AppCompatActivity {

    private Long showtimeId;
    private double basePrice;
    private String movieName;
    private String cinemaName;

    private RecyclerView recyclerSeats;
    private SeatAdapter seatAdapter;
    private SeatViewModel viewModel;
    private TextView txtSelectedSeats, txtTotalPrice;
    private MaterialButton btnConfirm;
    private ProgressBar progressBar;
    
    private List<Seat> selectedSeats = new ArrayList<>();
    private List<Seat> allSeats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        showtimeId = getIntent().getLongExtra("showtimeId", -1);
        basePrice = getIntent().getDoubleExtra("basePrice", 0);
        movieName = getIntent().getStringExtra("movieName");
        cinemaName = getIntent().getStringExtra("cinemaName");

        initViews();
        setupViewModel();
        loadData();
    }

    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(movieName);
            toolbar.setSubtitle(cinemaName);
        }

        recyclerSeats = findViewById(R.id.recyclerSeats);
        txtSelectedSeats = findViewById(R.id.txtSelectedSeats);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnConfirm = findViewById(R.id.btnConfirm);
        progressBar = findViewById(R.id.progressBar);

        seatAdapter = new SeatAdapter(this::onSeatSelected);
        recyclerSeats.setAdapter(seatAdapter);

        btnConfirm.setOnClickListener(v -> handleConfirm());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SeatViewModel.class);
        
        viewModel.getBookingSummary().observe(this, summary -> {
            txtSelectedSeats.setText(summary.seatNames);
            txtTotalPrice.setText(summary.totalPrice);
            btnConfirm.setEnabled(!selectedSeats.isEmpty());
        });

        viewModel.getBookingResult().observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    setLoading(true);
                    break;
                case SUCCESS:
                    setLoading(false);
                    Toast.makeText(this, "Đang khởi tạo thanh toán...", Toast.LENGTH_SHORT).show();
                    
                    SeatRepository.BookingSummary summary = viewModel.getBookingSummary().getValue();
                    Intent intent = new Intent(this, PaymentActivity.class);
                    intent.putExtra("movieName", movieName);
                    intent.putExtra("cinemaInfo", cinemaName);
                    intent.putExtra("seatInfo", summary != null ? summary.seatNames : "");
                    intent.putExtra("totalPrice", summary != null ? summary.totalPrice : "0đ");
                    startActivity(intent);
                    break;
                case ERROR:
                    setLoading(false);
                    Toast.makeText(this, "Lỗi đặt vé: " + resource.message, Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnConfirm.setEnabled(!isLoading && !selectedSeats.isEmpty());
    }

    private void handleConfirm() {
        SeatRepository.BookingSummary summary = viewModel.getBookingSummary().getValue();
        if (summary != null && !summary.seatIds.isEmpty()) {
            // Chuyển sang gọi createBooking thay vì lockSeats
            viewModel.createBooking(showtimeId, summary.seatIds);
        } else {
            Toast.makeText(this, "Vui lòng chọn ghế", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        if (showtimeId == null || showtimeId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin suất chiếu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel.getSeatLayout(showtimeId).observe(this, resource -> {
            if (resource == null) return;

            switch (resource.status) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.data != null && !resource.data.isEmpty()) {
                        this.allSeats = resource.data;
                        List<Seat> displaySeats = processSeats(resource.data);
                        setupGridLayout(displaySeats);
                        seatAdapter.setData(displaySeats, allSeats);
                    } else {
                        Toast.makeText(this, "Phòng chiếu này hiện chưa có sơ đồ ghế", Toast.LENGTH_LONG).show();
                    }
                    break;
                case ERROR:
                    String errorMsg = resource.message;
                    if (errorMsg != null && errorMsg.contains("500")) {
                        errorMsg = "Lỗi Server (500): Suất chiếu chưa có phòng hoặc ghế. Hãy kiểm tra lại Database Backend.";
                    }
                    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    private List<Seat> processSeats(List<Seat> rawSeats) {
        List<Seat> processed = new ArrayList<>();
        List<Long> addedPairIds = new ArrayList<>();
        for (Seat seat : rawSeats) {
            if (seat.isCouple() && seat.getPairId() != null) {
                if (!addedPairIds.contains(seat.getPairId())) {
                    processed.add(seat);
                    addedPairIds.add(seat.getPairId());
                }
            } else {
                processed.add(seat);
            }
        }
        return processed;
    }

    private void setupGridLayout(List<Seat> displaySeats) {
        int maxCol = 1;
        for (Seat s : allSeats) {
            if (s.getSeatNumber() > maxCol) maxCol = s.getSeatNumber();
        }
        
        GridLayoutManager layoutManager = new GridLayoutManager(this, maxCol);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position < displaySeats.size()) {
                    return displaySeats.get(position).isCouple() ? 2 : 1;
                }
                return 1;
            }
        });
        recyclerSeats.setLayoutManager(layoutManager);
    }

    private void onSeatSelected(Seat seat) {
        // Sử dụng isLocked() để chặn cả ghế đã đặt (BOOKED) và ghế đang bị khóa (LOCKED/locked=true)
        if (seat.isLocked()) return;

        boolean isSelected = !seat.isSelected();
        
        if (seat.isCouple() && seat.getPairId() != null) {
            // Xử lý ghế đôi: chọn/hủy cả cặp theo pairId
            for (Seat s : allSeats) {
                if (seat.getPairId().equals(s.getPairId())) {
                    s.setSelected(isSelected);
                    updateSelectionList(s, isSelected);
                }
            }
        } else {
            // Ghế thường hoặc VIP
            seat.setSelected(isSelected);
            updateSelectionList(seat, isSelected);
        }
        
        seatAdapter.notifyDataSetChanged();
        viewModel.updateSelectedSeats(selectedSeats, basePrice);
    }

    private void updateSelectionList(Seat seat, boolean isSelected) {
        if (isSelected) {
            if (!selectedSeats.contains(seat)) {
                selectedSeats.add(seat);
            }
        } else {
            selectedSeats.remove(seat);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
