package com.ppb.listin.view.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ppb.listin.api.response.LoginResponse;
import com.ppb.listin.preference.AppPreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddListFragment extends Fragment {

    private EditText etJudul, etKeterangan, etTanggal, etJam;
    private Spinner sKategori, sUlangJenis;
    private CheckBox cbUlang;
    private MaterialButton mbTambah;

    private int ulang;
    private String sTgl, sJam;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ulang = 0;

        Api api = ApiClient.getClient();
        LoginResponse.User user = AppPreference.getUser(getContext());

        View view = inflater.inflate(R.layout.fragment_add_list, container, false);

        etJudul = view.findViewById(R.id.etJudul);
        etKeterangan = view.findViewById(R.id.etKeterangan);
        etTanggal = view.findViewById(R.id.etTanggal);
        etJam = view.findViewById(R.id.etJam);
        sKategori = view.findViewById(R.id.sKategori);
        sUlangJenis = view.findViewById(R.id.sUlangJenis);
        cbUlang = view.findViewById(R.id.cbUlang);
        mbTambah = view.findViewById(R.id.mbTambah);

        String[] arrayKategori = new String[] {
                "Umum", "Kuliah", "Pekerjaan"
        };

        String[] arrayUlang = new String[] {
                "Harian", "Mingguan", "Bulanan"
        };

        ArrayAdapter<String> spinnerArrayAdapterKategori = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayKategori);
        spinnerArrayAdapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKategori.setAdapter(spinnerArrayAdapterKategori);

        ArrayAdapter<String> spinnerArrayAdapterUlang = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayUlang);
        spinnerArrayAdapterUlang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sUlangJenis.setAdapter(spinnerArrayAdapterKategori);

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
                    api.addList(user.email, etJudul.getText().toString(), etKeterangan.getText().toString(), sTgl, sJam, arrayKategori[sKategori.getSelectedItemPosition()], ulang, ((ulang == 0) ? "" : arrayUlang[sUlangJenis.getSelectedItemPosition()])).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            Toast.makeText(getContext(), response.body().message, Toast.LENGTH_LONG).show();
                            clear();
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Log.e("add_list", t.getMessage());
                        }
                    });
                }
            }
        });

        return view;
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormatEt = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "ID"));
        final SimpleDateFormat simpleDateFormatSt = new SimpleDateFormat("yyyy-MM-dd", new Locale("in", "ID"));
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                sJam = hourOfDay + ":" + minute;
                etJam.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, newTime.get(Calendar.HOUR_OF_DAY), newTime.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    public void clear() {
        etJudul.setText("");
        etKeterangan.setText("");
        etTanggal.setText("");
        etJam.setText("");
        sKategori.setSelection(0);
        cbUlang.setChecked(false);
        sUlangJenis.setSelection(0);
    }
}
