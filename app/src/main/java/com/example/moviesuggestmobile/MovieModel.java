package com.example.moviesuggestmobile;

import com.google.gson.annotations.SerializedName;

public class MovieModel {
    private int id;
    private String title;

    @SerializedName(value = "poster_path", alternate = {"MoviePosterPath", "moviePosterPath"})
    private String posterPath;

    private String overview;
    private String originalTitle;
    private String originalLanguage;
    private double popularity;
    private double voteAverage;

    // --- MÜHÜRLÜ DÜZELTME: Kullanıcının kendi yorumu ve kendi puanı modele işlendi ---
    @SerializedName(value = "userComment", alternate = {"UserComment"})
    private String userComment;

    @SerializedName(value = "userRating", alternate = {"UserRating"})
    private float userRating;

    // Boş Constructor
    public MovieModel() {
    }

    // Tam Parametreli Constructor
    public MovieModel(int id, String title, String originalLanguage, String originalTitle, String overview, double popularity, double voteAverage, String posterPath) {
        this.id = id;
        this.title = title;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
    }

    // GETTER VE SETTER METOTLARI
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; }

    public String getOriginalLanguage() { return originalLanguage; }
    public void setOriginalLanguage(String originalLanguage) { this.originalLanguage = originalLanguage; }

    public double getPopularity() { return popularity; }
    public void setPopularity(double popularity) { this.popularity = popularity; }

    public double getVoteAverage() { return voteAverage; }
    public void setVoteAverage(double voteAverage) { this.voteAverage = voteAverage; }

    public String getUserComment() { return userComment; }
    public void setUserComment(String userComment) { this.userComment = userComment; }

    public float getUserRating() { return userRating; }
    public void setUserRating(float userRating) { this.userRating = userRating; }
}