package com.example.moviesuggestmobile;

import java.util.HashMap;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/auth/register")
    Call<ResponseBody> registerUser(@Body UserModel user);

    @POST("api/auth/login")
    Call<ResponseBody> loginUser(@Body HashMap<String, Object> params);

    @GET("api/movies/popular")
    Call<ResponseBody> getPopularMovies();

    @GET("api/watchlists/user/{userId}")
    Call<List<MovieModel>> getMyWatchlist(@Path("userId") int userId);

    @POST("api/watchlists/add")
    Call<ResponseBody> addToWatchlist(
            @Query("movieId") int movieId,
            @Query("movieTitle") String movieTitle,
            @Query("userId") int userId,
            @Query("moviePosterPath") String moviePosterPath
    );

    // --- SAF JSON KAPISI: HashMap alan mühürlü metot ---
    @POST("api/watchedmovies/addwithreview")
    Call<ResponseBody> addWatchedMovieWithReview(@Body HashMap<String, Object> params);

    @GET("api/watchedmovies/getmywatchedmovies")
    Call<List<MovieModel>> getMyWatchedMovies();

    @GET("api/watchedmovies/getmyreviews")
    Call<List<MovieModel>> getMyReviews();
}