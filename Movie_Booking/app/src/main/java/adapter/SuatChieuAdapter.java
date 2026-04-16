package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_booking.R;
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
        holder.tvGioChieu.setText(suatChieu.getThoi_gian_bat_dau());
        
        holder.itemView.setOnClickListener(v -> {
            // Xử lý khi chọn suất chiếu
        });
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
