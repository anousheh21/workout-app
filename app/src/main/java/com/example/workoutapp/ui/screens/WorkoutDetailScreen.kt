package com.example.workoutapp.ui.screens

// WorkoutDetailScreen.kt is a screen that displays the information about a particular workout that has been selected by the user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.Workout
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.ExerciseLogSingle

@Composable
fun WorkoutDetailScreen(workoutId: Int) {
    // Variable to remember the scroll state
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        // Show the workout details
        ExerciseLogSingle(workoutId)
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutDetailScreenPreview() {
    WorkoutDetailScreen(workoutId = 123)
}