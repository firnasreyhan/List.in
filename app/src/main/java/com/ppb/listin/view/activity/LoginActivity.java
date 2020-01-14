package com.ppb.listin.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.ppb.listin.R;
import com.ppb.listin.api.Api;
import com.ppb.listin.api.ApiClient;
import com.ppb.listin.api.response.LoginResponse;
import com.ppb.listin.preference.AppPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Api api;

    private EditText etEmail, etPassword;
    private MaterialButton mbMasuk, mbDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        api = ApiClient.getClient();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        mbMasuk = findViewById(R.id.mbMasuk);
        mbDaftar = findViewById(R.id.mbDaftar);

        mbDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        mbMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean email = false;
                boolean password = false;

                if (etEmail.getText().toString().trim().equalsIgnoreCase("")) {
                    etEmail.setError("Mohon untuk diisi");
                } else {
                    email = true;
                }

                if (etPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    etPassword.setError("Mohon untuk diisi");
                } else {
                    password = true;
                }

                if (email && password) {
                    api.login(etEmail.getText().toString(), etPassword.getText().toString()).enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.body().error) {
                                Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                                AppPreference.saveUser(LoginActivity.this, response.body().list.get(0));
                                finish();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e("login", t.getMessage());
                        }
                    });
                }
            }
        });
    }
}
