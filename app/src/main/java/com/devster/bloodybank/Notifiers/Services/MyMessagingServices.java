package com.devster.bloodybank.Notifiers.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.devster.bloodybank.R;
import com.devster.bloodybank.Views.Main.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by MOD on 7/2/2018.
 */

public class MyMessagingServices extends FirebaseMessagingService {
    private final String TAG="FCM Services ";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG,"From: "+remoteMessage.getFrom());
        Log.d(TAG,"Notification Meassaging Body: "+ remoteMessage.getNotification().getBody());

        if(remoteMessage.getNotification()!=null){
            String reqBlood=remoteMessage.getNotification().getBody();
            String title=remoteMessage.getNotification().getTitle();
            String contact=remoteMessage.getData().get("phoneNumberWCode");
            String click_action=remoteMessage.getNotification().getClickAction();
            sendNotification(reqBlood,title,contact);
            Log.d(TAG,"Title: " +title+" \nMessage: "+reqBlood+"\n Contact: "+contact);

        }

    }

    private void sendNotification(String reqBlood,String title,String contact) {

        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultASoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder bulider=new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title).
                setContentText(reqBlood)
                .setSound(defaultASoundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,bulider.build());

    }


}
