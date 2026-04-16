package com.example.movie_booking.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.movie_booking.object.Phim;
import java.util.List;

@Dao
public interface PhimDao {
    @Insert
    void insert(Phim phim);

    @Update
    void update(Phim phim);

    @Delete
    void delete(Phim phim);

    @Query("SELECT * FROM Phim")
    List<Phim> getAllPhim();

    @Query("SELECT * FROM Phim WHERE id_phim = :id")
    Phim getPhimById(int id);
}
