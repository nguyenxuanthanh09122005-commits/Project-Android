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

public class MovieShowtimeCinemaAdapter extends ListAdapter<CinemaGroup, MovieShowtimeCinemaAdapter.ViewHolder> {

    private final RoomAdapter.OnShowtimeClickListener listener;

    public MovieShowtimeCinemaAdapter(RoomAdapter.OnShowtimeClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<CinemaGroup> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull CinemaGroup oldItem, @NonNull CinemaGroup newItem) {
            return oldItem.getCinemaName().equals(newItem.getCinemaName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CinemaGroup oldItem, @NonNull CinemaGroup newItem) {
            return oldItem.getRooms().size() == newItem.getRooms().size();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cinema_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CinemaGroup group = getItem(position);
        holder.cinemaName.setText(group.getCinemaName());
        holder.roomAdapter.submitList(group.getRooms());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cinemaName;
        RecyclerView roomList;
        RoomAdapter roomAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cinemaName = itemView.findViewById(R.id.cinemaName);
            roomList = itemView.findViewById(R.id.roomList);
            
            roomList.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            roomAdapter = new RoomAdapter(listener);
            roomList.setAdapter(roomAdapter);
        }
    }
}
