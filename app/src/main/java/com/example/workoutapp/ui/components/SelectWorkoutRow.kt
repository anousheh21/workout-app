package com.example.workoutapp.ui.components

// SelectWorkoutRow.kt shows a list of scheduled workouts, for when the user would like to begin a new workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.data.ScheduledWorkoutWithExercises


@Composable
fun SelectWorkoutRow(
    schedWorkout: ScheduledWorkoutWithExercises,
    startWorkout: () -> Unit,
) {
    Spacer(modifier = Modifier.height(11.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = schedWorkout.workoutName,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )
        // Button to start the workout
        StartWorkoutButton(startWorkout)
    }
    Spacer(modifier = Modifier.height(11.dp))
}