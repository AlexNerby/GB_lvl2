package com.example.appweathergb.broadcast_receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.app.NotificationCompat;

import com.example.appweathergb.R;
import com.example.appweathergb.storage.Constants;

public class ConnectNetworkReceiver extends BroadcastReceiver {

    private int messageId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager conn =  (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        if (networkInfo == null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.CHANNEL_ID_NOTIF)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getResources().getString(R.string.atention))
                    .setContentText(context.getResources().getString(R.string.check_connection_network));
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(messageId++, builder.build());

        }
    }
}
