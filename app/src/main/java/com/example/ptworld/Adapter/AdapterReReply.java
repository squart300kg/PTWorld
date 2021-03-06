package com.example.ptworld.Adapter;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptworld.R;
import com.example.ptworld.DTO.ReReplyObject;

import java.util.ArrayList;

public class AdapterReReply extends RecyclerView.Adapter<AdapterReReply.ViewHolder> {

    private ArrayList<ReReplyObject> reReplyObjects;
    Context context;
    String IP_ADDRESS = "squart300kg.cafe24.com";
    String rereplyno = null;
    //생성자
    public AdapterReReply(ArrayList<ReReplyObject> reReplyObjects, Context context) {
        this.reReplyObjects = reReplyObjects;
        this.context = context;
    }
    public AdapterReReply(ArrayList<ReReplyObject> reReplyObjects ) {
        this.reReplyObjects = reReplyObjects;
    }
    public AdapterReReply(ArrayList<ReReplyObject> reReplyObjects,Context context, String rereplyno ) {
        this.reReplyObjects = reReplyObjects;
        this.rereplyno = rereplyno;
        this.context = context;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {

        ImageView rereply_profile_image2;
        TextView rereply_nickname;
        TextView rereply_contents;
        LinearLayout rereply_frame;

        public ViewHolder(View itemView) {
            super(itemView);
            rereply_frame = itemView.findViewById(R.id.rereply_frame);
            rereply_contents = itemView.findViewById(R.id.rereply_contents);
            rereply_nickname = itemView.findViewById(R.id.rereply_nickname);
            rereply_profile_image2 = itemView.findViewById(R.id.rereply_profile_image2);
            rereply_profile_image2.setBackground(new ShapeDrawable(new OvalShape()));
            if(Build.VERSION.SDK_INT >= 21) {
                rereply_profile_image2.setClipToOutline(true);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_one_rereply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull   ViewHolder holder,   int position) {//뷰홀더 객체가 그거다. 액티비티에 위젯뷰들을 그려주는 녀석.
        if(rereplyno != null){
            if(Integer.parseInt(rereplyno)-1 == position){
//                holder.reply_contentsFrame.setBackgroundColor(Color.rgb(189, 183, 107));
                holder.rereply_frame.setBackgroundResource(R.color.grey);
                rereplyno = null;
            }
        }
        holder.rereply_profile_image2.setImageBitmap(reReplyObjects.get(position).profile_image);
        holder.rereply_nickname.setText(reReplyObjects.get(position).nickname);
        holder.rereply_contents.setText(reReplyObjects.get(position).contents);
    }

    @Override
    public int getItemCount() {
        //return mList.size();
        return reReplyObjects.size();
    }
}
