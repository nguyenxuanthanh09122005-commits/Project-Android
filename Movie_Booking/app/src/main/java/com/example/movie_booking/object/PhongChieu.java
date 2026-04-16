package com.example.movie_booking.object;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "PhongChieu",
        foreignKeys = @ForeignKey(entity = Rap.class,
                parentColumns = "id_rap",
                childColumns = "id_rap",
                onDelete = ForeignKey.CASCADE))
public class PhongChieu implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id_phong;
    private int id_rap;
    @NonNull
    private String ten_phong;
    private int tong_so_ghe;

    public PhongChieu() {}

    public PhongChieu(int id_phong, int id_rap, String ten_phong, int tong_so_ghe) {
        this.id_phong = id_phong;
        this.id_rap = id_rap;
        this.ten_phong = ten_phong;
        this.tong_so_ghe = tong_so_ghe;
    }

    public int getId_phong() { return id_phong; }
    public void setId_phong(int id_phong) { this.id_phong = id_phong; }

    public int getId_rap() { return id_rap; }
    public void setId_rap(int id_rap) { this.id_rap = id_rap; }

    public String getTen_phong() { return ten_phong; }
    public void setTen_phong(String ten_phong) { this.ten_phong = ten_phong; }

    public int getTong_so_ghe() { return tong_so_ghe; }
    public void setTong_so_ghe(int tong_so_ghe) { this.tong_so_ghe = tong_so_ghe; }
}
