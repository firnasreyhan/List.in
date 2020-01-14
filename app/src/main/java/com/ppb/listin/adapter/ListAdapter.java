package com.ppb.listin.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ppb.listin.R;
import com.ppb.listin.api.response.ListResponse;
import com.ppb.listin.view.activity.DetailListActivity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<ListResponse.ListData> list;

    public ListAdapter(ArrayList<ListResponse.ListData> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListResponse.ListData data = list.get(position);

        if (data.kategori.equalsIgnoreCase("Umum")) {
            holder.vKategori.setBackgroundResource(R.color.colorRedKategori);
        } else if (data.kategori.equalsIgnoreCase("Kuliah")) {
            holder.vKategori.setBackgroundResource(R.color.colorGreenKategori);
        } else if (data.kategori.equalsIgnoreCase("Pekerjaan")) {
            holder.vKategori.setBackgroundResource(R.color.colorBlueKategori);
        }

        holder.tvJudul.setText(data.judul);
        holder.tvKeterangan.setText(data.keterangan);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale("in", "ID"));
        SimpleDateFormat formatJam = new SimpleDateFormat("hh:mm:ss", new Locale("in", "ID"));
        try {
            Date date = format.parse(data.tanggal);
            Date date2 = formatJam.parse(data.jam);
            SimpleDateFormat dateFormatterText = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "ID"));
            SimpleDateFormat dateFormatterTextJam = new SimpleDateFormat("hh:mm", new Locale("in", "ID"));
            assert date != null;
            holder.tvTanggalWaktu.setText(dateFormatterText.format(date.getTime()) + ", " + dateFormatterTextJam.format(date2.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailListActivity.class);
                intent.putExtra("data", data);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View vKategori;
        private CheckBox cbStatus;
        private TextView tvJudul, tvKeterangan, tvTanggalWaktu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            vKategori = itemView.findViewById(R.id.vKategori);
            cbStatus = itemView.findViewById(R.id.cbStatus);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvKeterangan = itemView.findViewById(R.id.tvKeterangan);
            tvTanggalWaktu = itemView.findViewById(R.id.tvTanggalWaktu);
        }
    }
}
