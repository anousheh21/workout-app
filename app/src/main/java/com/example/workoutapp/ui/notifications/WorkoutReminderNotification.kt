package com.example.workoutapp.ui.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.workoutapp.ui.MainActivity

const val CHANNEL_ID = "scheduled-workout-notification"
const val NOTIFICATION_ID = 1

fun showScheduledWorkoutNotification(context: Context) {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

// @SuppressLint("StaticFieldLeak")
    var builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle("Workout Reminder")
        .setContentText("This is here to remind you about your workout")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)


    with(NotificationManagerCompat.from(context)) {
        if (androidx.core.app.ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {


            return
        }
        notify(NOTIFICATION_ID, builder.build())
    }
}

