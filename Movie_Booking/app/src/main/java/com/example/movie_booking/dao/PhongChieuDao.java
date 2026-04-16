package com.example.movie_booking.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.movie_booking.object.PhongChieu;
import java.util.List;

@Dao
public interface PhongChieuDao {
    @Insert
    void insert(PhongChieu phongChieu);

    @Update
    void update(PhongChieu phongChieu);

    @Delete
    void delete(PhongChieu phongChieu);

    @Query("SELECT * FROM PhongChieu WHERE id_rap = :idRap")
    List<PhongChieu> getPhongByRap(int idRap);
}
