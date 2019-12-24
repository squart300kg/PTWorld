package com.example.ptworld.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ptworld.R;

public class MainRecyclerView2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentbalance);


    }
    public Activity getContext(){
        return MainRecyclerView2.this;
    }
}
