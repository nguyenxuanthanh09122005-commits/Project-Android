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
import com.example.movie_booking.object.Ghe;
import java.util.ArrayList;
import java.util.List;

public class GheAdapter extends RecyclerView.Adapter<GheAdapter.GheViewHolder> {

    private List<Ghe> listGhe;
    private List<Ghe> selectedGhes = new ArrayList<>();
    private List<Integer> bookedSeatIds = new ArrayList<>(); // List of IDs that are already taken
    private OnSeatSelectedListener listener;

    public interface OnSeatSelectedListener {
        void onSeatSelected(List<Ghe> selectedGhes);
    }

    public GheAdapter(List<Ghe> listGhe, List<Integer> bookedSeatIds, OnSeatSelectedListener listener) {
        this.listGhe = listGhe;
        this.bookedSeatIds = bookedSeatIds != null ? bookedSeatIds : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public GheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ghe, parent, false);
        return new GheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GheViewHolder holder, int position) {
        Ghe ghe = listGhe.get(position);
        holder.tvTenGhe.setText(ghe.getHang_ghe() + ghe.getSo_ghe());

        // Check if booked
        if (bookedSeatIds.contains(ghe.getId_ghe())) {
            holder.cardGhe.setCardBackgroundColor(Color.RED); // Red for booked
            holder.tvTenGhe.setTextColor(Color.WHITE);
            holder.itemView.setOnClickListener(null); // Disable clicking
            return;
        }

        // Color based on type or selection
        boolean isSelected = false;
        for (Ghe selected : selectedGhes) {
            if (selected.getId_ghe() == ghe.getId_ghe()) {
                isSelected = true;
                break;
            }
        }

        if (isSelected) {
            holder.cardGhe.setCardBackgroundColor(Color.parseColor("#4CAF50")); // Green for selected
            holder.tvTenGhe.setTextColor(Color.WHITE);
        } else {
            if ("VIP".equalsIgnoreCase(ghe.getLoai_ghe())) {
                holder.cardGhe.setCardBackgroundColor(Color.parseColor("#FFD700")); // Gold for VIP
            } else {
                holder.cardGhe.setCardBackgroundColor(Color.parseColor("#E0E0E0")); // Light Gray for normal
            }
            holder.tvTenGhe.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(v -> {
            boolean alreadySelected = false;
            int selectedIdx = -1;
            for (int i = 0; i < selectedGhes.size(); i++) {
                if (selectedGhes.get(i).getId_ghe() == ghe.getId_ghe()) {
                    alreadySelected = true;
                    selectedIdx = i;
                    break;
                }
            }

            if (alreadySelected) {
                selectedGhes.remove(selectedIdx);
            } else {
                selectedGhes.add(ghe);
            }
            
            notifyItemChanged(position);
            if (listener != null) {
                listener.onSeatSelected(selectedGhes);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGhe != null ? listGhe.size() : 0;
    }

    public List<Ghe> getSelectedGhes() {
        return selectedGhes;
    }

    public static class GheViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenGhe;
        CardView cardGhe;

        public GheViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenGhe = itemView.findViewById(R.id.tvTenGhe);
            cardGhe = itemView.findViewById(R.id.cardGhe);
        }
    }
}
