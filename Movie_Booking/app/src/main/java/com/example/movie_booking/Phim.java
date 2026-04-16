package com.example.movie_booking;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Phim implements Serializable {
    private int id_phim;
    private String ten_phim;
    private String mo_ta;
    private int thoi_luong;
    private String ngay_khoi_chieu;
    private String anh_poster;
    private String trailer_url;
    private String the_loai;
    private String do_tuoi_quy_dinh;

    public Phim() {}

    public Phim(int id_phim, String ten_phim, String mo_ta, int thoi_luong, String ngay_khoi_chieu, String anh_poster, String trailer_url, String the_loai, String do_tuoi_quy_dinh) {
        this.id_phim = id_phim;
        this.ten_phim = ten_phim;
        this.mo_ta = mo_ta;
        this.thoi_luong = thoi_luong;
        this.ngay_khoi_chieu = ngay_khoi_chieu;
        this.anh_poster = anh_poster;
        this.trailer_url = trailer_url;
        this.the_loai = the_loai;
        this.do_tuoi_quy_dinh = do_tuoi_quy_dinh;
    }

    public Date getNgayKhoiChieuObject() {
        String[] formats = {"yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy"};
        for (String format : formats) {
            try {
                return new SimpleDateFormat(format, Locale.getDefault()).parse(ngay_khoi_chieu);
            } catch (ParseException ignored) {}
        }
        return null;
    }

    public int getId_phim() { return id_phim; }
    public void setId_phim(int id_phim) { this.id_phim = id_phim; }

    public String getTen_phim() { return ten_phim; }
    public void setTen_phim(String ten_phim) { this.ten_phim = ten_phim; }

    public String getMo_ta() { return mo_ta; }
    public void setMo_ta(String mo_ta) { this.mo_ta = mo_ta; }

    public int getThoi_luong() { return thoi_luong; }
    public void setThoi_luong(int thoi_luong) { this.thoi_luong = thoi_luong; }

    public String getNgay_khoi_chieu() { return ngay_khoi_chieu; }
    public void setNgay_khoi_chieu(String ngay_khoi_chieu) { this.ngay_khoi_chieu = ngay_khoi_chieu; }

    public String getAnh_poster() { return anh_poster; }
    public void setAnh_poster(String anh_poster) { this.anh_poster = anh_poster; }

    public String getTrailer_url() { return trailer_url; }
    public void setTrailer_url(String trailer_url) { this.trailer_url = trailer_url; }

    public String getThe_loai() { return the_loai; }
    public void setThe_loai(String the_loai) { this.the_loai = the_loai; }

    public String getDo_tuoi_quy_dinh() { return do_tuoi_quy_dinh; }
    public void setDo_tuoi_quy_dinh(String do_tuoi_quy_dinh) { this.do_tuoi_quy_dinh = do_tuoi_quy_dinh; }
}
