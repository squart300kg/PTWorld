package com.example.ptworld.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ptworld.R;

public class MainRecyclerView4 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentyoutube);


    }
    public Activity getContext(){
        return MainRecyclerView4.this;
    }
}
