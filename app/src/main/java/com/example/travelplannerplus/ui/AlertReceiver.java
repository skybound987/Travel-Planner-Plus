package com.example.travelplannerplus.ui;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.travelplannerplus.R;

public class AlertReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "destination_alerts";
    public static final String DESTINATION_TITLE = "destination_title";
    public static final String DATE_TYPE = "date_type";  // Refers to Start or End Date
    public static final String ACTIVITY_TITLE = "activity_title";

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        String destinationAlertTitle = intent.getStringExtra(DESTINATION_TITLE);
        String destinationAlertDate = intent.getStringExtra(DATE_TYPE);  // Start Date or End Date
        String activityTitle = intent.getStringExtra(ACTIVITY_TITLE);

        String alertMessage = "";
        String notificationTitle = "Destination Alert:";

        if ("START".equals(destinationAlertDate)) {
            alertMessage = destinationAlertTitle + " is starting now!";

        } else if ("END".equals(destinationAlertDate)) {
            alertMessage = destinationAlertTitle + " is ending now!";

        } else {
            alertMessage = activityTitle + " is starting today!";
            notificationTitle = "Activity Alert:";
        }

        Toast.makeText(context, alertMessage, Toast.LENGTH_LONG).show();  //  Duplicates alert message as a toast on the same screen

        Notification n = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notificationTitle)
                .setContentText(alertMessage)
                .setAutoCancel(true)
                .build();

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int) System.currentTimeMillis(), n);
    }

    private void createNotificationChannel(Context context) {
        CharSequence title = "Destination Alerts";
        String description = "Alerts for Destination Dates";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel nc = new NotificationChannel(CHANNEL_ID, title, importance);
        nc.setDescription(description);
        NotificationManager nm = context.getSystemService(NotificationManager.class);
        nm.createNotificationChannel(nc);
    }
}
