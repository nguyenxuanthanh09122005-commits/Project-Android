-- 1. Bảng Người Dùng
CREATE TABLE NguoiDung (
    id_nguoi_dung INTEGER PRIMARY KEY AUTOINCREMENT,
    ho_ten TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    mat_khau TEXT NOT NULL,
    so_dien_thoai TEXT,
    vai_tro TEXT DEFAULT 'KhachHang'
);

-- 2. Bảng Phim
CREATE TABLE Phim (
    id_phim INTEGER PRIMARY KEY AUTOINCREMENT,
    ten_phim TEXT NOT NULL,
    mo_ta TEXT,
    thoi_luong INTEGER NOT NULL, -- Tính bằng phút
    ngay_khoi_chieu TEXT,
    anh_poster TEXT,
    trailer_url TEXT,
    the_loai TEXT,
    do_tuoi_quy_dinh TEXT
);

-- 3. Bảng Rạp
CREATE TABLE Rap (
    id_rap INTEGER PRIMARY KEY AUTOINCREMENT,
    ten_rap TEXT NOT NULL,
    dia_chi TEXT NOT NULL,
    thanh_pho TEXT NOT NULL
);

-- 4. Bảng Phòng Chiếu
CREATE TABLE PhongChieu (
    id_phong INTEGER PRIMARY KEY AUTOINCREMENT,
    id_rap INTEGER NOT NULL,
    ten_phong TEXT NOT NULL,
    tong_so_ghe INTEGER NOT NULL,
    FOREIGN KEY (id_rap) REFERENCES Rap(id_rap) ON DELETE CASCADE
);

-- 5. Bảng Ghế
CREATE TABLE Ghe (
    id_ghe INTEGER PRIMARY KEY AUTOINCREMENT,
    id_phong INTEGER NOT NULL,
    hang_ghe TEXT NOT NULL, -- VD: 'A', 'B'
    so_ghe INTEGER NOT NULL, -- VD: 1, 2, 3
    loai_ghe TEXT DEFAULT 'Thuong', -- Thuong, VIP, Couple
    FOREIGN KEY (id_phong) REFERENCES PhongChieu(id_phong) ON DELETE CASCADE
);

-- 6. Bảng Suất Chiếu
CREATE TABLE SuatChieu (
    id_suat_chieu INTEGER PRIMARY KEY AUTOINCREMENT,
    id_phim INTEGER NOT NULL,
    id_phong INTEGER NOT NULL,
    thoi_gian_bat_dau TEXT NOT NULL,
    thoi_gian_ket_thuc TEXT NOT NULL,
    gia_ve_co_ban REAL NOT NULL,
    FOREIGN KEY (id_phim) REFERENCES Phim(id_phim) ON DELETE CASCADE,
    FOREIGN KEY (id_phong) REFERENCES PhongChieu(id_phong) ON DELETE CASCADE
);

-- 7. Bảng Đơn Đặt Vé
CREATE TABLE DonDatVe (
    id_don_ve INTEGER PRIMARY KEY AUTOINCREMENT,
    id_nguoi_dung INTEGER NOT NULL,
    id_suat_chieu INTEGER NOT NULL,
    ngay_dat TEXT DEFAULT CURRENT_TIMESTAMP,
    tong_tien REAL NOT NULL,
    trang_thai TEXT DEFAULT 'ChoThanhToan', -- ChoThanhToan, DaThanhToan, DaHuy
    FOREIGN KEY (id_nguoi_dung) REFERENCES NguoiDung(id_nguoi_dung),
    FOREIGN KEY (id_suat_chieu) REFERENCES SuatChieu(id_suat_chieu)
);

-- 8. Bảng Chi Tiết Vé
CREATE TABLE ChiTietVe (
    id_don_ve INTEGER NOT NULL,
    id_ghe INTEGER NOT NULL,
    gia_mua REAL NOT NULL,
    PRIMARY KEY (id_don_ve, id_ghe),
    FOREIGN KEY (id_don_ve) REFERENCES DonDatVe(id_don_ve),
    FOREIGN KEY (id_ghe) REFERENCES Ghe(id_ghe)
);