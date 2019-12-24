package com.example.ptworld.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ptworld.Activity.Insert_Trainner;
import com.example.ptworld.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

public class Popup_SelectSettingProfileImage extends AppCompatActivity {
    Intent intent;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    private Uri mImageCaptureUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup__select_setting_profile_image);
    }

    public void capture(View view) {//카메라 촬영 후 이미지를 가져온다.
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        mImageCaptureUri = FileProvider.getUriForFile(getBaseContext(), "com.bignerdranch.android.test.fileprovider", new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    public void gallery(View view) {//앨범에서 선택한 이미지를 가져온다.
        intent = new Intent(Intent.ACTION_PICK);

        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;

        Log.i("onActivityResult","들어옴");
        Log.i("RESULT_CODE",resultCode+"");
        Log.i("REQUEST_CODE",requestCode+"");
        if(resultCode == RESULT_OK){
            Log.i("REQUEST_CODE", resultCode+"");
            switch (requestCode){
                case PICK_FROM_ALBUM:
                    mImageCaptureUri = data.getData();
                    Log.d("popup_SelectImage", mImageCaptureUri.getPath().toString());


                case PICK_FROM_CAMERA:
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(mImageCaptureUri, "image/*");
                    Log.d("popup_SelectImage", "카메라실행");
                    Uri uri = intent.getData();
//                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        Log.i("사진",bitmap.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View view = inflater.inflate(R.layout.activity_insert__trainner, null);
//
//                    ImageView profile_image = view.findViewById(R.id.profile_image);
//                    profile_image.setImageBitmap(bitmap);

                    if(bitmap != null){
                        Log.i("PICK_FROM_CAMERA_이미지",bitmap.toString());
                    }
                    intent.putExtra("outputX", 200);
                    intent.putExtra("outputY", 200);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("scale",true);
                    intent.putExtra("return-data", true);
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, CROP_FROM_iMAGE);
                    Log.i("PICK_FROM_CAMERA","끝");
                    break;

                case CROP_FROM_iMAGE:
                    Log.i("CROP_FROM_iMAGE","들어옴");
                    if(resultCode != RESULT_OK)
                        return;

                    final Bundle extras = data.getExtras();

                    if(extras != null){
                        Bitmap photo = extras.getParcelable("data");
                        Log.i("이미지 : ", photo.toString());
//                        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                        View view = inflater.inflate(R.layout.activity_insert__trainner, null);
//
//                        ImageView profile_image = view.findViewById(R.id.profile_image_trainner);
//                        profile_image.setImageBitmap(photo);
                        Insert_Trainner.profile_image.setImageBitmap(photo);
                    }
                    File f = new File(mImageCaptureUri.getPath());
                    if(f.exists())
                        f.delete();

                    finish();
                    break;
            }
        }
    }



}
