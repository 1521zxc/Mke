package com.example.epay.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.example.epay.receiver.ClickReceiver;
import com.example.epay.receiver.FalseClickReceiver;
import com.igexin.sdk.PushManager;

/**
 * Created by liujin on 2018/4/25.
 */

public class NotificationUtil {

    protected static NotificationManager notificationManager = null;

    protected static int notifyID = 0525; // start notification id
    protected static int foregroundNotifyID = 0555;

    public static void sendNotification(Context appContext, String message, String url, Intent intent, boolean isForeground) {
        Log.i("NotificationManager", "sendNotification: ");
        if( PushManager.getInstance().isPushTurnedOn(appContext)==false)
            return;

        if (notificationManager == null) {
            notificationManager = (NotificationManager) appContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }

        try {
            String notifyText = message;

            PackageManager packageManager = appContext.getPackageManager();
            String appname = (String) packageManager
                    .getApplicationLabel(appContext.getApplicationInfo());

            // notification titile
            String contentTitle = appname;
            String packageName = appContext.getApplicationInfo().packageName;

            Uri defaultSoundUrlUri = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // create and send notificaiton
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    appContext)
                    .setSmallIcon(appContext.getApplicationInfo().icon)
                    .setSound(defaultSoundUrlUri)
                    .setWhen(System.currentTimeMillis()).setAutoCancel(true);
            Intent msgIntent = null;
            if(url.equals("")) {
                msgIntent= appContext.getPackageManager()
                        .getLaunchIntentForPackage(packageName);
            }else{
                Uri uri = Uri.parse(url);
                msgIntent = new Intent(Intent.ACTION_VIEW, uri);
            }

            Intent clickIntent=new Intent(appContext, ClickReceiver.class);
            clickIntent.putExtra("realIntent",msgIntent);
            clickIntent.putExtra("msg",intent);

            Intent deleteNotiIntent=new Intent(appContext, FalseClickReceiver.class);
            deleteNotiIntent.putExtra("msg",intent);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(appContext,
                    notifyID, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent deleteIntent = PendingIntent.getBroadcast(appContext,
                    notifyID, deleteNotiIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentTitle(contentTitle);
            mBuilder.setTicker(notifyText);
            mBuilder.setContentText(notifyText);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setDeleteIntent(deleteIntent);
            Notification notification = mBuilder.build();
            notificationManager.notify(notifyID, notification);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
