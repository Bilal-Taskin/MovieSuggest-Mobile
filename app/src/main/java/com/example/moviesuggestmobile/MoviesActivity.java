package com.example.moviesuggestmobile;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<MovieModel> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        recyclerView = findViewById(R.id.rvMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList = new ArrayList<>();

        // C# İnternet köprümüzü kurup popüler filmleri çağıran motoru ateşliyoruz
        fetchPopularMovies();
    }

    private void fetchPopularMovies() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.getPopularMovies();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // C#'tan gelen ham string cevabını alıyoruz
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        JSONArray resultsArray = jsonObject.getJSONArray("results");

                        movieList.clear();

                        for (int i = 0; i < resultsArray.length(); i++) {
                            JSONObject movieObject = resultsArray.getJSONObject(i);

                            // --- CAN ALICI DÜZELTME: Modelimizin beklediği 8 parçayı da JSON içinden söküyoruz ---
                            int id = movieObject.getInt("id");
                            String title = movieObject.getString("title");
                            String originalLanguage = movieObject.getString("original_language");
                            String originalTitle = movieObject.getString("original_title");
                            String overview = movieObject.getString("overview");
                            double popularity = movieObject.getDouble("popularity");
                            double voteAverage = movieObject.getDouble("vote_average");
                            String posterPath = movieObject.optString("poster_path", "default_poster.jpg");

                            // Artık fabrikamız (Constructor) eksiksiz 8 parametreyle tıkır tıkır çalışıyor Bilal!
                            MovieModel movie = new MovieModel(id, title, originalLanguage, originalTitle, overview, popularity, voteAverage, posterPath);
                            movieList.add(movie);
                        }

                        // Verileri matbaaya verip ekrana jilet gibi basıyoruz
                        movieAdapter = new MovieAdapter(movieList);
                        recyclerView.setAdapter(movieAdapter);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MoviesActivity.this, "Veri ayrıştırma hatası!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MoviesActivity.this, "Popüler filmler getirilemedi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MoviesActivity.this, "Bağlantı Hatası! C# Açık mı?", Toast.LENGTH_LONG).show();
            }
        });
    }
}