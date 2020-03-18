package com.example.ptworld.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ptworld.DTO.UserInfo;
import com.example.ptworld.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Insert_User extends AppCompatActivity {

    private static String TAG = "phptest";
    private static String IP_ADDRESS = "squart300kg.cafe24.com";

    String profile_imageUrl;
    EditText email;
    EditText password;
    EditText passwordConfirm;
    EditText nickname;
    EditText prize;
    EditText place;
    static ImageView profile_image;

    Intent intent;
    String SHA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert__trainner);

        email = findViewById(R.id.trainner_Email);
        password = findViewById(R.id.trainner_Password);
        passwordConfirm = findViewById(R.id.trainner_PasswordConfirm);
        nickname = findViewById(R.id.trainner_Nickname);
        prize = findViewById(R.id.trainner_Prize);
        place = findViewById(R.id.trainner_Place);

        profile_image = findViewById(R.id.profile_image_trainner);
        profile_image.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            profile_image.setClipToOutline(true);
        }

        if(getIntent() != null){
            intent = getIntent();
            email.setText(intent.getExtras().getString("email"));
            if(!email.getText().toString().equals("")){
                email.setEnabled(false);

                try{//카카오톡 프로필 사진이 있다면 프로필 이미지를 지정한다.
                    profile_imageUrl = intent.getExtras().getString("profile_imageUrl");
                    Log.i("회원가입_프사",profile_imageUrl);
//                    Glide.with(getApplicationContext()).asBitmap().load(profile_imageUrl)
//                            .into(profile_image);
//                    Glide.with(getApplicationContext()).load(profile_imageUrl).preload(100,100)
                    Glide.with(getApplicationContext()).load(profile_imageUrl).asBitmap()
                            .override(1110,1110).centerCrop().thumbnail(0.1f)
                            .into(profile_image);
                    Log.i("프로필사진 너비",profile_image.getWidth()+"");
                    Log.i("프로필사진 높이",profile_image.getHeight()+"");
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                password.setVisibility(View.GONE);
                passwordConfirm.setVisibility(View.GONE);

                String random = (int)(Math.random() * 100) + 1+"";
                try{
                    MessageDigest sh = MessageDigest.getInstance("SHA-256");
                    sh.update(random.getBytes());
                    byte byteData[] = sh.digest();
                    StringBuffer sb = new StringBuffer();
                    for(int i = 0 ; i < byteData.length ; i++){
                        sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
                    }
                    SHA = sb.toString();
                }catch(NoSuchAlgorithmException e){
                    e.printStackTrace();
                    SHA = null;
                }

                password.setText(SHA);
                passwordConfirm.setText(SHA);
            }
        }

    }

    public void signup_Trainner(View view) {

        String trainner_Email = email.getText().toString();
        String trainner_password = password.getText().toString();
        String trainner_passwordConfirm = passwordConfirm.getText().toString();
        String trainner_nickname = nickname.getText().toString();
        String trainner_place = "temp_data";
        String trainner_prize = "temp_data";

        //프로필 이미지를 형식에 맞게 변환한다.(ByteArray->Base64 -> URLEncode(YTF-8))
        Drawable drawable = profile_image.getDrawable();//이미지뷰에서 이미지를 받아온다.
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();//받아온 이미지에서 비트맵을 추출한다.
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);    //bitmap compress
        byte [] arr=baos.toByteArray();
        String image= Base64.encodeToString(arr, Base64.DEFAULT);
        String trainner_image="";
        try{
            trainner_image="&profile_image="+ URLEncoder.encode(image,"utf-8");
        }catch (Exception e){
            Log.e("exception",e.toString());
        }

        //1.회원가입을 완료하기 전, 예외처리를 해준다.
        // - 이메일이 중복되지는 않은지
        // - 비밀번호와 비밀번호 확인이 일치하는지



        //쓰레드를 통해서 서버에 데이터를 넣어준다.
        InsertData task = new InsertData();
        Log.i("Insert_Trainner", trainner_Email);
        Log.i("Insert_Trainner", trainner_password);
        Log.i("Insert_Trainner", trainner_passwordConfirm);
        Log.i("Insert_Trainner", trainner_nickname);
        Log.i("Insert_Trainner", trainner_place);
        Log.i("Insert_Trainner", trainner_prize);
        Log.i("Insert_Trainner", trainner_image);
        task.execute("http://"+IP_ADDRESS+"/user_signup/insertTrainner.php", trainner_Email, trainner_password, trainner_passwordConfirm, trainner_nickname, trainner_place, trainner_prize, trainner_image);


        //회원가입을 완료했으니 회원의 정보가 조회되는지 확인한다.
        //1. 우선 회원가입창을 닫는다
        //2. 그 후, 테스트 화면으로 이동하여 회원의 정보를 조회한다.

    }

    public void setProfileImage(View view) {
        startActivity(new Intent(this, Popup_SelectSettingProfileImage.class));
    }


    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String TAG = "Insert_Trainner";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Insert_User.this,
                    "Please Wait", "가입중입니다..", true, true);
        }





        @Override
        protected String doInBackground(String... params) {

            String email = params[1];
            String password = params[2];
            String passwordConfirm = params[3];
            String nickname = params[4];
            String place = params[5];
            String prize = params[6];
            String image = params[7];

            String serverURL = params[0];
            String postParameters = "email=" + email + "&password=" + password +"&passwordConfirm=" + passwordConfirm + "&nickname=" + nickname + "&place=" + place + "&prize=" + prize + "&profile_image" + image;
            Log.i(TAG + "파라미터 : ",postParameters);
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
                Log.d(TAG, "POST response code - " + responseStatusCode);

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

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG+"결과값 : ", result);

            progressDialog.dismiss();
            //이메일이 중복된지 확인한다.
            //이메일이 중복되지 않는다면 비밀번호와 비밀번호 확인이 일치하는지 확인한다.


            if(result.equals("duplicate email")){
                Log.i("onPostExecute","이메일 중복");
                Toast.makeText(getApplicationContext(), "이메일이 중복됩니다!", Toast.LENGTH_SHORT).show();
            }else{
                if(result.equals("wrong password")){
                    Log.i("onPostExecute","비밀번호 일치하지 않음");
                    Toast.makeText(getApplicationContext(), "비밀번호와 비밀번호 확인이 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                }else{
                    Log.i(TAG, "회원가입 성공!");
                    finish();
                    //회원가입을 위해 정보를 다 입력했다면 이제 메인으로 넘겨준다.
                    UserInfo.email = email.getText().toString();
                    UserInfo.password = password.getText().toString();
                    UserInfo.nickname = nickname.getText().toString();
                    UserInfo.place = place.getText().toString();
                    UserInfo.prize = prize.getText().toString();
                    Drawable drawable = profile_image.getDrawable();//이미지뷰에서 이미지를 받아온다.
                    Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();//받아온 이미지에서 비트맵을 추출한다.
                    UserInfo.profile_image = bitmap;

                    startActivity(new Intent(getApplicationContext(), MainDrawer.class));


                }
            }

            Log.d(TAG, "POST response  - " + result);
            //finish();
        }
    }




}
