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

import com.example.movie_booking.activity.DatVeActivity;
import com.example.movie_booking.object.Phim;
import com.example.movie_booking.R;

import java.util.ArrayList;
import java.util.List;

public class PhimAdapter extends RecyclerView.Adapter<PhimAdapter.PhimViewHolder> {
    private List<Phim> danhSachPhim;

    public PhimAdapter(List<Phim> danhSachPhim) {
        this.danhSachPhim = (danhSachPhim != null) ? danhSachPhim : new ArrayList<>();
    }

    public void updateData(List<Phim> newData) {
        this.danhSachPhim = (newData != null) ? newData : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phim, parent, false);
        return new PhimViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhimViewHolder holder, int position) {
        Phim phim = danhSachPhim.get(position);
        holder.tvTenPhim.setText(phim.getTen_phim());
        holder.tvDoTuoi.setText(phim.getDo_tuoi_quy_dinh());
        
        String hinhAnh = phim.getAnh_poster();
        if (hinhAnh != null && !hinhAnh.isEmpty()) {
            if (hinhAnh.contains(".")) {
                hinhAnh = hinhAnh.substring(0, hinhAnh.lastIndexOf("."));
            }
            
            Context context = holder.itemView.getContext();
            int resId = context.getResources().getIdentifier(
                    hinhAnh, "drawable", context.getPackageName());
            
            if (resId != 0) {
                holder.imgPhim.setImageResource(resId);
            } else {
                holder.imgPhim.setImageResource(R.drawable.logo_beta);
            }
        } else {
            holder.imgPhim.setImageResource(R.drawable.logo_beta);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DatVeActivity.class);
            intent.putExtra("movie_data", phim);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return danhSachPhim.size();
    }

    class PhimViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhim;
        TextView tvTenPhim, tvDoTuoi;

        public PhimViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhim = itemView.findViewById(R.id.imgPhim);
            tvTenPhim = itemView.findViewById(R.id.tvTenPhim);
            tvDoTuoi = itemView.findViewById(R.id.tvDoTuoi);
        }
    }
}
