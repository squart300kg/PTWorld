package com.example.ptworld.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ptworld.JavaUtils.GlobalApplication;
import com.example.ptworld.R;
import com.example.ptworld.DTO.UserInfo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    String TAG = "MainActivity : ";
    String IP_ADDRESS = "squart300kg.cafe24.com";
    EditText email;
    EditText password;

    private SessionCallback callback;

//    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 1000;

    static OAuthLogin naverLoginInstance;
    private OAuthLoginButton naverLogInButton;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        //카아로를 시작하기 전에 카카오 SDK를 초기화 한다.
//        KakaoSDK.init(new GlobalApplication.KakaoSDKAdapter());
        if (KakaoSDK.getAdapter() == null) {
            KakaoSDK.init(new GlobalApplication.KakaoSDKAdapter());
        }

        // 6.0 마쉬멜로우 이상일 경우에는 권한 체크 후 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
                Log.d("카메라 권한", "권한 설정 완료");
            } else {
                Log.d("카메라 권한", "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        //네이버 로그인
        naver_login_init();

        //카카오 로그인
        kakao_login_init();

        //구글 로그인
        google_login_init();

        Log.i("key_hash", getKeyHash(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

    }

    private void updateUI(GoogleSignInAccount acct) {
//        Log.i("구아로0", "email:" + acct.getEmail());
//        Log.i("구아로0", "id:" + acct.getId());
//        Log.i("구아로0", "profile:" + acct.getPhotoUrl());
//        Log.i("구아로0", "DispName > " + acct.getDisplayName());
//        Log.i("구아로0", "getFamilyName > " + acct.getFamilyName());
//        Log.i("구아로0", "getGivenName > " + acct.getGivenName());
//        Log.i("구아로0", "getId > " + acct.getId());
//        Log.i("구아로0", "getIdToken > " + acct.getIdToken());
//        Log.i("구아로0", "getServerAuthCode > " + acct.getServerAuthCode());

        SharedPreferences login_token =
                getSharedPreferences("login_token", MODE_PRIVATE);

        if(!login_token.getString("email", "").equals("") &&
                login_token.getString("social_type", "").equals("google")){
            Thread_LoginSuccess thread_loginSuccess = new Thread_LoginSuccess();
            thread_loginSuccess.execute("http://"+IP_ADDRESS+"/user_signup/login_success.php", acct.getEmail());
        }
        else
        {

        }
    }

    //TODO :: 네이버 로그인
    private void naver_login_init(){
        Log.i("social type", "네아로");
        SharedPreferences login_token =
                getSharedPreferences("login_token", MODE_PRIVATE);

        context = this;
        naverLoginInstance = OAuthLogin.getInstance();
        naverLoginInstance.init(this,
                getString(R.string.OAUTH_CLIENT_ID),
                getString(R.string.OAUTH_CLIENT_SECRET),
                getString(R.string.OAUTH_CLIENT_NAME));

        naverLogInButton = findViewById(R.id.buttonNaverLogin);

        //로그인 핸들러
        OAuthLoginHandler naverLoginHandler  = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {//로그인 성공

                    //DB에서 해당 이메일을 가지고 있는 회원을 검색한다. 검색해서 있으면
                    //1. 로그인 시켜준다 UserInfo에 회원정보 저장
                    //2. 로그인이 완료되었으니까 메인으로 이동시켜준다.

                    String accessToken = naverLoginInstance.getAccessToken(context);
                    String refreshToken = naverLoginInstance.getRefreshToken(context);
                    long expiresAt = naverLoginInstance.getExpiresAt(context);
                    String tokenType = naverLoginInstance.getTokenType(context);

                    //접근토큰을 SharedPreference에 저장
                    SharedPreferences login_token
                            = getSharedPreferences("login_token", MODE_PRIVATE);

                    SharedPreferences.Editor editor = login_token.edit();
                    editor.putString("social_type", "naver");
                    editor.putString("access_token", accessToken);
                    editor.putString("refresh_token", refreshToken);
                    editor.putString("expired_at", expiresAt+"");
                    editor.putString("token_type", tokenType);
                    editor.commit();

                    new RequestApiTask().execute();

                    Log.i("발급받은 - 접근토근 : ",accessToken);
                    Log.i("발급받은 - 갱신토근 : ", refreshToken);
                    Log.i("발급받은 - 만료시간 : ",expiresAt+"");
                    Log.i("발급받은 - 토근타입 : ",tokenType);
                } else {//로그인 실패
                    String errorCode = naverLoginInstance.getLastErrorCode(context).getCode();
                    String errorDesc = naverLoginInstance.getLastErrorDesc(context);
                    Log.i("로그인 - errorCode : ",errorCode);
                    Log.i("로그인 - errorDesc : ", errorDesc);
                    Toast.makeText(context, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            }

        };
        if(!login_token.getString("access_token", "").equals("") &&
                login_token.getString("social_type", "").equals("naver")){
            //이미 토큰이 있으므로 메인페이지로 넘겨줌
            Log.i("본래 소유 토큰 정보", login_token.getString("access_token" , ""));
            Log.i("본래 소유 토큰 정보", login_token.getString("refresh_token" , ""));
            Log.i("본래 소유 토큰 정보", login_token.getString("token_type" , ""));
            Log.i("본래 소유 토큰 정보", login_token.getString("expired_at" , ""));
            new RequestApiTask().execute();
        }
        else
        {
            //토큰이 없으므로 갱신시키는 로직을 시행
            naverLogInButton.setOAuthLoginHandler(naverLoginHandler);
        }


    }

    //TODO : kakao로그인
    private void kakao_login_init(){
        Log.i("social type", "카아로");
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    //TODO : Google로그인
    private void google_login_init() {
        Log.i("social type", "구아로");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient
                = GoogleSignIn.getClient(this, gso);

//                mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        GoogleSignInAccount acct = null;
        try {
            acct = completedTask.getResult(ApiException.class);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        Toast.makeText(Login.this, "구글 로그인 성공", Toast.LENGTH_SHORT).show();
//        Log.i("구아로", "email:" + acct.getEmail());
//        Log.i("구아로", "id:" + acct.getId());
//        Log.i("구아로", "profile:" + acct.getPhotoUrl());
//        Log.i("구아로", "DispName > " + acct.getDisplayName());

        SharedPreferences login_token =
                getSharedPreferences("login_token", MODE_PRIVATE);
        SharedPreferences.Editor editor = login_token.edit();
        editor.putString("social_type", "google");
        editor.putString("email", acct.getEmail());
        editor.commit();

        //카카오에서 가져온 이메일이 DB에 존재할 경우 회원가입을 하지 않고 바로 메인 액티비티로 넘겨준다.
        Thread_LoginSuccess thread_loginSuccess = new Thread_LoginSuccess();
        //Thread_LoginSuccess메소드는 로그인을 시도하려 할 때, 로그인을 시도하려는 이메일이 DB에 존재하는 경우는
        //바로 메인액티비티로 넘겨주고, DB에 존재하지 않는다면 회원가입 팝업창을 띄워주는 스레드이다.
        thread_loginSuccess.execute("http://"+IP_ADDRESS+"/user_signup/login_success.php", acct.getEmail());


    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("Error", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    private void requestMe() {
        List<String> keys = new ArrayList<>();
        keys.add("properties.nickname");
        keys.add("properties.profile_image");
        keys.add("kakao_account.email");
        keys.add("kakao_account.profile");

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                Log.i("카아로 세션 닫힐때","닫힐때");
            }

            @Override
            public void onSuccess(MeV2Response response) {
                Log.i("user id : " ,response.getId() +"");
                Log.i("email: " ,response.getKakaoAccount().getEmail());
                try{
                    Log.i("profile_image", response.getProfileImagePath());

                }catch(NullPointerException e){
                    Log.i("profile_image","없음");
                }
                Log.i("onSuccess","호출");

                //카카오에서 가져온 이메일이 DB에 존재할 경우 회원가입을 하지 않고 바로 메인 액티비티로 넘겨준다.
                Thread_LoginSuccess thread_loginSuccess = new Thread_LoginSuccess();
                //Thread_LoginSuccess메소드는 로그인을 시도하려 할 때, 로그인을 시도하려는 이메일이 DB에 존재하는 경우는
                //바로 메인액티비티로 넘겨주고, DB에 존재하지 않는다면 회원가입 팝업창을 띄워주는 스레드이다.
                thread_loginSuccess.execute("http://"+IP_ADDRESS+"/user_signup/login_success.php", response.getKakaoAccount().getEmail(), response.getProfileImagePath());

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //카카오 로그인 로직
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        //구글 로그인 로직
        if(requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }



    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
        protected void redirectSignupActivity() {
            requestMe();
        }
    }


    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/me";
            String at = naverLoginInstance.getAccessToken(context);

            return naverLoginInstance.requestApi(context, at, url);
        }

        protected void onPostExecute(String content) {
            String email = null;
            try{
                JSONObject jsonObject = new JSONObject(content);
                JSONObject response = jsonObject.getJSONObject("response");
                email = response.getString("email");
                String profile_image = response.getString("profile_image");

                //현재 로그인중인 네아로 이메일을 가져온다.
                // 가져온 네아로 이메일이 PTWorld의 DB에 존재하는 이메일이다. 여기서 DB라는 뜻은 트레이너의 DB와 회원 DB를 의미한다.
                //이는 회원가입이 되어있다는 뜻이므로 로그인을 진행시켜 준다.
                //하지만 만약 PTWorld의 DB에 이메일이 존재하지 않는다면, 이는 회원가입이 필요하단 뜻이다.
                //이때, 첫 회원가입을 고객으로 할것이냐, 트레이너로 할 것이냐를 결정해야 한다.
                Log.i("네아로", email);
                Log.i("네아로",profile_image);
                Thread_LoginSuccess thread_loginSuccess = new Thread_LoginSuccess();
                thread_loginSuccess.execute("http://"+IP_ADDRESS+"/user_signup/login_success.php", email, profile_image);
            }catch (Exception e){
                e.printStackTrace();
                //넘겨받은 프로필 이미지의 값이 없을때 다음의 코드를 실행한다.
                Thread_LoginSuccess thread_loginSuccess = new Thread_LoginSuccess();
                thread_loginSuccess.execute("http://"+IP_ADDRESS+"/user_signup/login_success.php", email);

            }

        }
    }

    // 권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("카메라 권한", "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
            Log.d("카메라 권한", "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }



    public void popup_InserUser(View view) {
//        Intent intent = new Intent(getApplicationContext(), Popup_InsertUser.class);
        Intent intent = new Intent(getApplicationContext(), Insert_User.class);
        intent.putExtra("email","");
        startActivity(intent);
    }

    public void trainner_login(View view) {
        String trainner_email = email.getText().toString();
        String trainner_password = password.getText().toString();

        Login_Thread login = new Login_Thread();
        login.execute("http://"+IP_ADDRESS+"/user_signup/login.php", trainner_email, trainner_password);

    }


    class Login_Thread extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Login.this,
                    "Please Wait", "로그인 중입니다..", true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            if(s.equals("login fail")){
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            Thread_LoginSuccess thread_loginSuccess = new Thread_LoginSuccess();
            thread_loginSuccess.execute("http://"+IP_ADDRESS+"/user_signup/login_success.php", email.getText().toString());
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }


        @Override
        protected String doInBackground(String... params) {


            String email = params[1];
            String password = params[2];

            String serverURL = params[0];
            String postParameters = "email=" + email + "&password=" + password;

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
    }
    class Thread_LoginSuccess extends AsyncTask<String, Void, String> {

        String email1;
        String profile_imageUrl;
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            String email = params[1];
            try{
                profile_imageUrl = params[2];
            }catch(NullPointerException e){
                e.printStackTrace();
            }catch(ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
//                profile_imageUrl = "https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwiDjKC9jP3kAhX3wosBHeIrAe0QjRx6BAgBEAQ&url=http%3A%2F%2Fm.blog.naver.com%2Fzikil337%2F220248559955&psig=AOvVaw3w5TX_5wJw09GNg7WZcuXj&ust=1570089272939110";
            }
            email1 = email;
            String serverURL = params[0];
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
                Log.d("THread_LoginSucess", "POST response code - " + responseStatusCode);

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

                Log.d("THread_LoginSucess", "THread_LoginSucess: Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

             //진행다일로그 시작
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.setCancelable(false);
            progressDialog.show();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.contains("false")){

            }else{
                Bitmap profile_image;
                String email;
                String password;
                String nickname;
                String prize;
                String place;

                try{
                    JSONArray jArray=new JSONArray(result);
                    JSONObject jsonObject=jArray.getJSONObject(0);

                    byte[] encodedByteArray = Base64.decode(jsonObject.getString("image"), Base64.DEFAULT);
                    Bitmap encodedBitmap = BitmapFactory.decodeByteArray(encodedByteArray, 0, encodedByteArray.length);
                    profile_image = encodedBitmap;
                    email = jsonObject.getString("email");
                    password = jsonObject.getString("pwd");
                    nickname = jsonObject.getString("nickname");
                    prize = jsonObject.getString("prize");
                    place = jsonObject.getString("place");

                    UserInfo.profile_image = profile_image;
                    UserInfo.email = email;
                    UserInfo.password = password;
                    UserInfo.nickname = nickname;
                    UserInfo.prize = prize;
                    UserInfo.place = place;

                    Log.i("Thread_email", UserInfo.email);
                    Log.i("Thread_bitbap",  UserInfo.profile_image+"");
                    Log.i("Thread_LoginSuccess","성공");

                    startActivity(new Intent(getApplicationContext(), MainDrawer.class));
                    finish();
                }catch (Exception e){
                    String temp=e.toString();
                    while (temp.length() > 0) {
                        if (temp.length() > 4000) {
                            Log.e("imageLog", temp.substring(0, 4000));
                            temp = temp.substring(4000);
                        } else {
                            //만약 회원이 아니라면 회원가입을 해야한다.
                            Log.i("OAuth로그인 필요","필요");
                            Intent intent = new Intent(getApplicationContext(), Insert_User.class);
                            intent.putExtra("email",email1);
                            intent.putExtra("profile_imageUrl", profile_imageUrl);
                            startActivity(intent);

                            Log.e("imageLog",  temp);
                            break;
                        }
                    }
                }
            }
            progressDialog.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }
    }


}
