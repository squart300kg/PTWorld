package com.example.ptworld.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ptworld.R;

public class MainRecyclerView3 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentstudy);


    }
    public Activity getContext(){
        return MainRecyclerView3.this;
    }
}
