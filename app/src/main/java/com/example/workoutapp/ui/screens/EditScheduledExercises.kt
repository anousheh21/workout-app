package com.example.workoutapp.ui.screens

// EditScheduledExercises.kt is a screen that displays the scheduled exercises for the scheduled workouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.ScheduledExerciseRow
import com.example.workoutapp.ui.components.editschedule.AddNewWorkoutScheduleEdit
import com.example.workoutapp.ui.components.editschedule.singleWorkout.SingleExerciseScheduleEdit
import com.example.workoutapp.ui.extensions.toTitleCase
import com.example.workoutapp.ui.theme.SeparatorGrey

@Composable
fun EditScheduledExercises(workoutPlanId: Int) {
    val scrollState = rememberScrollState()

    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // Load in workouts with exercises from the database via the ViewModel
        vm.loadScheduledWorkoutsWithExercises(context)
    }

    val workoutsWithExercises = vm.scheduledWorkoutsWithExercises

    // Filter for exercises that match the workout plan ID
    val relevantExercises = workoutsWithExercises
        .filter { it.workoutPlanId == workoutPlanId }
        .map { it.workoutExercises }
        .flatten()

    Column() {
        // Display the exercises in a scheduled exercise row
        relevantExercises.forEachIndexed() { index, item ->
            ScheduledExerciseRow(
                exercise = item,
                index = index,
            )

            Divider(
                color = SeparatorGrey,
                thickness = 1.dp,
            )
        }
    }
}

