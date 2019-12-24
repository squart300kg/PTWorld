package com.example.ptworld.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptworld.DTO.ItemObject;
import com.example.ptworld.Activity.MainRecyclerView;
import com.example.ptworld.Activity.MainRecyclerView2;
import com.example.ptworld.Activity.MainRecyclerView3;
import com.example.ptworld.Activity.MainRecyclerView4;
import com.example.ptworld.R;
import com.example.ptworld.DTO.TrainnerInfo;

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

public class FragmentMain extends Fragment{

    private RecyclerView recomendationRecyclerView;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_2;
    private RecyclerView recyclerView_3;
    private RecyclerView recyclerView_4;
    String IP_ADDRESS = "squart300kg.cafe24.com";
    Context context;

    private ArrayList<ItemObject> list = new ArrayList();
    TextView moreView_1;
    TextView moreView_2;
    TextView moreView_3;
    TextView moreView_4;

    //추천
    Fragment추천 Fragment추천;

    //아픈부위찾기
    private FragmentPain_ankle FragmentPain_ankle;
    private FragmentPain_back FragmentPain_back;
    private FragmentPain_chest FragmentPain_chest;
    private FragmentPain_chin FragmentPain_chin;
    private FragmentPain_elbow FragmentPain_elbow;
    private FragmentPain_headache FragmentPain_headache;
    private FragmentPain_knee FragmentPain_knee;
    private FragmentPain_neck FragmentPain_neck;
    private FragmentPain_pelvis FragmentPain_pelvis;
    private FragmentPain_sacrum FragmentPain_sacrum;
    private FragmentPain_shoulder FragmentPain_shoulder;
    private FragmentPain_waist FragmentPain_waist;
    private FragmentPain_wrist FragmentPain_wrist;

    //체형교정
    private Fragment체형교정_목 Fragment체형교정_목;
    private Fragment체형교정_측면척추 Fragment체형교정_측면척추;
    private Fragment체형교정_척추측만증 Fragment체형교정_척추측만증;
    private Fragment체형교정_골반 Fragment체형교정_골반;
    private Fragment체형교정_휜다리 Fragment체형교정_휜다리;
    private Fragment체형교정_평발말발 Fragment체형교정_평발말발;


    //Study
    private FragmentStudy_해부학 FragmentStudy_해부학;
    private FragmentStudy_물리치료학 FragmentStudy_물리치료학;
    private FragmentStudy_영양학 FragmentStudy_영양학;
    private FragmentStudy_약학 FragmentStudy_약학;


    //유튜브(Youtube)
    private Fragment유튜브_목 Fragment유튜브_목;
    private Fragment유튜브_어깨 Fragment유튜브_어깨;
    private Fragment유튜브_등 Fragment유튜브_등;
    private Fragment유튜브_허리 Fragment유튜브_허리;
    private Fragment유튜브_골반 Fragment유튜브_골반;
    private Fragment유튜브_무릎 Fragment유튜브_무릎;
    private Fragment유튜브_발목발 Fragment유튜브_발목발;
    private Fragment유튜브_팔꿈치 Fragment유튜브_팔꿈치;
    private Fragment유튜브_손목손 Fragment유튜브_손목손;
    private Fragment유튜브_두통 Fragment유튜브_두통;
    private Fragment유튜브_턱관절 Fragment유튜브_턱관절;
    private Fragment유튜브_웨이트운동 Fragment유튜브_웨이트운동;
    private Fragment유튜브_다이어트 Fragment유튜브_다이어트;
    private Fragment유튜브_스트레칭 Fragment유튜브_스트레칭;
    private Fragment유튜브_건강꿀팁정보 Fragment유튜브_건강꿀팁정보;
    private Fragment유튜브_빡빡이의예능 Fragment유튜브_빡빡이의예능;



    public FragmentMain(Context context){
        //한마디로 해당 파라미터로 받은 context는 MainDrawer의 context이다.
        MainRecyclerView mainRe1 = new MainRecyclerView();
        MainRecyclerView2 mainRe2 = new MainRecyclerView2();
        MainRecyclerView3 mainRe3 = new MainRecyclerView3();
        MainRecyclerView4 mainRe4 = new MainRecyclerView4();

        //아픈부위찾기
//        Activity context = mainRe1.getContext();
        Log.i("메인리사이클러뷰1",context+"");
        Fragment추천 = new Fragment추천(context);
        FragmentPain_ankle = new FragmentPain_ankle(context);
        FragmentPain_back = new FragmentPain_back(context);
        FragmentPain_chest = new FragmentPain_chest(context);
        FragmentPain_chin = new FragmentPain_chin(context);
        FragmentPain_elbow = new FragmentPain_elbow(context);
        FragmentPain_headache = new FragmentPain_headache(context);
        FragmentPain_knee = new FragmentPain_knee(context);
        FragmentPain_pelvis = new FragmentPain_pelvis(context);
        FragmentPain_sacrum = new FragmentPain_sacrum(context);
        FragmentPain_neck = new FragmentPain_neck(context);
        FragmentPain_shoulder = new FragmentPain_shoulder(context);
        FragmentPain_waist = new FragmentPain_waist(context);
        FragmentPain_wrist = new FragmentPain_wrist(context);

        //체형교정
//        Activity context2 = mainRe2.getContext();
//        Log.i("메인리사이클러뷰2",context2+"");
          Fragment체형교정_척추측만증 = new Fragment체형교정_척추측만증(context);
          Fragment체형교정_목 = new Fragment체형교정_목(context);
          Fragment체형교정_측면척추 = new Fragment체형교정_측면척추(context);
          Fragment체형교정_골반 = new Fragment체형교정_골반(context);
          Fragment체형교정_휜다리 = new Fragment체형교정_휜다리(context);
          Fragment체형교정_평발말발 = new Fragment체형교정_평발말발(context);

        //Study
//        Activity context3 = mainRe3.getContext();
//        Log.i("메인리사이클러뷰3",context3+"");
          FragmentStudy_해부학 = new FragmentStudy_해부학(context);
          FragmentStudy_물리치료학 = new FragmentStudy_물리치료학(context);
          FragmentStudy_영양학 = new FragmentStudy_영양학(context);
          FragmentStudy_약학 = new FragmentStudy_약학(context);

        //유튜브(Youtube)
//        Activity context4 = mainRe4.getContext();
//        Log.i("메인리사이클러뷰4",context4+"");
          Fragment유튜브_목 = new Fragment유튜브_목(context);
          Fragment유튜브_어깨 = new Fragment유튜브_어깨(context);
          Fragment유튜브_등 = new Fragment유튜브_등(context);
          Fragment유튜브_허리 = new Fragment유튜브_허리(context);
          Fragment유튜브_골반 = new Fragment유튜브_골반(context);
          Fragment유튜브_무릎 = new Fragment유튜브_무릎(context);
          Fragment유튜브_발목발 = new Fragment유튜브_발목발(context);
          Fragment유튜브_팔꿈치 = new Fragment유튜브_팔꿈치(context);
          Fragment유튜브_손목손 = new Fragment유튜브_손목손(context);
          Fragment유튜브_두통 = new Fragment유튜브_두통(context);
          Fragment유튜브_턱관절 = new Fragment유튜브_턱관절(context);
          Fragment유튜브_웨이트운동 = new Fragment유튜브_웨이트운동(context);
          Fragment유튜브_다이어트 = new Fragment유튜브_다이어트(context);
          Fragment유튜브_스트레칭 = new Fragment유튜브_스트레칭(context);
          Fragment유튜브_건강꿀팁정보 = new Fragment유튜브_건강꿀팁정보(context);
          Fragment유튜브_빡빡이의예능 = new Fragment유튜브_빡빡이의예능(context);


    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_main, container, false);

        TextView reco_text_name = rootView.findViewById(R.id.reco_text_nickname);
        reco_text_name.setText(TrainnerInfo.nickname+"님의 맞춤 콘텐츠");
//        recomendationRecyclerView = rootView.findViewById(R.id.recomendationRecyclerView);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.mainRecyclerView);
        recyclerView_2 = (RecyclerView)rootView.findViewById(R.id.mainRecyclerView_2);
        recyclerView_3 = (RecyclerView)rootView.findViewById(R.id.mainRecyclerView_3);
        recyclerView_4 = (RecyclerView)rootView.findViewById(R.id.mainRecyclerView_4);

        moreView_1 = (TextView)rootView.findViewById(R.id.moreView);//더보기 버튼
        moreView_2 = (TextView)rootView.findViewById(R.id.moreView_2);
        moreView_3 = (TextView)rootView.findViewById(R.id.moreView_3);
        moreView_4 = (TextView)rootView.findViewById(R.id.moreView_4);

        //================================메인 카테고리중 '아픈부위찾기'===================
        final TextView pain_ankle = rootView.findViewById(R.id.pain_ankle);
        final TextView pain_back = rootView.findViewById(R.id.pain_back);
        final TextView pain_chest = rootView.findViewById(R.id.pain_chest);
        final TextView pain_chin = rootView.findViewById(R.id.pain_chin);
        final TextView pain_elbow = rootView.findViewById(R.id.pain_elbow);
        final TextView pain_headache = rootView.findViewById(R.id.pain_headache);
        final TextView pain_knee = rootView.findViewById(R.id.pain_knee);
        final TextView pain_neck = rootView.findViewById(R.id.pain_neck);
        final TextView pain_pelvis = rootView.findViewById(R.id.pain_pelvis);
        final TextView pain_sacrum = rootView.findViewById(R.id.pain_sacrum);
        final TextView pain_shoulder = rootView.findViewById(R.id.pain_shoulder);
        final TextView pain_waist = rootView.findViewById(R.id.pain_waist);
        final TextView pain_wrist = rootView.findViewById(R.id.pain_wrist);
        //================================메인 카테고리중 '체형교정'===================
        final TextView 체형교정_목 = rootView.findViewById(R.id.체형교정_목);
        final TextView 체형교정_측면척추 = rootView.findViewById(R.id.체형교정_측면척추);
        final TextView 체형교정_척추측만증 = rootView.findViewById(R.id.체형교정_척추측만증);
        final TextView 체형교정_골반 = rootView.findViewById(R.id.체형교정_골반);
        final TextView 체형교정_휜다리 = rootView.findViewById(R.id.체형교정_휜다리);
        final TextView 체형교정_평발말발 = rootView.findViewById(R.id.체형교정_평발말발);
        //================================메인 카테고리중 'Study'===================
        final TextView Study_해부학 = rootView.findViewById(R.id.Study_해부학);
        final TextView Study_물리치료학 = rootView.findViewById(R.id.Study_물리치료학);
        final TextView Study_약학 = rootView.findViewById(R.id.Study_약학);
        final TextView Study_영양학 = rootView.findViewById(R.id.Study_영양학);
        //================================메인 카테고리중 '유튜브'===================
        final TextView 유튜브_목 = rootView.findViewById(R.id.유튜브_목);
        final TextView 유튜브_건강꿀팁정보 = rootView.findViewById(R.id.유튜브_건강꿀팁정보);
        final TextView 유튜브_골반고관절 = rootView.findViewById(R.id.유튜브_골반고관절);
        final TextView 유튜브_다이어트 = rootView.findViewById(R.id.유튜브_다이어트);
        final TextView 유튜브_등 = rootView.findViewById(R.id.유튜브_등);
        final TextView 유튜브_무릎 = rootView.findViewById(R.id.유튜브_무릎);
        final TextView 유튜브_발 = rootView.findViewById(R.id.유튜브_발);
        final TextView 유튜브_빡빡이의예능 = rootView.findViewById(R.id.유튜브_빡빡이의예능);
        final TextView 유튜브_손목손 = rootView.findViewById(R.id.유튜브_손목손);
        final TextView 유튜브_스트레칭 = rootView.findViewById(R.id.유튜브_스트레칭);
        final TextView 유튜브_어깨 = rootView.findViewById(R.id.유튜브_어깨);
        final TextView 유튜브_웨이트운동 = rootView.findViewById(R.id.유튜브_웨이트운동);
        final TextView 유튜브_턱관절 = rootView.findViewById(R.id.유튜브_턱관절);
        final TextView 유튜브_두통통증 = rootView.findViewById(R.id.유튜브_두통통증);
        final TextView 유튜브_팔꿈치 = rootView.findViewById(R.id.유튜브_팔꿈치);
        final TextView 유튜브_허리 = rootView.findViewById(R.id.유튜브_허리);


        //추천부분의 게시물을 보여준다.
        getFragmentManager().beginTransaction().replace(R.id.frameLayout_추천, Fragment추천).commitAllowingStateLoss();
        //처음 마주했을때의 UI를 설정한다.
        getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_neck).commitAllowingStateLoss();
        pain_neck.setTextColor(Color.parseColor("#133d88"));
        getFragmentManager().beginTransaction().replace(R.id.frameLayout_체형교정, Fragment체형교정_목).commitAllowingStateLoss();
        체형교정_목.setTextColor(Color.parseColor("#133d88"));
        getFragmentManager().beginTransaction().replace(R.id.frameLayout_Study, FragmentStudy_해부학).commitAllowingStateLoss();
        Study_해부학.setTextColor(Color.parseColor("#133d88"));
        getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_목).commitAllowingStateLoss();
        유튜브_목.setTextColor(Color.parseColor("#133d88"));

        Log.i("FragmentMain_pain_ankle",pain_ankle.getText().toString());
        //=======================아픈부위찾기-===============================
        pain_ankle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("아픈부위찾기_목","클릭됨");
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_ankle).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#133d88"));
                pain_back.setTextColor(Color.parseColor("#808080"));
                pain_chest.setTextColor(Color.parseColor("#808080"));
                pain_chin.setTextColor(Color.parseColor("#808080"));
                pain_elbow.setTextColor(Color.parseColor("#808080"));
                pain_headache.setTextColor(Color.parseColor("#808080"));
                pain_knee.setTextColor(Color.parseColor("#808080"));
                pain_neck.setTextColor(Color.parseColor("#808080"));
                pain_pelvis.setTextColor(Color.parseColor("#808080"));
                pain_sacrum.setTextColor(Color.parseColor("#808080"));
                pain_shoulder.setTextColor(Color.parseColor("#808080"));
                pain_waist.setTextColor(Color.parseColor("#808080"));
                pain_wrist.setTextColor(Color.parseColor("#808080"));
            }
        });
        pain_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_back).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#808080"));
                pain_back.setTextColor(Color.parseColor("#133d88"));
                pain_chest.setTextColor(Color.parseColor("#808080"));
                pain_chin.setTextColor(Color.parseColor("#808080"));
                pain_elbow.setTextColor(Color.parseColor("#808080"));
                pain_headache.setTextColor(Color.parseColor("#808080"));
                pain_knee.setTextColor(Color.parseColor("#808080"));
                pain_neck.setTextColor(Color.parseColor("#808080"));
                pain_pelvis.setTextColor(Color.parseColor("#808080"));
                pain_sacrum.setTextColor(Color.parseColor("#808080"));
                pain_shoulder.setTextColor(Color.parseColor("#808080"));
                pain_waist.setTextColor(Color.parseColor("#808080"));
                pain_wrist.setTextColor(Color.parseColor("#808080"));
            }
        });
        pain_chest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_chest).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#808080"));
                pain_back.setTextColor(Color.parseColor("#808080"));
                pain_chest.setTextColor(Color.parseColor("#133d88"));
                pain_chin.setTextColor(Color.parseColor("#808080"));
                pain_elbow.setTextColor(Color.parseColor("#808080"));
                pain_headache.setTextColor(Color.parseColor("#808080"));
                pain_knee.setTextColor(Color.parseColor("#808080"));
                pain_neck.setTextColor(Color.parseColor("#808080"));
                pain_pelvis.setTextColor(Color.parseColor("#808080"));
                pain_sacrum.setTextColor(Color.parseColor("#808080"));
                pain_shoulder.setTextColor(Color.parseColor("#808080"));
                pain_waist.setTextColor(Color.parseColor("#808080"));
                pain_wrist.setTextColor(Color.parseColor("#808080"));
            }
        });
        pain_chin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_chin).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#808080"));
                pain_back.setTextColor(Color.parseColor("#808080"));
                pain_chest.setTextColor(Color.parseColor("#808080"));
                pain_chin.setTextColor(Color.parseColor("#133d88"));
                pain_elbow.setTextColor(Color.parseColor("#808080"));
                pain_headache.setTextColor(Color.parseColor("#808080"));
                pain_knee.setTextColor(Color.parseColor("#808080"));
                pain_neck.setTextColor(Color.parseColor("#808080"));
                pain_pelvis.setTextColor(Color.parseColor("#808080"));
                pain_sacrum.setTextColor(Color.parseColor("#808080"));
                pain_shoulder.setTextColor(Color.parseColor("#808080"));
                pain_waist.setTextColor(Color.parseColor("#808080"));
                pain_wrist.setTextColor(Color.parseColor("#808080"));
            }
        });
        pain_elbow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_elbow).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#808080"));
                pain_back.setTextColor(Color.parseColor("#808080"));
                pain_chest.setTextColor(Color.parseColor("#808080"));
                pain_chin.setTextColor(Color.parseColor("#808080"));
                pain_elbow.setTextColor(Color.parseColor("#133d88"));
                pain_headache.setTextColor(Color.parseColor("#808080"));
                pain_knee.setTextColor(Color.parseColor("#808080"));
                pain_neck.setTextColor(Color.parseColor("#808080"));
                pain_pelvis.setTextColor(Color.parseColor("#808080"));
                pain_sacrum.setTextColor(Color.parseColor("#808080"));
                pain_shoulder.setTextColor(Color.parseColor("#808080"));
                pain_waist.setTextColor(Color.parseColor("#808080"));
                pain_wrist.setTextColor(Color.parseColor("#808080"));
            }
        });
        pain_headache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_headache).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#808080"));
                pain_back.setTextColor(Color.parseColor("#808080"));
                pain_chest.setTextColor(Color.parseColor("#808080"));
                pain_chin.setTextColor(Color.parseColor("#808080"));
                pain_elbow.setTextColor(Color.parseColor("#808080"));
                pain_headache.setTextColor(Color.parseColor("#133d88"));
                pain_knee.setTextColor(Color.parseColor("#808080"));
                pain_neck.setTextColor(Color.parseColor("#808080"));
                pain_pelvis.setTextColor(Color.parseColor("#808080"));
                pain_sacrum.setTextColor(Color.parseColor("#808080"));
                pain_shoulder.setTextColor(Color.parseColor("#808080"));
                pain_waist.setTextColor(Color.parseColor("#808080"));
                pain_wrist.setTextColor(Color.parseColor("#808080"));
            }
        });
        pain_knee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_knee).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#808080"));
                pain_back.setTextColor(Color.parseColor("#808080"));
                pain_chest.setTextColor(Color.parseColor("#808080"));
                pain_chin.setTextColor(Color.parseColor("#808080"));
                pain_elbow.setTextColor(Color.parseColor("#808080"));
                pain_headache.setTextColor(Color.parseColor("#808080"));
                pain_knee.setTextColor(Color.parseColor("#133d88"));
                pain_neck.setTextColor(Color.parseColor("#808080"));
                pain_pelvis.setTextColor(Color.parseColor("#808080"));
                pain_sacrum.setTextColor(Color.parseColor("#808080"));
                pain_shoulder.setTextColor(Color.parseColor("#808080"));
                pain_waist.setTextColor(Color.parseColor("#808080"));
                pain_wrist.setTextColor(Color.parseColor("#808080"));
            }
        });
        pain_neck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_neck).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#808080"));
                pain_back.setTextColor(Color.parseColor("#808080"));
                pain_chest.setTextColor(Color.parseColor("#808080"));
                pain_chin.setTextColor(Color.parseColor("#808080"));
                pain_elbow.setTextColor(Color.parseColor("#808080"));
                pain_headache.setTextColor(Color.parseColor("#808080"));
                pain_knee.setTextColor(Color.parseColor("#808080"));
                pain_neck.setTextColor(Color.parseColor("#133d88"));
                pain_pelvis.setTextColor(Color.parseColor("#808080"));
                pain_sacrum.setTextColor(Color.parseColor("#808080"));
                pain_shoulder.setTextColor(Color.parseColor("#808080"));
                pain_waist.setTextColor(Color.parseColor("#808080"));
                pain_wrist.setTextColor(Color.parseColor("#808080"));
            }
        });
        pain_pelvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_pelvis).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#808080"));
                pain_back.setTextColor(Color.parseColor("#808080"));
                pain_chest.setTextColor(Color.parseColor("#808080"));
                pain_chin.setTextColor(Color.parseColor("#808080"));
                pain_elbow.setTextColor(Color.parseColor("#808080"));
                pain_headache.setTextColor(Color.parseColor("#808080"));
                pain_knee.setTextColor(Color.parseColor("#808080"));
                pain_neck.setTextColor(Color.parseColor("#808080"));
                pain_pelvis.setTextColor(Color.parseColor("#133d88"));
                pain_sacrum.setTextColor(Color.parseColor("#808080"));
                pain_shoulder.setTextColor(Color.parseColor("#808080"));
                pain_waist.setTextColor(Color.parseColor("#808080"));
                pain_wrist.setTextColor(Color.parseColor("#808080"));
            }
        });
        pain_sacrum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_sacrum).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#808080"));
                pain_back.setTextColor(Color.parseColor("#808080"));
                pain_chest.setTextColor(Color.parseColor("#808080"));
                pain_chin.setTextColor(Color.parseColor("#808080"));
                pain_elbow.setTextColor(Color.parseColor("#808080"));
                pain_headache.setTextColor(Color.parseColor("#808080"));
                pain_knee.setTextColor(Color.parseColor("#808080"));
                pain_neck.setTextColor(Color.parseColor("#808080"));
                pain_pelvis.setTextColor(Color.parseColor("#808080"));
                pain_sacrum.setTextColor(Color.parseColor("#133d88"));
                pain_shoulder.setTextColor(Color.parseColor("#808080"));
                pain_waist.setTextColor(Color.parseColor("#808080"));
                pain_wrist.setTextColor(Color.parseColor("#808080"));
            }
        });
        pain_shoulder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_shoulder).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#808080"));
                pain_back.setTextColor(Color.parseColor("#808080"));
                pain_chest.setTextColor(Color.parseColor("#808080"));
                pain_chin.setTextColor(Color.parseColor("#808080"));
                pain_elbow.setTextColor(Color.parseColor("#808080"));
                pain_headache.setTextColor(Color.parseColor("#808080"));
                pain_knee.setTextColor(Color.parseColor("#808080"));
                pain_neck.setTextColor(Color.parseColor("#808080"));
                pain_pelvis.setTextColor(Color.parseColor("#808080"));
                pain_sacrum.setTextColor(Color.parseColor("#808080"));
                pain_shoulder.setTextColor(Color.parseColor("#133d88"));
                pain_waist.setTextColor(Color.parseColor("#808080"));
                pain_wrist.setTextColor(Color.parseColor("#808080"));
            }
        });
        pain_waist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_waist).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#808080"));
                pain_back.setTextColor(Color.parseColor("#808080"));
                pain_chest.setTextColor(Color.parseColor("#808080"));
                pain_chin.setTextColor(Color.parseColor("#808080"));
                pain_elbow.setTextColor(Color.parseColor("#808080"));
                pain_headache.setTextColor(Color.parseColor("#808080"));
                pain_knee.setTextColor(Color.parseColor("#808080"));
                pain_neck.setTextColor(Color.parseColor("#808080"));
                pain_pelvis.setTextColor(Color.parseColor("#808080"));
                pain_sacrum.setTextColor(Color.parseColor("#808080"));
                pain_shoulder.setTextColor(Color.parseColor("#808080"));
                pain_waist.setTextColor(Color.parseColor("#133d88"));
                pain_wrist.setTextColor(Color.parseColor("#808080"));
            }
        });
        pain_wrist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_pain, FragmentPain_wrist).commitAllowingStateLoss();
                pain_ankle.setTextColor(Color.parseColor("#808080"));
                pain_back.setTextColor(Color.parseColor("#808080"));
                pain_chest.setTextColor(Color.parseColor("#808080"));
                pain_chin.setTextColor(Color.parseColor("#808080"));
                pain_elbow.setTextColor(Color.parseColor("#808080"));
                pain_headache.setTextColor(Color.parseColor("#808080"));
                pain_knee.setTextColor(Color.parseColor("#808080"));
                pain_neck.setTextColor(Color.parseColor("#808080"));
                pain_pelvis.setTextColor(Color.parseColor("#808080"));
                pain_sacrum.setTextColor(Color.parseColor("#808080"));
                pain_shoulder.setTextColor(Color.parseColor("#808080"));
                pain_waist.setTextColor(Color.parseColor("#808080"));
                pain_wrist.setTextColor(Color.parseColor("#133d88"));
            }
        });
        //=======================체형교정-===============================
        체형교정_목.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_체형교정, Fragment체형교정_목).commitAllowingStateLoss();
                체형교정_목.setTextColor(Color.parseColor("#133d88"));
                체형교정_측면척추.setTextColor(Color.parseColor("#808080"));
                체형교정_척추측만증.setTextColor(Color.parseColor("#808080"));
                체형교정_골반.setTextColor(Color.parseColor("#808080"));
                체형교정_휜다리.setTextColor(Color.parseColor("#808080"));
                체형교정_평발말발.setTextColor(Color.parseColor("#808080"));
            }
        });
        체형교정_척추측만증.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_체형교정, Fragment체형교정_척추측만증).commitAllowingStateLoss();
                체형교정_목.setTextColor(Color.parseColor("#808080"));
                체형교정_측면척추.setTextColor(Color.parseColor("#808080"));
                체형교정_척추측만증.setTextColor(Color.parseColor("#133d88"));
                체형교정_골반.setTextColor(Color.parseColor("#808080"));
                체형교정_휜다리.setTextColor(Color.parseColor("#808080"));
                체형교정_평발말발.setTextColor(Color.parseColor("#808080"));
            }
        });
        체형교정_측면척추.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_체형교정, Fragment체형교정_측면척추).commitAllowingStateLoss();
                체형교정_목.setTextColor(Color.parseColor("#808080"));
                체형교정_측면척추.setTextColor(Color.parseColor("#133d88"));
                체형교정_척추측만증.setTextColor(Color.parseColor("#808080"));
                체형교정_골반.setTextColor(Color.parseColor("#808080"));
                체형교정_휜다리.setTextColor(Color.parseColor("#808080"));
                체형교정_평발말발.setTextColor(Color.parseColor("#808080"));
            }
        });
        체형교정_골반.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_체형교정, Fragment체형교정_골반).commitAllowingStateLoss();
                체형교정_목.setTextColor(Color.parseColor("#808080"));
                체형교정_측면척추.setTextColor(Color.parseColor("#808080"));
                체형교정_척추측만증.setTextColor(Color.parseColor("#808080"));
                체형교정_골반.setTextColor(Color.parseColor("#133d88"));
                체형교정_휜다리.setTextColor(Color.parseColor("#808080"));
                체형교정_평발말발.setTextColor(Color.parseColor("#808080"));
            }
        });
        체형교정_휜다리.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_체형교정, Fragment체형교정_휜다리).commitAllowingStateLoss();
                체형교정_목.setTextColor(Color.parseColor("#808080"));
                체형교정_측면척추.setTextColor(Color.parseColor("#808080"));
                체형교정_척추측만증.setTextColor(Color.parseColor("#808080"));
                체형교정_골반.setTextColor(Color.parseColor("#808080"));
                체형교정_휜다리.setTextColor(Color.parseColor("#133d88"));
                체형교정_평발말발.setTextColor(Color.parseColor("#808080"));
            }
        });
        체형교정_평발말발.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_체형교정, Fragment체형교정_평발말발).commitAllowingStateLoss();
                체형교정_목.setTextColor(Color.parseColor("#808080"));
                체형교정_측면척추.setTextColor(Color.parseColor("#808080"));
                체형교정_척추측만증.setTextColor(Color.parseColor("#808080"));
                체형교정_골반.setTextColor(Color.parseColor("#808080"));
                체형교정_휜다리.setTextColor(Color.parseColor("#808080"));
                체형교정_평발말발.setTextColor(Color.parseColor("#133d88"));
            }
        });
        //===================================Study=======================================
        Study_물리치료학.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_Study, FragmentStudy_물리치료학).commitAllowingStateLoss();
                Study_물리치료학.setTextColor(Color.parseColor("#133d88"));
                Study_약학.setTextColor(Color.parseColor("#808080"));
                Study_영양학.setTextColor(Color.parseColor("#808080"));
                Study_해부학.setTextColor(Color.parseColor("#808080"));
            }
        });Study_약학.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_Study, FragmentStudy_약학).commitAllowingStateLoss();
                Study_물리치료학.setTextColor(Color.parseColor("#808080"));
                Study_약학.setTextColor(Color.parseColor("#133d88"));
                Study_영양학.setTextColor(Color.parseColor("#808080"));
                Study_해부학.setTextColor(Color.parseColor("#808080"));
            }
        });Study_영양학.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_Study, FragmentStudy_영양학).commitAllowingStateLoss();
                Study_물리치료학.setTextColor(Color.parseColor("#808080"));
                Study_약학.setTextColor(Color.parseColor("#808080"));
                Study_영양학.setTextColor(Color.parseColor("#133d88"));
                Study_해부학.setTextColor(Color.parseColor("#808080"));
            }
        });Study_해부학.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_Study, FragmentStudy_해부학).commitAllowingStateLoss();
                Study_물리치료학.setTextColor(Color.parseColor("#808080"));
                Study_약학.setTextColor(Color.parseColor("#808080"));
                Study_영양학.setTextColor(Color.parseColor("#808080"));
                Study_해부학.setTextColor(Color.parseColor("#133d88"));
            }
        });
        //============================================드디어 마지막 대망의 유튜브다.===============================
        유튜브_건강꿀팁정보.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_건강꿀팁정보).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#133d88"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_골반고관절.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_골반).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#133d88"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_다이어트.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_다이어트).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#133d88"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_등.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_등).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#133d88"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_목.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_목).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#133d88"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_무릎.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_무릎).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#133d88"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_발.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_발목발).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#133d88"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_빡빡이의예능.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_빡빡이의예능).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#133d88"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_손목손.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_손목손).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#133d88"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_스트레칭.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_스트레칭).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#133d88"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_어깨.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_어깨).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#133d88"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_웨이트운동.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_웨이트운동).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#133d88"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_턱관절.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_턱관절).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#133d88"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_두통통증.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_두통).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#133d88"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_팔꿈치.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_팔꿈치).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#133d88"));
                유튜브_허리.setTextColor(Color.parseColor("#808080"));
            }
        });유튜브_허리.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout_유튜브, Fragment유튜브_허리).commitAllowingStateLoss();
                유튜브_목.setTextColor(Color.parseColor("#808080"));
                유튜브_건강꿀팁정보.setTextColor(Color.parseColor("#808080"));
                유튜브_골반고관절.setTextColor(Color.parseColor("#808080"));
                유튜브_다이어트.setTextColor(Color.parseColor("#808080"));
                유튜브_등.setTextColor(Color.parseColor("#808080"));
                유튜브_무릎.setTextColor(Color.parseColor("#808080"));
                유튜브_발.setTextColor(Color.parseColor("#808080"));
                유튜브_빡빡이의예능.setTextColor(Color.parseColor("#808080"));
                유튜브_손목손.setTextColor(Color.parseColor("#808080"));
                유튜브_스트레칭.setTextColor(Color.parseColor("#808080"));
                유튜브_어깨.setTextColor(Color.parseColor("#808080"));
                유튜브_웨이트운동.setTextColor(Color.parseColor("#808080"));
                유튜브_턱관절.setTextColor(Color.parseColor("#808080"));
                유튜브_두통통증.setTextColor(Color.parseColor("#808080"));
                유튜브_팔꿈치.setTextColor(Color.parseColor("#808080"));
                유튜브_허리.setTextColor(Color.parseColor("#133d88"));
            }
        });

//        //더보기 버튼을 클릭하면 게시물을 더볼수 있는 액티비티로 이동시켜 준다.
//        moreView_1.setOnClickListener(new View.OnClickListener() {//더보기 버튼을 클릭했을때이다.
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context.getApplicationContext(), MoreView.class);
////                intent.putExtra("list",list);
////                Log.i("list로그", list.get(100).getSubject());
//                startActivity(intent);
//            }
//        });
//        moreView_2.setOnClickListener(new View.OnClickListener() {//더보기 버튼을 클릭했을때이다.
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context.getApplicationContext(), MoreView.class);
////                intent.putExtra("list",list);
////                Log.i("list로그", list.get(100).getSubject());
//                startActivity(intent);
//            }
//        });
//        moreView_3.setOnClickListener(new View.OnClickListener() {//더보기 버튼을 클릭했을때이다.
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context.getApplicationContext(), MoreView.class);
////                intent.putExtra("list",list);
////                Log.i("list로그", list.get(100).getSubject());
//                startActivity(intent);
//            }
//        });
//        moreView_4.setOnClickListener(new View.OnClickListener() {//더보기 버튼을 클릭했을때이다.
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context.getApplicationContext(), MoreView.class);
////                intent.putExtra("list",list);
////                Log.i("list로그", list.get(100).getSubject());
//                startActivity(intent);
//            }
//        });

        //일단 추천카테고리의 게시물을 채우기 위한 임시 데이터로 모든 게시물을 가져온다.
//        new MainDescription().execute("http://"+IP_ADDRESS+"/user_signup/allContents.php");

        return rootView;
    }


    private class MainDescription extends AsyncTask<String, Void, String> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //진행다일로그 시작
//            progressDialog = new ProgressDialog(context.getApplicationContext());
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setMessage("잠시 기다려 주세요.");
//            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

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
                Log.i("JSON갯수_프레그먼트", total_json + "");
                for (int i = 1; i <= total_json; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);

                    ItemObject itemObject = new ItemObject(jsonObject.getString("subject"), jsonObject.getString("contents_url"), jsonObject.getString("thumbnail_url"));
                    list.add(itemObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //해당 리싸이클러뷰는 수직 방향으로 모든 게시물들을 보여주는 리싸이클러뷰다.
//            AdapterRecomendation recoAdapter = new AdapterRecomendation(list, context.getApplicationContext());
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//            recomendationRecyclerView.setLayoutManager(layoutManager);
//            recomendationRecyclerView.setAdapter(recoAdapter);
//            recomendationRecyclerView.setHasFixedSize(true);
//            AdapterMain myAdapter = new AdapterMain(list, context.getApplicationContext());
//            AdapterMain_2 myAdapter_2 = new AdapterMain_2(list, context.getApplicationContext());
//            AdapterMain_3 myAdapter_3 = new AdapterMain_3(list, context.getApplicationContext());
//            AdapterMain_4 myAdapter_4 = new AdapterMain_4(list, context.getApplicationContext());
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//            RecyclerView.LayoutManager layoutManager_2 = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//            RecyclerView.LayoutManager layoutManager_3 = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//            RecyclerView.LayoutManager layoutManager_4 = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setAdapter(myAdapter);
//            recyclerView.setHasFixedSize(true);

//            recyclerView_2.setLayoutManager(layoutManager_2);
//            recyclerView_2.setAdapter(myAdapter_2);
//            recyclerView_2.setHasFixedSize(true);
//
//            recyclerView_3.setLayoutManager(layoutManager_3);
//            recyclerView_3.setAdapter(myAdapter_3);
//            recyclerView_3.setHasFixedSize(true);
//
//            recyclerView_4.setLayoutManager(layoutManager_4);
//            recyclerView_4.setAdapter(myAdapter_4);
//            recyclerView_4.setHasFixedSize(true);


//            progressDialog.dismiss();
        }
    }
}
