package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.ScheduledWorkoutExercise
import com.example.workoutapp.data.ScheduledWorkoutWithExercises
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.theme.SeparatorGrey

@Composable
fun NewWorkoutScreen() {
    val scrollState = rememberScrollState()
    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.loadScheduledWorkoutsWithExercises(context)
    }

    val schedWorkouts = vm.scheduledWorkoutsWithExercises

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        schedWorkouts.forEach { item ->
            SelectWorkoutRow(schedWorkout = item)

            Divider(color = SeparatorGrey, thickness = 1.dp)
        }
    }
}

@Composable
fun SelectWorkoutRow(schedWorkout: ScheduledWorkoutWithExercises) {
    Text(text = schedWorkout.workoutName)
}