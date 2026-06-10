package com.example.moviesuggestmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterUsername, etRegisterPassword;
    private Button btnRegister;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etRegisterUsername.getText().toString().trim();
                String password = etRegisterPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
                } else {
                    UserModel registerData = new UserModel(username, password, username + "@gmail.com");

                    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

                    Call<ResponseBody> call = apiService.registerUser(registerData);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Kayıt Başarılı! Giriş yapabilirsiniz.", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Kayıt başarısız: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, "Bağlantı Hatası: Backend açık mı?", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}