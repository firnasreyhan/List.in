package com.ppb.listin.api;

import com.ppb.listin.api.response.BaseResponse;
import com.ppb.listin.api.response.ListResponse;
import com.ppb.listin.api.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("register.php")
    Call<BaseResponse> register(
            @Field("email") String email,
            @Field("nama") String nama,
            @Field("jenis_kelamin") String jenisKelamin,
            @Field("telepon") String telepon,
            @Field("tanggal_lahir") String tanggalLahir,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("add_list.php")
    Call<BaseResponse> addList(
            @Field("email") String email,
            @Field("judul") String judul,
            @Field("keterangan") String keterangan,
            @Field("tanggal") String tanggal,
            @Field("jam") String jam,
            @Field("kategori") String kategori,
            @Field("ulang") int ulang,
            @Field("jenis_ulang") String jenisUlang
    );

    @FormUrlEncoded
    @POST("show_list.php")
    Call<ListResponse> list(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("edit_list.php")
}
