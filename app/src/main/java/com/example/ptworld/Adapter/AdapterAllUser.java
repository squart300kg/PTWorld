package com.example.ptworld.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ptworld.Activity.Contents;
import com.example.ptworld.DTO.ContentsDTO;
import com.example.ptworld.DTO.Users;
import com.example.ptworld.R;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AdapterAllUser extends RecyclerView.Adapter<AdapterAllUser.ViewHolder> {

    ArrayList<Users> list;
    Activity mContext;

    public AdapterAllUser(ArrayList<Users> list, Activity context) {
        this.list = list;
        this.mContext = context;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image;
        TextView nickname;
        LinearLayout frame;
        public ViewHolder(View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            nickname = itemView.findViewById(R.id.nickname);
            frame = itemView.findViewById(R.id.frame);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_one_contents_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull   ViewHolder holder, final int position) {//뷰홀더 객체가 그거다. 액티비티에 위젯뷰들을 그려주는 녀석.

        holder.profile_image.setImageBitmap(list.get(position).getProfile_image());
        holder.nickname.setText(list.get(position).getNickname());

        holder.frame.setOnClickListener(view -> {
//                Intent intent = new Intent(mContext, Contents.class);
//                intent.putExtra("position", position);
//                intent.putExtra("contents_url", list.get(position).contents_url);
//                intent.putExtra("subject", list.get(position).subject);
//                mContext.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}
