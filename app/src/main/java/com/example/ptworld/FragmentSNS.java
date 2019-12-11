package com.example.ptworld;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.Collections;

public class FragmentSNS extends Fragment {
    private RecyclerView snsRecyclerView;
    SwipeRefreshLayout swipelayout;
    static Context context;
    String IP_ADDRESS = "squart300kg.cafe24.com";
    private ArrayList<BoardObject> list = new ArrayList();
    static String boardno;

    public FragmentSNS(Context context){
        this.context = context;
    }

    public static FragmentSNS fragment_sns(){
        return new FragmentSNS(context);
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_snsmain, container, false);

        snsRecyclerView = rootView.findViewById(R.id.snsRecyclerView);
        swipelayout = rootView.findViewById(R.id.swipelayout);

        //노티피케이션으로부터 들어왔다.
        if(boardno != null){
            new Thread_SNS_One().execute("http://"+IP_ADDRESS+"/user_signup/selectOneBoard.php", boardno);
            swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipelayout.setRefreshing(false);
                }
            });
        } else {
            //일반 경로로 들어왔다.
            swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    list.clear();
                    new Thread_SNS().execute("http://"+IP_ADDRESS+"/user_signup/selectBoard.php", TrainnerInfo.email);
                    swipelayout.setRefreshing(false);
                }
            });
            new Thread_SNS().execute("http://"+IP_ADDRESS+"/user_signup/selectBoard.php", TrainnerInfo.email);
        }


        return rootView;
    }
    private class Thread_SNS_One extends AsyncTask<String, Void, String> implements GoFragmentProfile {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String boardno = params[1];

            String postParameters = "boardno=" + boardno;
            Log.i("좋아요 노티",boardno+"");
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
                Log.d("Thread_SNS_One", "POST response code - " + responseStatusCode);

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

                Log.d("Thread_SNS_One", "Thread_Main : Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //DB로부터 데이터를 JSON형태로 받아온다.
            list.clear();
            Log.i("SNS노티 결과값", result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                int total_json = jsonArray.length();
                Log.i("JSON갯수_SNS노티", total_json + "");
                for (int i = 1; i <= total_json; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);

                    byte[] encodedByteArray = Base64.decode(jsonObject.getString("profile_image"), Base64.DEFAULT);
                    Bitmap profile_image = BitmapFactory.decodeByteArray(encodedByteArray, 0, encodedByteArray.length);

                    ArrayList<Bitmap> board_image = new ArrayList<>();

                    byte[] encodedByteArray1 = Base64.decode(jsonObject.getString("image1"), Base64.DEFAULT);
                    Bitmap image1 = BitmapFactory.decodeByteArray(encodedByteArray1, 0, encodedByteArray1.length);
                    board_image.add(image1);

                    byte[] encodedByteArray2 = Base64.decode(jsonObject.getString("image2"), Base64.DEFAULT);
                    Bitmap image2 = BitmapFactory.decodeByteArray(encodedByteArray2, 0, encodedByteArray2.length);
                    board_image.add(image2);


                    byte[] encodedByteArray3 = Base64.decode(jsonObject.getString("image3"), Base64.DEFAULT);
                    Bitmap image3 = BitmapFactory.decodeByteArray(encodedByteArray3, 0, encodedByteArray3.length);
                    board_image.add(image3);


                    byte[] encodedByteArray4 = Base64.decode(jsonObject.getString("image4"), Base64.DEFAULT);
                    Bitmap image4 = BitmapFactory.decodeByteArray(encodedByteArray4, 0, encodedByteArray4.length);
                    board_image.add(image4);


                    byte[] encodedByteArray5 = Base64.decode(jsonObject.getString("image5"), Base64.DEFAULT);
                    Bitmap image5 = BitmapFactory.decodeByteArray(encodedByteArray5, 0, encodedByteArray5.length);
                    board_image.add(image5);

                    BoardObject BoardObject = new BoardObject(jsonObject.getInt("replycount"), jsonObject.getInt("no"), jsonObject.getInt("like"),jsonObject.getString("nickname"), jsonObject.getString("contentsText"), profile_image, board_image, jsonObject.getString("isLike"), jsonObject.getString("device_token"));
                    Log.i("받아온 게시물 좋아요 여부_노티",jsonObject.getString("isLike")+"");
                    Log.i("받아온 게시물 번호DTO_노티",BoardObject.no+"");
                    list.add(BoardObject);
                }
                Collections.reverse(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            for(int i = 0 ; i < list.size() ; i ++){
                Log.i("받아온 게시물 번호2_노티", list.get(i).no+"");
            }

            //해당 리싸이클러뷰는 수직 방향으로 모든 게시물들을 보여주는 리싸이클러뷰다.
            AdapterSNS myAdapter = new AdapterSNS(list, context.getApplicationContext(), this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
            snsRecyclerView.setLayoutManager(layoutManager);
            snsRecyclerView.setAdapter(myAdapter);
            snsRecyclerView.setHasFixedSize(true);
            boardno = "";
//            progressDialog.dismiss();
        }
        @Override
        public void goFragmentProfile(String nick) {
            String nickname = nick;
            ((MainDrawer)getActivity()).replaceFragment(FragmentProfile.fragment_profile(nickname));
        }
    }
    private class Thread_SNS extends AsyncTask<String, Void, String> implements GoFragmentProfile {

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

            String postParameters = "email=" + email;
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
            //DB로부터 데이터를 JSON형태로 받아온다.
            list.clear();
            try {
                JSONArray jsonArray = new JSONArray(result);
                int total_json = jsonArray.length();
                Log.i("JSON갯수_SNS", total_json + "");
                for (int i = 1; i <= total_json; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);

                    byte[] encodedByteArray = Base64.decode(jsonObject.getString("profile_image"), Base64.DEFAULT);
                    Bitmap profile_image = BitmapFactory.decodeByteArray(encodedByteArray, 0, encodedByteArray.length);

                    ArrayList<Bitmap> board_image = new ArrayList<>();

                    byte[] encodedByteArray1 = Base64.decode(jsonObject.getString("image1"), Base64.DEFAULT);
                    Bitmap image1 = BitmapFactory.decodeByteArray(encodedByteArray1, 0, encodedByteArray1.length);
                    board_image.add(image1);

                    byte[] encodedByteArray2 = Base64.decode(jsonObject.getString("image2"), Base64.DEFAULT);
                    Bitmap image2 = BitmapFactory.decodeByteArray(encodedByteArray2, 0, encodedByteArray2.length);
                    board_image.add(image2);


                    byte[] encodedByteArray3 = Base64.decode(jsonObject.getString("image3"), Base64.DEFAULT);
                    Bitmap image3 = BitmapFactory.decodeByteArray(encodedByteArray3, 0, encodedByteArray3.length);
                    board_image.add(image3);


                    byte[] encodedByteArray4 = Base64.decode(jsonObject.getString("image4"), Base64.DEFAULT);
                    Bitmap image4 = BitmapFactory.decodeByteArray(encodedByteArray4, 0, encodedByteArray4.length);
                    board_image.add(image4);


                    byte[] encodedByteArray5 = Base64.decode(jsonObject.getString("image5"), Base64.DEFAULT);
                    Bitmap image5 = BitmapFactory.decodeByteArray(encodedByteArray5, 0, encodedByteArray5.length);
                    board_image.add(image5);

                    BoardObject BoardObject = new BoardObject(jsonObject.getInt("replycount"), jsonObject.getInt("no"), jsonObject.getInt("like"),jsonObject.getString("nickname"), jsonObject.getString("contentsText"), profile_image, board_image, jsonObject.getString("isLike"), jsonObject.getString("device_token"));
                    Log.i("받아온 게시물 좋아요 여부",jsonObject.getString("isLike")+"");
                    Log.i("받아온 게시물 번호DTO",BoardObject.no+"");
                    list.add(BoardObject);
                }
                Collections.reverse(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            for(int i = 0 ; i < list.size() ; i ++){
                Log.i("받아온 게시물 번호2", list.get(i).no+"");
            }

            //해당 리싸이클러뷰는 수직 방향으로 모든 게시물들을 보여주는 리싸이클러뷰다.
            AdapterSNS myAdapter = new AdapterSNS(list, context.getApplicationContext(), this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
            snsRecyclerView.setLayoutManager(layoutManager);
            snsRecyclerView.setAdapter(myAdapter);
            snsRecyclerView.setHasFixedSize(true);
//            progressDialog.dismiss();
        }
        @Override
        public void goFragmentProfile(String nick) {
            String nickname = nick;
            ((MainDrawer)getActivity()).replaceFragment(FragmentProfile.fragment_profile(nickname));
        }
    }
}
