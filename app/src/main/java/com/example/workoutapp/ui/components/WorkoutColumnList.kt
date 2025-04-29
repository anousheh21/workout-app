package com.example.workoutapp.ui.components

// WorkoutColumnList.kt shows a list of workouts, and calls WorkoutRow to display them all

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.theme.SeparatorGrey

@Composable
fun WorkoutColumnList(
    workouts: List<WorkoutDetails>,
    onClickWorkout: (WorkoutDetails) -> Unit,
) {
    LazyColumn {
        items(workouts) { workout ->
            WorkoutRow(workout, onClickWorkout)
            Divider(color = SeparatorGrey, thickness = 1.dp)
        }
    }
}