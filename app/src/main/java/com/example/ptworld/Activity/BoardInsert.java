package com.example.ptworld.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ptworld.Fragment.FragmentBoardInsert;
import com.example.ptworld.Fragment.FragmentSNS;
import com.example.ptworld.R;
import com.example.ptworld.DTO.TrainnerInfo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

public class BoardInsert extends AppCompatActivity {

    TextView boardInsertSuccess;
    EditText boardText;
    ImageView boardImage;
    String boardTextStr;
    ArrayList<String> insertBoard = new ArrayList<>(5);
    private com.example.ptworld.Fragment.FragmentSNS FragmentSNS = new FragmentSNS(BoardInsert.this);
    private FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
//    Intent intent;
//    ArrayList<Bitmap> bitmapArr = new ArrayList();

    private static String IP_ADDRESS = "squart300kg.cafe24.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_insert);


        FragmentBoardInsert.insertBitmap.removeAll(Collections.singleton(null));
        Log.i("이미지 갯수",FragmentBoardInsert.insertBitmap.size()+"개");
        for(int i = 1 ; i <= FragmentBoardInsert.insertBitmap.size() ; i ++){
            Log.i("이미지들",FragmentBoardInsert.insertBitmap.get(i - 1)+"");
        }
//        intent = getIntent();
//        bitmapArr = (ArrayList<Bitmap>) intent.getSerializableExtra("image");
//        Log.i("받은 이미지 갯수",bitmapArr.size()+"개");
        boardText = findViewById(R.id.boardText);
        boardImage = findViewById(R.id.boardImage);
        try{
            boardImage.setImageBitmap(FragmentBoardInsert.insertBitmap.get(0));
        }catch(IndexOutOfBoundsException e){

        }
        boardInsertSuccess = findViewById(R.id.boardInsertSuccess);

        for(int i = 1 ; i <= 5 ; i ++){
            insertBoard.add(null);
        }
        boardInsertSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //프로필 이미지를 꼭 넘겨야만 하는걸까? 꼭 그방법밖에 없었을까!? 아님 닉네임에 해당하는 프로필 이미지를 불러올 수도 있지 않은가!?
                //굳이 서버에 이미지를 또 저장할 필요가 있는가!?
                Log.i("게시물 삽입스레드 직전","실행중");
                boardTextStr = boardText.getText().toString();
                Log.i("게시물Text1",boardTextStr);

                for(int i = 1 ; i <= FragmentBoardInsert.insertBitmap.size() ; i ++){
                    Bitmap boardImage = FragmentBoardInsert.insertBitmap.get(i - 1);
                    ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                    boardImage.compress(Bitmap.CompressFormat.PNG,100, baos);    //bitmap compress
                    byte [] arr=baos.toByteArray();
                    String image= Base64.encodeToString(arr, Base64.DEFAULT);
                    try {
                        image = URLEncoder.encode(image,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    insertBoard.add(i - 1, image);
                }
                new Thread_BoardInsert().execute("http://"+IP_ADDRESS+"/user_signup/insertBoard.php", TrainnerInfo.nickname, boardTextStr, insertBoard.get(0), insertBoard.get(1), insertBoard.get(2), insertBoard.get(3), insertBoard.get(4));
//                new Thread_BoardInsert().execute("http://"+IP_ADDRESS+"/user_signup/insertBoard.php",TrainnerInfo.nickname, boardTextStr);

                Log.i("게시물 삽입스레드 직후","실행중");
                Log.i("게시물Text2",boardTextStr);

                finish();

            }
        });
    }

    private class Thread_BoardInsert extends AsyncTask<String, Void, String> {

        //진행바표시
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            //진행다일로그 시작
//            progressDialog = new ProgressDialog(BoardInsert.this);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setMessage("잠시 기다려 주세요.");
//            progressDialog.show();
//            Log.i("게시물 삽입 스레드pre","실행중");

        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String nickname = params[1];
            String contentsText = params[2];

            String image1 = params[3];
            String image2 = params[4];
            String image3 = params[5];
            String image4 = params[6];
            String image5 = params[7];

            String postParameters = "nickname=" + nickname + "&contentsText=" + contentsText + "&image1=" + image1 + "&image2=" + image2 + "&image3=" + image3 + "&image4=" + image4 + "&image5=" + image5;
//            String postParameters = "nickname=" + nickname + "&contentsText=" + contentsText + "&image1=" + image1;
//            String postParameters = "nickname=" + nickname + "&contentsText=" + contentsText;

            Log.i("게시물Insert파라미터들",postParameters);
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
                Log.d("게시물Insert", "POST response code - " + responseStatusCode);

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

                Log.d("게시물Insert", "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {

            Log.i("스레드 실행 결과",result);
            Log.i("게시물 삽입스레드 post","실행중");
//            builder.
//            progressDialog.dismiss();
        }
    }
}
