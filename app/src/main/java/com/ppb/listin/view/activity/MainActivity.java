package com.ppb.listin.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.ppb.listin.R;
import com.ppb.listin.api.response.LoginResponse;
import com.ppb.listin.preference.AppPreference;
import com.ppb.listin.view.fragment.AboutFragment;
import com.ppb.listin.view.fragment.AddListFragment;
import com.ppb.listin.view.fragment.ListFragment;
import com.ppb.listin.view.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dlMain;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nvMain;

    private Toolbar toolbar;

    private View vHeader;

    private TextView tvNama, tvEmail;

    private LoginResponse.User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = AppPreference.getUser(MainActivity.this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Daftar Catatan");

        dlMain = findViewById(R.id.dlMain);
        nvMain = findViewById(R.id.nvMain);
        vHeader = nvMain.getHeaderView(0);
        tvNama = vHeader.findViewById(R.id.tvNama);
        tvEmail = vHeader.findViewById(R.id.tvEmail);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, dlMain, R.string.open, R.string.close);

        dlMain.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNama.setText(user.nama);
        tvEmail.setText(user.email);

        replaceFragment(new ListFragment());

        nvMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;

        switch(menuItem.getItemId()) {
            case R.id.nav_list:
                setTitle("Daftar Catatan");
                fragment = new ListFragment();
                break;
            case R.id.nav_list_add:
                setTitle("Tambah Catatan");
                fragment = new AddListFragment();
                break;
            case R.id.nav_profile:
                setTitle("Profil");
                fragment = new ProfileFragment();
                break;
            case R.id.nav_about:
                setTitle("Tentang");
                fragment = new AboutFragment();
                break;
            case R.id.nav_logout:
                AppPreference.removeUser(MainActivity.this);
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            default:
                fragment = new ListFragment();
        }

        //replacing the fragment
        if (fragment != null) {
            replaceFragment(fragment);
        }

        dlMain.closeDrawers();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flMain, fragment);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
