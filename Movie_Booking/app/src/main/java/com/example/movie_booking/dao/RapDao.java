package com.example.movie_booking.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.movie_booking.object.Rap;
import java.util.List;

@Dao
public interface RapDao {
    @Insert
    void insert(Rap rap);

    @Update
    void update(Rap rap);

    @Delete
    void delete(Rap rap);

    @Query("SELECT * FROM Rap")
    List<Rap> getAllRap();
}
