package adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_booking.R;
import java.util.List;

public class NgayAdapter extends RecyclerView.Adapter<NgayAdapter.NgayViewHolder> {

    private List<NgayItem> listNgay;
    private int selectedPosition = 0;
    private OnNgayClickListener listener;

    public interface OnNgayClickListener {
        void onNgayClick(NgayItem ngayItem);
    }

    public NgayAdapter(List<NgayItem> listNgay, OnNgayClickListener listener) {
        this.listNgay = listNgay;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NgayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ngay, parent, false);
        return new NgayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NgayViewHolder holder, int position) {
        NgayItem item = listNgay.get(position);
        holder.tvThu.setText(item.getThu());
        holder.tvNgay.setText(item.getNgay());
        holder.tvThang.setText(item.getThang());

        if (selectedPosition == position) {
            holder.cardNgay.setCardBackgroundColor(Color.parseColor("#00468C"));
            holder.tvThu.setTextColor(Color.WHITE);
            holder.tvNgay.setTextColor(Color.WHITE);
            holder.tvThang.setTextColor(Color.WHITE);
        } else {
            holder.cardNgay.setCardBackgroundColor(Color.WHITE);
            holder.tvThu.setTextColor(Color.GRAY);
            holder.tvNgay.setTextColor(Color.BLACK);
            holder.tvThang.setTextColor(Color.GRAY);
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPos = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPos);
            notifyItemChanged(selectedPosition);
            if (listener != null) {
                listener.onNgayClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNgay != null ? listNgay.size() : 0;
    }

    public static class NgayViewHolder extends RecyclerView.ViewHolder {
        TextView tvThu, tvNgay, tvThang;
        CardView cardNgay;

        public NgayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvThu = itemView.findViewById(R.id.tvThu);
            tvNgay = itemView.findViewById(R.id.tvNgay);
            tvThang = itemView.findViewById(R.id.tvThang);
            cardNgay = itemView.findViewById(R.id.cardNgay);
        }
    }

    public static class NgayItem {
        private String thu;
        private String ngay;
        private String thang;
        private String fullDate; // To filter

        public NgayItem(String thu, String ngay, String thang, String fullDate) {
            this.thu = thu;
            this.ngay = ngay;
            this.thang = thang;
            this.fullDate = fullDate;
        }

        public String getThu() { return thu; }
        public String getNgay() { return ngay; }
        public String getThang() { return thang; }
        public String getFullDate() { return fullDate; }
    }
}
