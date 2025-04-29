package com.example.workoutapp.ui.components.editschedule

// SelectedExercisesList.kt shows a list of selected planned exercises (to be shown in the scheduled workout addition panel, so users can see what exercises are in the workout before they save it)

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workoutapp.data.PlannedExercise

@Composable
fun SelectedExercisesList(
    selectedExercises: List<PlannedExercise>
) {
    Text(
        text = "Selected: ${selectedExercises.joinToString {it.exerciseName}}",
        modifier = Modifier
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp)
    )
}