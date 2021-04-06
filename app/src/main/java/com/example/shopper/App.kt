package com.example.shopper

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

/**
 * This class create a Notification Channel for Shopper. Notification Channels become
 * necessary starting with Android Oreo (API 26) to be able to show
 */
class App : Application() {
    // override the onCreate method
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    /**
     * This method that Notification Channel for Shopper
     */
    private fun createNotificationChannel() {
        // check if Android Oreo (API 26) or higher because
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelshopper = NotificationChannel(
                    CHANNEL_SHOPPER_ID,
                    "Channel Shopper",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            channelshopper.description = "This is the Shopper Channel."
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channelshopper)
        }
    }

    companion object {
        // declare and initialize a channel Id
        const val CHANNEL_SHOPPER_ID = "channelshopper"
    }
}