package com.example.ptworld;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AdapterMain_4 extends RecyclerView.Adapter<AdapterMain_4.ViewHolder> {

    //데이터 배열 선언
    private ArrayList<ItemObject> mList;
    Context context;
    //생성자
    public AdapterMain_4(ArrayList<ItemObject> list, Context context) {
        this.mList = list;
        this.context = context;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mainImage;
        private TextView mainSubject, mainContents;
        private LinearLayout mainFrame;

        public ViewHolder(View itemView) {
            super(itemView);

            mainImage = itemView.findViewById(R.id.mainImage);
            mainSubject = itemView.findViewById(R.id.mainSubject);
            mainFrame = itemView.findViewById(R.id.mainFrame);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_contents, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {//뷰홀더 객체가 그거다. 액티비티에 위젯뷰들을 그려주는 녀석.
        holder.mainSubject.setText(String.valueOf(mList.get(position).getSubject()));
        Glide.with(context).load(mList.get(position).getImage_url())
                .into(holder.mainImage);
//        Log.i("받아온 span", mList.get(position).getContents());
        holder.mainFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), Contents.class);
                intent.putExtra("position", position);
                intent.putExtra("contents_url", mList.get(position).getContents_url());
                intent.putExtra("subject", mList.get(position).getSubject());
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));

            }
        });

//        //크롤링한 데이터들을 DB에 모두 넣기 위한 임시 스레드이다.
//        Thread_Temp thread_temp = new Thread_Temp();
//        String IP_ADDRESS = "squart300kg.cafe24.com";
//        thread_temp.execute("http://"+IP_ADDRESS+"/user_signup/temp_mainContentsInsert.php", mList.get(position).getSubject(), mList.get(position).getImage_url(), mList.get(position).getContents_url());
//        //1, 요청할 URL 2. 게시물 제목 3. 게시물 썸네일 4. 해당 게시물 URL
//        Log.i("제목", mList.get(position).getSubject());
//        Log.i("썸네일URL", mList.get(position).getImage_url());
//        Log.i("내용URL", mList.get(position).getContents_url());
////        Log.i("AdapterMain", mList.get(position).getImage_url());
    }

    @Override
    public int getItemCount() {
//        return mList.size();
        return mList.size() > 10 ? 10 : mList.size();
    }



}
