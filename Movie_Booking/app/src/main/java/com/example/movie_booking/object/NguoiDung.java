package com.example.movie_booking.object;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "NguoiDung")
public class NguoiDung implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id_nguoi_dung;

    @NonNull
    private String ho_ten;

    @NonNull
    private String email;

    @NonNull
    private String mat_khau;

    private String so_dien_thoai;

    @ColumnInfo(defaultValue = "KhachHang")
    private String vai_tro;

    public NguoiDung() {}

    public NguoiDung(int id_nguoi_dung, @NonNull String ho_ten, @NonNull String email, @NonNull String mat_khau, String so_dien_thoai, String vai_tro) {
        this.id_nguoi_dung = id_nguoi_dung;
        this.ho_ten = ho_ten;
        this.email = email;
        this.mat_khau = mat_khau;
        this.so_dien_thoai = so_dien_thoai;
        this.vai_tro = vai_tro;
    }

    public int getId_nguoi_dung() { return id_nguoi_dung; }
    public void setId_nguoi_dung(int id_nguoi_dung) { this.id_nguoi_dung = id_nguoi_dung; }

    @NonNull
    public String getHo_ten() { return ho_ten; }
    public void setHo_ten(@NonNull String ho_ten) { this.ho_ten = ho_ten; }

    @NonNull
    public String getEmail() { return email; }
    public void setEmail(@NonNull String email) { this.email = email; }

    @NonNull
    public String getMat_khau() { return mat_khau; }
    public void setMat_khau(@NonNull String mat_khau) { this.mat_khau = mat_khau; }

    public String getSo_dien_thoai() { return so_dien_thoai; }
    public void setSo_dien_thoai(String so_dien_thoai) { this.so_dien_thoai = so_dien_thoai; }

    public String getVai_tro() { return vai_tro; }
    public void setVai_tro(String vai_tro) { this.vai_tro = vai_tro; }
}
