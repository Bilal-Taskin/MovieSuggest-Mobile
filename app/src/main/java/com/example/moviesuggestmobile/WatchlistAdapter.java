package com.example.moviesuggestmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.HashMap;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder> {

    private List<MovieModel> watchlist;
    private Context context;

    public WatchlistAdapter(List<MovieModel> watchlist, Context context) {
        this.watchlist = watchlist;
        this.context = context;
    }

    @NonNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_watchlist, parent, false);
        return new WatchlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistViewHolder holder, int position) {
        MovieModel movie = watchlist.get(holder.getAdapterPosition());
        holder.tvWatchlistMovieTitle.setText(movie.getTitle());

        String path = movie.getPosterPath();
        if (path != null && !path.isEmpty() && !path.equals("default_poster.jpg")) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            String fullPosterUrl = "https://image.tmdb.org/t/p/w500" + path;

            Glide.with(context)
                    .load(fullPosterUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(holder.ivWatchlistPoster);
        } else {
            holder.ivWatchlistPoster.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.btnMarkAsWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_review, null);

                TextView tvDialogMovieTitle = dialogView.findViewById(R.id.tvDialogMovieTitle);
                RatingBar dialogRatingBar = dialogView.findViewById(R.id.dialogRatingBar);
                EditText etDialogComment = dialogView.findViewById(R.id.etDialogComment);
                Button btnDialogCancel = dialogView.findViewById(R.id.btnDialogCancel);
                Button btnDialogSave = dialogView.findViewById(R.id.btnDialogSave);

                tvDialogMovieTitle.setText(movie.getTitle());

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setView(dialogView)
                        .create();

                btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                btnDialogSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentUserId = SharedPrefManager.getInstance(context).getUserId();

                        if (currentUserId == -1) {
                            Toast.makeText(context, "Oturum hatası! Lütfen tekrar giriş yapın.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

                        // --- BÜYÜK MICROSOFT MODEL MÜHÜRÜ: WatchedMovie.cs modelindeki TÜM alanları eksiksiz dolduruyoruz Bilal! ---
                        HashMap<String, Object> movieParams = new HashMap<>();

                        // Girişte doldurduğumuz temel kimlikler ve değerlendirmeler
                        movieParams.put("MovieId", movie.getId());
                        movieParams.put("Title", movie.getTitle());
                        movieParams.put("MovieTitle", movie.getTitle());
                        movieParams.put("UserId", currentUserId);
                        movieParams.put("MoviePosterPath", movie.getPosterPath() != null ? movie.getPosterPath() : "default_poster.jpg");
                        movieParams.put("UserRating", dialogRatingBar.getRating()); // C# float bekliyor, Java otomatik match eder
                        movieParams.put("UserComment", etDialogComment.getText().toString());

                        // --- 400 HATASINI SÖNDÜREN EKSİKSİZ ZORUNLU VERİTABANI ALANLARI ---
                        movieParams.put("OriginalLanguage", movie.getOriginalLanguage() != null ? movie.getOriginalLanguage() : "en");
                        movieParams.put("OriginalTitle", movie.getOriginalTitle() != null ? movie.getOriginalTitle() : movie.getTitle());
                        movieParams.put("Overview", movie.getOverview() != null ? movie.getOverview() : "Özet bulunmuyor.");
                        movieParams.put("Popularity", movie.getPopularity());
                        movieParams.put("VoteAverage", movie.getVoteAverage());

                        // JSON paketini C#[FromBody] kapısına fırlatıyoruz
                        Call<ResponseBody> call = apiService.addWatchedMovieWithReview(movieParams);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, movie.getTitle() + " Başarıyla Arşivlendi! 🍿", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();

                                    int currentPos = holder.getAdapterPosition();
                                    if (currentPos != RecyclerView.NO_POSITION) {
                                        watchlist.remove(currentPos);
                                        notifyItemRemoved(currentPos);
                                        notifyItemRangeChanged(currentPos, watchlist.size());
                                    }
                                } else {
                                    Toast.makeText(context, "Sunucu Hatası: " + response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(context, "Bağlantı Hatası! C# Açık mı?", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return watchlist != null ? watchlist.size() : 0;
    }

    public static class WatchlistViewHolder extends RecyclerView.ViewHolder {
        TextView tvWatchlistMovieTitle;
        ImageView ivWatchlistPoster;
        Button btnMarkAsWatched;

        public WatchlistViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWatchlistMovieTitle = itemView.findViewById(R.id.tvWatchlistMovieTitle);
            ivWatchlistPoster = itemView.findViewById(R.id.ivWatchlistPoster);
            btnMarkAsWatched = itemView.findViewById(R.id.btnMarkAsWatched);
        }
    }
}