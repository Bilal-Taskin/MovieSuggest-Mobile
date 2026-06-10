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

public class WatchedMoviesActivity extends AppCompatActivity {

    private RecyclerView rvWatchedMovies;
    private WatchedMoviesAdapter watchedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched_movies);

        rvWatchedMovies = findViewById(R.id.rvWatchedMovies);
        rvWatchedMovies.setLayoutManager(new LinearLayoutManager(this));

        // Sayfa açılır açılmaz C# merkezinden canlı verileri çekiyoruz
        fetchWatchedMoviesFromCsharp();
    }

    private void fetchWatchedMoviesFromCsharp() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // ApiService içindeki List<MovieModel> dönen GET metodumuzu tetikliyoruz
        Call<List<MovieModel>> call = apiService.getMyWatchedMovies();

        call.enqueue(new Callback<List<MovieModel>>() {
            @Override
            public void onResponse(Call<List<MovieModel>> call, Response<List<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MovieModel> liveWatchedList = response.body();

                    if (liveWatchedList.isEmpty()) {
                        Toast.makeText(WatchedMoviesActivity.this, "Henüz izlenmiş bir film bulunmuyor!", Toast.LENGTH_SHORT).show();
                    }

                    // Gelen canlı listeyi yeni matbaaya verip ekrana basıyoruz
                    watchedAdapter = new WatchedMoviesAdapter(liveWatchedList);
                    rvWatchedMovies.setAdapter(watchedAdapter);
                } else {
                    Toast.makeText(WatchedMoviesActivity.this, "Veriler C#'tan çekilemedi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MovieModel>> call, Throwable t) {
                Toast.makeText(WatchedMoviesActivity.this, "Bağlantı Hatası! C# Backend projeniz çalışıyor mu?", Toast.LENGTH_LONG).show();
            }
        });
    }
}