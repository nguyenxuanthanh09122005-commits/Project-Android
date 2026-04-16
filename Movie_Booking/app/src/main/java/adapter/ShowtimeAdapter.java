package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_booking.R;
import com.example.movie_booking.SuatChieu;

import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {
    private List<SuatChieu> showtimeList;

    public ShowtimeAdapter(List<SuatChieu> showtimeList) {
        this.showtimeList = showtimeList;
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime, parent, false);
        return new ShowtimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        SuatChieu showtime = showtimeList.get(position);
        holder.tvTime.setText(showtime.getThoi_gian_bat_dau());
        
        holder.itemView.setOnClickListener(v -> {
            // Xử lý khi chọn suất chiếu (ví dụ: chuyển sang chọn ghế)
        });
    }

    @Override
    public int getItemCount() {
        return showtimeList != null ? showtimeList.size() : 0;
    }

    class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;

        public ShowtimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
