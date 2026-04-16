package adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_booking.BookingActivity;
import com.example.movie_booking.Phim;
import com.example.movie_booking.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Phim> movieList;

    public MovieAdapter(List<Phim> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Phim movie = movieList.get(position);
        holder.tvTitle.setText(movie.getTen_phim());
        
        holder.tvRating.setText(movie.getDo_tuoi_quy_dinh());
        
        String imageName = movie.getAnh_poster();
        if (imageName != null && !imageName.isEmpty()) {
            if (imageName.contains(".")) {
                imageName = imageName.substring(0, imageName.lastIndexOf("."));
            }
            
            Context context = holder.itemView.getContext();
            int resId = context.getResources().getIdentifier(
                    imageName, "drawable", context.getPackageName());
            
            if (resId != 0) {
                holder.imgMovie.setImageResource(resId);
            } else {
                Log.e("MovieAdapter", "Could not find image: " + imageName);
                holder.imgMovie.setImageResource(R.drawable.logo_beta);
            }
        } else {
            holder.imgMovie.setImageResource(R.drawable.logo_beta);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), BookingActivity.class);
            intent.putExtra("movie_data", movie);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMovie;
        TextView tvTitle, tvRating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovie = itemView.findViewById(R.id.imgMovie);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}
