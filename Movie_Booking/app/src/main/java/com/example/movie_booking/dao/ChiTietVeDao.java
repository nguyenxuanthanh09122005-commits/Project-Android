package com.example.movie_booking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import com.example.movie_booking.object.ChiTietVe;

@Dao
public interface ChiTietVeDao {
    @Insert
    void insert(ChiTietVe chiTietVe);
}
