package com.example.ptworld.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.ptworld.Activity.Insert_Trainner;
import com.example.ptworld.Activity.Login;
import com.example.ptworld.R;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class Popup_SNS extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup__sns);
    }

    public void insert_trainner_sns(View view) {
        intent = getIntent();
        String email = intent.getExtras().getString("email");
        Log.i("Popup_SNS",email);
        intent = new Intent(getApplicationContext(), Insert_Trainner.class);
        intent.putExtra("email", email);
        startActivity(intent);

    }

    public void insert_client_sns(View view) {
        intent.putExtra("email", intent.getExtras().getString("email"));
        startActivity(intent);
    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            Log.i("Popup_SNS_외부클릭","폴스");
            return false;
        }
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                finish();
            }
        });
        finish();
        Log.i("Popup_SNS_외부클릭","트루");
        return false;
    }
    public void onBackPressed(){
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        return;
    }
}
