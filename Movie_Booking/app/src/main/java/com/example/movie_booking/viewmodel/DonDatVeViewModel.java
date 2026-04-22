package com.example.movie_booking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.movie_booking.object.ChiTietVe;
import com.example.movie_booking.object.DonDatVe;
import com.example.movie_booking.repository.DonDatVeRepository;
import java.util.List;

public class DonDatVeViewModel extends AndroidViewModel {
    private DonDatVeRepository repository;

    public DonDatVeViewModel(@NonNull Application application) {
        super(application);
        repository = new DonDatVeRepository(application);
    }

    public void datVe(DonDatVe donVe, List<ChiTietVe> danhSachChiTiet, DonDatVeRepository.OnBookingCompleteListener listener) {
        repository.datVe(donVe, danhSachChiTiet, listener);
    }
}
