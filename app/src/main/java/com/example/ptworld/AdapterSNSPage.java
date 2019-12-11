package com.example.ptworld;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class AdapterSNSPage extends PagerAdapter{

    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    private Context mContext = null;
    ArrayList<Bitmap> board_image;
    ImageView pageimage;
    LottieAnimationView lottie;
    private long btnPressTime = 0;
    AdapterSNS.ViewHolder holder;
    int no = 0;
    String IP_ADDRESS = "squart300kg.cafe24.com";
    public AdapterSNSPage() {

    }

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public AdapterSNSPage(Context context, ArrayList<Bitmap> board_image, AdapterSNS.ViewHolder holder, int no) {
        mContext = context;
        this.board_image = board_image;
        this.holder = holder;
        this.no = no;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;

        if (mContext != null) {
            // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.page, container, false);
            lottie = view.findViewById(R.id.lottie);
//            pageimage = view.findViewById(R.id.page_image);

//            lottie.setImageBitmap(board_image.get(position));
            lottie.setBackground(new BitmapDrawable(board_image.get(position)));
//            lottie.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (System.currentTimeMillis() > btnPressTime + 1000) {
//                        btnPressTime = System.currentTimeMillis();
//                        Log.i("이미지 클릭 테스트", "한번 클릭");
//                        return;
//                    }
//                    if (System.currentTimeMillis() <= btnPressTime + 1000) {
//                        Log.i("이미지 클릭 테스트", "더블 클릭");
//
//                        if(holder.sns_like.getVisibility() == View.VISIBLE){
//                            //만약에 현재 좋아요가 눌린상태라면 애니메이션만 동작시켜준다.
//                            holder.sns_like.playAnimation();
//                            holder.sns_like2.setVisibility(View.GONE);
//                        }else{
//                            //좋아요가 눌린상태가 아니라면 스레드를 실행시켜 좋아요를 1 늘려준다.
//                            String type = "like";
////                            Thread_Like thread_like = new Thread_Like(position);
//                            new Thread_Like(holder.getAdapterPosition()).execute("http://"+IP_ADDRESS+"/user_signup/like.php", type, no+"", TrainnerInfo.email, TrainnerInfo.nickname);
//                            holder.sns_like.playAnimation();
//                            holder.sns_like.setVisibility(View.VISIBLE);
//                            holder.sns_like2.setVisibility(View.GONE);
//                        }
//
//
//
//
////                        holder.sns_like2.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View view) {
////                                holder.sns_like.setVisibility(View.VISIBLE);
////                                holder.sns_like.playAnimation();
////
////                                holder.sns_like2.setVisibility(View.GONE);
////                                String type = "like";
////                                Thread_Like thread_like = new Thread_Like();
////                                thread_like.execute("http://"+IP_ADDRESS+"/user_signup/like.php", type, no+"");
////
////                            }
////                        });
////                        //좋아요 버튼을 눌렀을 때.(빨간색 -> 검은색) 좋아요 취소
////                        holder.sns_like.setOnClickListener(new View.OnClickListener(){
////                            @Override
////                            public void onClick(View view) {
////                                holder.sns_like.setVisibility(View.GONE);
////                                holder.sns_like.playAnimation();
////
////                                holder.sns_like2.setVisibility(View.VISIBLE);
////                                String type = "dislike";
////                                Thread_Like thread_like = new Thread_Like();
////                                thread_like.execute("http://"+IP_ADDRESS+"/user_signup/like.php", type,no+"");
////                            }
////                        });
//
//                    }
//                }
//            });

        }

        // 뷰페이저에 추가.
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        // 전체 페이지 수는 10개로 고정.
        return board_image.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }


}
