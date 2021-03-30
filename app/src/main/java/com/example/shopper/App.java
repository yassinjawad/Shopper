package com.example.shopper;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * This class create a Notification Channel for Shopper. Notification Channels become
 * necessary starting with Android Oreo (API 26) to be able to show
 */
public class App extends Application {

    // declare and initialize a channel Id
    public static final String CHANNEL_SHOPPER_ID = "channelshopper";

    // override the onCreate method
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    /**
     * This method that Notification Channel for Shopper
     */
    private void createNotificationChannel() {
        // check if Android Oreo (API 26) or higher because
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channelshopper = new NotificationChannel(
                    CHANNEL_SHOPPER_ID,
                    "Channel Shopper",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            channelshopper.setDescription("This is the Shopper Channel.");

            NotificationManager manager = getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channelshopper);
        }

    }
}
