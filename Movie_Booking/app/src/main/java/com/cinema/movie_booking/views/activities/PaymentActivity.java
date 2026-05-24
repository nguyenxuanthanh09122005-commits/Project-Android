package com.cinema.movie_booking.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cinema.movie_booking.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        String movieName = getIntent().getStringExtra("movieName");
        String cinemaInfo = getIntent().getStringExtra("cinemaInfo");
        String seatInfo = getIntent().getStringExtra("seatInfo");
        String totalPrice = getIntent().getStringExtra("totalPrice");

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ((TextView) findViewById(R.id.txtMovieName)).setText(movieName);
        ((TextView) findViewById(R.id.txtCinemaInfo)).setText(cinemaInfo);
        ((TextView) findViewById(R.id.txtSeatInfo)).setText("Ghế: " + seatInfo);
        ((TextView) findViewById(R.id.txtPaymentAmount)).setText("Tổng tiền: " + totalPrice);

        MaterialButton btnConfirm = findViewById(R.id.btnConfirmPayment);
        btnConfirm.setOnClickListener(v -> {
            Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
            
            // Quay về màn hình chính hoặc màn hình vé
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
