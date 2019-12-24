package com.example.ptworld.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.ptworld.Adapter.AdapterMoreView;
import com.example.ptworld.DTO.ItemObject;
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

public class MoreView extends AppCompatActivity {

    RecyclerView moreViewRecyclerView;
    ArrayList<ItemObject> list = new ArrayList();
    String IP_ADDRESS = "squart300kg.cafe24.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_view);
        Log.i("MoreVIew로그","들어왔다");

        new Description().execute("http://"+IP_ADDRESS+"/user_signup/allContents.php");
    }
    private class Description extends AsyncTask<String, Void, String> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //진행다일로그 시작
            progressDialog = new ProgressDialog(MoreView.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
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
//                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("Thread_Main", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d("Thread_Main", "Thread_Main : Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //DB로부터 데이터를 JSON형태로 받아온다.
            try {
                JSONArray jsonArray = new JSONArray(result);
                int total_json = jsonArray.length();
                Log.i("JSON갯수",total_json+"");
                for(int i = 1 ; i <= total_json ; i ++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);

                    ItemObject itemObject = new ItemObject(jsonObject.getString("subject"), jsonObject.getString("contents_url"), jsonObject.getString("thumbnail_url"));
                    list.add(itemObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //해당 리싸이클러뷰는 수직 방향으로 모든 게시물들을 보여주는 리싸이클러뷰다.
            moreViewRecyclerView = findViewById(R.id.moreViewRecyclerView);
            AdapterMoreView myAdapter = new AdapterMoreView(list, getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            moreViewRecyclerView.setLayoutManager(layoutManager);
            moreViewRecyclerView.setAdapter(myAdapter);
            progressDialog.dismiss();

        }
    }
}
