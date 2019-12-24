package com.example.ptworld.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptworld.R;
import com.example.ptworld.DTO.ReReplyObject;
import com.example.ptworld.DTO.ReplyObject;
import com.example.ptworld.DTO.TrainnerInfo;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdapterReply extends RecyclerView.Adapter<AdapterReply.ViewHolder> {

    //데이터 배열 선언

    private ArrayList<ReplyObject> mList;
    private ArrayList<ReReplyObject> reReplyObjects = new ArrayList<>();
    Activity context;
    private long btnPressTime = 0;
    String IP_ADDRESS = "squart300kg.cafe24.com";
    RecyclerView rereplyRecyclerView;
    String replyno = null;
    String rereplyno = null;
    //생성자
    public AdapterReply(ArrayList<ReplyObject> list, Activity context) {
        this.mList = list;
        this.context = context;
    }
    public AdapterReply(ArrayList<ReplyObject> list, Activity context, String replyno) {
        this.mList = list;
        this.context = context;
        this.replyno = replyno;
    }
    public AdapterReply(ArrayList<ReplyObject> list, String rereplyno,Activity context) {
        this.mList = list;
        this.context = context;
        this.rereplyno = rereplyno;
    }
    public AdapterReply(ArrayList<ReplyObject> list ) {
        this.mList = list;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rereplyRecyclerView;
        ImageView reply_profile_image2;
        TextView reply_nickname;
        TextView reply_contents;
        TextView reply_rereply;
        TextView sns_reply_insert;
        LinearLayout reply_contentsFrame;

        public ViewHolder(View itemView) {
            super(itemView);
            reply_contentsFrame = itemView.findViewById(R.id.reply_contentsFrame);
            reply_rereply = itemView.findViewById(R.id.reply_rereply);
            rereplyRecyclerView = itemView.findViewById(R.id.rereplyRecyclerView);
            reply_contents = itemView.findViewById(R.id.reply_contents);
            reply_nickname = itemView.findViewById(R.id.reply_nickname);
            reply_profile_image2 = itemView.findViewById(R.id.reply_profile_image2);
            reply_profile_image2.setBackground(new ShapeDrawable(new OvalShape()));
            if(Build.VERSION.SDK_INT >= 21) {
                reply_profile_image2.setClipToOutline(true);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_one_reply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {//뷰홀더 객체가 그거다. 액티비티에 위젯뷰들을 그려주는 녀석.
        if(replyno != null){
            if(Integer.parseInt(replyno)-1 == position){
//                holder.reply_contentsFrame.setBackgroundColor(Color.rgb(189, 183, 107));
                holder.reply_contentsFrame.setBackgroundResource(R.color.grey);
                replyno = null;
            }
        }

        //일단 대댓글을 달고, 달린 대댓글들을 모두 조회해 오는덴 성공했다. 이제 댓글창을 처음 들어갔을 때, 대댓글들을 조회할 수 있도록 한다.
        new Thread_ReReply_Select(holder).execute("http://"+IP_ADDRESS+"/user_signup/select_rereply.php", mList.get(position).replyno+"", rereplyno);//한개의 댓글창에 있는 대댓글들을 모두 불러온다.
        holder.reply_profile_image2.setImageBitmap(mList.get(position).profile_image);
        holder.reply_nickname.setText(mList.get(position).nickname);
        holder.reply_contents.setText(mList.get(position).contents);
        holder.reply_rereply.setOnClickListener(new View.OnClickListener() {//댓글달기 버튼을 클릭한다.
            @Override
            public void onClick(View view) {
                //우선 첫번쨰로 댓글입력창의 포커스를 얻는다.
                final EditText sns_reply_writting = context.findViewById(R.id.sns_reply_writting);
                //에디트 텍스트에 포커스 맞추기
                sns_reply_writting.requestFocus();
                //키패드 올리기
                showkeypad(sns_reply_writting);
                //댓글달기 버튼 객체
                TextView sns_reply_insert = context.findViewById(R.id.sns_reply_insert);
                //대댓글 달기 버튼 클릭
                sns_reply_insert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!sns_reply_writting.equals("")){//댓글입력창에 내용이 있을 경우에만!
                            String rereply_contents = sns_reply_writting.getText().toString();
                            //대댓글을 입력해준다.
                            new Thread_ReReply(holder).execute("http://"+IP_ADDRESS+"/user_signup/testtest.php", TrainnerInfo.email, TrainnerInfo.nickname, mList.get(position).replyno+"", rereply_contents, mList.get(position).nickname, mList.get(position).boardno, position+"");
                            //대댓글 입력을 완료했으니 키패드를 내린다.
                            hidekeypad(sns_reply_writting);
                            //또한 대댓글 입력창도 지워준다.
                            sns_reply_writting.setText("");
                        }
                    }
                });
            }
        });

    }
    public void showkeypad(EditText edit){
        View view = context.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edit, 0);
            Log.i("대댓글쓰기 메소드 활성화","클릭");
        }
    }
    public void hidekeypad(EditText edit){
        View view = context.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        }
    }
    @Override
    public int getItemCount() {
        //return mList.size();
        return mList.size();
    }

    class Thread_ReReply extends AsyncTask<String, Void, String> {
        String IP_ADDRESS = "squart300kg.cafe24.com";
        ViewHolder holder;
        String boardno;
        String position;

        public Thread_ReReply(ViewHolder holder) {
            this.holder= holder;
        }
        public Thread_ReReply(){}

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String email = params[1];
            String nickname = params[2];
            String replyno = params[3];
            String contents = params[4];
            String writter_nickname = params[5];
            boardno = params[6];
            position = params[7];

            String postParameters = "email=" + email + "&replyno="+replyno + "&nickname=" + nickname + "&contents=" + contents + "&writter_nickname=" + writter_nickname;
            Log.i("Thread_ReReply서버 URL",serverURL);
            Log.i("Thread_ReReply서버 파라미터",postParameters);

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
                Log.d("Thread_ReReply서버", "POST response code - " + responseStatusCode);

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
            Log.i("대댓글 삽입입현황",result);
            if(!result.equals("대댓글 등록 에러...ㅠ")){//대댓글 입력을 완료했으니 대댓글창을 갱신해야한다.
                String result2[] = result.split("AND");
                String replyno = result2[0];
                String device_token = result2[1];
                String rereplyno = result2[2];

                 new Thread_ReReply_Select(holder).execute("http://"+IP_ADDRESS+"/user_signup/select_rereply.php", replyno, rereplyno);//한개의 댓글창에 있는 대댓글들을 모두 불러온다.
                 new Thread_Push().execute("https://fcm.googleapis.com/fcm/send",device_token, boardno, replyno, rereplyno, position);
            }


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
            String boardno = params[2];
            String replyno = params[3];
            String rereplyno = params[4];
            String position = params[5];

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
            notification.addProperty("title", "대댓글");
            notification.addProperty("body", TrainnerInfo.nickname+"님이 회원님의 게시글에 대댓글을 달았습니다.");
            Log.i("댓글노티전송2",notification.get("title")+"");
            Log.i("댓글노티전송2",notification.get("body")+"");
            jsonObj.add("notification", notification);

            JsonObject data = new JsonObject();
            data.addProperty("boardno",boardno);
            data.addProperty("replyno",position);
            data.addProperty("rereplyno",rereplyno);

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

    class Thread_ReReply_Select extends AsyncTask<String, Void, String> {
        String IP_ADDRESS = "squart300kg.cafe24.com";
        ViewHolder holder;
        String rereplyno=null;

        public Thread_ReReply_Select(ViewHolder holder){
            this.holder = holder;
        }
        public Thread_ReReply_Select(){}
        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String replyno = params[1];
            rereplyno = params[2];

            String postParameters = "replyno="+replyno ;
            Log.i("Thread_ReReply_Select",postParameters);

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
            //해당 댓글번호에 달린 대댓글을 모조리 가져온다.
            reReplyObjects.clear();
            Log.i("대댓글 조회 현황",result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                int total_json = jsonArray.length();
                Log.i("JSON갯수_ReReply", total_json + "");
                for (int i = 1; i <= total_json; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);

                    byte[] encodedByteArray = Base64.decode(jsonObject.getString("profile_image"), Base64.DEFAULT);
                    Bitmap profile_image = BitmapFactory.decodeByteArray(encodedByteArray, 0, encodedByteArray.length);

                    ReReplyObject ReReplyObject = new ReReplyObject(jsonObject.getString("nickname"), jsonObject.getString("email"),jsonObject.getString("contents"), profile_image, jsonObject.getInt("rereplyno"));

                    reReplyObjects.add(ReReplyObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //이로써 하나의 댓글에 달린 대댓글들을 모조리 가져왔다. 이를 해당하는 댓글번호에 달아주기만 하면 된다. 몇번째 리싸이클러뷰인지 어떻게 지정할 것인가??
            try{
                for(int i = 0 ; i < reReplyObjects.size() ; i ++){
                    Log.i("대댓글 결과들", reReplyObjects.get(i).profile_image+"");

                }

            }catch (NullPointerException e){
                Log.i("대댓글 결과들","대댓글 없다우");
            }
            AdapterReReply adapterReply;
            if(rereplyno != null){
                adapterReply = new AdapterReReply(reReplyObjects, context, reReplyObjects.size()+"");
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                holder.rereplyRecyclerView.setLayoutManager(layoutManager);
                holder.rereplyRecyclerView.setAdapter(adapterReply);
                holder.rereplyRecyclerView.setHasFixedSize(true);
                holder.rereplyRecyclerView.scrollToPosition(reReplyObjects.size()-1);
                adapterReply.notifyDataSetChanged();
            } else {
                  adapterReply = new AdapterReReply(reReplyObjects, context);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                holder.rereplyRecyclerView.setLayoutManager(layoutManager);
                holder.rereplyRecyclerView.setAdapter(adapterReply);
                holder.rereplyRecyclerView.setHasFixedSize(true);
                adapterReply.notifyDataSetChanged();
            }





        }
    }



}
