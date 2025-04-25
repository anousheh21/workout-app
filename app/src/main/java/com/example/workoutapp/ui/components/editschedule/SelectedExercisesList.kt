package com.example.workoutapp.ui.components.editschedule

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