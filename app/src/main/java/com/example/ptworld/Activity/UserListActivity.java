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

import com.example.ptworld.Adapter.AdapterAllUser;
import com.example.ptworld.Adapter.AdapterContentsHistory;
import com.example.ptworld.DTO.ContentsDTO;
import com.example.ptworld.DTO.Users;
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

public class UserListActivity extends AppCompatActivity {

    RecyclerView userRecyclerView;
    private String TAG = "UserListActivity";
    ArrayList<Users> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userRecyclerView = findViewById(R.id.userRecyclerView);

        new Thread_SelectAllUsers().execute(getString(R.string.server_url) + "select_all_users.php");

    }
    private class Thread_SelectAllUsers extends AsyncTask<String, Void, String> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            진행다일로그 시작
            progressDialog = new ProgressDialog(UserListActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("Thread_SelectAllUsers", "POST response code - " + responseStatusCode);

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

                Log.d("Thread_SelectAllUsers", "Thread_Main : Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //DB로부터 데이터를 JSON형태로 받아온다.
            Log.i(TAG + "결과",result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                int total_json = jsonArray.length();
                Log.i("JSON갯수_콘텐츠기록", total_json + "");
                for (int i = 0; i < total_json; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    byte[] encodedByteArray = Base64.decode(jsonObject.getString("profile_image"), Base64.DEFAULT);
                    Bitmap profile_image = BitmapFactory.decodeByteArray(encodedByteArray, 0, encodedByteArray.length);

                    Users users = new Users(jsonObject.getString("nickname"), profile_image);
                    list.add(users);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0 ; i < list.size() ; i ++){
                Log.i(TAG + "nickname : ", list.get(i).getNickname());
                Log.i(TAG + "profile_image : ", list.get(i).getProfile_image()+"");
            }

//            //해당 리싸이클러뷰는 수직 방향으로 모든 게시물들을 보여주는 리싸이클러뷰다.
            AdapterAllUser myAdapter = new AdapterAllUser(list, UserListActivity.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserListActivity.this);
            userRecyclerView.setLayoutManager(layoutManager);
            userRecyclerView.setAdapter(myAdapter);
            userRecyclerView.setHasFixedSize(true);
            progressDialog.dismiss();
        }
    }
}
