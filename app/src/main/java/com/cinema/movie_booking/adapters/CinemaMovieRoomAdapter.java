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
import com.cinema.movie_booking.models.CinemaMovieResponse;

import java.util.List;

public class CinemaMovieRoomAdapter extends ListAdapter<CinemaMovieResponse.Room, CinemaMovieRoomAdapter.ViewHolder> {

    private final OnShowtimeClickListener listener;
    private final String selectedDate;

    public interface OnShowtimeClickListener {
        void onShowtimeClick(CinemaMovieResponse.Room room, CinemaMovieResponse.ShowtimeInfo showtime);
    }

    public CinemaMovieRoomAdapter(String selectedDate, OnShowtimeClickListener listener) {
        super(DIFF_CALLBACK);
        this.selectedDate = selectedDate;
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<CinemaMovieResponse.Room> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull CinemaMovieResponse.Room oldItem, @NonNull CinemaMovieResponse.Room newItem) {
                    return oldItem.getRoomId().equals(newItem.getRoomId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull CinemaMovieResponse.Room oldItem, @NonNull CinemaMovieResponse.Room newItem) {
                    return oldItem.getRoomName().equals(newItem.getRoomName()) &&
                            oldItem.getShowtimes().size() == newItem.getShowtimes().size();
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
        CinemaMovieResponse.Room room = getItem(position);
        holder.roomName.setText(room.getRoomName());

        // Lọc suất chiếu của phòng này theo ngày đã chọn
        java.util.List<CinemaMovieResponse.ShowtimeInfo> filtered = new java.util.ArrayList<>();
        if (room.getShowtimes() != null) {
            for (CinemaMovieResponse.ShowtimeInfo info : room.getShowtimes()) {
                if (info.getStartTime() != null && info.getStartTime().startsWith(selectedDate)) {
                    filtered.add(info);
                }
            }
        }

        holder.chipAdapter.submitList(filtered);
        // Ẩn phòng nếu không có suất chiếu nào trong ngày đã chọn
        holder.itemView.setVisibility(filtered.isEmpty() ? View.GONE : View.VISIBLE);
        holder.itemView.setLayoutParams(filtered.isEmpty() ?
                new RecyclerView.LayoutParams(0, 0) :
                new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomName;
        RecyclerView recyclerShowtimes;
        ShowtimeChipAdapter chipAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.roomName);
            recyclerShowtimes = itemView.findViewById(R.id.recyclerShowtimes);

            recyclerShowtimes.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            chipAdapter = new ShowtimeChipAdapter(showtime -> {
                if (listener != null) {
                    listener.onShowtimeClick(getItem(getBindingAdapterPosition()), showtime);
                }
            });
            recyclerShowtimes.setAdapter(chipAdapter);
        }
    }
}
