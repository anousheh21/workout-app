package com.example.workoutapp.ui

// MainActivity.kt is the main activity for the app that instantiates it

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.workoutapp.R
import com.example.workoutapp.ui.screens.NavScaffold
import com.example.workoutapp.ui.theme.WorkoutAppTheme
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        // Sets alarms, which are needed for the notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }

        // Request notification permissions
        requestNotificationPermissionIfNeeded()

        // Set the initial app screen
        setContent {
            WorkoutAppTheme {
                NavScaffold()

            }
        }
    }

    // Function to request notification permissions if they are not enabled
    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    // Function to create the channel for the notifications
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Scheduled Workout Notifications"
            val descriptionText = "To remind you of your scheduled workouts, one hour before"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("scheduled-workout-notification", name, importance).apply {
                description = descriptionText
            }
            // Register the notification channel
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkoutAppTheme {
//        Greeting("Android")
    }
}