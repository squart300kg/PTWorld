package com.example.ptworld;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class FragmentBoardInsert extends Fragment {
    private AdapterBoardInsert adapter;
    public String basePath = null;
    private RecyclerView photo_select_album;
    TextView boardInsertNext;
    static ArrayList<Bitmap> insertBitmap;

    BitmapFactory.Options options;
//    static ArrayList<Bitmap> bitmapArr = new ArrayList<>();
    Activity context;
    String IP_ADDRESS = "squart300kg.cafe24.com";

    ImageView big_thumnail;
    ViewGroup rootView;

    public FragmentBoardInsert(Activity context){
        this.context = context;
    }
//    public FragmentBoardInsert(){}
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_boardinsert, container, false);
        Log.i("사진올리기 들어옴","들어옴");
        photo_select_album = rootView.findViewById(R.id.photo_select_album);

        big_thumnail = rootView.findViewById(R.id.big_thumnail);
        boardInsertNext = rootView.findViewById(R.id.boardInsertNext);
        //사진 선택 -> 사진 게시 버튼 클릭
        boardInsertNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BoardInsert.class);
                startActivity(intent);
                ((MainDrawer)getActivity()).replaceFragment(FragmentSNS.fragment_sns());
            }
        });

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
            }
        }

        basePath = mediaStorageDir.getPath();
        Log.i("앨범속 사진갯수",new File(basePath).list().length+"");
        Log.i("앨범 사진 경로",basePath);
        insertBitmap = new ArrayList<>();

        Log.i("Bitmap어레이리스트 갯수",insertBitmap.size()+"개");

        new Thread_BoardInsert().execute();


//
//        photo_select_album.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                int count = totalItemCount - visibleItemCount;
//                if(firstVisibleItem  >= count && totalItemCount != 0
//                        && mLockListView == false)
//                {
//                    Log.i(LOG, "Loading next items");
//                    Log.i( "firstVisibleItem값",firstVisibleItem+"");
//                    addItems(firstVisibleItem, firstVisibleItem + 48);
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//            }
//            private void addItems(final int firstVisibleItem, final int size)
//            {
//                // 아이템을 추가하는 동안 중복 요청을 방지하기 위해 락을 걸어둡니다.
//                mLockListView = true;
//
//                Runnable run = new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        for(int i = 0 + firstVisibleItem ; i < size ; i++)
//                        {
////                            mRowList.add("Item " + i);
//
//                                Log.i("앨범 리스트 초기화 중",".........................");
//
//                                Bitmap bm = BitmapFactory.decodeFile(basePath+ File.separator +mImgs[index], options);
//                                Bitmap bm2 = ThumbnailUtils.extractThumbnail(bm, 300, 300);
//                                bitmapArr.add(i, bm2);
//                                index ++;
//                                adapter = new AlbumAdapter(context, basePath, bitmapArr);
//                                photo_select_album.setAdapter(adapter);
//
//                        }
//
//                        // 모든 데이터를 로드하여 적용하였다면 어댑터에 알리고
//                        // 리스트뷰의 락을 해제합니다.
//                        adapter.notifyDataSetChanged();
//                        mLockListView = false;
//                    }
//                };
//
//                // 속도의 딜레이를 구현하기 위한 꼼수
//                Handler handler = new Handler();
//                handler.postDelayed(run, 500);
//            }
//        });
        return rootView;
    }
    private class Thread_BoardInsert extends AsyncTask<String, Void, String> {

        //진행바표시
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //진행다일로그 시작
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
             return null;
        }

        @Override
        protected void onPostExecute(String result) {

            adapter = new AdapterBoardInsert(context, basePath, rootView);
            photo_select_album.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context.getApplicationContext(), 4);
            photo_select_album.setLayoutManager(layoutManager);
            photo_select_album.setAdapter(adapter);
            photo_select_album.setHasFixedSize(true);
            for (int i = 0; i < new File(basePath).list().length; i++) {
                insertBitmap.add(null);
            }
//            builder.
            progressDialog.dismiss();
        }
    }

}
