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
import com.cinema.movie_booking.models.Showtime;

public class ShowtimeAdapter extends ListAdapter<Showtime, ShowtimeAdapter.ViewHolder> {

    public interface OnShowtimeClickListener {
        void onShowtimeClick(Showtime showtime);
    }

    private final OnShowtimeClickListener listener;

    public ShowtimeAdapter(OnShowtimeClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Showtime> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Showtime oldItem, @NonNull Showtime newItem) {
            return oldItem.getShowtimeId().equals(newItem.getShowtimeId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Showtime oldItem, @NonNull Showtime newItem) {
            return oldItem.getStartTime().equals(newItem.getStartTime());
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
        Showtime showtime = getItem(position);
        
        String start = formatTime(showtime.getStartTime());
        String end = formatTime(showtime.getEndTime());
        holder.timeText.setText(String.format("%s - %s", start, end));
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onShowtimeClick(showtime);
        });
    }

    private String formatTime(String dateTime) {
        if (dateTime == null) return "--:--";
        if (dateTime.contains("T")) {
            String[] parts = dateTime.split("T");
            if (parts.length > 1 && parts[1].length() >= 5) return parts[1].substring(0, 5);
        } else if (dateTime.contains(" ")) {
            String[] parts = dateTime.split(" ");
            if (parts.length > 1 && parts[1].length() >= 5) return parts[1].substring(0, 5);
        }
        if (dateTime.length() >= 5) return dateTime.substring(0, 5);
        return dateTime;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.showtimeButton);
        }
    }
}
