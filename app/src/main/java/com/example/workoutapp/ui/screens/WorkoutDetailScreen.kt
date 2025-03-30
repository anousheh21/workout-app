package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.ui.components.ExerciseLogSingle

@Composable
fun WorkoutDetailScreen(workout: String) {
    Column {
        ExerciseLogSingle(workout)
    }

}

@Preview(showBackground = true)
@Composable
fun WorkoutDetailScreenPreview() {
    WorkoutDetailScreen(workout = "Push Workout")
}