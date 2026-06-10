package com.example.moviesuggestmobile;

// --- CAN ALICI DÜZELTME: R sınıfını el ile projeye mühürlüyoruz Bilal ---
import com.example.moviesuggestmobile.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<MovieModel> reviewList;

    public ReviewsAdapter(List<MovieModel> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Yukarıda R sınıfını import ettiğimiz için buradaki kırmızı lamba sönecek
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        MovieModel movie = reviewList.get(holder.getAdapterPosition());

        holder.tvReviewMovieTitle.setText(movie.getTitle());
        holder.reviewRatingBar.setRating(movie.getUserRating());

        if (movie.getUserComment() != null && !movie.getUserComment().isEmpty()) {
            holder.tvReviewUserComment.setText(movie.getUserComment());
        } else {
            holder.tvReviewUserComment.setText("Yorumsuz.");
        }
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvReviewMovieTitle, tvReviewUserComment;
        RatingBar reviewRatingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReviewMovieTitle = itemView.findViewById(R.id.tvReviewMovieTitle);
            tvReviewUserComment = itemView.findViewById(R.id.tvReviewUserComment);
            reviewRatingBar = itemView.findViewById(R.id.reviewRatingBar);
        }
    }
}