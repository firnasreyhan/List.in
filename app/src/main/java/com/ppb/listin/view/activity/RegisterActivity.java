package com.ppb.listin.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.ppb.listin.R;
import com.ppb.listin.api.Api;
import com.ppb.listin.api.ApiClient;
import com.ppb.listin.api.response.BaseResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etNama, etTelepon, etTanggalLahir, etPassword;
    private MaterialButton mbDaftar;
    private RadioGroup rgJenisKelamin;
    private Api api;
    private String jk, tglLahir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        api = ApiClient.getClient();
        jk = "L";

        etEmail = findViewById(R.id.etEmail);
        etNama = findViewById(R.id.etNama);
        etTelepon = findViewById(R.id.etTelepon);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        etPassword = findViewById(R.id.etPassword);
        rgJenisKelamin = findViewById(R.id.rgJenisKelamin);
        mbDaftar = findViewById(R.id.mbDaftar);

        rgJenisKelamin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbPria) {
                    jk = "L";
                } else if (checkedId == R.id.rbWanita) {
                    jk = "P";
                }
            }
        });

        etTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        mbDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean email = false;
                boolean nama = false;
                boolean telepon = false;
                boolean tanggal_lahir = false;
                boolean password = false;

                if (etEmail.getText().toString().trim().equalsIgnoreCase("")) {
                    etEmail.setError("Mohon untuk diisi");
                } else {
                    email = true;
                }

                if (etNama.getText().toString().trim().equalsIgnoreCase("")) {
                    etNama.setError("Mohon untuk diisi");
                } else {
                    nama = true;
                }

                if (etTelepon.getText().toString().trim().equalsIgnoreCase("")) {
                    etTelepon.setError("Mohon untuk diisi");
                } else {
                    telepon = true;
                }

                if (etTanggalLahir.getText().toString().trim().equalsIgnoreCase("")) {
                    etTanggalLahir.setError("Mohon untuk diisi");
                } else {
                    tanggal_lahir = true;
                }

                if (etPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    etPassword.setError("Mohon untuk diisi");
                } else {
                    password = true;
                }

                if (email && nama && telepon && tanggal_lahir && password) {
                    api.register(etEmail.getText().toString(), etNama.getText().toString(), jk, etTelepon.getText().toString(), tglLahir, etPassword.getText().toString()).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body().error) {
                                Toast.makeText(RegisterActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Log.e("register", t.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormatEt = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "ID"));
        final SimpleDateFormat simpleDateFormatSt = new SimpleDateFormat("yyyy-MM-dd", new Locale("in", "ID"));
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etTanggalLahir.setText(simpleDateFormatEt.format(newDate.getTime()));
                tglLahir = simpleDateFormatSt.format(newDate.getTime());
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

}
