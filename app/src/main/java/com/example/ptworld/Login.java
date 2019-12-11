package com.example.ptworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaSession2;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import org.json.JSONException;
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
    String TAG = "MainActivity_php";
    String IP_ADDRESS = "squart300kg.cafe24.com";
    EditText email;
    EditText password;

    private SessionCallback callback;

//    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 1000;

    private static String OAUTH_CLIENT_ID = "4Fa9DLWUlQLXJB1BZLuX";
    private static String OAUTH_CLIENT_SECRET = "pi24PFeuzJ";
    private static String OAUTH_CLIENT_NAME = "PTWorld";

    static OAuthLogin naverLoginInstance;
    private OAuthLoginButton naverLogInButton;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //카아로를 시작하기 전에 카카오 SDK를 초기화 한다.
//        KakaoSDK.init(new GlobalApplication.KakaoSDKAdapter());
        if (KakaoSDK.getAdapter() == null) {
            KakaoSDK.init(new GlobalApplication.KakaoSDKAdapter());
        }

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // 6.0 마쉬멜로우 이상일 경우에는 권한 체크 후 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
                Log.d("카메라 권한", "권한 설정 완료");
            } else {
                Log.d("카메라 권한", "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        //네아로를 시작
        init();
        init_View();

        //카아로를 시작
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();


        Log.i("key_hash", getKeyHash(getApplicationContext()));

        setGoogleLogin();

    }
    private void setGoogleLogin() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//        mAuth = FirebaseAuth.getInstance();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }//카카오 로그인부분과 합침. 왜? 메소드 이름이 같기떄문에
    private void handleSignInResult(GoogleSignInResult result) {

        if(result.isSuccess()) {
            final GoogleSignInAccount acct = result.getSignInAccount();

//            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
//            mAuth.signInWithCredential(credential)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(!task.isSuccessful()){
//                                Toast.makeText(Login.this, "인증 실패", Toast.LENGTH_SHORT).show();
//                            }else{
                                Toast.makeText(Login.this, "구글 로그인 성공", Toast.LENGTH_SHORT).show();
                                Log.i("구아로", "email:" + acct.getEmail());
                                Log.i("구아로", "id:" + acct.getId());
                                Log.i("구아로", "profile:" + acct.getPhotoUrl());
                                Log.i("구아로", "DispName > " + acct.getDisplayName());

            //카카오에서 가져온 이메일이 DB에 존재할 경우 회원가입을 하지 않고 바로 메인 액티비티로 넘겨준다.
            Thread_LoginSuccess thread_loginSuccess = new Thread_LoginSuccess();
            //Thread_LoginSuccess메소드는 로그인을 시도하려 할 때, 로그인을 시도하려는 이메일이 DB에 존재하는 경우는
            //바로 메인액티비티로 넘겨주고, DB에 존재하지 않는다면 회원가입 팝업창을 띄워주는 스레드이다.
            thread_loginSuccess.execute("http://"+IP_ADDRESS+"/user_signup/login_success.php", acct.getEmail(), acct.getPhotoUrl().toString());
//                            }
//                        }
//                    });


        }
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
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }//카카오 로그인 로직
        if(requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }//구글 로그인 로직
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
    }
    protected void redirectSignupActivity() {
        requestMe();
    }
    private void init(){
        context = this;
        naverLoginInstance = OAuthLogin.getInstance();
        naverLoginInstance.init(this,OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

    }
    private void init_View(){
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

                    new RequestApiTask().execute();

                    Log.i("로그인 - 접근토근 : ",accessToken);
                    Log.i("로그인 - 갱신토근 : ", refreshToken);
                    Log.i("로그인 - 만료시간 : ",expiresAt+"");
                    Log.i("로그인 - 토근타입 : ",tokenType);
                } else {//로그인 실패
                    String errorCode = naverLoginInstance.getLastErrorCode(context).getCode();
                    String errorDesc = naverLoginInstance.getLastErrorDesc(context);
                    Log.i("로그인 - errorCode : ",errorCode);
                    Log.i("로그인 - errorDesc : ", errorDesc);
                    Toast.makeText(context, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            }

        };
        naverLogInButton.setOAuthLoginHandler(naverLoginHandler);

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
        Intent intent = new Intent(getApplicationContext(), Insert_Trainner.class);
        intent.putExtra("email","");
        startActivity(intent);
    }

    public void trainner_login(View view) {
        String trainner_email = email.getText().toString();
        String trainner_password = password.getText().toString();

        Login_Thread login = new Login_Thread();
        login.execute("http://"+IP_ADDRESS+"/user_signup/login.php", trainner_email, trainner_password);

    }

    public void client_login(View view) {
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

                    TrainnerInfo.profile_image = profile_image;
                    TrainnerInfo.email = email;
                    TrainnerInfo.password = password;
                    TrainnerInfo.nickname = nickname;
                    TrainnerInfo.prize = prize;
                    TrainnerInfo.place = place;

                    Log.i("Thread_email",TrainnerInfo.email);
                    Log.i("Thread_bitbap",  TrainnerInfo.profile_image+"");
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
                            Intent intent = new Intent(getApplicationContext(), Insert_Trainner.class);
                            intent.putExtra("email",email1);
                            intent.putExtra("profile_imageUrl", profile_imageUrl);
                            startActivity(intent);

                            Log.e("imageLog",  temp);
                            break;
                        }
                    }
                }
            }


        }
    }


}
