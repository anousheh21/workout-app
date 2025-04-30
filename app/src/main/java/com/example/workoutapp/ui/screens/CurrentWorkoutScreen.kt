package com.example.workoutapp.ui.screens

// CurrentWorkoutScreen.kt is the screen that shows the current workout being carried out and logged by the user

import android.content.Context
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.R
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.data.Workout
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.workout.ExerciseSwipeScreen
import com.example.workoutapp.ui.components.workout.SwipeScreenChild
import com.example.workoutapp.ui.components.workout.TableCell
import com.example.workoutapp.ui.extensions.toTitleCase
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.SecondPurple
import com.example.workoutapp.ui.theme.ThirdPurple

@Composable
fun CurrentWorkoutScreen(workoutId: Int) {
    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    // Scroll state, so that the screen will scrolle
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        // Load the workout by it's ID
        vm.loadWorkoutById(context, workoutId)
        vm.loadScheduledWorkoutsWithExercises(context)

        // Load all exercises for the workout that have the matching workout ID
        vm.loadExercisesForWorkout(context, workoutId)
    }

    // Assign variables via the ViewModel
    val selectedWorkout = vm.selectedWorkout
    val selectedWorkoutName = vm.selectedWorkoutName

    val completedExercises = vm.exercisesForWorkoutArray


    val schedWorkoutWithExercises = vm.scheduledWorkoutsWithExercises

    // Filter scheduled exercises for the ones that match the current workout plan
    val scheduledExerciseArray = schedWorkoutWithExercises
        .filter {
            it.workoutPlanId == (selectedWorkout?.workoutPlanId ?: -1)
        }
        .map { it.workoutExercises }

    val listToUse = scheduledExerciseArray.flatten()

    // If there are exercises, then pass them to the ExerciseSwipeScreen to be used
    // If there are no exercises, provide a message to the user saying this
    if (listToUse.isNotEmpty()) {
        if (selectedWorkout != null) {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                ExerciseSwipeScreen(listToUse, selectedWorkout, completedExercises)
            }
        }
    } else {
        Text(stringResource(R.string.no_exercises_found_for_this_workout))
    }

//    if (selectedWorkout != null) {
//        TestingDisplay(
//            workoutId,
//            selectedWorkoutName,
//            selectedWorkout,
//            scheduledExerciseArray
//        )
//    }

}











