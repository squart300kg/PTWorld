package com.example.ptworld.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptworld.Adapter.AdapterRecomendation;
import com.example.ptworld.DTO.ItemObject;
import com.example.ptworld.R;
import com.example.ptworld.DTO.UserInfo;

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

public class Fragment추천  extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ItemObject> list = new ArrayList();
    String IP_ADDRESS = "squart300kg.cafe24.com";
    Context context;

    public Fragment추천(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragmentreco, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recomendationRecyclerView);
        context = inflater.getContext();
//        new MainDescription().execute("http://"+IP_ADDRESS+"/user_signup/allContents.php");
        new MainDescription().execute("http://"+IP_ADDRESS+"/user_signup/recomend.php", UserInfo.email);
        Log.i("Fragment추천","MainDescription쓰레드 실행");
        Log.i("Fragment추천 닉네임은(Client)", UserInfo.nickname);
        return rootView;

    }
    private class MainDescription extends AsyncTask<String, Void, String> {

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
                Log.d("Fragment추천쓰레드", "POST response code - " + responseStatusCode);

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

                Log.d("Fragment추천쓰레드", "Thread_Main : Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("추천쓰레드Post",result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                String recomendResult = jsonArray.getString(0);

                String recomendList[] = recomendResult.split(" AND ");
                for (int i = 0 ; i < recomendList.length; i++) {
                    new Thread_Recomend().execute("http://"+IP_ADDRESS+"/user_signup/selectContent.php", recomendList[i]);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//
        }
    }

    private class Thread_Recomend extends AsyncTask<String, Void, String> {

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
            String subject = params[1];
            String postParameters = "subject=" + subject;
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
                Log.d("Thread_Main", "POST response code - " + responseStatusCode);

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
                Log.i("JSON갯수_recomendList", total_json + "");
                for (int i = 1; i <= total_json; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);
                    Log.i("추천 contents_url",jsonObject.getString("contents_url"));
                    ItemObject itemObject = new ItemObject(jsonObject.getString("subject"), jsonObject.getString("contents_url"), jsonObject.getString("thumbnail_url"));
                    list.add(itemObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



            //해당 리싸이클러뷰는 수직 방향으로 모든 게시물들을 보여주는 리싸이클러뷰다.
            AdapterRecomendation myAdapter = new AdapterRecomendation(list, context.getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);
            recyclerView.setHasFixedSize(true);
            myAdapter.notifyDataSetChanged();
//            myAdapter.notifyItemChanged(list.size()-1);
//            progressDialog.dismiss();
        }
    }

}
