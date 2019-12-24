package com.example.ptworld.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.example.ptworld.Adapter.AdapterLikeList;
import com.example.ptworld.DTO.LikeListDTO;
import com.example.ptworld.R;

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

public class LikeList extends AppCompatActivity {
    Intent intent;
    String IP_ADDRESS = "squart300kg.cafe24.com";
    RecyclerView likeListRecyclerView;
    ArrayList<LikeListDTO> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_list);

        likeListRecyclerView = findViewById(R.id.likeListRecyclerView);
        intent = getIntent();
        int boardno = intent.getExtras().getInt("no");
        //해당 게시물에 좋아요를 누른 사람들을 모두 불러온다.
        new Thread_LikeList().execute("http://"+IP_ADDRESS+"/user_signup/selectLikeList.php",boardno+"");

    }
    private class Thread_LikeList extends AsyncTask<String, Void, String> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //진행다일로그 시작
//            progressDialog = new ProgressDialog(context.getApplicationContext());
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setMessage("잠시 기다려 주세요.");
//            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String no = params[1];

            String postParameters = "no=" + no;
            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("BoardSelect", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d("JSON갯수_LikeList", "Thread_Main : Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //DB로부터 데이터를 JSON형태로 받아온다.
            try {
                JSONArray jsonArray = new JSONArray(result);
                int total_json = jsonArray.length();
                Log.i("JSON갯수_LikeList", total_json + "");
                for (int i = 1; i <= total_json; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);

                    byte[] encodedByteArray = Base64.decode(jsonObject.getString("profile_image"), Base64.DEFAULT);
                    Bitmap profile_image = BitmapFactory.decodeByteArray(encodedByteArray, 0, encodedByteArray.length);
                    Log.i("좋아요 프로필사진1",profile_image+"");
                    LikeListDTO LikeListDTO = new LikeListDTO( jsonObject.getString("nickname"), jsonObject.getString("email"), profile_image);
                    list.add(LikeListDTO);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Log.i("좋아요 프로필사진2",list.get(0).profile_image+"");
            //해당 리싸이클러뷰는 수직 방향으로 모든 게시물들을 보여주는 리싸이클러뷰다.
            AdapterLikeList myAdapter = new AdapterLikeList(list, LikeList.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LikeList.this);
            likeListRecyclerView.setLayoutManager(layoutManager);
            likeListRecyclerView.setAdapter(myAdapter);
            likeListRecyclerView.setHasFixedSize(true);
//            progressDialog.dismiss();
        }
    }
}
