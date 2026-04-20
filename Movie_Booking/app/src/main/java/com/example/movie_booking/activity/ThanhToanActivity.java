package com.example.movie_booking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.movie_booking.R;
import com.example.movie_booking.database.AppDatabase;
import com.example.movie_booking.object.ChiTietVe;
import com.example.movie_booking.object.DonDatVe;
import com.example.movie_booking.object.Ghe;
import com.example.movie_booking.object.NguoiDung;
import com.example.movie_booking.object.SuatChieu;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ThanhToanActivity extends AppCompatActivity {

    private TextView tvTimer, tvSoTien;
    private ImageView imgQRCode, btnBack;
    private Button btnXacNhan;
    private CountDownTimer countDownTimer;
    private DecimalFormat formatter = new DecimalFormat("###,###,###");
    
    private AppDatabase db;
    private SuatChieu suatChieu;
    private List<Ghe> selectedGhes;
    private double tongTien;

    // Cấu hình Email gửi đi
    private final String EMAIL_GUI = "lethu1011xx@gmail.com";
    private final String MAT_KHAU_APP = "mrxh tbqr rhlk slve";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        db = AppDatabase.getInstance(this);
        
        tongTien = getIntent().getDoubleExtra("tong_tien", 0);
        suatChieu = (SuatChieu) getIntent().getSerializableExtra("suat_chieu_data");
        selectedGhes = (List<Ghe>) getIntent().getSerializableExtra("selected_ghes");

        tvTimer = findViewById(R.id.tvTimer);
        tvSoTien = findViewById(R.id.tvSoTien);
        imgQRCode = findViewById(R.id.imgQRCode);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        btnBack = findViewById(R.id.btnBack);

        tvSoTien.setText("Số tiền: " + formatter.format(tongTien) + "đ");

        String qrUrl = "https://img.vietqr.io/image/970422-123456789-compact.png?amount=" 
                + (int)tongTien + "&addInfo=ThanhToanVePhim";
        
        Glide.with(this).load(qrUrl).into(imgQRCode);

        startCountdown();

        btnBack.setOnClickListener(v -> finish());
        btnXacNhan.setOnClickListener(v -> xuLyThanhToan());
    }

    private void xuLyThanhToan() {
        if (suatChieu == null || selectedGhes == null || selectedGhes.isEmpty()) {
            Toast.makeText(this, "Lỗi dữ liệu thanh toán!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                List<NguoiDung> users = db.nguoiDungDao().getAll();
                if (users == null || users.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(this, "Lỗi: Không tìm thấy người dùng!", Toast.LENGTH_LONG).show());
                    return;
                }
                NguoiDung currentUser = users.get(0); 

                final long[] finalIdDonHang = new long[1];
                db.runInTransaction(() -> {
                    DonDatVe donHang = new DonDatVe();
                    donHang.setId_nguoi_dung(currentUser.getId_nguoi_dung()); 
                    donHang.setId_suat_chieu(suatChieu.getId_suat_chieu());
                    donHang.setTong_tien(tongTien);
                    donHang.setNgay_dat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                    donHang.setTrang_thai("DaThanhToan");

                    finalIdDonHang[0] = db.donDatVeDao().insert(donHang);
                    
                    double giaCoBan = suatChieu.getGia_ve_co_ban();
                    for (Ghe g : selectedGhes) {
                        double giaMua = "VIP".equalsIgnoreCase(g.getLoai_ghe()) ? (giaCoBan + 10000) : giaCoBan;
                        ChiTietVe ct = new ChiTietVe((int)finalIdDonHang[0], g.getId_ghe(), giaMua);
                        db.chiTietVeDao().insert(ct);
                    }
                });

                String tenPhim = db.phimDao().getPhimById(suatChieu.getId_phim()).getTen_phim();
                String tenRap = db.suatChieuDao().getTenRapBySuatChieu(suatChieu.getId_suat_chieu());
                String tenPhong = db.phongChieuDao().getPhongById(suatChieu.getId_phong()).getTen_phong();
                
                StringBuilder gheNames = new StringBuilder();
                for (int i = 0; i < selectedGhes.size(); i++) {
                    gheNames.append(selectedGhes.get(i).getHang_ghe()).append(selectedGhes.get(i).getSo_ghe());
                    if (i < selectedGhes.size() - 1) gheNames.append(", ");
                }

                // Gửi email xác nhận kèm mã QR vé
                guiEmailXacNhan(currentUser.getEmail(), tenPhim, tenRap, tenPhong, gheNames.toString(), finalIdDonHang[0]);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Thanh toán thành công! Vé đã được gửi về email.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, TrangChuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });

            } catch (Exception e) {
                Log.e("ThanhToanError", "Lỗi xử lý thanh toán", e);
                runOnUiThread(() -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void guiEmailXacNhan(String emailNhan, String tenPhim, String tenRap, String tenPhong, String viTriGhe, long idDonHang) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_GUI, MAT_KHAU_APP);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_GUI));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailNhan));
            message.setSubject("VÉ XEM PHIM ĐIỆN TỬ - " + tenPhim.toUpperCase());

            // Tạo link mã QR dựa trên mã đơn hàng
            String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=MOVIE_TICKET_" + idDonHang;

            // Nội dung Email dạng HTML
            String htmlContent = "<html><body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>" +
                    "<div style='max-width: 600px; margin: auto; border: 1px solid #ddd; padding: 20px; border-radius: 10px;'>" +
                    "<h2 style='color: #e50914; text-align: center;'>XÁC NHẬN ĐẶT VÉ THÀNH CÔNG</h2>" +
                    "<p>Chào bạn, chúc mừng bạn đã đặt vé thành công tại <b>Movie Booking</b>.</p>" +
                    "<hr>" +
                    "<h3>THÔNG TIN VÉ</h3>" +
                    "<p><b>🎬 Phim:</b> " + tenPhim + "</p>" +
                    "<p><b>📅 Suất chiếu:</b> " + suatChieu.getThoi_gian_bat_dau() + "</p>" +
                    "<p><b>📍 Rạp:</b> " + tenRap + "</p>" +
                    "<p><b>🚪 Phòng chiếu:</b> " + tenPhong + "</p>" +
                    "<p><b>💺 Vị trí ghế:</b> <span style='color: #e50914; font-weight: bold;'>" + viTriGhe + "</span></p>" +
                    "<p><b>💰 Tổng tiền:</b> " + formatter.format(tongTien) + "đ</p>" +
                    "<hr>" +
                    "<div style='text-align: center;'>" +
                    "<p><b>MÃ QR VÀO CỔNG:</b></p>" +
                    "<img src='" + qrUrl + "' width='200' height='200' style='border: 5px solid #333; padding: 5px;' alt='Mã QR Vé'>" +
                    "<p style='font-size: 12px; color: #666;'><i>(Vui lòng đưa mã này cho nhân viên soát vé tại rạp)</i></p>" +
                    "</div>" +
                    "<p style='text-align: center; margin-top: 20px;'>Chúc bạn có những giây phút xem phim vui vẻ!</p>" +
                    "</div>" +
                    "</body></html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");
            
            Transport.send(message);
            Log.d("EmailSuccess", "Mail HTML kèm mã QR đã được gửi tới: " + emailNhan);
        } catch (MessagingException e) {
            Log.e("EmailError", "Lỗi gửi mail: " + e.getMessage());
        }
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(10 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }
            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
