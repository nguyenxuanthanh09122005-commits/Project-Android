const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const sqlite3 = require('sqlite3').verbose();
const path = require('path');
const nodemailer = require('nodemailer');

const app = express();
app.use(cors());
app.use(bodyParser.json());

const dbPath = path.resolve(__dirname, 'movie_booking.db');
const db = new sqlite3.Database(dbPath);

// Cấu hình gửi Email (Sử dụng Gmail)
// HƯỚNG DẪN: Bạn cần tạo "Mật khẩu ứng dụng" (App Password) trong tài khoản Google để gửi được mail
const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'your_email@gmail.com', // Thay bằng email thật của bạn
        pass: 'your_app_password'     // Thay bằng mật khẩu ứng dụng thật (16 ký tự)
    }
});

// Lưu trữ mã OTP tạm thời trong bộ nhớ (Email -> {otp, expires})
const otpStore = {};

app.post('/auth/register', (req, res) => {
    const { ho_ten, email, mat_khau, so_dien_thoai } = req.body;
    const vai_tro = 'KhachHang';
    const sql = `INSERT INTO NguoiDung (ho_ten, email, mat_khau, so_dien_thoai, vai_tro) VALUES (?, ?, ?, ?, ?)`;
    db.run(sql, [ho_ten, email, mat_khau, so_dien_thoai, vai_tro], function(err) {
        if (err) return res.status(500).json({ message: "Lỗi Server!" });
        res.status(200).json({ message: "Đăng ký thành công!" });
    });
});

app.post('/auth/login', (req, res) => {
    const { email } = req.body;
    const mat_khau_android = req.body.mat_khau || req.body.password;
    const sql = `SELECT * FROM NguoiDung WHERE email = ?`;
    db.get(sql, [email], (err, user) => {
        if (err) return res.status(500).json({ message: "Lỗi Server!" });
        if (user && user.mat_khau === mat_khau_android) {
            res.status(200).json({ token: "token-" + Date.now(), role: user.vai_tro, message: "Đăng nhập thành công" });
        } else {
            res.status(401).json({ message: "Sai tài khoản hoặc mật khẩu!" });
        }
    });
});

// 1. API GỬI MÃ OTP QUÊN MẬT KHẨU
app.post('/auth/forgot-password', (req, res) => {
    const { email } = req.body;

    db.get("SELECT * FROM NguoiDung WHERE email = ?", [email], (err, user) => {
        if (err) return res.status(500).json({ message: "Lỗi database" });
        if (!user) return res.status(404).json({ message: "Email không tồn tại!" });

        // Tạo mã 6 số ngẫu nhiên
        const otp = Math.floor(100000 + Math.random() * 900000).toString();
        // Lưu OTP vào bộ nhớ (hết hạn sau 5 phút)
        otpStore[email] = { otp: otp, expires: Date.now() + 300000 };

        console.log(`[FORGOT PASSWORD] OTP cho ${email}: ${otp}`);

        const mailOptions = {
            from: '"Movie Booking Support" <your_email@gmail.com>',
            to: email,
            subject: 'Mã xác nhận đặt lại mật khẩu',
            text: `Mã OTP của bạn là: ${otp}. Mã này có hiệu lực trong 5 phút.`
        };

        transporter.sendMail(mailOptions, (error, info) => {
            if (error) {
                console.log("Lỗi gửi mail:", error.message);
                // Giả lập cho dev: Vẫn báo thành công nhưng hiện mã ở Terminal để test
                return res.status(200).json({
                    message: "Đã tạo mã xác nhận (Lỗi gửi mail, hãy xem mã ở Terminal của Server)",
                    dev_mode: true
                });
            }
            res.status(200).json({ message: "Mã xác nhận đã được gửi tới email của bạn!" });
        });
    });
});

// 2. API ĐẶT LẠI MẬT KHẨU MỚI
app.post('/auth/reset-password', (req, res) => {
    const { email, otp, mat_khau_moi } = req.body;
    const record = otpStore[email];

    if (record && record.otp === otp) {
        if (Date.now() > record.expires) {
            delete otpStore[email];
            return res.status(400).json({ message: "Mã OTP đã hết hạn!" });
        }

        db.run("UPDATE NguoiDung SET mat_khau = ? WHERE email = ?", [mat_khau_moi, email], function(err) {
            if (err) return res.status(500).json({ message: "Lỗi cập nhật mật khẩu" });

            delete otpStore[email];
            res.status(200).json({ message: "Đổi mật khẩu thành công!" });
        });
    } else {
        res.status(400).json({ message: "Mã xác nhận (OTP) không chính xác!" });
    }
});

const PORT = 3000;
app.listen(PORT, () => {
    console.log(`SERVER ĐANG CHẠY TẠI PORT: ${PORT}`);
});
