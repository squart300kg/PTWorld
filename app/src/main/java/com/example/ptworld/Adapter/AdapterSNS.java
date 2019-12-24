package com.example.ptworld.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ptworld.Activity.LikeList;
import com.example.ptworld.Activity.Reply;
import com.example.ptworld.DTO.BoardObject;
import com.example.ptworld.DTO.TrainnerInfo;
import com.example.ptworld.Fragment.GoFragmentProfile;
import com.example.ptworld.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AdapterSNS extends RecyclerView.Adapter<AdapterSNS.ViewHolder> {

    //데이터 배열 선언
    public static ArrayList<BoardObject> mList;
    Context context;
    private long btnPressTime = 0;
    String IP_ADDRESS = "squart300kg.cafe24.com";
    GoFragmentProfile goFragmentProfile;
    String device_token;
    //생성자
    public AdapterSNS(ArrayList<BoardObject> list, Context context, GoFragmentProfile goFragmentProfile) {
        this.mList = list;
        this.context = context;
        this.goFragmentProfile = goFragmentProfile;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {

        ImageView sns_profile_image;
        ImageView reply_profile_image;
        LottieAnimationView sns_like;
        ImageView sns_like2;
        TextView sns_nickname;
        TextView sns_nickname2;
        TextView sns_contentsTest;
        ViewPager viewPage;
        LinearLayout viewPage_Linear;
        TextView sns_좋아요;
        ImageView sns_reply;
        TextView sns_reply_text;
        TextView sns_reply_count;

        public ViewHolder(View itemView) {
            super(itemView);
            sns_reply_count = itemView.findViewById(R.id.sns_reply_count);
            sns_reply_text = itemView.findViewById(R.id.sns_reply_text);
            sns_reply = itemView.findViewById(R.id.sns_reply);
            sns_좋아요 = itemView.findViewById(R.id.sns_좋아요);
            sns_like = itemView.findViewById(R.id.sns_like);
            sns_like2 = itemView.findViewById(R.id.sns_like2);
            viewPage_Linear = itemView.findViewById(R.id.viewPage_Linear);
            viewPage = itemView.findViewById(R.id.viewPage);
            sns_nickname2 = itemView.findViewById(R.id.sns_nickname2);
            sns_nickname = itemView.findViewById(R.id.sns_nickname);
            sns_contentsTest = itemView.findViewById(R.id.sns_contentsTest);
            sns_profile_image = itemView.findViewById(R.id.sns_profile_image);
            sns_profile_image.setBackground(new ShapeDrawable(new OvalShape()));
            reply_profile_image = itemView.findViewById(R.id.reply_profile_image);
            reply_profile_image.setBackground(new ShapeDrawable(new OvalShape()));
            if(Build.VERSION.SDK_INT >= 21) {
                sns_profile_image.setClipToOutline(true);
                reply_profile_image.setClipToOutline(true);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_snscontents, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {//뷰홀더 객체가 그거다. 액티비티에 위젯뷰들을 그려주는 녀석.
        DecimalFormat formatter = new DecimalFormat("###,###");
        String like = formatter.format(mList.get(position).좋아요);
        if(mList.get(position).replycount == 0){
            holder.sns_reply_count.setVisibility(View.GONE);
        }else{
            holder.sns_reply_count.setText("댓글"+mList.get(position).replycount+"개 모두보기");
        }
        holder.sns_좋아요.setText("좋아요  "+like+"개");
        holder.sns_nickname.setText(mList.get(position).nickname);
        holder.sns_nickname2.setText(mList.get(position).nickname);
        holder.sns_profile_image.setImageBitmap(mList.get(position).profile_image);
        holder.sns_contentsTest.setText(mList.get(position).contentsText);
        holder.reply_profile_image.setImageBitmap(mList.get(position).profile_image);


        //좋아요한 사람을 확인하려할때 클릭하는 버튼
        holder.sns_좋아요.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LikeList.class);
                intent.putExtra("no", mList.get(position).no);
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });
        //좋아요가 눌린 상태를 저장하기 위해서는 사용자가 좋아요를 눌렀는지를 DB에서부터 검사한다.
        //만약 사용자가 해당 게시물에 좋아요를 누른 데이터가 있다면 버튼을 색칠한 상태로 유지시켜준다.
        if(mList.get(position).isLike.equals("yes")){//만약 사용자가 좋아요를 누른 상태라면
            holder.sns_like.setVisibility(View.VISIBLE);
            holder.sns_like2.setVisibility(View.GONE);
            holder.sns_like.playAnimation();
//            holder.sns_like2.setImageResource(R.drawable.heart2);
        } else if (mList.get(position).isLike.equals("no")){
            holder.sns_like.setVisibility(View.GONE);
            holder.sns_like2.setVisibility(View.VISIBLE);
        }

        //(댓글쓰기)이미지
        holder.sns_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), Reply.class);
                intent.putExtra("no",mList.get(position).no+"");

//                Bitmap sendBitmap = mList.get(position).profile_image;
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//                intent.putExtra("profile_image", byteArray);

                intent.putExtra("contents", mList.get(position).contentsText);
                intent.putExtra("nickname", mList.get(position).nickname);
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });
        //(댓글쓰기)텍스트
        holder.sns_reply_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), Reply.class);
                intent.putExtra("no",mList.get(position).no+"");

//                Bitmap sendBitmap = mList.get(position).profile_image;
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//                intent.putExtra("profile_image", byteArray);

                intent.putExtra("contents", mList.get(position).contentsText);
                intent.putExtra("nickname", mList.get(position).nickname);
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });

        //좋아요 버튼을 눌렀을 때
        holder.sns_like2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.sns_like.setVisibility(View.VISIBLE);
                holder.sns_like.playAnimation();

                holder.sns_like2.setVisibility(View.GONE);
                String type = "like";
//                Thread_Like thread_like = new Thread_Like(position);
                Log.i("좋아요 쓰레드 직전의 no값",mList.get(position).no+"");
                new Thread_Like(position).execute("http://"+IP_ADDRESS+"/user_signup/like.php", type, mList.get(position).no+"", TrainnerInfo.email, TrainnerInfo.nickname);

                //좋아요 버튼 클릭에 대한 푸쉬메시지를 보내준다.
                new Thread_Push().execute("https://fcm.googleapis.com/fcm/send",mList.get(position).device_token, mList.get(position).no+"");

            }
        });
        //좋아요 취소를 눌렀을 때.
        holder.sns_like.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                holder.sns_like.setVisibility(View.GONE);
                holder.sns_like.playAnimation();

                holder.sns_like2.setVisibility(View.VISIBLE);
                String type = "dislike";
//                Thread_Like thread_like = new Thread_Like();
                Log.i("좋아요 쓰레드 직전의 no값",mList.get(position).no+"");
                new Thread_Like(position).execute("http://"+IP_ADDRESS+"/user_signup/like.php", type, mList.get(position).no+"", TrainnerInfo.email, TrainnerInfo.nickname);

            }
        });
        //프로필 사진을 클릭했을 때
        holder.sns_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goFragmentProfile.goFragmentProfile(mList.get(position).nickname);
            }
        });

        AdapterSNSPage adapterSNSPage = new AdapterSNSPage(context, mList.get(position).boardImage, holder, mList.get(position).no);
        holder.viewPage.setAdapter(adapterSNSPage);

    }

    @Override
    public int getItemCount() {
        //return mList.size();
        return mList.size();
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
            String no = params[2];

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
            notification.addProperty("title", "게시물 좋아요");
            notification.addProperty("body", TrainnerInfo.nickname+"님이 회원님의 게시물을 좋아합니다.");
            Log.i("노티전송2",notification.get("title")+"");
            Log.i("노티전송2",notification.get("body")+"");
            jsonObj.add("notification", notification);

            //데이터
            JsonObject data = new JsonObject();
            data.addProperty("boardno",no);
            jsonObj.add("data",data);
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
    class Thread_Like extends AsyncTask<String, Void, String> {
        String IP_ADDRESS = "squart300kg.cafe24.com";
        int position = 0;
        public Thread_Like(int position){
            this.position = position;
        }
        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String type = params[1];
            String no = params[2];
            String email = params[3];
            String nickname = params[4];

            String postParameters = "type=" + type + "&no="+no + "&email=" + email + "&nickname=" + nickname;
            Log.i("Thread_Like서버 파라미터",postParameters);

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
                Log.d("Thread_Like", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                    Log.i("인풋스트림",inputStream+"");
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                Log.i("좋아요 현황 인풋스트림0",inputStreamReader+"");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                    Log.i("좋아요 현황 인풋스트림1",bufferedReader.readLine()+"");
                    Log.i("좋아요 현황 인풋스트림2",line+"");
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d("Thread_Like", "Thread_Like: Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int likeCount = 0;
            String isLike = null;
            try{
                JSONArray jsonArray = new JSONArray(result);
                int total_json = jsonArray.length();
                Log.i("JSON갯수_SNS", total_json + "");
                for (int i = 1; i <= total_json; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i- 1);
                    likeCount = jsonObject.getInt("likeCount");
                    isLike = jsonObject.getString("isLike");
                }
            }catch(Exception e){}
            Log.i("좋아요 현황 내부클래스",result);
            AdapterSNS.mList.get(position).좋아요 = likeCount;
            AdapterSNS.mList.get(position).isLike = isLike;

            Log.i("isLike값 : ",isLike);
            Log.i("likeCount값 : ",likeCount+"");

            notifyItemChanged(position);

            //한마디로 좋아요 버튼을 누른다면 isLike에 대한 값을 지금 여기서 응답받아서 mList.get(position).isLike에 세팅한다.
            //세팅하기 위해서는 like.php에서 좋아요 갯수뿐만 아니라 좋아요 여부도 가져와야 한다.
            //즉 하나의 게시물을 조회할 땐, 두가지를 가져온다. 한개는 좋아요 갯수, 둘째는 내가 좋아요를 눌렀는지의 여부이다.
        }
    }



}
