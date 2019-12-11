package com.example.ptworld;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class AlbumAdapter extends BaseAdapter {
    int CustomGalleryItemBg; // 앞서 정의해 둔 attrs.xml의 resource를 background로 받아올 변수 선언
    String mBasePath; // CustomGalleryAdapter를 선언할 때 지정 경로를 받아오기 위한 변수
    Context mContext; // CustomGalleryAdapter를 선언할 때 해당 activity의 context를 받아오기 위한 context 변수
    String[] mImgs; // 위 mBasePath내의 file list를 String 배열로 저장받을 변수
    Bitmap bm; // 지정 경로의 사진을 Bitmap으로 받아오기 위한 변수
    Bitmap bm2;
    ArrayList<Bitmap> mlist = new ArrayList<>();
    View rootView;
    public String TAG = "Gallery Adapter Example :: ";

    public AlbumAdapter(Context context, String basepath, View view){ // CustomGalleryAdapter의 생성자
        this.mContext = context;
        this.mBasePath = basepath;
        this.rootView = view;

        File file = new File(mBasePath); // 지정 경로의 directory를 File 변수로 받아
        if(!file.exists()){
            if(!file.mkdirs()){
                Log.d(TAG, "failed to create directory");
            }
        }
        mImgs = file.list(); // file.list() method를 통해 directory 내 file 명들을 String[] 에 저장
        Log.i("file_List0번째 인덱스", mImgs[0]);
        Log.i("file_List1번째 인덱스", mImgs[1]);
        Log.i("file_List2번째 인덱스", mImgs[2]);

//        mImgs = new HashSet<String>(Arrays.asList(mImgs)).toArray(new String[0]);

        Log.i("앨범 리스트 초기화 갯수adapter",mImgs.length+"");
        /* 앞서 정의한 attrs.xml에서 gallery array의 배경 style attribute를 받아옴 */
        TypedArray array = mContext.obtainStyledAttributes(R.styleable.GalleryTheme);
        CustomGalleryItemBg = array.getResourceId(R.styleable.GalleryTheme_android_galleryItemBackground, 0);
        array.recycle();
    }


    @Override
    public int getCount() { // Gallery array의 객체 갯수를 앞서 세어 둔 file.list()를 받은 String[]의 원소 갯수와 동일하다는 가정 하에 반환
        return mImgs.length;
//        return 20;
    }


    @Override
    public Object getItem(int position) { // Gallery array의 해당 position을 반환
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) { // Gallery array의 해당 position을 long 값으로 반환
        return position;
    }

    static class ViewHolder{
        ImageView i;
    }

    // Override this method according to your need
    // 지정 경로 내 사진들을 보여주는 method.
    // Bitmap을 사용할 경우, memory 사용량이 커서 Thumbnail을 사용하거나 크기를 줄일 필요가 있음
    // setImageDrawable()이나 setImageURI() 등의 method로 대체 가능
    @Override
    public View getView(final int index, View view, ViewGroup viewGroup)
    {

        final ImageView i = new ImageView(mContext); // Gallery array에 들어갈 ImageView 생성

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


        bm = BitmapFactory.decodeFile(mBasePath+ File.separator +mImgs[index], options);
        Log.i("bBasePath","========================"+mBasePath);
        Log.i("File.separator","========================"+File.separator);
        Log.i("mImgs[index]","========================"+mImgs[index]);
        Log.i("options","========================"+options+"");
        Log.i("index","========================"+index+"");

        bm2 = ThumbnailUtils.extractThumbnail(bm, 300, 300); // 크기가 큰 원본에서 image를 300*300 thumnail을 추출.
        // 이를 통해 view 가 까맣게 보이는 null 값을 피할 수 있었다.
        i.setLayoutParams(new Gallery.LayoutParams(300, 300));
//        i.setImageBitmap(bm2);
        mlist.add(bm2);
        i.setImageBitmap(mlist.get(index));
        i.setVisibility(ImageView.VISIBLE);

        i.setBackgroundResource(CustomGalleryItemBg); // Gallery 원소의 BackGround를 생성자에서 지정한 값으로 지정
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index1 = index;
                Toast.makeText(mContext, index1+"번쨰 클릭", Toast.LENGTH_LONG).show();
                Log.i("앨범 클릭 비트맵인덱스", index1+"번쨰 클릭"+"|||||||||||");
                Log.i("앨범 클릭 비트맵총갯수", mlist.size()+"개"+"|||||||||||");
                Log.i("앨범 클릭 비트맵", mlist.get(index1)+"|||||||||||");
                ImageView big_thumbnail = rootView.findViewById(R.id.big_thumnail);
                big_thumbnail.setImageBitmap(null);
                big_thumbnail.setImageBitmap((Bitmap)mlist.get(index1));
            }
        });
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
        return i;

    }


}
