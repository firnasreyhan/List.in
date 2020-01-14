package com.ppb.listin.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ListResponse extends BaseResponse {

    @SerializedName("data")
    public ArrayList<ListData> data;

    public class ListData implements Serializable {

        @SerializedName("id_list")
        public String idList;

        @SerializedName("email")
        public String email;

        @SerializedName("judul")
        public String judul;

        @SerializedName("keterangan")
        public String keterangan;

        @SerializedName("tanggal")
        public String tanggal;

        @SerializedName("jam")
        public String jam;

        @SerializedName("kategori")
        public String kategori;

        @SerializedName("ulang")
        public int ulang;

        @SerializedName("jenis_ulang")
        public String jenisUlang;

        @SerializedName("status")
        public int status;

    }
}
