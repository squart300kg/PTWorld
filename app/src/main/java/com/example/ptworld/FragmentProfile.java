package com.example.ptworld;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FragmentProfile extends Fragment{
    ViewGroup rootView;
    static  Activity context;
    private RecyclerView profileRecyclerView;
    String IP_ADDRESS = "squart300kg.cafe24.com";
    ArrayList<ProfileDTO> list = new ArrayList<>();
    ArrayList<Bitmap> list_thumbnail = new ArrayList<>();
    ImageView sns_profile_profile_image;
    TextView sns_profile_nickname;

    TextView sns_profile_update;
    TextView sns_profile_follow;
    TextView sns_profile_eachfollow;
    TextView sns_profile_following;
    static String nickname = "";
    String device_token;

    TextView boardCount;
    TextView followerCount;
    TextView followingCount;

    LinearLayout followerCount2;
    LinearLayout followingCount2;

    public FragmentProfile(Activity context){
        this.context = context;
    }
    public static FragmentProfile fragment_profile(String nick){
        nickname = nick;
        return new FragmentProfile(context);
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //두가지의 경우가 있다.
        //1. 내 프로필에 들어온 경우
        //2. 다른사람 프로필에 들어온 경우

        //2.1. 나만 팔로우
        //2.2. 상대만 팔로우
        //2.3. 서로 팔로우
        //2.4. 팔로우 안함
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_snsprofile, container, false);

        followingCount2 = rootView.findViewById(R.id.followingCount2);
        followerCount2 = rootView.findViewById(R.id.followerCount2);

        boardCount = rootView.findViewById(R.id.boardCount);
        followerCount = rootView.findViewById(R.id.followerCount);
        followingCount = rootView.findViewById(R.id.followingCount);

        sns_profile_update = rootView.findViewById(R.id.sns_profile_update);
        sns_profile_follow = rootView.findViewById(R.id.sns_profile_follow);
        sns_profile_eachfollow = rootView.findViewById(R.id.sns_profile_eachfollow);
        sns_profile_following = rootView.findViewById(R.id.sns_profile_following);

        profileRecyclerView = rootView.findViewById(R.id.profileRecyclerView);
        sns_profile_nickname = rootView.findViewById(R.id.snsprofile_nickname);
        sns_profile_profile_image = rootView.findViewById(R.id.snsprofile_profile_image);

        sns_profile_profile_image.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            sns_profile_profile_image.setClipToOutline(true);
        }

        //팔로워 버튼 클릭
        followerCount2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FollowerFollowingCount.class);
                intent.putExtra("type", "followerCount");
                intent.putExtra("email", TrainnerInfo.email);
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });
        //팔로잉 버튼 클릭
        followingCount2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, FollowerFollowingCount.class);
                intent.putExtra("type", "followingCount");
                intent.putExtra("email", TrainnerInfo.email);
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });


        //팔로우하기 버튼 클릭
        sns_profile_follow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //팔로우 버튼 갱신 스레드
                new Thread_GoFollow().execute("http://"+IP_ADDRESS+"/user_signup/gotofollow.php", "follow", TrainnerInfo.nickname, nickname);
                //이 버튼을 클릭했을 경우엔 해당 닉네임 사용자에게 FCM알림이 가야한다.
                new Thread_Push().execute("https://fcm.googleapis.com/fcm/send",device_token);
            }
        });

        //맞팔로우하기 버튼 클릭
        sns_profile_eachfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //팔로우 버튼 갱신 스레드
                new Thread_GoFollow().execute("http://"+IP_ADDRESS+"/user_signup/gotofollow.php", "eachfollow", TrainnerInfo.nickname, nickname);
                //노티피케이션 전송 스레드
                new Thread_Push().execute("https://fcm.googleapis.com/fcm/send",device_token);
            }
        });
        //팔로우중 버튼 클릭
        sns_profile_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("로그인한 회원의 닉네임",TrainnerInfo.nickname);
                new Thread_GoFollow().execute("http://"+IP_ADDRESS+"/user_signup/gotofollow.php", "unfollow", TrainnerInfo.nickname, nickname);
            }
        });



        Log.i("SNS프로필 조회","들어옴");

        if(!nickname.equals("")){
            //다른사람 프로필에 들어왔다.
            sns_profile_follow.setVisibility(View.GONE);
            sns_profile_eachfollow.setVisibility(View.GONE);
            sns_profile_following.setVisibility(View.GONE);
            sns_profile_update.setVisibility(View.GONE);
            Log.i("Profile이동","SNS프로필사진을 클릭함");
            Log.i("Profile이동,닉네임",nickname);
            //해당 회원의 프로필 정보들을 모두 가져온다.(nickname으로 식별)
            new Thread_SNSProfile().execute("http://"+IP_ADDRESS+"/user_signup/snsprofile.php", nickname);

            //나와 어떤 관계인지 파악한다.
            new Thread_Follow().execute("http://"+IP_ADDRESS+"/user_signup/user_relation.php", nickname, TrainnerInfo.nickname);
//            nickname = "";
        } else {
            //내 프로필에 들어왔다.
            sns_profile_follow.setVisibility(View.GONE);
            sns_profile_eachfollow.setVisibility(View.GONE);
            sns_profile_following.setVisibility(View.GONE);
            sns_profile_update.setVisibility(View.VISIBLE);
            Log.i("Profile이동","내껄로 이동함");
            Log.i("Profile이동,닉네임",TrainnerInfo.nickname);
            new Thread_SNSProfile().execute("http://"+IP_ADDRESS+"/user_signup/snsprofile.php", TrainnerInfo.nickname);
            //로그인중인 회원의 프로필 정보들을 모두 가져온다.(nickname으로 식별)
        }

        return rootView;
    }
    public FragmentProfile fragmentProfile(){
        return new FragmentProfile(context);
    }
    private class Thread_Push extends AsyncTask<String, Void, String>  {

        //진행바표시
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            //진행다일로그 시작
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setMessage("잠시 기다려 주세요.");
//            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String res = null;
            String server_key = "AAAA1LP-XYg:APA91bEKQotwjovg23rmwFVDPq7fWUbag42wdxGSPmij6LJG3XYi8Y88HcjQ8Eop0ltAGBnaH1gZcikfksPbieVz1V6saiWeF5vBPkuPTHpieIUGrYyMzYCrrUqtCwuevcMB19L3gzM3";
            String serverURL = params[0];
            String device_token = params[1];

//            String postParameters = "nickname=" +  nickname ;

            //메시지 가공
            JsonObject jsonObj = new JsonObject();
            //token
            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(device_token);
            jsonObj.add("to", jsonElement);
            Log.i("노티전송1",jsonObj.get("to").toString());
            //Notification
            JsonObject notification = new JsonObject();
            notification.addProperty("title", "팔로우 요청");
            notification.addProperty("body", TrainnerInfo.nickname+"님이 회원님께 팔로우를 신청하셨습니다.");
            Log.i("노티전송2",notification.get("title")+"");
            Log.i("노티전송2",notification.get("body")+"");
            jsonObj.add("notification", notification);
            Log.i("받아온 유저의 토큰3",device_token);

            /*발송*/
            Log.i("노티전송3_서버키",server_key);
            final MediaType mediaType = MediaType.parse("application/json");
            OkHttpClient httpClient = new OkHttpClient();
            try {
                Request request = new Request.Builder().url(serverURL)
                        .addHeader("Content-Type", "application/json; UTF-8")
                        .addHeader("Authorization", "key=" + server_key)
                        .post(RequestBody.create(mediaType, jsonObj.toString())).build();
                Response response = httpClient.newCall(request).execute();
                  res = response.body().string();
                Log.i("notification response " , res);
            } catch (IOException e) {
                Log.i("팔로우 noti전달에러", e+"");
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

        }

    }
    private class Thread_SNSProfile extends AsyncTask<String, Void, String>  {

        //진행바표시
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            //진행다일로그 시작
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setMessage("잠시 기다려 주세요.");
//            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String nickname = params[1];

            String postParameters = "nickname=" +  nickname ;

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
                Log.d("Thread_SNSProfile", "POST response code - " + responseStatusCode);

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

                Log.d("Thread_SNSProfile", "Thread_Main : Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                int total_json = jsonArray.length();
                Log.i("JSON갯수_Profile", total_json + "");
                for (int i = 1; i <= total_json; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);

                    byte[] encodedByteArray = Base64.decode(jsonObject.getString("profile_image"), Base64.DEFAULT);
                    Bitmap profile_image = BitmapFactory.decodeByteArray(encodedByteArray, 0, encodedByteArray.length);


                    JSONArray json_thumbnail_image = jsonObject.getJSONArray("thumbnail_image");
                    int thumbnail_image_count = json_thumbnail_image.length();
                    Log.i("받아온 썸네일 갯수",thumbnail_image_count+"개");
                    for(int j = 0 ; j < thumbnail_image_count ; j ++){
                        JSONObject jsonObject1 = json_thumbnail_image.getJSONObject(j);
                        byte[] encodedByteArray1 = Base64.decode(jsonObject1.getString("thumbnail_image"), Base64.DEFAULT);
                        Bitmap thumbnail_image = BitmapFactory.decodeByteArray(encodedByteArray1, 0, encodedByteArray1.length);
                        Log.i("받아온 썸네일",thumbnail_image+"");
                        list_thumbnail.add(thumbnail_image);
                    }
                    device_token = jsonObject.getString("device_token");
                    Log.i("받아온 유저의 토큰1",device_token);
                    ProfileDTO ProfileDTO = new ProfileDTO(jsonObject.getString("email"), jsonObject.getString("nickname"), profile_image, list_thumbnail, jsonObject.getString("device_token"), jsonObject.getInt("boardCount"), jsonObject.getInt("followingCount"), jsonObject.getInt("followerCount"));
//                    Log.i("받아온 게시물 좋아요 여부",jsonObject.getString("isLike")+"");
//                    Log.i("받아온 게시물 번호DTO",BoardObject.no+"");
                    Log.i("받아온 게시물 수",ProfileDTO.boardCount+"");
                    Log.i("받아온 팔로잉 수",ProfileDTO.followingCount+"");
                    Log.i("받아온 팔로워 수",ProfileDTO.followerCount+"");
                    list.add(ProfileDTO);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0 ; i < list.size() ; i ++){
                Log.i("받아온 profile갯수",list.size()+"개");
                Log.i("받아온 썸네일 갯수",list.get(i).thumbnail_Image.size()+"개");
            }
            //프로필이미지 세팅
            sns_profile_profile_image.setImageBitmap(list.get(0).profile_image);
            //닉네임 세팅
            sns_profile_nickname.setText(list.get(0).nickname);
            //게시물 수 세팅
            boardCount.setText(list.get(0).boardCount+"");
            //팔로워 수 세팅
            followerCount.setText(list.get(0).followerCount+"");
            //팔로잉 수 세팅
            followingCount.setText(list.get(0).followingCount+"");

            //해당 리싸이클러뷰는 수직 방향으로 모든 게시물들을 보여주는 리싸이클러뷰다.
            AdapterProfile myAdapter = new AdapterProfile(list.get(0).thumbnail_Image, context);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
            profileRecyclerView.setLayoutManager(layoutManager);
            profileRecyclerView.setAdapter(myAdapter);
            profileRecyclerView.setHasFixedSize(true);
        }

    }

    private class Thread_Follow extends AsyncTask<String, Void, String>  {

        //진행바표시
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            //진행다일로그 시작
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setMessage("잠시 기다려 주세요.");
//            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String your_nickname = params[1];
            String my_nickname = params[2];

            String postParameters = "your_nickname=" + your_nickname + "&my_nickname=" + my_nickname;
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
                Log.d("Thread_Follow", "POST response code - " + responseStatusCode);

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

                Log.d("Thread_Follow", "Thread_Main : Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
//            TextView sns_profile_follow = null;
//            TextView sns_profile_eachfollow;
//            TextView sns_profile_following;
            Log.i("팔로잉 상태",result);

             if(result.equals("each other")){
                 //서로 팔로우 하는 경우
                 sns_profile_follow.setVisibility(View.GONE);
                 sns_profile_following.setVisibility(View.VISIBLE);
                 sns_profile_eachfollow.setVisibility(View.GONE);
             } else if (result.equals("follow")){
                 //내가 상대를 팔로우 한다. 상대는 안한다.
                 sns_profile_follow.setVisibility(View.GONE);
                 sns_profile_following.setVisibility(View.VISIBLE);
                 sns_profile_eachfollow.setVisibility(View.GONE);
             } else if (result.equals("follower")){
                 //상대가 나를 팔로우 한다. 난 안한다.
                 sns_profile_follow.setVisibility(View.GONE);
                 sns_profile_following.setVisibility(View.GONE);
                 sns_profile_eachfollow.setVisibility(View.VISIBLE);
             } else if (result.equals("no")) {
                 //서로 그 어떤 관계도 없다.
                 sns_profile_follow.setVisibility(View.VISIBLE);
                 sns_profile_following.setVisibility(View.GONE);
                 sns_profile_eachfollow.setVisibility(View.GONE);
             } else if (result.equals("me")){
                 sns_profile_update.setVisibility(View.VISIBLE);
                 sns_profile_follow.setVisibility(View.GONE);
                 sns_profile_following.setVisibility(View.GONE);
                 sns_profile_eachfollow.setVisibility(View.GONE);
             }
        }

    }
    private class Thread_GoFollow extends AsyncTask<String, Void, String>  {

        //진행바표시
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            //진행다일로그 시작
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setMessage("잠시 기다려 주세요.");
//            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String type = params[1];
            String my_nickname = params[2];
            String your_nickname = params[3];

            String postParameters = "type=" + type + "&my_nickname=" + my_nickname + "&your_nickname=" + your_nickname;
            Log.i("Thread_GoFollow파라미터",postParameters);
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
                Log.d("Thread_GoFollow", "POST response code - " + responseStatusCode);

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

                Log.d("Thread_GoFollow", "Thread_Main : Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
//            TextView sns_profile_follow = null;
//            TextView sns_profile_eachfollow;
//            TextView sns_profile_following;
            Log.i("팔로우 요청 상태",result);
            if(result.equals("follow")){
                //팔로우를 요청함
                sns_profile_follow.setVisibility(View.GONE);
                sns_profile_following.setVisibility(View.VISIBLE);
            } else if (result.equals("unfollow")){
                //팔로우를 취소함
                sns_profile_following.setVisibility(View.GONE);
                sns_profile_follow.setVisibility(View.VISIBLE);
            } else if (result.equals("eachfollow")){
                //맞팔로우함
                sns_profile_eachfollow.setVisibility(View.GONE);
                sns_profile_following.setVisibility(View.VISIBLE);
            }

        }

    }
}
