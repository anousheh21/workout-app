package com.example.workoutapp.ui.screens

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    // Load the workout, from the workout ID
    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        vm.loadWorkoutById(context, workoutId)
        vm.loadScheduledWorkoutsWithExercises(context)

        // Load all relevant
        vm.loadExercisesForWorkout(context, workoutId)
    }

    val selectedWorkout = vm.selectedWorkout
    val selectedWorkoutName = vm.selectedWorkoutName

    val completedExercises = vm.exercisesForWorkoutArray

    // Get array of exercises associated with the workout plan that the workout is associated with
    val schedWorkoutWithExercises = vm.scheduledWorkoutsWithExercises
    val scheduledExerciseArray = schedWorkoutWithExercises
        .filter {
            it.workoutPlanId == (selectedWorkout?.workoutPlanId ?: -1)
        }
        .map { it.workoutExercises }

    val listToUse = scheduledExerciseArray.flatten()

    if (listToUse.isNotEmpty()) {
        if (selectedWorkout != null) {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                ExerciseSwipeScreen(listToUse, selectedWorkout, completedExercises)
            }
        }
    } else {
        Text("No exercises found for this workout.")
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











