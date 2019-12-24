package com.example.ptworld.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptworld.Fragment.FragmentBoardInsert;
import com.example.ptworld.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AdapterBoardInsert extends RecyclerView.Adapter<AdapterBoardInsert.ViewHolder> {

    public String TAG = "Gallery Adapter Example :: ";
    String[] mImgs;
    Bitmap bm;

    //데이터 배열 선언
    ArrayList<Bitmap> mlist = new ArrayList<>();
    Context mContext;
    String mBasePath;
    View rootView;
    int checknum = 0;
    //생성자
    static ArrayList<Boolean> isCheck = new ArrayList<>();

    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

    public AdapterBoardInsert(Context context, String basepath, View view) {
        this.rootView = view;
        this.mContext = context;
        this.mBasePath = basepath;

        File file = new File(mBasePath);
        if(!file.exists()){
            if(!file.mkdirs()){
                Log.d(TAG, "failed to create directory");
            }
        }
        mImgs = file.list();
        Log.i("file_List0번째 인덱스", mImgs[0]);
        Log.i("file_List1번째 인덱스", mImgs[1]);
        Log.i("file_List2번째 인덱스", mImgs[2]);

    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView album_one_picture;
        public ViewHolder(View itemView) {
            super(itemView);
            album_one_picture = itemView.findViewById(R.id.album_one_picture);
//            album_one_picture.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    album_one_picture.setBackgroundResource(R.drawable.border_imageselect);
//                    Log.i("뷰홀더 아이템Check","옳지!"+getAdapterPosition());
//                }
//            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_one_picture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {//뷰홀더 객체가 그거다. 액티비티에 위젯뷰들을 그려주는 녀석.
        File dir = new File(mBasePath);
        mImgs = dir.list();

        //============================================================================
        // 이 부분에서부터 'options.inJustDecodeBounds=false'까지는
        // Bitmap 사용시 나타나는 memory 부족 현상을 예방하기 위한 code. 경우에 따라서는 생략해도 가능하다.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        int reqWidth = 256;
        int reqHeight = 192;
        if((width > reqWidth) || (height > reqHeight)){
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        //============================================================================

        bm = BitmapFactory.decodeFile(mBasePath+ File.separator +mImgs[position], options);
        Bitmap bm2 = ThumbnailUtils.extractThumbnail(bm, 300, 300);
        Log.i("Adapter_앨범",bm2+"");

        Bitmap rotatedBitmap = null;
        if(bm2 != null){
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(mBasePath+ File.separator +mImgs[position]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

              rotatedBitmap = null;
            switch(orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bm2, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bm2, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bm2, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bm2;
            }
        }
        mlist.add(rotatedBitmap);
        isCheck.add(false);
        Log.i("사진 수========",mlist.size()+"개");
        Log.i("체크배열 수========",isCheck.size()+"개");
        holder.album_one_picture.setImageBitmap(rotatedBitmap);
        holder.album_one_picture.setScaleType(ImageView.ScaleType.FIT_CENTER);
        holder.album_one_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("온바인드 아이템Check","옳지!"+position);
                if(isCheck.get(position) == false && checknum < 5){
                    //false란 뜻인 이전에 체크한 적이 없단 뜻이다.
                    isCheck.add(position, true);//사진을 체크하면 체크변수에 넣는다.
                    holder.album_one_picture.setBackgroundResource(R.drawable.border_imageselect);
                    ImageView big_thumbnail = rootView.findViewById(R.id.big_thumnail);
                    big_thumbnail.setImageBitmap(mlist.get(position));
                    FragmentBoardInsert.insertBitmap.add(position, mlist.get(position));
                    checknum ++;
                }else if(isCheck.get(position) == true){
                    //지금 클릭한 사진의 체크 인덱스가 true인것은 전에 한번 눌렀었단 뜻이므로 또다시 눌렀단 뜻은 체크를 해제한단 뜻이다.
                    isCheck.add(position, false);
                    holder.album_one_picture.setBackgroundResource(R.drawable.border_imageselect2);
                    ImageView big_thumbnail = rootView.findViewById(R.id.big_thumnail);
                    big_thumbnail.setImageBitmap(mlist.get(position));
                    FragmentBoardInsert.insertBitmap.remove(position);
                    checknum--;
                }else{

                }


            }
        });
//        holder.album_one_picture.setSelected(isItemSelected(position));

        if(position == 0){
            ImageView big_thumbnail = rootView.findViewById(R.id.big_thumnail);
            big_thumbnail.setImageBitmap(mlist.get(0));
        }
        if(checknum == 0){
            FragmentBoardInsert.insertBitmap.add(mlist.get(0));
        }
    }
    private boolean isItemSelected(int position) {
        return mSelectedItems.get(position, false);
    }
    private void toggleItemSelected(int position) {

        if (mSelectedItems.get(position, false) == true) {
            mSelectedItems.delete(position);
            notifyItemChanged(position);
        } else {
            mSelectedItems.put(position, true);
            notifyItemChanged(position);
        }
    }



    @Override
    public int getItemCount() {
        File dir = new File(mBasePath);
        mImgs = dir.list();
        return mImgs.length;
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}
