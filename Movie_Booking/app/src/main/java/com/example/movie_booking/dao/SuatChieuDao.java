package com.example.movie_booking.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.movie_booking.object.SuatChieu;
import java.util.List;

@Dao
public interface SuatChieuDao {
    @Insert
    void insert(SuatChieu suatChieu);

    @Update
    void update(SuatChieu suatChieu);

    @Delete
    void delete(SuatChieu suatChieu);

    @Query("SELECT * FROM SuatChieu WHERE id_phim = :idPhim")
    List<SuatChieu> getSuatChieuByPhim(int idPhim);

    @Query("SELECT r.ten_rap FROM SuatChieu s " +
           "JOIN PhongChieu p ON s.id_phong = p.id_phong " +
           "JOIN Rap r ON p.id_rap = r.id_rap " +
           "WHERE s.id_suat_chieu = :idSuatChieu")
    String getTenRapBySuatChieu(int idSuatChieu);

    @Query("SELECT r.thanh_pho FROM Rap r WHERE r.ten_rap = :tenRap LIMIT 1")
    String getThanhPhoByRap(String tenRap);

    @Query("SELECT r.ten_rap, s.id_suat_chieu, s.id_phim, s.id_phong, s.thoi_gian_bat_dau, s.thoi_gian_ket_thuc, s.gia_ve_co_ban, p.ten_phong " +
           "FROM SuatChieu s " +
           "JOIN PhongChieu p ON s.id_phong = p.id_phong " +
           "JOIN Rap r ON p.id_rap = r.id_rap " +
           "WHERE s.id_phim = :idPhim")
    List<SuatChieuWithTheater> getSuatChieuWithTheaterByPhim(int idPhim);

    class SuatChieuWithTheater {
        public String ten_rap;
        public int id_suat_chieu;
        public int id_phim;
        public int id_phong;
        public String thoi_gian_bat_dau;
        public String thoi_gian_ket_thuc;
        public double gia_ve_co_ban;
        public String ten_phong;

        public SuatChieu toSuatChieu() {
            return new SuatChieu(id_suat_chieu, id_phim, id_phong, thoi_gian_bat_dau, thoi_gian_ket_thuc, gia_ve_co_ban);
        }
    }
}
