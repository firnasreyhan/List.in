package com.ppb.listin.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ppb.listin.R;
import com.ppb.listin.api.response.LoginResponse;
import com.ppb.listin.preference.AppPreference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        LoginResponse.User user = AppPreference.getUser(getContext());

        TextView tvNama = view.findViewById(R.id.tvNama);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvJenisKelamin = view.findViewById(R.id.tvJenisKelamin);
        TextView tvTelepon = view.findViewById(R.id.tvTelepon);
        TextView tvTanggalLahir = view.findViewById(R.id.tvTanggalLahir);

        tvNama.setText(user.nama);
        tvEmail.setText(user.email);
        tvTelepon.setText(user.telepon);
        if (user.jenisKelamin.equalsIgnoreCase("L")) {
            tvJenisKelamin.setText("Pria");
        } else {
            tvJenisKelamin.setText("Wanita");
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale("in", "ID"));
        try {
            Date date = format.parse(user.tanggalLahir);
            SimpleDateFormat dateFormatterText = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "ID"));
            assert date != null;
            tvTanggalLahir.setText(dateFormatterText.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }
}
