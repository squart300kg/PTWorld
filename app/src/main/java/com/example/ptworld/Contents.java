package com.example.ptworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class Contents extends AppCompatActivity {
    Intent intent;
    WebView contents;
    WebSettings contents_Settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        intent = getIntent();
        int position = intent.getExtras().getInt("position");
        String contents_url = intent.getExtras().getString("contents_url");
        String subject = intent.getExtras().getString("subject");
        WebView contents = findViewById(R.id.contents);

        contents.setWebViewClient(new WebViewClient());
        contents_Settings = contents.getSettings();
        contents_Settings.setJavaScriptEnabled(true);
        contents_Settings.setJavaScriptCanOpenWindowsAutomatically(false);
        contents_Settings.setUseWideViewPort(true);
        contents_Settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        contents.loadUrl(contents_url);
        Log.i("Contents.java_span",contents_url); //로그데이터를 수집하기 위해 어떤 사용자가 어떤 게시물을 클릭했는지 저장하기 위한 스레드를 실행

        String IP_ADDRESS = "squart300kg.cafe24.com";
        Thread_views thread_views = new Thread_views();
        thread_views.execute("http://"+IP_ADDRESS+"/user_signup/views_up.php", TrainnerInfo.email, subject);
        Log.i("로그데이터 이메일",TrainnerInfo.email);
        Log.i("로그데이터 제목", subject);


    }
}
