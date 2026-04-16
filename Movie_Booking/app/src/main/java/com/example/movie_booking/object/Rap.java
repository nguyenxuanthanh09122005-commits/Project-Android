package com.example.movie_booking.object;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "Rap")
public class Rap implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id_rap;

    @NonNull
    private String ten_rap;

    @NonNull
    private String dia_chi;

    @NonNull
    private String thanh_pho;

    public Rap() {}

    public Rap(int id_rap, @NonNull String ten_rap, @NonNull String dia_chi, @NonNull String thanh_pho) {
        this.id_rap = id_rap;
        this.ten_rap = ten_rap;
        this.dia_chi = dia_chi;
        this.thanh_pho = thanh_pho;
    }

    public int getId_rap() { return id_rap; }
    public void setId_rap(int id_rap) { this.id_rap = id_rap; }

    @NonNull
    public String getTen_rap() { return ten_rap; }
    public void setTen_rap(@NonNull String ten_rap) { this.ten_rap = ten_rap; }

    @NonNull
    public String getDia_chi() { return dia_chi; }
    public void setDia_chi(@NonNull String dia_chi) { this.dia_chi = dia_chi; }

    @NonNull
    public String getThanh_pho() { return thanh_pho; }
    public void setThanh_pho(@NonNull String thanh_pho) { this.thanh_pho = thanh_pho; }
}
