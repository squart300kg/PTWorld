package com.example.ptworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Reply extends AppCompatActivity {

    String IP_ADDRESS = "squart300kg.cafe24.com";
    TextView sns_reply_insert;
    EditText sns_reply_writting;

    TextView writter_nickname;
    ImageView writter_profile_image;
    ImageView writter_profile_image2;
    TextView writter_contents;
    private RecyclerView replyRecyclerView;
    ArrayList<ReplyObject> replyList = new ArrayList<>();
    BoardObject BoardObject = new BoardObject();
    Intent intent;
    String boardno;
    String replyno;
    String rereplyno;
    String device_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        replyno = getIntent().getStringExtra("replyno");
        rereplyno = getIntent().getStringExtra("rereplyno");

        intent = getIntent();

        String nickname = intent.getExtras().getString("nickname");
        String contentsText = intent.getExtras().getString("contents");

        writter_contents = findViewById(R.id.writter_contents);
        writter_nickname = findViewById(R.id.writter_nickname);
        writter_profile_image = findViewById(R.id.writter_profile_image);
//

        writter_nickname.setText( nickname);
        writter_contents.setText( contentsText);
//        writter_profile_image2.setImageBitmap(profile_image);
//        writter_profile_image.setImageBitmap(profile_image);

        replyRecyclerView = findViewById(R.id.replyRecyclerView);
        sns_reply_insert = findViewById(R.id.sns_reply_insert);
        sns_reply_writting = findViewById(R.id.sns_reply_writting);

        boardno = getIntent().getStringExtra("no");
        new Thread_Reply_Select().execute("http://"+IP_ADDRESS+"/user_signup/select_reply.php", boardno+"");//댓글창에 있는 댓글들을 모두 불러온다.

        sns_reply_writting.requestFocus();
        //댓글달기 버튼 클릭
        sns_reply_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reply_contents = sns_reply_writting.getText().toString();
                Log.i("작성한 댓글 내용",reply_contents + boardno);

                //댓글창이 비어있지 않다면 댓글쓰기를 수행.
                if(!reply_contents.equals("")){
                    new Thread_Reply().execute("http://"+IP_ADDRESS+"/user_signup/insert_reply.php", TrainnerInfo.email, TrainnerInfo.nickname, boardno+"", reply_contents, intent.getExtras().getString("nickname"));
                    sns_reply_writting.setText("");
                    hidekeypad(sns_reply_writting);//키패드를 내린다.
                }
            }
        });
    }
    private class Thread_Push extends AsyncTask<String, Void, String>  {

        //진행바표시
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = null;
            String server_key = "AAAA1LP-XYg:APA91bEKQotwjovg23rmwFVDPq7fWUbag42wdxGSPmij6LJG3XYi8Y88HcjQ8Eop0ltAGBnaH1gZcikfksPbieVz1V6saiWeF5vBPkuPTHpieIUGrYyMzYCrrUqtCwuevcMB19L3gzM3";
            String serverURL = params[0];
            String device_token = params[1];
            String boardno = params[2];
            String replyno = params[3];

//            String postParameters = "nickname=" +  nickname ;

            //메시지 가공
            JsonObject jsonObj = new JsonObject();
            //token
            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(device_token);
            jsonObj.add("to", jsonElement);
            Log.i("댓글노티전송1",jsonObj.get("to").toString());
            //Notification
            JsonObject notification = new JsonObject();
            notification.addProperty("title", "댓글");
            notification.addProperty("body", TrainnerInfo.nickname+"님이 회원님의 게시글에 댓글을 달았습니다.");
            Log.i("댓글노티전송2",notification.get("title")+"");
            Log.i("댓글노티전송2",notification.get("body")+"");
            jsonObj.add("notification", notification);

            JsonObject data = new JsonObject();
            data.addProperty("boardno",boardno);
            data.addProperty("replyno",replyno);
            jsonObj.add("data", data);
            Log.i("받아온 유저의 토큰3",device_token);

            /*발송*/
            Log.i("댓글노티전송3_서버키",server_key);
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
    public void hidekeypad(EditText edit){
        View view = getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        }
    }
    public void showkeypad(EditText edit){
        View view = getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edit, 0);
            Log.i("대댓글쓰기 메소드 활성화","클릭");
        }
    }

    class Thread_Reply extends AsyncTask<String, Void, String> {
        String IP_ADDRESS = "squart300kg.cafe24.com";
        String email1;
        String profile_imageUrl;
        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String email = params[1];
            String nickname = params[2];
            String no = params[3];
            String contents = params[4];
            String writter_nickname = params[5];

            String postParameters = "email=" + email + "&no="+no + "&nickname=" + nickname + "&contents=" + contents + "&writter_nickname=" + writter_nickname;
            Log.i("Thread_Reply서버 파라미터",postParameters);

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
                Log.d("Thread_Reply서버", "POST response code - " + responseStatusCode);

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

                Log.d("Thread_Reply서버", "Thread_Like: Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("댓글 결과",result);
//            String result2[] = result.split("AND");
//            String device_token = result2[0];
//            String result3[] = result2[1].split("AND");
//            String boardno = result3[0];
//            String replyno = result3[1];

            String result2[] = result.split("AND");
            String device_token = result2[0];
            String boardno = result2[1];
            String replyno = result2[2];
            Log.i("Reply_device_token",device_token);
            Log.i("Reply_boardno",boardno);
            Log.i("Reply_replyno",replyno);

            Log.i("댓글 삽입입현황_디바이스토큰",result);
            //댓글 입력을 완료했다. 이제 올린 댓글을 실시간으로 조회해야 한다.
            new Thread_Reply_Select().execute("http://"+IP_ADDRESS+"/user_signup/select_reply.php", boardno+"");//댓글창에 있는 댓글들을 모두 불러온다.

            //작성자에게 댓글을 달았다고 알림 메시지를 보낸다.
            if(!result.equals("댓글 등록 에러...ㅠ")){
                new Thread_Push().execute("https://fcm.googleapis.com/fcm/send",device_token, boardno, replyno);
            }

        }
    }
    class Thread_Reply_Select extends AsyncTask<String, Void, String> {
        String IP_ADDRESS = "squart300kg.cafe24.com";
        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String no = params[1];

            String postParameters = "no="+no ;
            Log.i("Thread_Reply_Select",postParameters);

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
                Log.d("Thread_Reply_Select", "POST response code - " + responseStatusCode);

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

                Log.d("Thread_Reply_Select", "Thread_Like: Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            replyList.clear();
            Log.i("댓글 조회 현황",result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                int total_json = jsonArray.length();
                Log.i("JSON갯수_Reply", total_json + "");
                for (int i = 1; i <= total_json; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);

                    byte[] encodedByteArray = Base64.decode(jsonObject.getString("profile_image"), Base64.DEFAULT);
                    Bitmap profile_image = BitmapFactory.decodeByteArray(encodedByteArray, 0, encodedByteArray.length);

                    ReplyObject ReplyObject = new ReplyObject(jsonObject.getString("nickname"), jsonObject.getString("email"),jsonObject.getString("contents"), profile_image, jsonObject.getInt("replyno"), jsonObject.getString("device_token"), jsonObject.getString("boardno"));

                    replyList.add(ReplyObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i = 0 ; i < replyList.size() ; i ++){
                Log.i("댓글 결과들", replyList.get(i).profile_image+"");
            }
            AdapterReply adapterReply;
            if(replyno != null && rereplyno != null){
                 adapterReply = new AdapterReply(replyList, rereplyno, Reply.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Reply.this);
                replyRecyclerView.setLayoutManager(layoutManager);
                replyRecyclerView.setAdapter(adapterReply);
                replyRecyclerView.setHasFixedSize(true);
                replyRecyclerView.scrollToPosition(Integer.parseInt(replyno));
                adapterReply.notifyDataSetChanged();
            } else if(replyno != null){
                adapterReply = new AdapterReply(replyList, Reply.this, replyList.size()+"");
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Reply.this);
                replyRecyclerView.setLayoutManager(layoutManager);
                replyRecyclerView.setAdapter(adapterReply);
                replyRecyclerView.setHasFixedSize(true);
                replyRecyclerView.scrollToPosition(replyList.size()-1);
                adapterReply.notifyDataSetChanged();
            } else {
                adapterReply = new AdapterReply(replyList, Reply.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Reply.this);
                replyRecyclerView.setLayoutManager(layoutManager);
                replyRecyclerView.setAdapter(adapterReply);
                replyRecyclerView.setHasFixedSize(true);
                adapterReply.notifyDataSetChanged();
            }



        }
    }
}
