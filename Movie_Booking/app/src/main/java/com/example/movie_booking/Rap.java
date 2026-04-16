package com.example.movie_booking;

import java.io.Serializable;

public class Rap implements Serializable {
    private int id_rap;
    private String ten_rap;
    private String dia_chi;
    private String thanh_pho;

    public Rap() {}

    public Rap(int id_rap, String ten_rap, String dia_chi, String thanh_pho) {
        this.id_rap = id_rap;
        this.ten_rap = ten_rap;
        this.dia_chi = dia_chi;
        this.thanh_pho = thanh_pho;
    }

    public int getId_rap() { return id_rap; }
    public void setId_rap(int id_rap) { this.id_rap = id_rap; }

    public String getTen_rap() { return ten_rap; }
    public void setTen_rap(String ten_rap) { this.ten_rap = ten_rap; }

    public String getDia_chi() { return dia_chi; }
    public void setDia_chi(String dia_chi) { this.dia_chi = dia_chi; }

    public String getThanh_pho() { return thanh_pho; }
    public void setThanh_pho(String thanh_pho) { this.thanh_pho = thanh_pho; }
}
