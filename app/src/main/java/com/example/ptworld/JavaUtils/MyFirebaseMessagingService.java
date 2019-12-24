package com.example.ptworld.JavaUtils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.ptworld.Activity.MainDrawer;
import com.example.ptworld.Activity.Message;
import com.example.ptworld.DTO.TrainnerInfo;
import com.example.ptworld.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "서비스_퐈이어베이스메시지";
    String IP_ADDRESS = "squart300kg.cafe24.com";
    String targetNickname = TrainnerInfo.nickname;
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        //생성된 앱 토큰을 서버로 전송
        sendRegistrationToServer(token);
    }
    private void sendRegistrationToServer(String token) {
        //서버로 전송할 코드를 작성
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom()+"1");

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());


            if ( true) {

            } else {
                handleNow();
            }

        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            if(remoteMessage.getNotification().getTitle().equals("게시물 좋아요")){
                sendNotification_Like(remoteMessage.getNotification().getBody(), remoteMessage.getData().get("boardno"));
            } else if (remoteMessage.getNotification().getTitle().equals("팔로우 요청")){
                sendNotification_UserProfile(remoteMessage.getNotification().getBody());
            } else if (remoteMessage.getNotification().getTitle().equals("댓글")) {
                sendNotification_Reply(remoteMessage.getNotification().getBody(), remoteMessage.getData().get("boardno"), remoteMessage.getData().get("replyno"));
            } else if (remoteMessage.getNotification().getTitle().equals("대댓글")){
                sendNotification_ReReply(remoteMessage.getNotification().getBody(), remoteMessage.getData().get("boardno"), remoteMessage.getData().get("replyno"), remoteMessage.getData().get("rereplyno"));
            } else if (remoteMessage.getNotification().getTitle().equals("채팅")){
                sendNotification_Chatting(remoteMessage.getNotification().getBody(), remoteMessage.getData().get("receiveYourNickname"));
            }
//            remoteMessage.getNotification().get


        }
    }

    private void sendNotification_Chatting(String messageBody, String receiveYourNickname) {
//        String nickname[] = messageBody.split("님이 회원님");


        Intent intent = new Intent(this, Message.class);
//        intent.putExtra("receiveYourNickname", receiveYourNickname);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
//        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name",NotificationManager.IMPORTANCE_DEFAULT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("채팅채팅")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());

        //노티피케이션을 전송했으면 이제 DB에 넣어준다.
//        new Thread_Insert_Noti().execute("http://"+IP_ADDRESS+"/user_signup/insert_noti.php", messageBody, nickname[0], targetNickname, TYPE);
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }
    //대댓글 노티피케이션
    private void sendNotification_ReReply(String messageBody, String boardno, String replyno, String rereplyno) {
        String nickname[] = messageBody.split("님이 회원님");
        String TYPE = "rereply";

        Intent intent = new Intent(this, MainDrawer.class);
        intent.putExtra("boardno", boardno);
        intent.putExtra("replyno", replyno);
        intent.putExtra("rereplyno", rereplyno);


        intent.putExtra("notification","notification_ReReply");
        intent.putExtra("nickname",nickname[0]);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
//        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name",NotificationManager.IMPORTANCE_DEFAULT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("대댓글")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());

        //노티피케이션을 전송했으면 이제 DB에 넣어준다.
        new Thread_Insert_Noti().execute("http://"+IP_ADDRESS+"/user_signup/insert_noti.php", messageBody, nickname[0], targetNickname, TYPE);
    }

    //댓글 노티피케이션
    private void sendNotification_Reply(String messageBody, String boardno, String replyno) {
        String TYPE = "reply";
        String nickname[] = messageBody.split("님이 회원님");

        Intent intent = new Intent(this, MainDrawer.class);
        intent.putExtra("boardno", boardno);
        intent.putExtra("replyno", replyno);
        Log.i("댓글전송 노티 서비스에서 boardno",boardno);
        Log.i("댓글전송 노티 서비스에서 replyno",replyno);
        intent.putExtra("notification","notification_Reply");
        intent.putExtra("nickname",nickname[0]);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
//        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name",NotificationManager.IMPORTANCE_DEFAULT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("댓글")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());

        //노티피케이션을 전송했으면 이제 DB에 넣어준다.
        new Thread_Insert_Noti().execute("http://"+IP_ADDRESS+"/user_signup/insert_noti.php", messageBody, nickname[0], targetNickname, TYPE);

    }
    private void sendNotification_UserProfile(String messageBody) {
        String TYPE = "follow";
        String nickname[] = messageBody.split("님이 회원님께");

        Intent intent = new Intent(this, MainDrawer.class);

        intent.putExtra("notification","notification_UserProfile");
        intent.putExtra("nickname",nickname[0]);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
//        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name",NotificationManager.IMPORTANCE_DEFAULT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("팔로우 요청")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());

        //노티피케이션을 전송했으면 이제 DB에 넣어준다.
        new Thread_Insert_Noti().execute("http://"+IP_ADDRESS+"/user_signup/insert_noti.php", messageBody, nickname[0], targetNickname, TYPE);
    }
    private void sendNotification_Like(String messageBody, String boardno) {
        String TYPE = "like";
        String nickname[] = messageBody.split("님이 회원님");

        Intent intent = new Intent(this, MainDrawer.class);
        intent.putExtra("boardno",boardno);

        Log.i("좋아요 노티 전달받은 boardno", boardno);
        intent.putExtra("notification","notification_Like");
        intent.putExtra("nickname",nickname[0]);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
//        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name",NotificationManager.IMPORTANCE_DEFAULT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("게시물 좋아요")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());

        //노티피케이션을 전송했으면 이제 DB에 넣어준다.
        new Thread_Insert_Noti().execute("http://"+IP_ADDRESS+"/user_signup/insert_noti.php", messageBody, nickname[0], targetNickname, TYPE);
    }

    class Thread_Insert_Noti extends AsyncTask<String, Void, String> {
        String IP_ADDRESS = "squart300kg.cafe24.com";



        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String contents = params[1];
            String nickname = params[2];
            String targetNickname = params[3];
            String TYPE = params[4];

            String postParameters = "contents="+contents+"&nickname="+nickname+"&TYPE="+TYPE+"&targetNickname="+targetNickname ;

            Log.i("Thread_Insert_Noti",postParameters);

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
                Log.d("Thread_Insert_Noti", "POST response code - " + responseStatusCode);

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

                Log.d("Thread_Insert_Noti", "Thread_Like: Error ");

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("Thread_Insert_Noti결과",result);
        }
    }
}
