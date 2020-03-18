package com.example.ptworld.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ptworld.Adapter.AdapterMessage;
import com.example.ptworld.DTO.ChattingDTO;
import com.example.ptworld.Fragment.FragmentProfile;
import com.example.ptworld.R;
import com.example.ptworld.DTO.UserInfo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Message extends AppCompatActivity implements View.OnClickListener {

    String TAG = "Message : ";
    RecyclerView messageRecyclerView;
    EditText message_writting;
    TextView message_insert;
    ArrayList<ChattingDTO> chatting_list = new ArrayList<>();
    AdapterMessage myAdapter;


    TextView chatting_title;

    Socket socket = null;
    BufferedReader in = null;
    BufferedReader in2 = null;
    PrintWriter out = null;
    InetAddress ia = null;

    String receiveMessage;
    String receiveFromNickname;
    String receiveTime;
    String receiveToNickname;
    Handler handler1;
    String message;

    String toNickname = FragmentProfile.nickname;
    String myNickname = UserInfo.nickname;

    String device_token;
    String IP_ADDRESS = "squart300kg.cafe24.com";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<ChattingDTO> shared_list = new ArrayList<>();
    ArrayList<ChattingDTO> chattingDTOS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        handler1 = new Handler();
        chatting_title = findViewById(R.id.chatting_title);

        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        message_writting = findViewById(R.id.message_writting);
        message_insert = findViewById(R.id.message_insert);

        message_writting.requestFocus();
        //전송버튼 클릭
        message_insert.setOnClickListener(this);
        
        //이전의 대화 내용들을 불러온다.
//        getChattingContents();

        }

        private void getChattingContents()
        {
            Gson gson = new Gson();
            sharedPreferences = getSharedPreferences("chatting",MODE_PRIVATE);
            String json = sharedPreferences.getString(toNickname, "");
            Type type = new TypeToken<ArrayList<ChattingDTO>>() {}.getType();
            try{
                Log.i(TAG+"json : ", json);
                chattingDTOS = gson.fromJson(json, type);
        }catch (NullPointerException ne){
            chattingDTOS = new ArrayList<>();
        }
        chatting_list = chattingDTOS;
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         for(int i = 0 ; i < chatting_list.size() ; i ++)
                         {
                             showUiChatting(chatting_list.get(i).direction); 
                         } 
                    }
                });
            }
        }).start();  
    }

    @Override
    protected void onResume() {
        super.onResume();
        //1. 소켓을 만든다.
        //2. 서버와 연결한다.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ia = InetAddress.getByName("192.168.56.1");
                    socket = new Socket(ia, 7778);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                    System.out.println(socket.toString());
                }catch(IOException e){}
            }
        }).start();

        //서버로부터 메시지를 받는다.
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String receiveData[] = in.readLine().split(":");//서버로부터 되돌아오는 데이터 읽어들임
                        receiveMessage = receiveData[0];
                        receiveFromNickname = receiveData[1];
                        receiveTime = receiveData[2];
                        try{
                            receiveToNickname = receiveData[3];
                        }catch(ArrayIndexOutOfBoundsException ai){
                            ai.printStackTrace();
                            receiveToNickname = UserInfo.nickname;
                        }


                        if(!receiveToNickname.equals(UserInfo.nickname)){
//                            //닉네임에 알맞은 디바이스토큰을 반환받는다.
                            new Thread_Return_DeviceToken().execute("http://"+IP_ADDRESS+"/user_signup/return_devicetoken.php", receiveToNickname);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Log.i(TAG,"서버로부터 받은 메세지 : " + receiveMessage);
                    Log.i(TAG,"서버로부터 받은 송신자 : " + receiveFromNickname);
                    Log.i(TAG,"서버로부터 받은 시간 : " + receiveTime);
                    Log.i(TAG,"서버로부터 받은 수신자 : " + receiveToNickname);

                    //내가 말한 메시지이다.
                    if(receiveFromNickname.equals(UserInfo.nickname)){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ChattingDTO chattingDTO = new ChattingDTO();
                                        chattingDTO.contents = receiveMessage;
                                        chattingDTO.nickname = receiveFromNickname;
                                        chattingDTO.direction = "right";
                                        chattingDTO.time = receiveTime;
                                        chatting_list.add(chattingDTO);

                                        showUiChatting("right");

                                        Log.i(TAG+" contents : ", receiveMessage);
                                        Log.i(TAG+" nickname : ", receiveFromNickname);
                                        Log.i(TAG+" time : ", receiveTime);
                                        Log.i(TAG+" chatting_list size : ", chatting_list.size()+"");

                                        //메시지 내용을 내부저장소에 저장
//                                        setStringArrayPref(receiveToNickname, chatting_list);



                                    }
                                });
                            }
                        }).start();
                        //다른사람이 말한 메시지이다.
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ChattingDTO chattingDTO = new ChattingDTO();
                                        chattingDTO.contents = receiveMessage;
                                        chattingDTO.nickname = receiveFromNickname;
                                        chattingDTO.direction = "left";
                                        chattingDTO.time = receiveTime;
                                        chatting_list.add(chattingDTO);

                                        showUiChatting("left");

                                        Log.i(TAG+" contents : ", receiveMessage);
                                        Log.i(TAG+" nickname : ", receiveFromNickname);
                                        Log.i(TAG+" time : ", receiveTime);
                                        Log.i(TAG+" chatting_list size : ", chatting_list.size()+"");

                                        //메시지 내용을 내부저장소에 저장
//                                        setStringArrayPref(receiveToNickname, chatting_list);
                                    }
                                });
                            }
                        }).start();
                    }
                }
            }
        }).start();
    }

    //TODO : 메시지 내용을 내부저장소에 저장함
    private void setStringArrayPref(String receiveFromNickname, ArrayList<ChattingDTO> values) {
        //기존의 채팅내용에다가 새로운 채팅내용을 덧붙인다.
//        shared_list = getStringArrayPref(receiveFromNickname);
//
//
//        shared_list.addAll(values);
        shared_list = values;
        sharedPreferences = getSharedPreferences("chatting",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        JSONArray array = new JSONArray();
        for (int i = 0; i < shared_list.size(); i++) {
            array.put(shared_list.get(i));
        }
        if (!shared_list.isEmpty()) {
            editor.putString(receiveFromNickname, array.toString());
            Log.i(TAG+"shared저장 : ","shared저장 성공");
        } else {
            editor.putString(receiveFromNickname, null);
        }
        editor.commit();
    }


    //TODO : 내부 저장소에 저장된 메시지 내용을 불러옴
    private ArrayList<ChattingDTO> getStringArrayPref(String receiveFromNickname) {
        Gson gson = new Gson();
        sharedPreferences = getSharedPreferences("chatting",MODE_PRIVATE);
        String json = sharedPreferences.getString(receiveFromNickname, null);


        Type type = new TypeToken<ArrayList<ChattingDTO>>() {}.getType();
        try{
            chattingDTOS = gson.fromJson(json, type);
        }catch (NullPointerException ne){
            chattingDTOS = new ArrayList<>();
        }

        return chattingDTOS;
    }

    //소켓을 제거한다.
    @Override
    protected void onStop() {
        super.onStop();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.message_insert:
                String message_contents = message_writting.getText().toString();
                Log.i("작성한 채팅 메시지 내용", message_contents);
//                TextUtils.isEmpty(message_writting.getText())
                //메시지창이 비어있지 않다면 댓글쓰기를 수행.
                if(!message_contents.equals("") || message_contents != null){
                    message = message_writting.getText().toString();

                    //메시지를 보낸다.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SimpleDateFormat format1 = new SimpleDateFormat( "HH시mm분");
                            Date time = new Date();
                            String time1 = format1.format(time);
                            message += ":" + UserInfo.nickname;
                            message += ":" + time1;
                            message += ":" + toNickname;
                            //메시지 형식 : 메시지 내용:전송하는사람 닉네임:현재시간:받는사람 닉네임
                            Log.i(TAG, "서버로 보낼 메시지 : " + message);
                            out.println(message);                        //서버로 데이터 전송
                            out.flush();


                        }
                    }).start();

                    message_writting.setText("");

                    hidekeypad(message_writting);//키패드를 내린다.
                }
                break;
        }
    }

    private void showUiChatting(String direction)
    {
        myAdapter = new AdapterMessage(Message.this, chatting_list, direction);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Message.this, LinearLayoutManager.VERTICAL, false);
        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setAdapter(myAdapter);
        messageRecyclerView.scrollToPosition(chatting_list.size()-1);
        myAdapter.notifyDataSetChanged();
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
            Toast.makeText(this, "채팅글쓰기 메소드 활성화", Toast.LENGTH_SHORT).show();
        }
    }
    class Thread_Return_DeviceToken extends AsyncTask<String, Void, String> {
        String TAG = "Thread_Return_DeviceToken";
        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String receiveToNickname = params[1];

            String postParameters = "nickname=" + receiveToNickname;
            Log.i(TAG+"_URL",serverURL);
            Log.i(TAG+"_파라미터",postParameters);

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
                Log.d(TAG+"서버", "POST response code - " + responseStatusCode);

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

                Log.d("Thread_ReReply서버", "Thread_Like: Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //한개의 대댓글 삽입을 완료했고, 대댓글을 달은 댓글 번호를 가져온다.
            Log.i(TAG+"_결과",result);
            device_token = result;

            //알림을 보낸다(채팅 수신자에게)
            new Thread_Push().execute("https://fcm.googleapis.com/fcm/send", device_token, receiveToNickname);
        }
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
            String receiveToNickname = params[2];


//            String postParameters = "nickname=" +  nickname ;

            //메시지 가공
            JsonObject jsonObj = new JsonObject();
            //token
            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(device_token);
            jsonObj.add("to", jsonElement);
            Log.i("채팅노티_device_Token",jsonObj.get("to").toString());
            //Notification
            JsonObject notification = new JsonObject();
            notification.addProperty("title", "채팅");
            notification.addProperty("body", "추후에 채팅 내용으로 채울 예정임");
            Log.i("채팅노티_title",notification.get("title")+"");
            Log.i("채팅노티_body",notification.get("body")+"");
            jsonObj.add("notification", notification);

            JsonObject data = new JsonObject();
            data.addProperty("receiveToNickname",receiveToNickname);

            jsonObj.add("data", data);
            Log.i("받아온 유저의 토큰3",device_token);

            /*발송*/
            Log.i("채팅노티전송3_서버키",server_key);
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
}
