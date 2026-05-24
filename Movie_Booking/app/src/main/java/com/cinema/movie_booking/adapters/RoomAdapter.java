package com.cinema.movie_booking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.models.CinemaGroup;

public class RoomAdapter extends ListAdapter<CinemaGroup.RoomGroup, RoomAdapter.ViewHolder> {

    private final OnShowtimeClickListener listener;

    public interface OnShowtimeClickListener {
        void onShowtimeClick(com.cinema.movie_booking.models.Showtime showtime);
    }

    public RoomAdapter(OnShowtimeClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<CinemaGroup.RoomGroup> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull CinemaGroup.RoomGroup oldItem, @NonNull CinemaGroup.RoomGroup newItem) {
            return oldItem.getRoomName().equals(newItem.getRoomName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CinemaGroup.RoomGroup oldItem, @NonNull CinemaGroup.RoomGroup newItem) {
            return oldItem.getShowtimes().size() == newItem.getShowtimes().size();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CinemaGroup.RoomGroup group = getItem(position);
        holder.roomName.setText(group.getRoomName());
        holder.showtimeAdapter.submitList(group.getShowtimes());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomName;
        RecyclerView recyclerShowtimes;
        ShowtimeAdapter showtimeAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.roomName);
            recyclerShowtimes = itemView.findViewById(R.id.recyclerShowtimes);
            
            recyclerShowtimes.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            showtimeAdapter = new ShowtimeAdapter(showtime -> {
                if (listener != null) listener.onShowtimeClick(showtime);
            });
            recyclerShowtimes.setAdapter(showtimeAdapter);
        }
    }
}
