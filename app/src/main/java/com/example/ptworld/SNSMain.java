package com.example.ptworld;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

public class SNSMain extends AppCompatActivity {

    ImageView sns_profile_image;
    ImageView reply_profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snsmain);
        sns_profile_image = findViewById(R.id.sns_profile_image);
        sns_profile_image.setBackground(new ShapeDrawable(new OvalShape()));
        reply_profile_image = findViewById(R.id.reply_profile_image);
        reply_profile_image.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            sns_profile_image.setClipToOutline(true);
            reply_profile_image.setClipToOutline(true);
        }
    }
}
