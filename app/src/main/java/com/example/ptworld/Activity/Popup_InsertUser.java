package com.example.ptworld.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ptworld.R;

public class Popup_InsertUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup__insert_user2);
    }

    public void insert_trainner(View view) {
        Intent intent = new Intent(getApplicationContext(), Insert_User.class);
        intent.putExtra("email","");
        startActivity(intent);
    }

//    public void insert_client(View view) {
//        startActivity(new Intent(this, Insert_Client.class));
//    }

}
