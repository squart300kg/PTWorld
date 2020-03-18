package com.example.ptworld.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.example.ptworld.Adapter.AdapterLikeList;
import com.example.ptworld.DTO.LikeListDTO;
import com.example.ptworld.DTO.UserInfo;
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

public class FollowerFollowingCount extends AppCompatActivity {

    String IP_ADDRESS = "squart300kg.cafe24.com";
    ArrayList<LikeListDTO> list = new ArrayList<>();
    RecyclerView followRecyclerView;
    TextView followTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_following_count);

        followRecyclerView = findViewById(R.id.followRecyclerView);
        followTitle = findViewById(R.id.followTitle);

        String type = getIntent().getStringExtra("type");
        if(type.equals("followingCount")){
            //팔로잉 수 조회
            followTitle.setText("팔로잉");
            new Thread_Select_Follow().execute("http://"+IP_ADDRESS+"/user_signup/selectFollow.php","following", UserInfo.email);
        } else if (type.equals("followerCount")){
            //팔로워 수 조회
            followTitle.setText("팔로워");
            new Thread_Select_Follow().execute("http://"+IP_ADDRESS+"/user_signup/selectFollow.php","follower", UserInfo.email);
        }
    }

    private class Thread_Select_Follow extends AsyncTask<String, Void, String> {

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
            String type = params[1];
            String email = params[2];

            String postParameters = "type=" + type + "&email=" + email;
            Log.i("팔로잉팔로워 파라미터",postParameters);
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

                Log.d("BoardSelect", "Thread_Main : Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("팔로잉팔로워 결과값",result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                int total_json = jsonArray.length();
                Log.i("JSON갯수_팔로잉팔로워", total_json + "");
                for (int i = 1; i <= total_json; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);

                    byte[] encodedByteArray = Base64.decode(jsonObject.getString("profile_image"), Base64.DEFAULT);
                    Bitmap profile_image = BitmapFactory.decodeByteArray(encodedByteArray, 0, encodedByteArray.length);

                    LikeListDTO LikeListDTO = new LikeListDTO(jsonObject.getString("nickname"), jsonObject.getString("email"), profile_image);
//                    Log.i("받아온 게시물 좋아요 여부",jsonObject.getString("isLike")+"");
//                    Log.i("받아온 게시물 번호DTO",BoardObject.no+"");
                    list.add(LikeListDTO);
                }
//                Collections.reverse(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            for(int i = 0 ; i < list.size() ; i ++){
                Log.i("받아온 게시물 번호2", list.get(i).nickname+"");
            }

            //해당 리싸이클러뷰는 수직 방향으로 모든 게시물들을 보여주는 리싸이클러뷰다.
            AdapterLikeList myAdapter = new AdapterLikeList(list, FollowerFollowingCount.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FollowerFollowingCount.this);
            followRecyclerView.setLayoutManager(layoutManager);
            followRecyclerView.setAdapter(myAdapter);
            followRecyclerView.setHasFixedSize(true);
        }
    }
}
