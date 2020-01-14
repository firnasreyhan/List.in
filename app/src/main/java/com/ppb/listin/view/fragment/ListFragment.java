package com.ppb.listin.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ppb.listin.R;
import com.ppb.listin.adapter.ListAdapter;
import com.ppb.listin.api.Api;
import com.ppb.listin.api.ApiClient;
import com.ppb.listin.api.response.ListResponse;
import com.ppb.listin.api.response.LoginResponse;
import com.ppb.listin.preference.AppPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment {

    private Api api;
    private LoginResponse.User user;

    private RecyclerView rvList;
    private TextView tvKosong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        api = ApiClient.getClient();
        user = AppPreference.getUser(getContext());
        tvKosong = view.findViewById(R.id.tvKosong);
        rvList = view.findViewById(R.id.rvList);

        getData();

        return view;
    }

    public void getData() {
        api.list(user.email).enqueue(new Callback<ListResponse>() {
            @Override
            public void onResponse(Call<ListResponse> call, Response<ListResponse> response) {
                if (response.body().data.isEmpty()) {
                    tvKosong.setVisibility(View.VISIBLE);
                    rvList.setVisibility(View.GONE);
                } else {
                    tvKosong.setVisibility(View.GONE);
                    rvList.setVisibility(View.VISIBLE);
                    setRvList(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<ListResponse> call, Throwable t) {
                Log.e("list", t.getMessage());
            }
        });
    }

    public void setRvList(ArrayList<ListResponse.ListData> list) {
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvList.setAdapter(new ListAdapter(list));
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
