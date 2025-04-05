package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.editschedule.AddNewWorkoutScheduleEdit
import com.example.workoutapp.ui.components.editschedule.singleWorkout.SingleExerciseScheduleEdit

@Composable
fun EditScheduledExercises(workoutPlanId: Int) {
    val scrollState = rememberScrollState()

    // Load in relevant exercises
    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.loadScheduledWorkoutsWithExercises(context)
    }

    val workoutsWithExercises = vm.scheduledWorkoutsWithExercises
    val relevantExercises = workoutsWithExercises
        .filter { it.workoutPlanId == workoutPlanId }
        .map { it.workoutExercises }
        .flatten()

    Column() {
        relevantExercises.forEach { item ->
            Text("$item")
        }
    }


}