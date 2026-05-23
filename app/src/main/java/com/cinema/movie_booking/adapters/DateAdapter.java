package com.cinema.movie_booking.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cinema.movie_booking.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateAdapter extends ListAdapter<Calendar, DateAdapter.ViewHolder> {

    private int selectedPosition = 0;
    private final OnDateClickListener listener;
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", new Locale("vi", "VN"));
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
    private final SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public interface OnDateClickListener {
        void onDateClick(String dateQuery);
    }

    public DateAdapter(OnDateClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Calendar> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Calendar oldItem, @NonNull Calendar newItem) {
            return oldItem.getTimeInMillis() == newItem.getTimeInMillis();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Calendar oldItem, @NonNull Calendar newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Calendar calendar = getItem(position);
        holder.txtDayName.setText(dayFormat.format(calendar.getTime()));
        holder.txtDate.setText(dateFormat.format(calendar.getTime()));

        if (selectedPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.bg_date);
            holder.txtDayName.setTextColor(Color.WHITE);
            holder.txtDate.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            holder.txtDayName.setTextColor(Color.BLACK);
            holder.txtDate.setTextColor(Color.GRAY);
        }

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            int previous = selectedPosition;
            selectedPosition = pos;
            notifyItemChanged(previous);
            notifyItemChanged(selectedPosition);
            if (listener != null) {
                listener.onDateClick(apiFormat.format(calendar.getTime()));
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDayName, txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDayName = itemView.findViewById(R.id.day);
            txtDate = itemView.findViewById(R.id.date);
        }
    }
}
