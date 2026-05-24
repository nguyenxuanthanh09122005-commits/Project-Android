package com.cinema.movie_booking.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cinema.movie_booking.BuildConfig;
import com.cinema.movie_booking.R;
import com.cinema.movie_booking.models.Movie;
import com.cinema.movie_booking.views.activities.MovieDetailActivity;

public class MovieAdapter
        extends ListAdapter<Movie, MovieAdapter.MovieViewHolder> {

    private static final String IMAGE_URL = BuildConfig.IMAGE_URL;

    public MovieAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getMovieId().equals(newItem.getMovieId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_movie,
                        parent,
                        false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = getItem(position);

        holder.txtMovieName.setText(movie.getMovieName());
        holder.txtDuration.setText(movie.getDuration() + " phút");
        holder.txtAge.setText(movie.getAgeRating());

        Glide.with(holder.itemView.getContext())
                .load(IMAGE_URL + movie.getPosterImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imgPoster);

        // SỬA ĐOẠN CLICK TẠI ĐÂY
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, MovieDetailActivity.class);

            intent.putExtra("movieId", movie.getMovieId());
            intent.putExtra("movieName", movie.getMovieName());

            // 1. Đọc dữ liệu thành phố người dùng đã chọn từ SharedPreferences
            // (CinemaPrefs là tên file xml cấu hình lưu dữ liệu từ CinemaFragment bước trước)
            android.content.SharedPreferences prefs = context.getSharedPreferences(
                    "CinemaPrefs",
                    Context.MODE_PRIVATE
            );
            String selectedCity = prefs.getString("selected_city", "Hanoi"); // Mặc định là Hanoi nếu kho trống

            // 2. Đính kèm tham số thành phố vào intent gửi đi
            intent.putExtra("city", selectedCity);

            context.startActivity(intent);
        });
    }

    public static class MovieViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgPoster;

        TextView txtMovieName,
                txtDuration,
                txtAge;

        public MovieViewHolder(
                @NonNull View itemView) {

            super(itemView);

            imgPoster =
                    itemView.findViewById(R.id.imgPoster);

            txtMovieName =
                    itemView.findViewById(R.id.txtMovieName);

            txtDuration =
                    itemView.findViewById(R.id.txtDuration);

            txtAge =
                    itemView.findViewById(R.id.txtAge);
        }
    }
}
