package com.example.workoutapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.ui.WorkoutViewModel
import androidx.compose.ui.platform.LocalContext

@Composable
fun ExerciseLogSingle(workoutId: Int, vm: WorkoutViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect(workoutId) {
        vm.loadWorkoutById(context, workoutId)
    }

    val selectedWorkout = vm.selectedWorkout

    Column {
        Text(text = "$workoutId")
        Text(text = "Workout Date: ${selectedWorkout?.workoutDate}")
        Text(text = "Workout Name: ${vm.selectedWorkoutName}")
    }
}