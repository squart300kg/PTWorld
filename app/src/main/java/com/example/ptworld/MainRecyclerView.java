package com.example.ptworld;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainRecyclerView extends AppCompatActivity {
//    Activity ac = MainRecyclerView.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentpain_ankle);

    }
    public Activity getContext(){
        return MainRecyclerView.this;
    }
}
