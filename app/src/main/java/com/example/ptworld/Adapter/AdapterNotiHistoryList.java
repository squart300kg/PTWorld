package com.example.ptworld.Adapter;

import android.app.Activity;
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

import com.example.ptworld.DTO.BoardObject;
import com.example.ptworld.R;

import java.util.ArrayList;

public class AdapterNotiHistoryList extends RecyclerView.Adapter<AdapterNotiHistoryList.ViewHolder> {

    Activity activity;
    ArrayList<BoardObject> list;


    public AdapterNotiHistoryList(Activity activity, ArrayList<BoardObject> list) {
        this.activity = activity;
        this.list = list;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView notihistory_profile_image;
        TextView notihistory_contents;

        public ViewHolder(View itemView) {
            super(itemView);
            notihistory_profile_image = itemView.findViewById(R.id.notihistory_profile_image);
            notihistory_contents = itemView.findViewById(R.id.notihistory_contents);
            notihistory_profile_image.setBackground(new ShapeDrawable(new OvalShape()));
            if(Build.VERSION.SDK_INT >= 21) {
                notihistory_profile_image.setClipToOutline(true);
            }

        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notihistory_one, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.notihistory_contents.setText(list.get(position).contentsText);
        holder.notihistory_profile_image.setImageBitmap(list.get(position).profile_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
