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
import com.cinema.movie_booking.models.CinemaResponse;

public class CinemaAdapter extends ListAdapter<CinemaResponse, CinemaAdapter.ViewHolder> {

    public interface OnCinemaClickListener {
        void onCinemaClick(CinemaResponse cinema);
    }

    private final OnCinemaClickListener listener;

    public CinemaAdapter(OnCinemaClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<CinemaResponse> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull CinemaResponse oldItem, @NonNull CinemaResponse newItem) {
            return oldItem.getCinemaId().equals(newItem.getCinemaId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CinemaResponse oldItem, @NonNull CinemaResponse newItem) {
            return oldItem.getCinemaName().equals(newItem.getCinemaName()) &&
                   oldItem.getAddress().equals(newItem.getAddress());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cinema, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CinemaResponse cinema = getItem(position);
        holder.txtCinemaName.setText(cinema.getCinemaName());
        holder.txtCinemaAddress.setText(cinema.getAddress());
        holder.itemView.setOnClickListener(v -> listener.onCinemaClick(cinema));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCinemaName, txtCinemaAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCinemaName = itemView.findViewById(R.id.txtCinemaName);
            txtCinemaAddress = itemView.findViewById(R.id.txtCinemaAddress);
        }
    }
}
