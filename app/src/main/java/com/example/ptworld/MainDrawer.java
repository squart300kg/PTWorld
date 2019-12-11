package com.example.ptworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.util.helper.log.Logger;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentMain FragmentMain = new FragmentMain(MainDrawer.this);
    private FragmentSNS FragmentSNS = new FragmentSNS(MainDrawer.this);
    private FragmentBoardInsert FragmentBoardInsert = new FragmentBoardInsert(MainDrawer.this);
    private FragmentNotiHistory FragmentNotiHistory = new FragmentNotiHistory(MainDrawer.this);
    private FragmentProfile FragmentProfile = new FragmentProfile(MainDrawer.this);
    private FragmentMyPage FragmentMyPage = new FragmentMyPage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //하단 메뉴바에 대한 코드를 작성한다.
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        String str = getIntent().getStringExtra("notification");

        if(str != null){
            //노티 - 좋아요
            if(str.equals("notification_Like")){
                String boardno = getIntent().getStringExtra("boardno");
                FragmentSNS.boardno = boardno;
                transaction.replace(R.id.frameLayout, FragmentSNS).commitAllowingStateLoss();
            //노티 - 팔로우
            } else if(str.equals("notification_UserProfile")){
                String nickname2 = getIntent().getStringExtra("nickname");
                FragmentProfile.nickname = nickname2;
                transaction.replace(R.id.frameLayout, FragmentProfile).commitAllowingStateLoss();
            } else if(str.equals("notification_Reply")){
                String boardno = getIntent().getStringExtra("boardno");
                String replyno = getIntent().getStringExtra("replyno");
                FragmentSNS.boardno = boardno;
                transaction.replace(R.id.frameLayout, FragmentSNS).commitAllowingStateLoss();
                Intent intent = new Intent(MainDrawer.this, Reply.class);
                intent.putExtra("replyno", replyno);
                intent.putExtra("no", boardno);
                Log.i("메인에서 boardno",boardno);
                startActivity(intent);
            } else if(str.equals("notification_ReReply")){
                String boardno = getIntent().getStringExtra("boardno");
                String replyno = getIntent().getStringExtra("replyno");
                String rereplyno = getIntent().getStringExtra("rereplyno");

                FragmentSNS.boardno = boardno;
                transaction.replace(R.id.frameLayout, FragmentSNS).commitAllowingStateLoss();
                Intent intent = new Intent(MainDrawer.this, Reply.class);
                intent.putExtra("replyno", replyno);
                intent.putExtra("rereplyno", rereplyno);
                intent.putExtra("no", boardno);
                Log.i("메인에서 boardno",boardno);
                startActivity(intent);
            }
        } else {
            //정상 실행시켜 들어온 경우
            transaction.replace(R.id.frameLayout, FragmentMain).commitAllowingStateLoss();
        }

        BottomNavigationView navView = findViewById(R.id.nav_view_bottom);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        View header = navigationView.getHeaderView(0);
        ImageView profile_image = header.findViewById(R.id.mainHeader_profileImage);
        TextView email = header.findViewById(R.id.mainHeader_email);
        TextView nickname = header.findViewById(R.id.mainHeader_nickname);

        profile_image.setImageBitmap(TrainnerInfo.profile_image);
        email.setText(TrainnerInfo.email);
        nickname.setText(TrainnerInfo.nickname);

        profile_image.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            profile_image.setClipToOutline(true);
        }

        //크롤링을 하기 위한 임시 메서드이다. 한번 실행하고 반드시 지워준다.
//        Crawling_Thread crawling_thread = new Crawling_Thread();
//        crawling_thread.execute();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM토큰 초기화", "getInstanceId failed", task.getException());
                            return;
                        }

                        //새로운 토큰을 받았다.
                        String token = task.getResult().getToken();
                        Log.i("FCM NEW토큰",token);
                        String IP_ADDRESS = "squart300kg.cafe24.com";
                        Log.i("토큰 길이",token.length()+"");
                        UserDTO.token = token;
                        UserDTO.email = TrainnerInfo.email;
                        new Thread_TokenSave().execute("http://"+IP_ADDRESS+"/user_signup/user_token.php", TrainnerInfo.email, token);
                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });



    }
    private class Thread_TokenSave extends AsyncTask<String, Void, String>  {

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
            String email = params[1];
            String token = params[2];

            String postParameters = "email=" +  email +"&token="+token;
            Log.i("Thread_TokenSave_파라미터",postParameters);

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
                Log.d("Thread_TokenSave", "POST response code - " + responseStatusCode);

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

                Log.d("Thread_TokenSave", "Thread_Main : Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("FCM토큰 갱신 여부",result);

        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.frameLayout, FragmentMain).commitAllowingStateLoss();
                    Log.i("하단 navBar","홈클릭");
                    return true;
                case R.id.navigation_camera1:
                    transaction.replace(R.id.frameLayout, FragmentSNS).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_camera2:
                    transaction.replace(R.id.frameLayout, FragmentBoardInsert).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_camera3:
                    transaction.replace(R.id.frameLayout, FragmentNotiHistory).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_mypage:
                    transaction.replace(R.id.frameLayout, FragmentProfile).commitAllowingStateLoss();
                    return true;
            }
            return false;
        }
    };
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {
            //컨텐츠 조회 기록이다.
            Intent intent = new Intent(MainDrawer.this, ContentsHistory.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            //카아로 세션을 반납함과 동시에 네아로 세션도 반납시켜준다.
            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    Log.i("로그아웃","로그아웃");
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    FirebaseAuth.getInstance().signOut();//구아로 로그아웃
                    finish();
                }
            });

            TrainnerInfo.email = "";
            TrainnerInfo.password = "";
            TrainnerInfo.nickname = "";
            TrainnerInfo.place = "";
            TrainnerInfo.prize = "";
            TrainnerInfo.profile_image = null;
        } else if (id == R.id.nav_getout) {
            //탈퇴하면 일단 네이버와 카카오톡을 동시에 탈퇴한다.
            final String appendMessage = getString(R.string.com_kakao_confirm_unlink);
            new AlertDialog.Builder(this)
                    .setMessage(appendMessage)
                    .setPositiveButton(getString(R.string.com_kakao_ok_button),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                        @Override
                                        public void onFailure(ErrorResult errorResult) {
                                            Logger.e(errorResult.toString());

                                            Log.i("앱탈퇴실패1","앱탈퇴실패");
                                        }

                                        @Override
                                        public void onSessionClosed(ErrorResult errorResult) {
//                                            redirectLoginActivity();

                                            Log.i("앱탈퇴실패2","앱탈퇴실패");
                                        }

                                        @Override
                                        public void onNotSignedUp() {
//                                            redirectSignupActivity();

                                            Log.i("앱탈퇴실패3","앱탈퇴실패");
                                        }

                                        @Override
                                        public void onSuccess(Long userId) {
//
                                            Log.i("앱탈퇴","앱탈퇴");
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), Login.class));
                                            Login.naverLoginInstance.logoutAndDeleteToken(getApplicationContext());
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
