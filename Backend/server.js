const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const sqlite3 = require('sqlite3').verbose();
const path = require('path');

const app = express();
app.use(cors());
app.use(bodyParser.json());

// Đường dẫn chuẩn xác dựa trên image_66657e.png
const dbPath = path.resolve(__dirname, 'movie_booking.db');

const db = new sqlite3.Database(dbPath, (err) => {
    if (err) {
        console.error("Lỗi kết nối SQLite:", err.message);
    } else {
        // KÍCH HOẠT CHẾ ĐỘ WAL: Giúp dữ liệu ghi xuống file ngay lập tức
        // để DB Browser có thể thấy ngay mà không bị kẹt trong bộ nhớ đệm.
        db.run("PRAGMA journal_mode = WAL;");
        console.log("------------------------------------------");
        console.log("KẾT NỐI DATABASE THÀNH CÔNG!");
        console.log("ĐƯỜNG DẪN FILE:", dbPath);
        console.log("------------------------------------------");

        db.run(`CREATE TABLE IF NOT EXISTS NguoiDung (
            id_nguoi_dung INTEGER PRIMARY KEY AUTOINCREMENT,
            ho_ten TEXT,
            email TEXT UNIQUE,
            mat_khau TEXT,
            so_dien_thoai TEXT,
            vai_tro TEXT DEFAULT 'KhachHang'
        )`);
    }
});

app.post('/auth/register', (req, res) => {
    const { ho_ten, email, mat_khau, so_dien_thoai } = req.body;
    const vai_tro = 'KhachHang';

    const sql = `INSERT INTO NguoiDung (ho_ten, email, mat_khau, so_dien_thoai, vai_tro) VALUES (?, ?, ?, ?, ?)`;

    db.run(sql, [ho_ten, email, mat_khau, so_dien_thoai, vai_tro], function(err) {
        if (err) {
            console.error("Lỗi INSERT:", err.message);
            return res.status(500).json({ message: "Lỗi Server!" });
        }
        // Thông báo này xuất hiện trong image_66657e.png xác nhận đã lưu thành công
        console.log(`[ĐĂNG KÝ] Đã thêm tài khoản: ${email}`);
        res.status(200).json({ message: "Đăng ký thành công!" });
    });
});

app.post('/auth/login', (req, res) => {
    const { email } = req.body;
    // Hỗ trợ cả trường hợp Android gửi biến 'mat_khau' hoặc 'password'
    const mat_khau_android = req.body.mat_khau || req.body.password;

    console.log(`[LOGIN] Đang kiểm tra: ${email}`);

    const sql = `SELECT * FROM NguoiDung WHERE email = ?`;
    db.get(sql, [email], (err, user) => {
        if (err) return res.status(500).json({ message: "Lỗi Server!" });

        if (user) {
            // So sánh mật khẩu trực tiếp
            if (user.mat_khau === mat_khau_android) {
                console.log("=> Đăng nhập THÀNH CÔNG");
                res.status(200).json({
                    token: "token-" + Date.now(),
                    role: user.vai_tro,
                    message: "Đăng nhập thành công"
                });
            } else {
                console.log(`=> THẤT BẠI: Sai mật khẩu (Nhận: [${mat_khau_android}] - Trong DB: [${user.mat_khau}])`);
                res.status(401).json({ message: "Mật khẩu không đúng!" });
            }
        } else {
            console.log("=> THẤT BẠI: Email không tồn tại");
            res.status(401).json({ message: "Tài khoản không tồn tại!" });
        }
    });
});

const PORT = 3000;
app.listen(PORT, () => {
    console.log(`SERVER ĐANG CHẠY TẠI PORT: ${PORT}`);
});