package com.example.movie_booking.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.movie_booking.object.DonDatVe;
import java.util.List;

@Dao
public interface DonDatVeDao {
    @Insert
    long insert(DonDatVe donDatVe);

    @Update
    void update(DonDatVe donDatVe);

    @Delete
    void delete(DonDatVe donDatVe);

    @Query("SELECT * FROM DonDatVe WHERE id_nguoi_dung = :idNguoiDung")
    List<DonDatVe> getDonVeByNguoiDung(int idNguoiDung);

    @Query("SELECT * FROM DonDatVe WHERE id_don_ve = :id")
    DonDatVe getDonVeById(int id);
}
