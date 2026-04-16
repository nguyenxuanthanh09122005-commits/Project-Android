package com.example.movie_booking;

import java.io.Serializable;

public class ChiTietVe implements Serializable {
    private int id_don_ve;
    private int id_ghe;
    private double gia_mua;

    public ChiTietVe() {}

    public ChiTietVe(int id_don_ve, int id_ghe, double gia_mua) {
        this.id_don_ve = id_don_ve;
        this.id_ghe = id_ghe;
        this.gia_mua = gia_mua;
    }

    public int getId_don_ve() { return id_don_ve; }
    public void setId_don_ve(int id_don_ve) { this.id_don_ve = id_don_ve; }

    public int getId_ghe() { return id_ghe; }
    public void setId_ghe(int id_ghe) { this.id_ghe = id_ghe; }

    public double getGia_mua() { return gia_mua; }
    public void setGia_mua(double gia_mua) { this.gia_mua = gia_mua; }
}
