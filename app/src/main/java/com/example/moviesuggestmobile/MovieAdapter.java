package com.example.moviesuggestmobile;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieModel> movieList;

    public MovieAdapter(List<MovieModel> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieModel movie = movieList.get(position);
        holder.tvMovieTitle.setText(movie.getTitle());

        // --- GÖRSEL MÜHÜRLER: GLIDE MOTORU ---
        String fullPosterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();

        Glide.with(holder.itemView.getContext())
                .load(fullPosterUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.ivMoviePoster);

        // Detay Butonuna Basıldığında (Alt Pencere Açılması)
        holder.btnMovieDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext());
                LinearLayout layout = new LinearLayout(v.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(60, 60, 60, 80);
                layout.setBackgroundColor(android.graphics.Color.parseColor("#1E1E1E"));

                TextView titleView = new TextView(v.getContext());
                titleView.setText(movie.getTitle());
                titleView.setTextColor(android.graphics.Color.parseColor("#FFD700"));
                titleView.setTextSize(24);
                titleView.setTypeface(null, Typeface.BOLD);
                titleView.setPadding(0, 0, 0, 24);
                layout.addView(titleView);

                layout.addView(createDetailTextView(v.getContext(), "Orijinal Başlık: ", movie.getOriginalTitle()));
                layout.addView(createDetailTextView(v.getContext(), "Orijinal Dil: ", movie.getOriginalLanguage().toUpperCase()));
                layout.addView(createDetailTextView(v.getContext(), "Popülerlik: ", String.valueOf(movie.getPopularity())));
                layout.addView(createDetailTextView(v.getContext(), "🍿 IMDb Puanı: ", movie.getVoteAverage() + " / 10"));

                TextView overviewTitle = new TextView(v.getContext());
                overviewTitle.setText("\nÖzet:");
                overviewTitle.setTextColor(android.graphics.Color.parseColor("#FFD700"));
                overviewTitle.setTextSize(16);
                overviewTitle.setTypeface(null, Typeface.BOLD);
                layout.addView(overviewTitle);

                TextView overviewView = new TextView(v.getContext());
                overviewView.setText(movie.getOverview());
                overviewView.setTextColor(android.graphics.Color.WHITE);
                overviewView.setTextSize(15);
                overviewView.setPadding(0, 8, 0, 0);
                layout.addView(overviewView);

                bottomSheetDialog.setContentView(layout);
                bottomSheetDialog.show();
            }
        });

        // İzleme Listeme Ekle Butonuna Basıldığında
        holder.btnAddToWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Context viewContext = v.getContext();

                // --- KULLANICI AYRIMI: Giriş yapan aktif kullanıcının ID'sini çekiyoruz ---
                int currentUserId = SharedPrefManager.getInstance(viewContext).getUserId();

                if (currentUserId == -1) {
                    Toast.makeText(viewContext, "Oturum hatası! Lütfen tekrar giriş yapın.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

                // Dinamik 'currentUserId' parametresini C#'a yolluyoruz
                Call<ResponseBody> call = apiService.addToWatchlist(
                        movie.getId(),
                        movie.getTitle(),
                        currentUserId,
                        movie.getPosterPath() != null ? movie.getPosterPath() : "default_poster.jpg"
                );

                // Temizlenmiş ve düzeltilmiş metodu çağırıyoruz Bilal
                sendRequestToRetrofit(call, viewContext, movie.getTitle());
            }
        });
    }

    // --- GÜVENLİ RETROFIT ÇALIŞTIRMA MOTORU ---
    private void sendRequestToRetrofit(Call<ResponseBody> call, android.content.Context viewContext, String movieTitle) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(viewContext, movieTitle + " İzleme Listene Eklendi! 🍿", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(viewContext, "Ekleme başarısız oldu: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(viewContext, "Bağlantı Hatası!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private TextView createDetailTextView(android.content.Context context, String label, String value) {
        TextView textView = new TextView(context);
        textView.setText(label + value);
        textView.setTextColor(android.graphics.Color.WHITE);
        textView.setTextSize(15);
        textView.setPadding(0, 4, 0, 4);
        return textView;
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitle;
        ImageView ivMoviePoster;
        Button btnAddToWatchlist, btnMovieDetails;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            ivMoviePoster = itemView.findViewById(R.id.ivMoviePoster);
            btnAddToWatchlist = itemView.findViewById(R.id.btnAddToWatchlist);
            btnMovieDetails = itemView.findViewById(R.id.btnMovieDetails);
        }
    }
}