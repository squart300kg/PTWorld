package com.example.ptworld;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMessage  extends RecyclerView.Adapter<AdapterMessage.ViewHolder> {

    String direction;
    Activity activity;
    ArrayList<ChattingDTO> chatting_list;
    public AdapterMessage(Activity activity, ArrayList<ChattingDTO> chatting_list)
    {
        this.activity = activity;
        this.chatting_list = chatting_list;
    }
    public AdapterMessage(Activity activity, ArrayList<ChattingDTO> chatting_list, String direction)
    {
        this.direction = direction;
        this.activity = activity;
        this.chatting_list = chatting_list;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView one_message_right;
        LinearLayout one_message;
        TextView one_message_time_right;
        TextView one_message_time_left;


        public ViewHolder(View itemView) {
            super(itemView);
            one_message_right = itemView.findViewById(R.id.one_message_right);
            one_message = itemView.findViewById(R.id.one_message);
            one_message_time_right = itemView.findViewById(R.id.one_message_time_right);
            one_message_time_left = itemView.findViewById(R.id.one_message_time_left);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_message_right, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(chatting_list.get(position).direction.equals("right")){
            holder.one_message_time_left.setVisibility(View.VISIBLE);
            holder.one_message_time_right.setVisibility(View.GONE);

            holder.one_message.setGravity(Gravity.RIGHT);
            holder.one_message_time_left.setText(chatting_list.get(position).time);
        } else {
            holder.one_message_time_left.setVisibility(View.GONE);
            holder.one_message_time_right.setVisibility(View.VISIBLE);

            holder.one_message.setGravity(Gravity.LEFT);
            holder.one_message_time_right.setText(chatting_list.get(position).time);
        }
        holder.one_message_right.setText(chatting_list.get(position).contents);

    }

    @Override
    public int getItemCount() {
        return chatting_list.size();
    }


}
