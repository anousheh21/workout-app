package com.example.workoutapp.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.workoutapp.data.Workout
import com.example.workoutapp.ui.theme.SeparatorGrey
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.WorkoutViewModel


@Composable
fun WorkoutsScreen(
    onClickWorkout: (WorkoutDetails) -> Unit,
    viewModel: WorkoutViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        //viewModel.clearAllWorkouts(context = context)
        viewModel.debugExercisesFor(context, 22)
        viewModel.seedDummyData(context = context)
        viewModel.loadWorkouts(context = context)
    }
    val workoutsArray = viewModel.workoutsArray

    LaunchedEffect(workoutsArray) {
        workoutsArray.forEach {
            Log.d("DEBUG", "WorkoutDetails => workoutId=${it.workoutId}, planId=${it.workoutPlanId}, date=${it.workoutDate}, name=${it.workoutName}")
        }
    }

    WorkoutColumnList(workoutsArray, onClickWorkout)



}

@Composable
fun WorkoutColumnList(
    workouts: List<WorkoutDetails>,
    onClickWorkout: (WorkoutDetails) -> Unit
    ) {
    LazyColumn {
        items(workouts) { workout ->
            WorkoutRow(workout, onClickWorkout)
            Divider(color = SeparatorGrey, thickness = 1.dp)
        }
    }
}

@Composable
fun WorkoutRow(workout: WorkoutDetails, onClickWorkout: (WorkoutDetails) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickWorkout(workout) }
            .padding(start = 32.dp)
            .padding(top = 18.dp)
            .padding(bottom = 22.dp)
    ) {

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                    append(workout.workoutName)
                }

                append("   -   ")
                append(workout.workoutDate)
            }
        )
    }
}