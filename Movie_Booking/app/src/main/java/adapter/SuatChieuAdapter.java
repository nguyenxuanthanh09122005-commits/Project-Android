package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_booking.R;
import com.example.movie_booking.activity.ChonGheActivity;
import com.example.movie_booking.object.SuatChieu;

import java.util.List;

public class SuatChieuAdapter extends RecyclerView.Adapter<SuatChieuAdapter.SuatChieuViewHolder> {
    private List<SuatChieu> danhSachSuatChieu;

    public SuatChieuAdapter(List<SuatChieu> danhSachSuatChieu) {
        this.danhSachSuatChieu = danhSachSuatChieu;
    }

    @NonNull
    @Override
    public SuatChieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suat_chieu, parent, false);
        return new SuatChieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuatChieuViewHolder holder, int position) {
        SuatChieu suatChieu = danhSachSuatChieu.get(position);
        
        String fullTime = suatChieu.getThoi_gian_bat_dau();
        if (fullTime != null && fullTime.contains(" ")) {
            String timePart = fullTime.split(" ")[1];
            if (timePart.length() >= 5) {
                holder.tvGioChieu.setText(timePart.substring(0, 5));
            } else {
                holder.tvGioChieu.setText(timePart);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            navigateToSeatSelection(v.getContext(), suatChieu);
        });
    }

    private void navigateToSeatSelection(Context context, SuatChieu suatChieu) {
        Intent intent = new Intent(context, ChonGheActivity.class);
        intent.putExtra("suat_chieu_data", suatChieu);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return danhSachSuatChieu != null ? danhSachSuatChieu.size() : 0;
    }

    class SuatChieuViewHolder extends RecyclerView.ViewHolder {
        TextView tvGioChieu;

        public SuatChieuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGioChieu = itemView.findViewById(R.id.tvGioChieu);
        }
    }
}
