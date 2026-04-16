package com.example.movie_booking;

import java.io.Serializable;

public class NguoiDung implements Serializable {
    private int id_nguoi_dung;
    private String ho_ten;
    private String email;
    private String mat_khau;
    private String so_dien_thoai;
    private String vai_tro;

    public NguoiDung() {}

    public NguoiDung(int id_nguoi_dung, String ho_ten, String email, String mat_khau, String so_dien_thoai, String vai_tro) {
        this.id_nguoi_dung = id_nguoi_dung;
        this.ho_ten = ho_ten;
        this.email = email;
        this.mat_khau = mat_khau;
        this.so_dien_thoai = so_dien_thoai;
        this.vai_tro = vai_tro;
    }

    public int getId_nguoi_dung() { return id_nguoi_dung; }
    public void setId_nguoi_dung(int id_nguoi_dung) { this.id_nguoi_dung = id_nguoi_dung; }

    public String getHo_ten() { return ho_ten; }
    public void setHo_ten(String ho_ten) { this.ho_ten = ho_ten; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMat_khau() { return mat_khau; }
    public void setMat_khau(String mat_khau) { this.mat_khau = mat_khau; }

    public String getSo_dien_thoai() { return so_dien_thoai; }
    public void setSo_dien_thoai(String so_dien_thoai) { this.so_dien_thoai = so_dien_thoai; }

    public String getVai_tro() { return vai_tro; }
    public void setVai_tro(String vai_tro) { this.vai_tro = vai_tro; }
}
