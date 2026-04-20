package com.example.movie_booking.object;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "DonDatVe",
        foreignKeys = {
                @ForeignKey(entity = NguoiDung.class,
                        parentColumns = "id_nguoi_dung",
                        childColumns = "id_nguoi_dung"),
                @ForeignKey(entity = SuatChieu.class,
                        parentColumns = "id_suat_chieu",
                        childColumns = "id_suat_chieu")
        })
public class DonDatVe implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id_don_ve;
    private int id_nguoi_dung;
    private int id_suat_chieu;
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String ngay_dat;
    private double tong_tien;
    @ColumnInfo(defaultValue = "ChoThanhToan")
    private String trang_thai;

    public DonDatVe() {}

    public DonDatVe(Integer id_don_ve, int id_nguoi_dung, int id_suat_chieu, String ngay_dat, double tong_tien, String trang_thai) {
        this.id_don_ve = id_don_ve;
        this.id_nguoi_dung = id_nguoi_dung;
        this.id_suat_chieu = id_suat_chieu;
        this.ngay_dat = ngay_dat;
        this.tong_tien = tong_tien;
        this.trang_thai = trang_thai;
    }

    public Integer getId_don_ve() { return id_don_ve; }
    public void setId_don_ve(Integer id_don_ve) { this.id_don_ve = id_don_ve; }

    public int getId_nguoi_dung() { return id_nguoi_dung; }
    public void setId_nguoi_dung(int id_nguoi_dung) { this.id_nguoi_dung = id_nguoi_dung; }

    public int getId_suat_chieu() { return id_suat_chieu; }
    public void setId_suat_chieu(int id_suat_chieu) { this.id_suat_chieu = id_suat_chieu; }

    public String getNgay_dat() { return ngay_dat; }
    public void setNgay_dat(String ngay_dat) { this.ngay_dat = ngay_dat; }

    public double getTong_tien() { return tong_tien; }
    public void setTong_tien(double tong_tien) { this.tong_tien = tong_tien; }

    public String getTrang_thai() { return trang_thai; }
    public void setTrang_thai(String trang_thai) { this.trang_thai = trang_thai; }
}
