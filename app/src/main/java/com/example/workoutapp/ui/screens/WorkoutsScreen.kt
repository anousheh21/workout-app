package com.example.workoutapp.ui.screens

// WorkoutsScreen.kt is a screen that displays a list of all workouts that the user has completed
// This screen can be navigated to from the bottom bar, and is the first screen the user lands on when they open the app

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.data.Workout
import com.example.workoutapp.ui.theme.SeparatorGrey
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.InstructionText
import com.example.workoutapp.ui.components.NoWorkoutScreen
import com.example.workoutapp.ui.components.WorkoutColumnList
import com.example.workoutapp.ui.components.WorkoutRow


@Composable
fun WorkoutsScreen(
    onClickWorkout: (WorkoutDetails) -> Unit,
    viewModel: WorkoutViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
       // Load workouts from the database via the ViewModel
        viewModel.loadWorkouts(context = context)
    }

    val initialWorkoutsArray = viewModel.workoutsArray
    // Sort the workouts chronologically, showing the most recent workout at the top
    val workoutsArray = initialWorkoutsArray.sortedByDescending { it.workoutId }

    LaunchedEffect(workoutsArray) {
        // Code used for debugging
        workoutsArray.forEach {
            Log.d("DEBUG", "WorkoutDetails => workoutId=${it.workoutId}, planId=${it.workoutPlanId}, date=${it.workoutDate}, name=${it.workoutName}")
        }
    }

    // Checks if the user has added workouts to the database, and either shows those workouts or instructions on how to use the app, depending on this
    if (workoutsArray.isEmpty()) {
        NoWorkoutScreen()
    } else {
        WorkoutColumnList(workoutsArray, onClickWorkout)
    }
}



