package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_booking.R;
import com.example.movie_booking.dao.SuatChieuDao;
import com.example.movie_booking.object.SuatChieu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RapAdapter extends RecyclerView.Adapter<RapAdapter.RapViewHolder> {
    private final List<String> danhSachTenRap;
    private final Map<String, List<SuatChieuDao.SuatChieuWithTheater>> lichChieuTheoRap;

    public RapAdapter(Map<String, List<SuatChieuDao.SuatChieuWithTheater>> lichChieuTheoRap) {
        this.lichChieuTheoRap = lichChieuTheoRap;
        this.danhSachTenRap = new ArrayList<>(lichChieuTheoRap.keySet());
    }

    @NonNull
    @Override
    public RapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rap, parent, false);
        return new RapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RapViewHolder holder, int position) {
        String tenRap = danhSachTenRap.get(position);
        holder.tvTenRap.setText(tenRap);

        List<SuatChieuDao.SuatChieuWithTheater> listSC = lichChieuTheoRap.get(tenRap);
        
        if (listSC != null && !listSC.isEmpty()) {
            holder.tvTenPhong.setText(listSC.get(0).ten_phong);
            
            List<SuatChieu> danhSachSuatChieu = new ArrayList<>();
            for (SuatChieuDao.SuatChieuWithTheater swt : listSC) {
                danhSachSuatChieu.add(swt.toSuatChieu());
            }
            
            SuatChieuAdapter suatChieuAdapter = new SuatChieuAdapter(danhSachSuatChieu);
            holder.rvDanhSachSuatChieu.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), 3));
            holder.rvDanhSachSuatChieu.setAdapter(suatChieuAdapter);

            View.OnClickListener toggleAction = v -> {
                if (holder.rvDanhSachSuatChieu.getVisibility() == View.VISIBLE) {
                    holder.rvDanhSachSuatChieu.setVisibility(View.GONE);
                    holder.tvTenPhong.setVisibility(View.GONE);
                    holder.imgArrow.setImageResource(android.R.drawable.arrow_down_float);
                } else {
                    holder.rvDanhSachSuatChieu.setVisibility(View.VISIBLE);
                    holder.tvTenPhong.setVisibility(View.VISIBLE);
                    holder.imgArrow.setImageResource(android.R.drawable.arrow_up_float);
                }
            };

            holder.tvTenRap.setOnClickListener(toggleAction);
            holder.imgArrow.setOnClickListener(toggleAction);
            holder.tvTenPhong.setOnClickListener(toggleAction);
        }
    }

    @Override
    public int getItemCount() {
        return danhSachTenRap.size();
    }

    static class RapViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenRap, tvTenPhong;
        RecyclerView rvDanhSachSuatChieu;
        ImageView imgArrow;

        public RapViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenRap = itemView.findViewById(R.id.tvTenRap);
            tvTenPhong = itemView.findViewById(R.id.tvTenPhong);
            rvDanhSachSuatChieu = itemView.findViewById(R.id.rvDanhSachSuatChieu);
            imgArrow = itemView.findViewById(R.id.imgArrow);
        }
    }
}
