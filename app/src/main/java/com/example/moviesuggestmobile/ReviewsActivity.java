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

public class ReviewsActivity extends AppCompatActivity {

    private RecyclerView rvReviews;
    private ReviewsAdapter reviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        rvReviews = findViewById(R.id.rvReviews);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));

        fetchMyReviewsFromCsharp();
    }

    private void fetchMyReviewsFromCsharp() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // ApiService içindeki sadece yorumlu filmleri getiren GET metodumuzu çağırıyoruz
        Call<List<MovieModel>> call = apiService.getMyReviews();

        call.enqueue(new Callback<List<MovieModel>>() {
            @Override
            public void onResponse(Call<List<MovieModel>> call, Response<List<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MovieModel> liveReviews = response.body();

                    if (liveReviews.isEmpty()) {
                        Toast.makeText(ReviewsActivity.this, "Henüz değerlendirdiğiniz bir film yok!", Toast.LENGTH_SHORT).show();
                    }

                    // Canlı verileri alan matbaayı listeye bağlıyoruz
                    reviewsAdapter = new ReviewsAdapter(liveReviews);
                    rvReviews.setAdapter(reviewsAdapter);
                } else {
                    Toast.makeText(ReviewsActivity.this, "Yorumlar çekilemedi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MovieModel>> call, Throwable t) {
                Toast.makeText(ReviewsActivity.this, "Bağlantı Hatası! C# Açık mı?", Toast.LENGTH_LONG).show();
            }
        });
    }
}