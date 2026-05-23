package com.cinema.movie_booking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cinema.movie_booking.BuildConfig;
import com.cinema.movie_booking.R;
import com.cinema.movie_booking.models.CinemaMovieResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CinemaMovieAdapter extends ListAdapter<CinemaMovieResponse, CinemaMovieAdapter.ViewHolder> {

    private OnShowtimeClickListener listener;
    private static final String IMAGE_URL = BuildConfig.IMAGE_URL;
    private String selectedDate = "";

    private SimpleDateFormat getDateOnlyFormat() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    private SimpleDateFormat getDateTimeFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
    }
    public interface OnShowtimeClickListener {
        void onShowtimeClick(CinemaMovieResponse movie, CinemaMovieResponse.ShowtimeInfo showtime);
    }

    public CinemaMovieAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<CinemaMovieResponse> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull CinemaMovieResponse oldItem, @NonNull CinemaMovieResponse newItem) {
            return oldItem.getMovieId().equals(newItem.getMovieId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CinemaMovieResponse oldItem, @NonNull CinemaMovieResponse newItem) {
            return oldItem.getMovieName().equals(newItem.getMovieName()) &&
                   oldItem.getRooms().size() == newItem.getRooms().size();
        }
    };

    public void setOnShowtimeClickListener(OnShowtimeClickListener listener) {
        this.listener = listener;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cinema_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView txtMovieName, txtDuration, txtAge;
        RecyclerView recyclerRooms;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            txtMovieName = itemView.findViewById(R.id.txtMovieName);
            txtDuration = itemView.findViewById(R.id.txtDuration);
            txtAge = itemView.findViewById(R.id.txtAge);
            recyclerRooms = itemView.findViewById(R.id.recyclerRooms);

            recyclerRooms.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }

        void bind(CinemaMovieResponse movie) {
            txtMovieName.setText(movie.getMovieName());
            txtDuration.setText(movie.getDuration() + " phút");
            txtAge.setText(movie.getAgeRating());

            Glide.with(itemView.getContext())
                    .load(IMAGE_URL + movie.getPosterImage())
                    .placeholder(R.drawable.placeholder)
                    .into(imgPoster);

            CinemaMovieRoomAdapter roomAdapter = new CinemaMovieRoomAdapter(selectedDate, (room, showtime) -> {
                if (listener != null) {
                    listener.onShowtimeClick(movie, showtime);
                }
            });
            recyclerRooms.setAdapter(roomAdapter);
            roomAdapter.submitList(movie.getRooms());
        }
    }
}
