-- Chèn dữ liệu Người dùng
INSERT INTO NguoiDung (ho_ten, email, mat_khau, so_dien_thoai, vai_tro) VALUES 
('Nguyễn Văn An', 'admin@cinema.com', 'hashed_password_123', '0901234567', 'Admin'),
('Trần Thị Bình', 'binh.tran@gmail.com', 'password456', '0912345678', 'KhachHang');

-- Chèn dữ liệu Phim
INSERT INTO Phim (ten_phim, mo_ta, thoi_luong, ngay_khoi_chieu, anh_poster, trailer_url, the_loai, do_tuoi_quy_dinh) VALUES 
('Sonic the Hedgehog 2', 'Nhím Sonic trở lại trong cuộc phiêu lưu mới.', 122, '2026-04-01', 'sonic2_poster.jpg', 'url_sonic', 'Hành Động, Phiêu Lưu', 'P'),
('Morbius', 'Tiến sĩ Michael Morbius và hành trình biến thành ma cà rồng.', 104, '2026-04-05', 'morbius_poster.jpg', 'url_morbius', 'Hành Động, Kinh Dị', 'C16'),
('Black Panther: Wakanda Forever', 'Cuộc chiến bảo vệ Wakanda.', 161, '2026-05-20', 'black_panther_poster.jpg', 'url_bp', 'Hành Động', 'C13'),
('Aquaman and the Lost Kingdom', 'Hành trình mới của vua biển cả.', 120, '2026-06-15', 'aquaman_poster.jpg', 'url_aqua', 'Hành Động, Viễn Tưởng', 'C13'),
('Avatar: The Way of Water', 'Câu chuyện tiếp theo về hành tinh Pandora.', 192, '2026-07-01', 'avatar2_poster.jpg', 'url_avatar', 'Viễn Tưởng', 'P'),
('The Batman', 'Thám tử lừng danh đối đầu với Riddler.', 176, '2026-03-10', 'batman_poster.jpg', 'url_batman', 'Hành Động, Hình Sự', 'C16'),
('Father Stu', 'Dựa trên câu chuyện có thật về võ sĩ thành linh mục.', 124, '2026-03-15', 'father_stu_poster.jpg', 'url_stu', 'Chính Kịch', 'C13');

-- Thêm Rạp
INSERT INTO Rap (ten_rap, dia_chi, thanh_pho) VALUES 
('Cinema Star Hà Nội', 'Số 1 Thái Hà', 'Hà Nội'),
('Cinema Star TP.HCM', 'Lê Thánh Tôn, Quận 1', 'TP. Hồ Chí Minh');

-- Thêm Phòng Chiếu
INSERT INTO PhongChieu (id_rap, ten_phong, tong_so_ghe) VALUES 
(1, 'Phòng 01 - IMAX', 100),
(1, 'Phòng 02 - 2D', 50),
(2, 'Phòng VIP', 20);

-- Thêm ghế cho Phòng 2 (Hàng A)
INSERT INTO Ghe (id_phong, hang_ghe, so_ghe, loai_ghe) VALUES 
(2, 'A', 1, 'Thuong'),
(2, 'A', 2, 'Thuong'),
(2, 'A', 3, 'VIP'),
(2, 'A', 4, 'VIP');

-- Suất chiếu cho phim Sonic (id_phim = 1) tại Phòng 2 (id_phong = 2)
INSERT INTO SuatChieu (id_phim, id_phong, thoi_gian_bat_dau, thoi_gian_ket_thuc, gia_ve_co_ban) VALUES 
(1, 2, '2026-04-20 19:00:00', '2026-04-20 21:02:00', 85000.00),
(1, 2, '2026-04-20 21:30:00', '2026-04-20 23:32:00', 95000.00);

-- Khách hàng id=2 đặt vé cho suất chiếu id=1
INSERT INTO DonDatVe (id_nguoi_dung, id_suat_chieu, ngay_dat, tong_tien, trang_thai) VALUES 
(2, 1, CURRENT_TIMESTAMP, 170000.00, 'DaThanhToan');

-- Chi tiết: Đặt 2 ghế (id_ghe 1 và 2) cho đơn hàng trên (id_don_ve = 1)
INSERT INTO ChiTietVe (id_don_ve, id_ghe, gia_mua) VALUES 
(1, 1, 85000.00),
(1, 2, 85000.00);