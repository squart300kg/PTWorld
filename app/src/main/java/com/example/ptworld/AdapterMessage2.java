package com.example.ptworld;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMessage2  extends RecyclerView.Adapter<AdapterMessage2.ViewHolder> {

    Activity activity;
    ArrayList<ChattingDTO> chatting_list;
    public AdapterMessage2(Activity activity, ArrayList<ChattingDTO> chatting_list)
    {
        this.activity = activity;
        this.chatting_list = chatting_list;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView one_message_left;

        public ViewHolder(View itemView) {
            super(itemView);
            one_message_left = itemView.findViewById(R.id.one_message_left);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_message_left, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.one_message_left.setText(chatting_list.get(position).contents);

    }

    @Override
    public int getItemCount() {
        return chatting_list.size();
    }


}
