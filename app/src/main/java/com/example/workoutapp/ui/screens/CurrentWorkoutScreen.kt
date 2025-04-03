package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.ui.WorkoutViewModel

@Composable
fun CurrentWorkoutScreen(workoutId: Int) {
    // Load the workout, from the workout ID
    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.loadWorkoutById(context, workoutId)
        vm.loadScheduledWorkoutsWithExercises(context)
    }

    val selectedWorkout = vm.selectedWorkout
    val selectedWorkoutName = vm.selectedWorkoutName

    // Get array of exercises associated with the workout plan that the workout is associated with
    val schedWorkoutWithExercises = vm.scheduledWorkoutsWithExercises
    val scheduledExerciseArray = schedWorkoutWithExercises
        .filter {
            it.workoutPlanId == (selectedWorkout?.workoutPlanId ?: -1)
        }
        .map { it.workoutExercises }

    Column() {
        Text("$workoutId")
        Text(selectedWorkoutName)
        Text("$selectedWorkout")
        if (selectedWorkout != null) {
            Text("${selectedWorkout.workoutPlanId}")
        }
        Text("$scheduledExerciseArray")
    }


}