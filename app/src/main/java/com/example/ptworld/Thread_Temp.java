package com.example.ptworld;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Thread_Temp extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String server_url = params[0];
        String subject = params[1];
        String thumbnail_url = params[2];
        String contents_url = params[3];
        String type = params[4];

        Log.i("Thread_Temp","들어왔다!");

        String postParameters = "subject=" + subject + "&thumbnail_url=" + thumbnail_url + "&contents_url=" + contents_url + "&type=" + type;

        Log.i("Temp에서 받은 파라미터", postParameters);
        try {

            URL url = new URL(server_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d("Thread_TempSucess", "POST response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }


            bufferedReader.close();


            return sb.toString();


        } catch (Exception e) {

            Log.d("THread_LoginSucess", "THread_LoginSucess: Error ");

            return new String("Error: " + e.getMessage());
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(s.equals("dataInsert success")){
            Log.i("Thread_Temp","데이터 입력 성공");
            Log.i("Thread_Temp",s);

        }else{
            Log.i("Thread_Temp","데이터 입력 실패");
            Log.i("Thread_Temp",s);
        }
    }
}
