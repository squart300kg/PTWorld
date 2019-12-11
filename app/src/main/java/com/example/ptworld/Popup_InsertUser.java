package com.example.ptworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class Popup_InsertUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup__insert_user2);
    }

    public void insert_trainner(View view) {
        Intent intent = new Intent(getApplicationContext(), Insert_Trainner.class);
        intent.putExtra("email","");
        startActivity(intent);
    }

    public void insert_client(View view) {
        startActivity(new Intent(this, Insert_Client.class));
    }

}
