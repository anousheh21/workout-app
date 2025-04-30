package com.example.workoutapp.ui.notifications

// WorkoutReminderNotifications contains the code to schedule notifications, and to deduce what to display on them

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.workoutapp.ui.MainActivity
import java.util.Calendar

const val CHANNEL_ID = "scheduled-workout-notification"
const val NOTIFICATION_ID = 1

@SuppressLint("ScheduleExactAlarm")
fun scheduleWorkoutNotification(
    context: Context,
    dayInt: Int,
    timeString: String,
    workoutName: String,
    workoutDay: String
) {


    // Intent that will be received when the alarm for the notification pings
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("dayInt", dayInt)
        putExtra("timeString", timeString)
        putExtra("workoutName", workoutName)
        putExtra("workoutDay", workoutDay)
    }

    // Creates a unique ID
    val requestId = (workoutName + timeString + workoutDay).hashCode()

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestId,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Split the time into hour and minute
    val (hour, minute) = timeString.split(":").map { it.toInt() }

    val now = Calendar.getInstance()

    // Sets the notification to send 1 hour before the workout
    val calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, dayInt)
        set(Calendar.HOUR_OF_DAY, hour - 1)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

    }

    // If the notification is before the current time, move it to next week to prevent it firing straight away
    if (calendar.timeInMillis <= now.timeInMillis) {
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
    }




    // Sets alarm via alarm manager
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
    Log.d("WorkoutViewModel", "Alarm set for: ${calendar.time}")
}

// Function to show the notification
@SuppressLint("ScheduleExactAlarm")
fun showScheduledWorkoutNotification(
    context: Context,
    dayInt: Int,
    timeString: String,
    workoutName: String,
    workoutDay: String
) {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val requestId = (workoutName + timeString + workoutDay).hashCode()
    val pendingIntent = PendingIntent.getActivity(
        context,
        requestId,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Builds the content of the notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle("Workout Scheduled in 1 Hour")
        .setContentText("You have a $workoutName workout at $timeString on $workoutDay")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    // uses permissions for the notification
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notify(NOTIFICATION_ID, builder.build())
    }
}

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val dayInt = intent?.getIntExtra("dayInt", 2) ?: 2
            val time = intent?.getStringExtra("timeString") ?: "09:00"
            val workoutName = intent?.getStringExtra("workoutName") ?: "Unnamed"
            val workoutDay = intent?.getStringExtra("workoutDay") ?: "Monday"
            showScheduledWorkoutNotification(it, dayInt, time, workoutName, workoutDay)


        }
    }
}