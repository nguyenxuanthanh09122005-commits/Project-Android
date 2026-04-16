package com.example.movie_booking.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.movie_booking.object.Ghe;
import java.util.List;

@Dao
public interface GheDao {
    @Insert
    void insert(Ghe ghe);

    @Update
    void update(Ghe ghe);

    @Delete
    void delete(Ghe ghe);

    @Query("SELECT * FROM Ghe WHERE id_phong = :idPhong")
    List<Ghe> getGheByPhong(int idPhong);
}
