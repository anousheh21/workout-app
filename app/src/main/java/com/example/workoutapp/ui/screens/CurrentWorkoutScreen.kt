package com.example.workoutapp.ui.screens

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.data.Workout
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

    val listToUse = scheduledExerciseArray.flatten()

    if (listToUse.isNotEmpty()) {
        ExerciseSwipeScreen(listToUse)
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

@Composable
fun ExerciseSwipeScreen(
    scheduledExerciseArray: List<PlannedExercise>
) {
    var currentIndex by remember { mutableStateOf(0) }
    var accumulatedDrag by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (accumulatedDrag > 100) {
                            if (currentIndex > 0) currentIndex--
                        } else if (accumulatedDrag < -100) {
                            if (currentIndex < scheduledExerciseArray.lastIndex) currentIndex++
                        }
                        accumulatedDrag = 0f // reset for next gesture
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        accumulatedDrag += dragAmount
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        val safeIndex = currentIndex.coerceIn(0, scheduledExerciseArray.lastIndex)
        Text(
            text = scheduledExerciseArray[safeIndex].exerciseName,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TestingDisplay(
    workoutId: Int,
    selectedWorkoutName: String,
    selectedWorkout: Workout,
    scheduledExerciseArray: List<List<PlannedExercise>>
    ) {

    Column() {
        Text("$workoutId")
        Text(selectedWorkoutName)
        Text("$selectedWorkout")
        Text("${selectedWorkout.workoutPlanId}")
        Text("$scheduledExerciseArray")
    }
}