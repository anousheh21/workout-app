package com.example.workoutapp.ui.components.editschedule

// AddToCalendarButton.kt contains a button that allows the user to add the scheduled workout to their calendar

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.workoutapp.R
import com.example.workoutapp.ui.theme.PrimaryColor

@Composable
fun AddToCalendarButton() {
    Button(
        onClick = { addWorkoutToCalendar() },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
        contentPadding = PaddingValues(10.dp),
        modifier = Modifier
            .size(47.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = "Calendar",
        )
    }
}

fun addWorkoutToCalendar() {
    Log.d("PLACEHOLDER", "This will add the workout to the calendar")
}