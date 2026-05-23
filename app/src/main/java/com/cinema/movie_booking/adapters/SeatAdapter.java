package com.cinema.movie_booking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.models.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private List<Seat> seats = new ArrayList<>();
    private List<Seat> allSeats = new ArrayList<>();
    private OnSeatClickListener listener;

    public interface OnSeatClickListener {
        void onSeatClick(Seat seat);
    }

    public SeatAdapter(OnSeatClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Seat> displaySeats, List<Seat> allSeats) {
        this.seats = displaySeats;
        this.allSeats = allSeats;
        notifyDataSetChanged();
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seats.get(position);
        holder.bind(seat);
    }

    @Override
    public int getItemCount() {
        return seats.size();
    }

    class SeatViewHolder extends RecyclerView.ViewHolder {
        View viewSeat;
        TextView txtSeatName;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            viewSeat = itemView.findViewById(R.id.viewSeat);
            txtSeatName = itemView.findViewById(R.id.txtSeatName);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onSeatClick(seats.get(position));
                }
            });
        }

        public void bind(Seat seat) {
            if (seat.isCouple() && seat.getPairId() != null) {
                String partnerName = "";
                for (Seat s : allSeats) {
                    if (s != seat && seat.getPairId().equals(s.getPairId())) {
                        partnerName = s.getSeatName();
                        break;
                    }
                }
                txtSeatName.setText(seat.getSeatName() + "-" + partnerName);
            } else {
                txtSeatName.setText(seat.getSeatName());
            }

            if ("BOOKED".equalsIgnoreCase(seat.getStatus())) {
                viewSeat.setBackgroundResource(R.drawable.bg_seat_booked);
                itemView.setEnabled(false);
                txtSeatName.setTextColor(0xFFFFFFFF);
            } else if (seat.isSelected()) {
                viewSeat.setBackgroundResource(R.drawable.bg_seat_selected);
                itemView.setEnabled(true);
                txtSeatName.setTextColor(0xFFFFFFFF);
            } else {
                itemView.setEnabled(true);
                txtSeatName.setTextColor(0xFF757575);
                if (seat.isCouple()) {
                    viewSeat.setBackgroundResource(R.drawable.bg_seat_couple);
                } else if ("VIP".equalsIgnoreCase(seat.getSeatType())) {
                    viewSeat.setBackgroundResource(R.drawable.bg_seat_vip);
                } else {
                    viewSeat.setBackgroundResource(R.drawable.bg_seat_available);
                }
            }
        }
    }
}
