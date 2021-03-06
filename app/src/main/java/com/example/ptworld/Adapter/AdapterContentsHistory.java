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
import com.example.ptworld.R;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AdapterContentsHistory extends RecyclerView.Adapter<AdapterContentsHistory.ViewHolder> {

    ArrayList<ContentsDTO> list;
    Activity mContext;

    public AdapterContentsHistory(ArrayList<ContentsDTO> list, Activity context) {
        this.list = list;
        this.mContext = context;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView historyImage;
        TextView historySubject;
        TextView historyViews;
        LinearLayout historyFrame;
        public ViewHolder(View itemView) {
            super(itemView);
            historyFrame = itemView.findViewById(R.id.historyFrame);
            historySubject = itemView.findViewById(R.id.historySubject);
            historyImage = itemView.findViewById(R.id.historyImage);
            historyViews = itemView.findViewById(R.id.historyViews);

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
        Glide.with(mContext).load(list.get(position).thumbnail_url)
                .into(holder.historyImage);
        holder.historySubject.setText(list.get(position).subject);
        holder.historyViews.setText("조회수 : "+list.get(position).views + "회");

        holder.historyFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Contents.class);
                intent.putExtra("position", position);
                intent.putExtra("contents_url", list.get(position).contents_url);
                intent.putExtra("subject", list.get(position).subject);
                mContext.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}
