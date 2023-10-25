package com.example.l999;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notification = createNotification(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1245, notification);
        Log.d("NotificationReceiver", "onReceive called");

    }

    private Notification createNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1245")
                .setSmallIcon(R.drawable.b1)
                .setContentTitle("Antrenament apropiat!")
                .setContentText("Mai ai 30 de minute până la antrenament!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return builder.build();
    }
}
