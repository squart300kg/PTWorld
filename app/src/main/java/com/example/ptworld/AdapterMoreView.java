package com.example.ptworld;

import android.content.Context;
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

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AdapterMoreView extends RecyclerView.Adapter<AdapterMoreView.ViewHolder> {

    ArrayList<ItemObject> list;
    Context context;

    public AdapterMoreView(ArrayList<ItemObject> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView moreViewImage;
        private TextView moreViewSubject;
        private LinearLayout moreViewFrame;

        public ViewHolder(View itemView) {
            super(itemView);

            moreViewImage = itemView.findViewById(R.id.moreViewImage);
            moreViewSubject = itemView.findViewById(R.id.moreViewSubject);
            moreViewFrame = itemView.findViewById(R.id.moreViewFrame);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_more_view_contents, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.moreViewSubject.setText(String.valueOf(list.get(position).getSubject()));
        Glide.with(context).load(list.get(position).getImage_url())
                .into(holder.moreViewImage);
        holder.moreViewFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), Contents.class);
                intent.putExtra("contents_url", list.get(position).getContents_url());
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
