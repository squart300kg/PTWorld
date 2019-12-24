package com.example.ptworld.Adapter;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptworld.DTO.LikeListDTO;
import com.example.ptworld.R;

import java.util.ArrayList;

public class AdapterLikeList extends RecyclerView.Adapter<AdapterLikeList.ViewHolder> {

    //데이터 배열 선언
    static ArrayList<LikeListDTO> mList;
    Context context;
    String IP_ADDRESS = "squart300kg.cafe24.com";
    //생성자
    public AdapterLikeList(ArrayList<LikeListDTO> list, Context context) {
        this.mList = list;
        this.context = context;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView likeList_profile_image;
        TextView likeList_email;
        TextView likeList_nickname;


        public ViewHolder(View itemView) {
            super(itemView);

//            likeList_email = itemView.findViewById(R.id.likeList_email);
            likeList_nickname = itemView.findViewById(R.id.likeList_nickname);
            likeList_profile_image = itemView.findViewById(R.id.likeList_profile_image);
            likeList_profile_image.setBackground(new ShapeDrawable(new OvalShape()));
            if(Build.VERSION.SDK_INT >= 21) {
                likeList_profile_image.setClipToOutline(true);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_likelist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {//뷰홀더 객체가 그거다. 액티비티에 위젯뷰들을 그려주는 녀석.
//        holder.likeList_email.setText(mList.get(position).email);
        holder.likeList_nickname.setText(mList.get(position).nickname);
        holder.likeList_profile_image.setImageBitmap(mList.get(position).profile_image);

    }

    @Override
    public int getItemCount() {
        //return mList.size();
        return mList.size();
    }




}
