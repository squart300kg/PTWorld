package com.example.ptworld.Thread;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ptworld.Adapter.AdapterSNS;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class Thread_Like extends AsyncTask<String, Void, String> {
    String IP_ADDRESS = "squart300kg.cafe24.com";
    int position = 0;
    public Thread_Like(int position){
        this.position = position;
    }
    @Override
    protected String doInBackground(String... params) {
        String serverURL = params[0];
        String type = params[1];
        String no = params[2];
        String email = params[3];
        String nickname = params[4];

        String postParameters = "type=" + type + "&no="+no + "&email=" + email + "&nickname=" + nickname;
        Log.i("Thread_Like서버 파라미터",postParameters);

        try {

            URL url = new URL(serverURL);
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
            Log.d("Thread_Like", "POST response code - " + responseStatusCode);

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

            Log.d("Thread_Like", "Thread_Like: Error ");

            return new String("Error: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.i("좋아요 현황 외부클래스",result);
        AdapterSNS.mList.get(position).좋아요 = Integer.parseInt(result);

    }
}