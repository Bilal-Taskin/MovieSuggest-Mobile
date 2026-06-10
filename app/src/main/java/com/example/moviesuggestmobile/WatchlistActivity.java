package com.example.moviesuggestmobile;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchlistActivity extends AppCompatActivity {

    private RecyclerView rvWatchlist;
    private WatchlistAdapter watchlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);

        rvWatchlist = findViewById(R.id.rvWatchlist);
        rvWatchlist.setLayoutManager(new LinearLayoutManager(this));

        fetchWatchlistFromCsharp();
    }

    private void fetchWatchlistFromCsharp() {
        // --- CAN ALICI DÜZELTME: Giriş yapan gerçek kullanıcının ID'sini hafızadan çekiyoruz ---
        int activeUserId = SharedPrefManager.getInstance(this).getUserId();

        if (activeUserId == -1) {
            Toast.makeText(this, "Kullanıcı oturumu bulunamadı! Lütfen tekrar giriş yapın.", Toast.LENGTH_LONG).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Artık C#'a dinamik olarak giriş yapan adamın ID'sini postalıyoruz Bilal!
        Call<List<MovieModel>> call = apiService.getMyWatchlist(activeUserId);

        call.enqueue(new Callback<List<MovieModel>>() {
            @Override
            public void onResponse(Call<List<MovieModel>> call, Response<List<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MovieModel> dbWatchlist = response.body();

                    if (dbWatchlist.isEmpty()) {
                        Toast.makeText(WatchlistActivity.this, "İzleme listeniz şu an boş!", Toast.LENGTH_SHORT).show();
                    }

                    watchlistAdapter = new WatchlistAdapter(dbWatchlist, WatchlistActivity.this);
                    rvWatchlist.setAdapter(watchlistAdapter);
                } else {
                    Toast.makeText(WatchlistActivity.this, "Veriler çekilemedi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MovieModel>> call, Throwable t) {
                Toast.makeText(WatchlistActivity.this, "Bağlantı Hatası!", Toast.LENGTH_LONG).show();
            }
        });
    }
}