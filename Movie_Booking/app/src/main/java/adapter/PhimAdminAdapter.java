package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_booking.R;
import com.example.movie_booking.object.Phim;
import java.util.List;

public class PhimAdminAdapter extends RecyclerView.Adapter<PhimAdminAdapter.ViewHolder> {

    private List<Phim> list;
    private OnPhimClickListener listener;

    public interface OnPhimClickListener {
        void onEdit(Phim phim);
        void onDelete(Phim phim);
    }

    public PhimAdminAdapter(List<Phim> list, OnPhimClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void updateData(List<Phim> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phim_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Phim phim = list.get(position);
        holder.tvTenPhim.setText(phim.getTen_phim());
        holder.tvTheLoai.setText(phim.getThe_loai());
        holder.tvThoiLuong.setText(phim.getThoi_luong() + " phút");

        // Xử lý ảnh
        String tenHinhAnh = phim.getAnh_poster();
        if (tenHinhAnh != null && !tenHinhAnh.isEmpty()) {
            if (tenHinhAnh.contains(".")) {
                tenHinhAnh = tenHinhAnh.substring(0, tenHinhAnh.lastIndexOf("."));
            }
            Context context = holder.itemView.getContext();
            int resId = context.getResources().getIdentifier(tenHinhAnh, "drawable", context.getPackageName());
            if (resId != 0) {
                holder.imgPhim.setImageResource(resId);
            } else {
                holder.imgPhim.setImageResource(R.drawable.logo_beta);
            }
        } else {
            holder.imgPhim.setImageResource(R.drawable.logo_beta);
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(phim));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(phim));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhim;
        TextView tvTenPhim, tvTheLoai, tvThoiLuong;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhim = itemView.findViewById(R.id.imgPhim);
            tvTenPhim = itemView.findViewById(R.id.tvTenPhim);
            tvTheLoai = itemView.findViewById(R.id.tvTheLoai);
            tvThoiLuong = itemView.findViewById(R.id.tvThoiLuong);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
