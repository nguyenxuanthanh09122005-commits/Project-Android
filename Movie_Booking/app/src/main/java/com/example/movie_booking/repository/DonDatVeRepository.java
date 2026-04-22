package com.example.movie_booking.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.movie_booking.dao.ChiTietVeDao;
import com.example.movie_booking.dao.DonDatVeDao;
import com.example.movie_booking.database.AppDatabase;
import com.example.movie_booking.object.ChiTietVe;
import com.example.movie_booking.object.DonDatVe;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DonDatVeRepository {
    private DonDatVeDao donDatVeDao;
    private ChiTietVeDao chiTietVeDao;
    private ExecutorService executorService;

    public DonDatVeRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        donDatVeDao = db.donDatVeDao();
        chiTietVeDao = db.chiTietVeDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public interface OnBookingCompleteListener {
        void onComplete(long idDonHang);
        void onError(Exception e);
    }

    public void datVe(DonDatVe donVe, List<ChiTietVe> danhSachChiTiet, OnBookingCompleteListener listener) {
        executorService.execute(() -> {
            try {
                long idDonVe = donDatVeDao.insert(donVe);
                for (ChiTietVe ct : danhSachChiTiet) {
                    ct.setId_don_ve((int) idDonVe);
                    chiTietVeDao.insert(ct);
                }
                if (listener != null) listener.onComplete(idDonVe);
            } catch (Exception e) {
                if (listener != null) listener.onError(e);
            }
        });
    }

    public LiveData<List<DonDatVe>> getDonVeByNguoiDung(int idNguoiDung) {
        MutableLiveData<List<DonDatVe>> data = new MutableLiveData<>();
        executorService.execute(() -> data.postValue(donDatVeDao.getDonVeByNguoiDung(idNguoiDung)));
        return data;
    }
}
