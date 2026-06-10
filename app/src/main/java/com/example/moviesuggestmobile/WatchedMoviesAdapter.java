package com.example.moviesuggestmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class WatchedMoviesAdapter extends RecyclerView.Adapter<WatchedMoviesAdapter.WatchedViewHolder> {

    private List<MovieModel> watchedList;

    public WatchedMoviesAdapter(List<MovieModel> watchedList) {
        this.watchedList = watchedList;
    }

    @NonNull
    @Override
    public WatchedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_watched_movie, parent, false);
        return new WatchedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchedViewHolder holder, int position) {
        MovieModel movie = watchedList.get(position);
        holder.tvWatchedMovieTitle.setText(movie.getTitle());
        holder.tvWatchedUserComment.setText(movie.getUserComment() != null ? movie.getUserComment() : "Yorum yapılmamış.");

        // --- KESİN GÖRSEL ÇÖZÜM: GERÇEK VERİTABANI AFİŞ MOTORU ---
        String path = movie.getPosterPath();

        if (path != null && !path.isEmpty() && !path.equals("default_poster.jpg")) {
            // Veritabanından gelen kodun başında taksim yoksa ekliyoruz
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            String fullPosterUrl = "https://image.tmdb.org/t/p/w500" + path;

            Glide.with(holder.itemView.getContext())
                    .load(fullPosterUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(holder.ivWatchedPoster);
        } else {
            // Eğer eski verilerden ötürü boş gelirse varsayılan gri ikon kalıyor
            holder.ivWatchedPoster.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    @Override
    public int getItemCount() {
        return watchedList != null ? watchedList.size() : 0;
    }

    public static class WatchedViewHolder extends RecyclerView.ViewHolder {
        TextView tvWatchedMovieTitle, tvWatchedUserComment;
        ImageView ivWatchedPoster;

        public WatchedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWatchedMovieTitle = itemView.findViewById(R.id.tvWatchedMovieTitle);
            tvWatchedUserComment = itemView.findViewById(R.id.tvWatchedUserComment);
            ivWatchedPoster = itemView.findViewById(R.id.ivWatchedPoster);
        }
    }
}