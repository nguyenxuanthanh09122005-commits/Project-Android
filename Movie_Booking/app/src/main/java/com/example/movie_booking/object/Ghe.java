package com.example.movie_booking.object;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "Ghe",
        foreignKeys = @ForeignKey(entity = PhongChieu.class,
                parentColumns = "id_phong",
                childColumns = "id_phong",
                onDelete = ForeignKey.CASCADE))
public class Ghe implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id_ghe;
    private int id_phong;
    @NonNull
    private String hang_ghe;
    private int so_ghe;
    @ColumnInfo(defaultValue = "Thuong")
    private String loai_ghe;

    public Ghe() {}

    public Ghe(Integer id_ghe, int id_phong, @NonNull String hang_ghe, int so_ghe, String loai_ghe) {
        this.id_ghe = id_ghe;
        this.id_phong = id_phong;
        this.hang_ghe = hang_ghe;
        this.so_ghe = so_ghe;
        this.loai_ghe = loai_ghe;
    }

    public Integer getId_ghe() { return id_ghe; }
    public void setId_ghe(Integer id_ghe) { this.id_ghe = id_ghe; }

    public int getId_phong() { return id_phong; }
    public void setId_phong(int id_phong) { this.id_phong = id_phong; }

    @NonNull
    public String getHang_ghe() { return hang_ghe; }
    public void setHang_ghe(@NonNull String hang_ghe) { this.hang_ghe = hang_ghe; }

    public int getSo_ghe() { return so_ghe; }
    public void setSo_ghe(int so_ghe) { this.so_ghe = so_ghe; }

    public String getLoai_ghe() { return loai_ghe; }
    public void setLoai_ghe(String loai_ghe) { this.loai_ghe = loai_ghe; }
}
