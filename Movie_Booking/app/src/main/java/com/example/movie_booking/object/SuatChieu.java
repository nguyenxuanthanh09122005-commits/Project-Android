package com.example.movie_booking.object;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "SuatChieu",
        foreignKeys = {
                @ForeignKey(entity = Phim.class,
                        parentColumns = "id_phim",
                        childColumns = "id_phim",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = PhongChieu.class,
                        parentColumns = "id_phong",
                        childColumns = "id_phong",
                        onDelete = ForeignKey.CASCADE)
        })
public class SuatChieu implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id_suat_chieu;
    private int id_phim;
    private int id_phong;
    @NonNull
    private String thoi_gian_bat_dau;
    @NonNull
    private String thoi_gian_ket_thuc;
    private double gia_ve_co_ban;

    public SuatChieu() {}

    public SuatChieu(int id_suat_chieu, int id_phim, int id_phong, String thoi_gian_bat_dau, String thoi_gian_ket_thuc, double gia_ve_co_ban) {
        this.id_suat_chieu = id_suat_chieu;
        this.id_phim = id_phim;
        this.id_phong = id_phong;
        this.thoi_gian_bat_dau = thoi_gian_bat_dau;
        this.thoi_gian_ket_thuc = thoi_gian_ket_thuc;
        this.gia_ve_co_ban = gia_ve_co_ban;
    }

    public int getId_suat_chieu() { return id_suat_chieu; }
    public void setId_suat_chieu(int id_suat_chieu) { this.id_suat_chieu = id_suat_chieu; }

    public int getId_phim() { return id_phim; }
    public void setId_phim(int id_phim) { this.id_phim = id_phim; }

    public int getId_phong() { return id_phong; }
    public void setId_phong(int id_phong) { this.id_phong = id_phong; }

    public String getThoi_gian_bat_dau() { return thoi_gian_bat_dau; }
    public void setThoi_gian_bat_dau(String thoi_gian_bat_dau) { this.thoi_gian_bat_dau = thoi_gian_bat_dau; }

    public String getThoi_gian_ket_thuc() { return thoi_gian_ket_thuc; }
    public void setThoi_gian_ket_thuc(String thoi_gian_ket_thuc) { this.thoi_gian_ket_thuc = thoi_gian_ket_thuc; }

    public double getGia_ve_co_ban() { return gia_ve_co_ban; }
    public void setGia_ve_co_ban(double gia_ve_co_ban) { this.gia_ve_co_ban = gia_ve_co_ban; }
}
