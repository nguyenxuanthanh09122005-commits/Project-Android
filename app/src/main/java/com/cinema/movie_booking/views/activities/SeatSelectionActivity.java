package com.cinema.movie_booking.views.activities;

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
import com.cinema.movie_booking.models.SeatLayoutResponse;
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

        seatAdapter = new SeatAdapter(this::onSeatSelected);
        recyclerSeats.setAdapter(seatAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SeatViewModel.class);
        
        viewModel.getBookingSummary().observe(this, summary -> {
            txtSelectedSeats.setText(summary.seatNames);
            txtTotalPrice.setText(summary.totalPrice);
        });
    }

    private void loadData() {
        viewModel.getSeatLayout(showtimeId).observe(this, resource -> {
            if (resource.status == Resource.Status.SUCCESS && resource.data != null) {
                this.allSeats = resource.data.getRawSeats();
                setupGridLayout(resource.data);
                seatAdapter.setData(resource.data.getSeats(), allSeats);
            } else if (resource.status == Resource.Status.ERROR) {
                Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupGridLayout(SeatLayoutResponse data) {
        int columns = data.getTotalColumns();
        GridLayoutManager layoutManager = new GridLayoutManager(this, columns);
        
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Seat seat = data.getSeats().get(position);
                return seat.isCouple() ? 2 : 1;
            }
        });
        
        recyclerSeats.setLayoutManager(layoutManager);
    }

    private void onSeatSelected(Seat seat) {
        if ("BOOKED".equalsIgnoreCase(seat.getStatus())) return;

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
