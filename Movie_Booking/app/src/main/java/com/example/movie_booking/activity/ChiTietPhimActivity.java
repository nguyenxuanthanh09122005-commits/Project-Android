package com.example.movie_booking.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.movie_booking.R;
import com.example.movie_booking.object.Phim;

public class ChiTietPhimActivity extends AppCompatActivity {

    private ImageView imgBackdrop, imgPoster, btnBack, btnPlayTrailer;
    private TextView tvMovieTitle, tvAgeRating, tvGenre, tvDuration, tvReleaseDate, tvDescription;
    private Button btnShare;
    private Phim phim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phim);

        // Ánh xạ View
        initViews();

        // Nhận dữ liệu phim từ Intent
        phim = (Phim) getIntent().getSerializableExtra("phim_data");

        if (phim != null) {
            displayMovieDetails();
        } else {
            Toast.makeText(this, "Không có dữ liệu phim!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Sự kiện nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Sự kiện nút Chia sẻ
        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, phim.getTen_phim());
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Xem phim " + phim.getTen_phim() + " tại Movie Booking ngay!");
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
        });
        
        // Sự kiện xem Trailer
        btnPlayTrailer.setOnClickListener(v -> {
            if (phim.getTrailer_url() != null && !phim.getTrailer_url().isEmpty()) {
                showTrailerDialog(phim.getTrailer_url());
            } else {
                Toast.makeText(this, "Phim này hiện chưa có trailer!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        imgBackdrop = findViewById(R.id.imgBackdrop);
        imgPoster = findViewById(R.id.imgPoster);
        btnBack = findViewById(R.id.btnBack);
        btnPlayTrailer = findViewById(R.id.btnPlayTrailer);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvAgeRating = findViewById(R.id.tvAgeRating);
        tvGenre = findViewById(R.id.tvGenre);
        tvDuration = findViewById(R.id.tvDuration);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvDescription = findViewById(R.id.tvDescription);
        btnShare = findViewById(R.id.btnShare);
    }

    private void displayMovieDetails() {
        tvMovieTitle.setText(phim.getTen_phim());
        tvAgeRating.setText("Phân loại: " + (phim.getDo_tuoi_quy_dinh() != null ? phim.getDo_tuoi_quy_dinh() : "N/A"));
        tvGenre.setText(phim.getThe_loai());
        tvDuration.setText(phim.getThoi_luong() + " phút");
        tvReleaseDate.setText(phim.getNgay_khoi_chieu());
        tvDescription.setText(phim.getMo_ta());

        String tenHinhAnh = phim.getAnh_poster();
        int resId = 0;
        if (tenHinhAnh != null && !tenHinhAnh.isEmpty()) {
            if (tenHinhAnh.contains(".")) {
                tenHinhAnh = tenHinhAnh.substring(0, tenHinhAnh.lastIndexOf("."));
            }
            resId = getResources().getIdentifier(tenHinhAnh, "drawable", getPackageName());
        }

        Object imageSource = (resId != 0) ? resId : R.drawable.logo_beta;

        Glide.with(this)
                .load(imageSource)
                .placeholder(R.drawable.placeholder_backdrop)
                .into(imgBackdrop);

        Glide.with(this)
                .load(imageSource)
                .placeholder(R.drawable.placeholder_poster)
                .into(imgPoster);
    }

    private void showTrailerDialog(String trailerUrl) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_trailer);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WebView wvTrailer = dialog.findViewById(R.id.wvTrailer);
        ProgressBar pbLoading = dialog.findViewById(R.id.pbLoading);
        Button btnClose = dialog.findViewById(R.id.btnCloseTrailer);

        // Cấu hình WebView để phát video
        WebSettings webSettings = wvTrailer.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        
        wvTrailer.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                pbLoading.setVisibility(View.GONE);
            }
        });

        wvTrailer.setWebChromeClient(new WebChromeClient());

        // Xử lý link YouTube để có thể chạy trong WebView (chuyển sang dạng embed)
        String finalUrl = trailerUrl;
        if (trailerUrl.contains("watch?v=")) {
            finalUrl = trailerUrl.replace("watch?v=", "embed/");
        } else if (trailerUrl.contains("youtu.be/")) {
            finalUrl = trailerUrl.replace("youtu.be/", "youtube.com/embed/");
        }

        // Load video với iframe để hỗ trợ full màn hình/tự động scale
        String html = "<html><body style='margin:0;padding:0;'><iframe width=\"100%\" height=\"100%\" src=\"" + finalUrl + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        wvTrailer.loadData(html, "text/html", "utf-8");

        btnClose.setOnClickListener(v -> {
            wvTrailer.destroy();
            dialog.dismiss();
        });

        dialog.setOnDismissListener(d -> wvTrailer.destroy());

        dialog.show();
    }
}
