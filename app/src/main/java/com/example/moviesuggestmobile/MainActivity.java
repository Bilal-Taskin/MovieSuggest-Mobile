package com.example.moviesuggestmobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUserToCsharp(username, password);
            }
        });

        tvGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUserToCsharp(String username, String password) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        java.util.HashMap<String, Object> loginParams = new java.util.HashMap<>();
        loginParams.put("Username", username);
        loginParams.put("Password", password);

        Call<ResponseBody> call = apiService.loginUser(loginParams);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        if (jsonObject.has("token")) {
                            String jwtToken = jsonObject.getString("token");

                            String[] parts = jwtToken.split("\\.");
                            if (parts.length >= 2) {
                                String payloadJson = new String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8);
                                JSONObject payloadObject = new JSONObject(payloadJson);

                                int loggedInUserId = -1;

                                // --- BÜYÜK MICROSOFT MÜHÜRÜ: .NET'in ürettiği tüm uzun ve kısa ID etiketlerini tarıyoruz ---
                                String dotNetIdClaim = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier";

                                if (payloadObject.has(dotNetIdClaim)) {
                                    loggedInUserId = payloadObject.getInt(dotNetIdClaim);
                                } else if (payloadObject.has("nameid")) {
                                    loggedInUserId = payloadObject.getInt("nameid");
                                } else if (payloadObject.has("id")) {
                                    loggedInUserId = payloadObject.getInt("id");
                                } else if (payloadObject.has("userId")) {
                                    loggedInUserId = payloadObject.getInt("userId");
                                }

                                if (loggedInUserId != -1) {
                                    // Bulunan gerçek kullanıcı ID'sini telefona mühürle
                                    SharedPrefManager.getInstance(MainActivity.this).saveUserId(loggedInUserId);

                                    Toast.makeText(MainActivity.this, "Giriş Başarılı! 🍿", Toast.LENGTH_SHORT).show();

                                    // Dashboard (Ana Ekran) sayfasına jilet gibi geçiş yapıyoruz
                                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                            }
                        }

                        Toast.makeText(MainActivity.this, "Hata: Token çözülemedi veya geçerli bir Kullanıcı ID bulunamadı!", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Veri ayrıştırma hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Giriş Başarısız! Kullanıcı adı veya şifre hatalı.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Bağlantı Hatası! C# Backend açık mı?", Toast.LENGTH_LONG).show();
            }
        });
    }
}