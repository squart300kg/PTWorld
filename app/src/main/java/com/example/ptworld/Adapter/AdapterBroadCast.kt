package com.example.ptworld.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.VideoView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ptworld.Activity.SeeingBroadCastActivity
import com.example.ptworld.DTO.BroadCast
import com.example.ptworld.DTO.Users
import com.example.ptworld.R
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.net.URL
import java.util.*


class AdapterBroadCast(var list: ArrayList<BroadCast>, var mContext: Activity) : RecyclerView.Adapter<AdapterBroadCast.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var broad_cast_thumbnail: ImageView
        var broad_cast_title : TextView
        var broad_cast_frame : LinearLayout
        init {
            broad_cast_thumbnail = itemView.findViewById(R.id.broad_cast_thumbnail)
            broad_cast_title = itemView.findViewById(R.id.broad_cast_title)
            broad_cast_frame = itemView.findViewById(R.id.broad_cast_frame)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.broad_cast_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        var thumbnail = getWebVideoThumbnail(mContext, Uri.parse(list[position].url))
//        Log.i("rtmp_url", list[position].url)
//        var thumbnail = ThumbnailUtils.createVideoThumbnail(
//                list[position].url,
//                MediaStore.Video.Thumbnails.MICRO_KIND)
//        holder.broad_cast_item.setImageBitmap(thumbnail)
//        Log.i("broad_cast_thumbnail_result : ", thumbnail.toString())

        Glide.with(mContext).load(list[position].thumbnail_url).into(holder.broad_cast_thumbnail)
        holder.broad_cast_title.text = list[position].title
        holder.broad_cast_frame.setOnClickListener {
            var intent = Intent(mContext, SeeingBroadCastActivity::class.java)
            intent.putExtra("title", list[position].title)
            intent.putExtra("streaming_url", list[position].streaming_url)
            mContext.startActivity(intent)
        }
//        holder.broad_cast_item.setImageBitmap(thumbnail)
//        holder.profile_image.setImageBitmap(list[position].profile_image)
//        holder.nickname.text = list[position].nickname
//        holder.frame.setOnClickListener { view: View? -> }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getWebVideoThumbnail(context : Context, uri : Uri) : Bitmap? {
        var retriever = MediaMetadataRetriever()
        try{
            Log.i("getWebVideoThumbnail_uri", uri.toString())
            retriever.setDataSource(uri.toString(), HashMap<String, String>())
            return retriever.getFrameAtTime(1000)
        }catch( e : IllegalArgumentException){
            e.printStackTrace()
        }catch( e : RuntimeException){
            e.printStackTrace()
        }finally {
            try{
                retriever.release()
            }catch ( e : RuntimeException){
                e.printStackTrace()
            }
        }
        return null
    }

}