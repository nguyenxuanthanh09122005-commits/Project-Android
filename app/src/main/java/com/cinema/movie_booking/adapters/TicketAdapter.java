package com.cinema.movie_booking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.models.BookingResponse;
import com.cinema.movie_booking.models.TicketDetailResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<BookingResponse> bookings = new ArrayList<>();

    public void setData(List<BookingResponse> data) {
        this.bookings = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        BookingResponse booking = bookings.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView txtBookingId, txtStatus, txtBookingDate, txtSeats, txtTotalAmount;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBookingId = itemView.findViewById(R.id.txtBookingId);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtBookingDate = itemView.findViewById(R.id.txtBookingDate);
            txtSeats = itemView.findViewById(R.id.txtSeats);
            txtTotalAmount = itemView.findViewById(R.id.txtTotalAmount);
        }

        public void bind(BookingResponse booking) {
            txtBookingId.setText("Mã ĐH: #" + booking.getBookingId());
            txtBookingDate.setText("Ngày đặt: " + booking.getBookingDate());
            
            DecimalFormat formatter = new DecimalFormat("#,###đ");
            txtTotalAmount.setText(formatter.format(booking.getTotalAmount()));

            StringBuilder seats = new StringBuilder("Ghế: ");
            if (booking.getTickets() != null) {
                for (int i = 0; i < booking.getTickets().size(); i++) {
                    TicketDetailResponse ticket = booking.getTickets().get(i);
                    seats.append(ticket.getRowLetter()).append(ticket.getSeatNumber());
                    if (i < booking.getTickets().size() - 1) {
                        seats.append(", ");
                    }
                }
            }
            txtSeats.setText(seats.toString());

            if ("PAID".equalsIgnoreCase(booking.getStatus()) || "SUCCESS".equalsIgnoreCase(booking.getStatus())) {
                txtStatus.setText("ĐÃ THANH TOÁN");
                txtStatus.setBackgroundResource(R.drawable.bg_status_paid);
            } else {
                txtStatus.setText(booking.getStatus());
            }
        }
    }
}
