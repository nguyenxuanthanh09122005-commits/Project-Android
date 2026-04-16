package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_booking.R;
import com.example.movie_booking.SuatChieu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder> {
    private List<String> theaterNames;
    private Map<String, List<SuatChieu>> theaterShowtimes;

    public TheaterAdapter(Map<String, List<SuatChieu>> theaterShowtimes) {
        this.theaterShowtimes = theaterShowtimes;
        this.theaterNames = new ArrayList<>(theaterShowtimes.keySet());
    }

    @NonNull
    @Override
    public TheaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theater, parent, false);
        return new TheaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheaterViewHolder holder, int position) {
        String theaterName = theaterNames.get(position);
        holder.tvTheaterName.setText(theaterName);

        List<SuatChieu> showtimes = theaterShowtimes.get(theaterName);
        ShowtimeAdapter showtimeAdapter = new ShowtimeAdapter(showtimes);
        
        holder.rvShowtimeList.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), 3));
        holder.rvShowtimeList.setAdapter(showtimeAdapter);
    }

    @Override
    public int getItemCount() {
        return theaterNames.size();
    }

    class TheaterViewHolder extends RecyclerView.ViewHolder {
        TextView tvTheaterName;
        RecyclerView rvShowtimeList;

        public TheaterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTheaterName = itemView.findViewById(R.id.tvTheaterName);
            rvShowtimeList = itemView.findViewById(R.id.rvShowtimeList);
        }
    }
}
