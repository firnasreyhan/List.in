package com.ppb.listin.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoginResponse extends BaseResponse{

    @SerializedName("data")
    public ArrayList<User> list;

    public class User {

        @SerializedName("email")
        public String email;

        @SerializedName("nama")
        public String nama;

        @SerializedName("jenis_kelamin")
        public String jenisKelamin;

        @SerializedName("telepon")
        public String telepon;

        @SerializedName("tanggal_lahir")
        public String tanggalLahir;

    }

}
