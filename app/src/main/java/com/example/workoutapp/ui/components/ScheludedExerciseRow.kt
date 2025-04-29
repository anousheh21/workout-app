package com.example.workoutapp.ui.components

// ScheduledExerciseRow.kt displays a row for a scheduled exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.ui.extensions.toTitleCase

@Composable
fun ScheduledExerciseRow(
    exercise: PlannedExercise,
    index: Int
) {
    Spacer(modifier = Modifier.height(26.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 38.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Displays the exercise itself, alongside the number order that exercise appears in the scheduled workout
        Text(
            text = "${index + 1} - ${exercise.exerciseName}",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )

        // Displays the muscle group and set number for the exercise
        Text(
            text = "${exercise.muscleGroup.toTitleCase()} - ${exercise.setNumber} Sets"
        )

    }
    Spacer(modifier = Modifier.height(26.dp))
}