package com.example.ptworld;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//===============================================얘는 안씀 이쪽의 코드를 MainDrawer로 옮겼음============================================================
public class BottomNavBar extends AppCompatActivity {
//    private TextView mTextMessage;

    String IP_ADDRESS = "squart300kg.cafe24.com";

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentMain FragmentMain = new FragmentMain(BottomNavBar.this);
    private FragmentMyPage FragmentMyPage = new FragmentMyPage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_bar);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, FragmentMain).commitAllowingStateLoss();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.frameLayout, FragmentMain).commitAllowingStateLoss();
                    Log.i("하단 navBar","홈클릭");
                    break;
                case R.id.navigation_camera1:
                    transaction.replace(R.id.frameLayout, FragmentMain).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_camera2:
                    transaction.replace(R.id.frameLayout, FragmentMain).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_camera3:
                    transaction.replace(R.id.frameLayout, FragmentMain).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_mypage:
                    transaction.replace(R.id.frameLayout, FragmentMyPage).commitAllowingStateLoss();
                    return true;
            }
            return false;
        }
    };
}
