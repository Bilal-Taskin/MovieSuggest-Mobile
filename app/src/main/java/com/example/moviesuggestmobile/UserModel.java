package com.example.moviesuggestmobile;

import com.google.gson.annotations.SerializedName;

public class UserModel {

    // @SerializedName sayesinde Java'daki küçük harfleri, C#'taki Büyük harflere pürüzsüzce bağlıyoruz
    @SerializedName("Username")
    private String username;

    @SerializedName("Password")
    private String password;

    @SerializedName("Email")
    private String email;

    // Constructor (Kutuyu Doldurma Alanı)
    public UserModel(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getter ve Setter Metotları
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}