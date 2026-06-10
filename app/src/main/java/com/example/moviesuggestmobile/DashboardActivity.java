package com.example.moviesuggestmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DashboardActivity extends AppCompatActivity {

    private CardView cardPopularMovies, cardWatchlist, cardWatchedMovies, cardReviews;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Modern Kartların ve Çıkış Butonunun Kablolaması
        cardPopularMovies = findViewById(R.id.cardPopularMovies);
        cardWatchlist = findViewById(R.id.cardWatchlist);
        cardWatchedMovies = findViewById(R.id.cardWatchedMovies);
        cardReviews = findViewById(R.id.cardReviews);
        btnLogout = findViewById(R.id.btnLogout);

        // 1. Popüler Filmler Kartına Tıklandığında
        cardPopularMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MoviesActivity.class);
                startActivity(intent);
            }
        });

        // 2. İzleme Listem Kartına Tıklandığında
        cardWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, WatchlistActivity.class);
                startActivity(intent);
            }
        });

        // 3. İzlediğim Filmler Kartına Tıklandığında
        cardWatchedMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, WatchedMoviesActivity.class);
                startActivity(intent);
            }
        });

        // 4. Değerlendirmeler Kartına Tıklandığında
        cardReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ReviewsActivity.class);
                startActivity(intent);
            }
        });

        // --- BÜYÜK ÇIKIŞ KAPISI: Oturumu kapatıp giriş ekranına döndüren motor ---
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adım A: Telefon yerel hafızasındaki aktif kullanıcı ID'sini sıfırlıyoruz (-1 yapıyoruz)
                SharedPrefManager.getInstance(DashboardActivity.this).saveUserId(-1);

                Toast.makeText(DashboardActivity.this, "Oturum güvenli bir şekilde kapatıldı! 🚪", Toast.LENGTH_SHORT).show();

                // Adım B: Giriş ekranına (MainActivity) yönlendiriyoruz
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                // Geri tuşuna basınca Dashboard'un tekrar açılmaması için tüm geçmişi temizliyoruz Bilal
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}