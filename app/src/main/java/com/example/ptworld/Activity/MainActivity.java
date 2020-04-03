package com.example.ptworld.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.ptworld.Adapter.AdapterMain;
import com.example.ptworld.Adapter.AdapterMain_2;
import com.example.ptworld.Adapter.AdapterMain_3;
import com.example.ptworld.Adapter.AdapterMain_4;

import android.util.Log;
import android.view.View;

import android.view.MenuItem;

import com.example.ptworld.DTO.ItemObject;
import com.example.ptworld.DTO.UserInfo;
import com.example.ptworld.R;
import com.google.android.material.navigation.NavigationView;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.util.helper.log.Logger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String IP_ADDRESS = "squart300kg.cafe24.com";
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_2;
    private RecyclerView recyclerView_3;
    private RecyclerView recyclerView_4;
    private ArrayList<ItemObject> list = new ArrayList();
    TextView moreView_1;
    TextView moreView_2;
    TextView moreView_3;
    TextView moreView_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        recyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
//        recyclerView_2 = (RecyclerView) findViewById(R.id.mainRecyclerView_2);
//        recyclerView_3 = (RecyclerView) findViewById(R.id.mainRecyclerView_3);
//        recyclerView_4 = (RecyclerView) findViewById(R.id.mainRecyclerView_4);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);

//        profile_image = findViewById(R.id.imageView);
//        email = findViewById(R.id.textView);

        Log.i("Main_Image", UserInfo.profile_image+"");
        Log.i("Main_email", UserInfo.email);
        Log.i("Main_nickname", UserInfo.nickname);

//        View header = navigationView.getHeaderView(0);
//        ImageView profile_image = header.findViewById(R.id.mainHeader_ProfileImage);
//        TextView email = header.findViewById(R.id.mainHeader_email);
//        TextView nickname = header.findViewById(R.id.mainHeader_nickname);

//        profile_image.setImageBitmap(UserInfo.profile_image);
//        email.setText(UserInfo.email);
//        nickname.setText(UserInfo.nickname);
//
//        profile_image.setBackground(new ShapeDrawable(new OvalShape()));
//        if(Build.VERSION.SDK_INT >= 21) {
//            profile_image.setClipToOutline(true);
//        }

        //메인화면 게시물 크롤링
        new Description().execute("http://"+IP_ADDRESS+"/user_signup/allContents.php");
        moreView_1 = findViewById(R.id.moreView);//더보기 버튼
        moreView_2 = findViewById(R.id.moreView_2);
        moreView_3 = findViewById(R.id.moreView_3);
        moreView_4 = findViewById(R.id.moreView_4);

        moreView_1.setOnClickListener(new View.OnClickListener() {//더보기 버튼을 클릭했을때이다.
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MoreView.class);
//                intent.putExtra("list",list);
//                Log.i("list로그", list.get(100).getSubject());
                startActivity(intent);
            }
        });
        moreView_2.setOnClickListener(new View.OnClickListener() {//더보기 버튼을 클릭했을때이다.
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MoreView.class);
//                intent.putExtra("list",list);
//                Log.i("list로그", list.get(100).getSubject());
                startActivity(intent);
            }
        });
        moreView_3.setOnClickListener(new View.OnClickListener() {//더보기 버튼을 클릭했을때이다.
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MoreView.class);
//                intent.putExtra("list",list);
//                Log.i("list로그", list.get(100).getSubject());
                startActivity(intent);
            }
        });
        moreView_4.setOnClickListener(new View.OnClickListener() {//더보기 버튼을 클릭했을때이다.
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MoreView.class);
//                intent.putExtra("list",list);
//                Log.i("list로그", list.get(100).getSubject());
                startActivity(intent);
            }
        });

    }

    public class Description extends AsyncTask<String, Void, String> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //진행다일로그 시작
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
//            ====================================피지컬 갤러리의 모든 게시물을 크롤링하는 코드이다===============================
//            int mElementSize = 0;
//            String category[] = {"아픈부위찾기","체형교정","Study","유튜브%28Youtube%29"};
//            String total_page_num[] = new String[category.length];
//            int paging1[] = new int[category.length];
//            String span = null;
//
//            for(int num = 1 ; num <= 4 ; num ++){
//                try {
//                    Document paging = Jsoup.connect("https://healthkeeper100.tistory.com/category/"+category[num-1]).get();
//                    Elements size = paging.select("div[class=area_list]").select("a[class=link_category]");
//                    String total_paging = size.text();
//                    int lastIndex = total_paging.lastIndexOf('(');
//                    String total_paging2 = size.text().substring(lastIndex+1, total_paging.length()-1);
//                    total_page_num[num - 1] = total_paging2;//각 카테고리별로 페이징 수들을 저장하고 있다.
//                    paging1[num - 1] = Integer.parseInt(total_page_num[num - 1]) % 12 == 0 ? Integer.parseInt(total_page_num[num - 1]) / 12 : Integer.parseInt(total_page_num[num - 1]) / 12 + 1;
////                    Log.i("총 페이징 수", total_paging);
////                    Log.i("총 페이징 수", total_paging2);
////                    Log.i("12로 나눈 페이징", paging1+"");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            try {
//                for(int i = 1 ; i <= 4 ; i ++){
//                    for(int j = 1 ; j <= paging1[i - 1] ; j ++){
//                        Document doc = Jsoup.connect("https://healthkeeper100.tistory.com/category/"+category[i-1]+"?page="+j).ignoreHttpErrors(true).get();
//                        Log.i("돌아다닌URL","https://healthkeeper100.tistory.com/category/"+category[i-1]+"?page="+j);
//                        Elements mElementDataSize = doc.select("div[id=mArticle]"); //필요한 녀석만 꼬집어서 지정
//                        mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다. 그만큼 루프를 돌려야 하나깐.
//
//                        for(Element elem : mElementDataSize){ //이렇게 요긴한 기능이
//                            //우선 한개 게시물의 제목과 썸네일을 추출한다.
//                            String subject = elem.select("strong[class=tit_post]").text();//제목추출
//                            String imageUrlArr[] = elem.select("img").attr("src").split("fname=");//썸네일추출
//
//                            //해당 게시물의 URL을 추출한다.
//                            String URL = "https://healthkeeper100.tistory.com" + elem.select("a[class=thumbnail_post]").attr("href");
////                            Log.i("크롤링 추출URL",URL);
//                            Document doc_2 = Jsoup.connect(URL).ignoreHttpErrors(true).get();
//                            Elements mElementDataSize_2 = doc_2.select("div[id=mArticle]");
////                            Elements mElementDataSize_2 = doc_2.select("tt_article_useless_p_margin");
//
//                            //span의 갯수를 구한다.
//                            for(Element elem_span : mElementDataSize_2){
//                                span = elem_span.select("span").text();
////                                Log.i("span : ",span);
//                            }
//
//                            //이미지의 갯수를 구한다.
//                            for(Element elem_image : mElementDataSize_2){
//                                String image_Url_1 = elem_image.select("img").attr("src");
//                                String image_Url_2 = elem_image.select("iframe").attr("src");
////                                Log.i("image : ", image_Url_1);
////                                Log.i("image : ", image_Url_2);
//                            }
//
//                            try{
//                                String thumbnail = imageUrlArr[1];
//                                String contents = elem.select("p[class=txt_post]").text();
//                                //ArrayList에 계속 추가한다.
//                                list.add(new ItemObject(subject, URL, thumbnail));
//
//                            }catch (ArrayIndexOutOfBoundsException e){
//                                Log.i("크롤링중 이미지없음","이미지없음");
//                            }
//
//                        }
//                    }
//                }
//                //추출한 전체 <li> 출력해 보자.
//                Log.d("크롤링데이터수 :", mElementSize+"개");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            String serverURL = params[0];
            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
//                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("Thread_Main", "POST response code - " + responseStatusCode);

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

                Log.d("Thread_Main", "Thread_Main : Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //DB로부터 데이터를 JSON형태로 받아온다.
            try {
                JSONArray jsonArray = new JSONArray(result);
                int total_json = jsonArray.length();
                Log.i("JSON갯수",total_json+"");
                for(int i = 1 ; i <= total_json ; i ++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);

                    ItemObject itemObject = new ItemObject(jsonObject.getString("subject"), jsonObject.getString("contents_url"), jsonObject.getString("thumbnail_url"));
                    list.add(itemObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //해당 리싸이클러뷰는 수직 방향으로 모든 게시물들을 보여주는 리싸이클러뷰다.
            AdapterMain myAdapter = new AdapterMain(list, getApplicationContext());
            AdapterMain_2 myAdapter_2 = new AdapterMain_2(list, getApplicationContext());
            AdapterMain_3 myAdapter_3 = new AdapterMain_3(list, getApplicationContext());
            AdapterMain_4 myAdapter_4 = new AdapterMain_4(list, getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            RecyclerView.LayoutManager layoutManager_2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            RecyclerView.LayoutManager layoutManager_3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            RecyclerView.LayoutManager layoutManager_4 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);

            recyclerView_2.setLayoutManager(layoutManager_2);
            recyclerView_2.setAdapter(myAdapter_2);

            recyclerView_3.setLayoutManager(layoutManager_3);
            recyclerView_3.setAdapter(myAdapter_3);

            recyclerView_4.setLayoutManager(layoutManager_4);
            recyclerView_4.setAdapter(myAdapter_4);


            progressDialog.dismiss();
        }
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_tools) {
            // Handle the camera action
        } else if (id == R.id.see_BroadCast) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_logout){
            //로그아웃을 하면 카아로 세션을 반납시켜준다.
            //로그아웃을 하면 로그인 페이지로 보내준다(?) - 꼭 그래야만 하나?
            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    Log.i("로그아웃","로그아웃");
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            });

            UserInfo.email = "";
            UserInfo.password = "";
            UserInfo.nickname = "";
            UserInfo.place = "";
            UserInfo.prize = "";
            UserInfo.profile_image = null;
        }else if (id == R.id.nav_getout){
            //=======================카카오톡 탈퇴==========================
            //토큰을 다시 갱신받기 위해서 PTWorld앱과 카카오톡 연결을 해제하는 코드이다.
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
                                        }

                                        @Override
                                        public void onSessionClosed(ErrorResult errorResult) {
//                                            redirectLoginActivity();
                                        }

                                        @Override
                                        public void onNotSignedUp() {
//                                            redirectSignupActivity();
                                        }

                                        @Override
                                        public void onSuccess(Long userId) {
//                                            redirectLoginActivity();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), Login.class));
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

//            //=================네이버 탈퇴====================
//            Login.naverLoginInstance.logoutAndDeleteToken(getApplicationContext());
//            startActivity(new Intent(getApplicationContext(), Login.class));
//            finish();

        }

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
