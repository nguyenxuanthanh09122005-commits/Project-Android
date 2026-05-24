package com.cinema.movie_booking.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.adapters.TicketAdapter;
import com.cinema.movie_booking.api.RetrofitClient;
import com.cinema.movie_booking.models.BookingResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTicketFragment extends Fragment {

    private RecyclerView recyclerTickets;
    private TicketAdapter ticketAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private View layoutEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_ticket, container, false);
        
        initViews(view);
        loadTickets();
        
        return view;
    }

    private void initViews(View view) {
        recyclerTickets = view.findViewById(R.id.recyclerTickets);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        progressBar = view.findViewById(R.id.progressBar);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);

        recyclerTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        ticketAdapter = new TicketAdapter();
        recyclerTickets.setAdapter(ticketAdapter);

        swipeRefresh.setOnRefreshListener(this::loadTickets);
    }

    private void loadTickets() {
        if (!swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }
        layoutEmpty.setVisibility(View.GONE);

        RetrofitClient.getApiService().getMyBookings().enqueue(new Callback<List<BookingResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<BookingResponse>> call, @NonNull Response<List<BookingResponse>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<BookingResponse> bookings = response.body();
                    if (bookings.isEmpty()) {
                        layoutEmpty.setVisibility(View.VISIBLE);
                    } else {
                        ticketAdapter.setData(bookings);
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách vé", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BookingResponse>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
