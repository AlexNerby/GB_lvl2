package com.example.appweathergb.broadcast_receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.appweathergb.R;
import com.example.appweathergb.storage.Constants;

public class BatteryLevelReceiver extends BroadcastReceiver {

    private int messageId=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isBatteryLow = intent.getAction().equals(Intent.ACTION_BATTERY_LOW);

        if (isBatteryLow) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.CHANNEL_ID_NOTIF)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getResources().getString(R.string.atention))
                    .setContentText(context.getResources().getString(R.string.battery_low));
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(messageId++, builder.build());

            Toast.makeText(
                    context,
                    "Системая нотификация о заряде перекрывает уведомление от приложения",
                    Toast.LENGTH_LONG).show();
        }
    }
}