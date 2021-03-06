package com.ppb.listin.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.ppb.listin.R;
import com.ppb.listin.api.Api;
import com.ppb.listin.api.ApiClient;
import com.ppb.listin.api.response.BaseResponse;
import com.ppb.listin.api.response.ListResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailListActivity extends AppCompatActivity {

    private ListResponse.ListData data;

    private Toolbar toolbar;
    private EditText etJudul, etKeterangan, etTanggal, etJam;
    private Spinner sKategori, sUlangJenis;
    private CheckBox cbUlang;
    private MaterialButton mbTambah, mbBatal;

    private int ulang, sIndexKat, sIndexUlang;
    private String sTgl, sJam;

    private Api api = ApiClient.getClient();

    private String[] arrayKategori = new String[] {
            "Umum", "Kuliah", "Pekerjaan"
    };

    private String[] arrayUlang = new String[] {
            "Harian", "Mingguan", "Bulanan"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Detail Catatan");

        ulang = 0;
        data = (ListResponse.ListData) getIntent().getSerializableExtra("data");
        sJam = data.jam;
        sTgl = data.tanggal;

        etJudul = findViewById(R.id.etJudul);
        etKeterangan = findViewById(R.id.etKeterangan);
        etTanggal = findViewById(R.id.etTanggal);
        etJam = findViewById(R.id.etJam);
        sKategori = findViewById(R.id.sKategori);
        sUlangJenis = findViewById(R.id.sUlangJenis);
        cbUlang = findViewById(R.id.cbUlang);
        mbTambah = findViewById(R.id.mbTambah);
        mbBatal = findViewById(R.id.mbBatal);

        disabled();

        ArrayAdapter<String> spinnerArrayAdapterKategori = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayKategori);
        spinnerArrayAdapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKategori.setAdapter(spinnerArrayAdapterKategori);

        ArrayAdapter<String> spinnerArrayAdapterUlang = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayUlang);
        spinnerArrayAdapterUlang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sUlangJenis.setAdapter(spinnerArrayAdapterKategori);

        setOldData();

        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        etJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });

        cbUlang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sUlangJenis.setVisibility(View.VISIBLE);
                    ulang = 1;
                } else {
                    sUlangJenis.setVisibility(View.GONE);
                    ulang = 0;
                }
            }
        });

        mbTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean judul = false;
                boolean tanggal = false;
                boolean jam = false;

                if (etJudul.getText().toString().trim().equalsIgnoreCase("")) {
                    etJudul.setError("Mohon untuk diisi");
                } else {
                    judul = true;
                }

                if (etTanggal.getText().toString().trim().equalsIgnoreCase("")) {
                    etTanggal.setError("Mohon untuk diisi");
                } else {
                    tanggal = true;
                }

                if (etJam.getText().toString().trim().equalsIgnoreCase("")) {
                    etJam.setError("Mohon untuk diisi");
                } else {
                    jam = true;
                }

                if (judul && tanggal && jam) {
                    api.edit(data.idList, etJudul.getText().toString(), etKeterangan.getText().toString(), sTgl, sJam, arrayKategori[sKategori.getSelectedItemPosition()], ulang, arrayUlang[sUlangJenis.getSelectedItemPosition()]).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            disabled();
                            finish();
                            Toast.makeText(DetailListActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Log.e("edit", t.getMessage());
                        }
                    });
                }
            }
        });

        mbBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disabled();
                setOldData();
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
                etTanggal.setText(simpleDateFormatEt.format(newDate.getTime()));
                sTgl = simpleDateFormatSt.format(newDate.getTime());
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void showTimeDialog() {
        Calendar newTime = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                sJam = hourOfDay + ":" + minute;
                etJam.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, newTime.get(Calendar.HOUR_OF_DAY), newTime.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                enabled();
                return true;
            case R.id.menu_delete:
                api.delete(data.idList).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        Toast.makeText(DetailListActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("delete", t.getMessage());
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void enabled() {
        etJudul.setEnabled(true);
        etKeterangan.setEnabled(true);
        etTanggal.setEnabled(true);
        etJam.setEnabled(true);
        sKategori.setEnabled(true);
        sUlangJenis.setEnabled(true);
        cbUlang.setEnabled(true);
        mbTambah.setEnabled(true);
        mbBatal.setEnabled(true);
        mbTambah.setVisibility(View.VISIBLE);
        mbBatal.setVisibility(View.VISIBLE);
    }

    public void disabled() {
        etJudul.setEnabled(false);
        etKeterangan.setEnabled(false);
        etTanggal.setEnabled(false);
        etJam.setEnabled(false);
        sKategori.setEnabled(false);
        sUlangJenis.setEnabled(false);
        cbUlang.setEnabled(false);
        mbTambah.setEnabled(false);
        mbBatal.setEnabled(false);
        mbTambah.setVisibility(View.GONE);
        mbBatal.setVisibility(View.GONE);
    }

    public void setOldData() {
        etJudul.setText(data.judul);
        etKeterangan.setText(data.keterangan);
        etTanggal.setText(data.tanggal);
        etJam.setText(data.jam);
        if (data.kategori.equalsIgnoreCase("umum")) {
            sKategori.setSelection(0);
        } else if (data.kategori.equalsIgnoreCase("kuliah")) {
            sKategori.setSelection(1);
        } else if (data.kategori.equalsIgnoreCase("pekerjaan")) {
            sKategori.setSelection(2);
        }

        if (data.ulang == 0) {
            sUlangJenis.setVisibility(View.GONE);
            cbUlang.setChecked(false);
        } else {
            sUlangJenis.setVisibility(View.VISIBLE);
            cbUlang.setChecked(true);
        }

        if (data.jenisUlang.equalsIgnoreCase("harian")) {
            sUlangJenis.setSelection(0);
        } else if (data.jenisUlang.equalsIgnoreCase("mingguan")) {
            sUlangJenis.setSelection(1);
        } else if (data.jenisUlang.equalsIgnoreCase("bulanan")) {
            sUlangJenis.setSelection(2);
        }
    }
}
