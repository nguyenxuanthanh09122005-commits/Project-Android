package com.cinema.movie_booking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.models.CinemaMovieResponse;

public class ShowtimeChipAdapter extends ListAdapter<CinemaMovieResponse.ShowtimeInfo, ShowtimeChipAdapter.ViewHolder> {

    public interface OnChipClickListener {
        void onChipClick(CinemaMovieResponse.ShowtimeInfo showtime);
    }

    private final OnChipClickListener listener;

    public ShowtimeChipAdapter(OnChipClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<CinemaMovieResponse.ShowtimeInfo> DIFF_CALLBACK = 
        new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull CinemaMovieResponse.ShowtimeInfo oldItem, @NonNull CinemaMovieResponse.ShowtimeInfo newItem) {
                return oldItem.getShowtimeId().equals(newItem.getShowtimeId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull CinemaMovieResponse.ShowtimeInfo oldItem, @NonNull CinemaMovieResponse.ShowtimeInfo newItem) {
                return oldItem.getStartTime().equals(newItem.getStartTime()) && 
                       oldItem.getEndTime().equals(newItem.getEndTime());
            }
        };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_chip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CinemaMovieResponse.ShowtimeInfo showtime = getItem(position);
        
        // Use substring to get HH:mm from yyyy-MM-dd'T'HH:mm
        String start = formatTime(showtime.getStartTime());
        String end = formatTime(showtime.getEndTime());
        
        holder.showtimeButton.setText(String.format("%s - %s", start, end));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onChipClick(showtime);
        });
    }

    private String formatTime(String dateTime) {
        if (dateTime == null || dateTime.length() < 16) return "--:--";
        return dateTime.substring(11, 16);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView showtimeButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            showtimeButton = itemView.findViewById(R.id.showtimeButton);
        }
    }
}
