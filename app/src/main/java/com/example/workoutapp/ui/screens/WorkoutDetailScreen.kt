package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.Workout
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.ExerciseLogSingle

@Composable
fun WorkoutDetailScreen(workoutId: Int, viewModel: WorkoutViewModel = viewModel()) {
    val context = LocalContext.current
    // val workout by remember { mutableStateOf<Workout?>(null) }

    LaunchedEffect(workoutId) {
        viewModel.loadWorkoutById(context, workoutId)
    }

    val selectedWorkout = viewModel.selectedWorkout

    Column {
        if (selectedWorkout != null) {
            Text("Workout: ${selectedWorkout.workoutDate}")

            // If you want to show workout name:
            Text("Name: ${viewModel.selectedWorkoutName}")

            ExerciseLogSingle(selectedWorkout.workoutId)
        } else {
            Text("Loading workout...")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun WorkoutDetailScreenPreview() {
    WorkoutDetailScreen(workoutId = 123)
}