package com.cinema.movie_booking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cinema.movie_booking.R;
import com.cinema.movie_booking.models.Movie;

import java.util.List;

public class MovieAdapter
        extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private final Context context;

    private final List<Movie> movieList;

    private static final String IMAGE_URL =
            "http://192.168.1.20:8080/uploads/";

    public MovieAdapter(
            Context context,
            List<Movie> movieList) {

        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(context)
                .inflate(
                        R.layout.item_movie,
                        parent,
                        false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MovieViewHolder holder,
            int position) {

        Movie movie = movieList.get(position);

        holder.txtMovieName.setText(
                movie.getMovieName());

        holder.txtDuration.setText(
                movie.getDuration() + " phút");

        holder.txtAge.setText(
                movie.getAgeRating());

        Glide.with(context)
                .load(IMAGE_URL + movie.getPosterImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imgPoster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
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