package com.example.movie_booking.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.movie_booking.object.NguoiDung;
import java.util.List;

@Dao
public interface NguoiDungDao {
    @Insert
    void insert(NguoiDung nguoiDung);

    @Update
    void update(NguoiDung nguoiDung);

    @Delete
    void delete(NguoiDung nguoiDung);

    @Query("SELECT * FROM NguoiDung")
    List<NguoiDung> getAllNguoiDung();

    @Query("SELECT * FROM NguoiDung WHERE email = :email AND mat_khau = :password")
    NguoiDung login(String email, String password);

    @Query("SELECT * FROM NguoiDung")
    List<NguoiDung> getAll();
}
