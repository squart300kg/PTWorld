package com.example.ptworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

public class ContentsHistory extends AppCompatActivity {
    RecyclerView contentsHistoryRecyclerView;
    String IP_ADDRESS = "squart300kg.cafe24.com";
    ArrayList<ContentsDTO> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_history);

        contentsHistoryRecyclerView = findViewById(R.id.contentsHistoryRecyclerView);

        new Thread_ContentsHistory().execute("http://"+IP_ADDRESS+"/user_signup/select_contentsHistory.php",TrainnerInfo.email);


    }
    private class Thread_ContentsHistory extends AsyncTask<String, Void, String> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            진행다일로그 시작
            progressDialog = new ProgressDialog(ContentsHistory.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String email = params[1];
            String postParameters = "email="+email;
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
                Log.d("Thread_ContentsHistory", "POST response code - " + responseStatusCode);

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

                Log.d("Thread_ContentsHistory", "Thread_Main : Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //DB로부터 데이터를 JSON형태로 받아온다.
            Log.i("컨텐츠 조회 기록 결과",result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                int total_json = jsonArray.length();
                Log.i("JSON갯수_콘텐츠기록", total_json + "");
                for (int i = 1; i <= total_json; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);

                    ContentsDTO ContentsDTO = new ContentsDTO(jsonObject.getString("thumbnail_url"), jsonObject.getString("contents_url"),jsonObject.getString("subject"), jsonObject.getInt("views"));
                    list.add(ContentsDTO);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0 ; i < list.size() ; i ++){
                Log.i("콘텐츠 조회기록_제목"+(i+1),list.get(i).subject+"");
                Log.i("콘텐츠 조회기록_썸네일"+(i+1),list.get(i).thumbnail_url+"");
                Log.i("콘텐츠 조회기록_콘텐츠"+(i+1),list.get(i).contents_url+"");
                Log.i("콘텐츠 조회기록_조회수"+(i+1),list.get(i).views+"");
            }

            //해당 리싸이클러뷰는 수직 방향으로 모든 게시물들을 보여주는 리싸이클러뷰다.
            AdapterContentsHistory myAdapter = new AdapterContentsHistory(list, ContentsHistory.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ContentsHistory.this);
            contentsHistoryRecyclerView.setLayoutManager(layoutManager);
            contentsHistoryRecyclerView.setAdapter(myAdapter);
            contentsHistoryRecyclerView.setHasFixedSize(true);
            progressDialog.dismiss();
        }
    }
}
