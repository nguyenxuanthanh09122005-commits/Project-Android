package com.example.movie_booking.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.movie_booking.dao.*;
import com.example.movie_booking.object.*;

@Database(entities = {
        NguoiDung.class,
        Phim.class,
        Rap.class,
        PhongChieu.class,
        Ghe.class,
        SuatChieu.class,
        DonDatVe.class,
        ChiTietVe.class
}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract NguoiDungDao nguoiDungDao();
    public abstract PhimDao phimDao();
    public abstract RapDao rapDao();
    public abstract PhongChieuDao phongChieuDao();
    public abstract GheDao gheDao();
    public abstract SuatChieuDao suatChieuDao();
    public abstract DonDatVeDao donDatVeDao();
    public abstract ChiTietVeDao chiTietVeDao(); // Thêm dòng này

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "MovieBooking.db")
                    .createFromAsset("MovieBooking.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
