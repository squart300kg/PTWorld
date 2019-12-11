package com.example.ptworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class AdapterProfile extends RecyclerView.Adapter<AdapterProfile.ViewHolder> {

    //데이터 배열 선언
    private ArrayList<Bitmap> mList;
    Activity context;
    //생성자
    public AdapterProfile(ArrayList<Bitmap> list, Activity context ) {
        this.mList = list;
        this.context = context;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {

        ImageView album_one_picture;
        public ViewHolder(View itemView) {
            super(itemView);
            album_one_picture = itemView.findViewById(R.id.album_one_picture);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_one_picture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {//뷰홀더 객체가 그거다. 액티비티에 위젯뷰들을 그려주는 녀석.
        holder.album_one_picture.setImageBitmap(mList.get(position));
    }

    @Override
    public int getItemCount() {
//        return mList.size();
        return  mList.size();
    }



}
