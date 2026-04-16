package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_booking.R;
import com.example.movie_booking.object.SuatChieu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RapAdapter extends RecyclerView.Adapter<RapAdapter.RapViewHolder> {
    private List<String> danhSachTenRap;
    private Map<String, List<SuatChieu>> lichChieuTheoRap;

    public RapAdapter(Map<String, List<SuatChieu>> lichChieuTheoRap) {
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

        List<SuatChieu> danhSachSuatChieu = lichChieuTheoRap.get(tenRap);
        SuatChieuAdapter suatChieuAdapter = new SuatChieuAdapter(danhSachSuatChieu);
        
        holder.rvDanhSachSuatChieu.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), 3));
        holder.rvDanhSachSuatChieu.setAdapter(suatChieuAdapter);
    }

    @Override
    public int getItemCount() {
        return danhSachTenRap.size();
    }

    class RapViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenRap;
        RecyclerView rvDanhSachSuatChieu;

        public RapViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenRap = itemView.findViewById(R.id.tvTenRap);
            rvDanhSachSuatChieu = itemView.findViewById(R.id.rvDanhSachSuatChieu);
        }
    }
}
