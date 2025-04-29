package com.example.workoutapp.ui.components.workout

// ExerciseSwipeScreen.kt uses custom touch gestures to move between the exercises of the current workout

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.data.Workout

@Composable
fun ExerciseSwipeScreen(
    scheduledExerciseArray: List<PlannedExercise>,
    selectedWorkout: Workout,
    completedExercises: List<Exercise>
) {
    // Variables for the custom touch gestures
    var currentIndex by remember { mutableStateOf(0) }
    var accumulatedDrag by remember { mutableStateOf(0f) }
    val arrayLength = scheduledExerciseArray.size

    // Variables fer the weight and reps input for a current exerise
    var weightInput by remember { mutableStateOf("") }
    var repsInput by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // code for the custom touch gestures (left and right swipe)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (accumulatedDrag > 100) {
                            if (currentIndex > 0) currentIndex--
                        } else if (accumulatedDrag < -100) {
                            if (currentIndex < scheduledExerciseArray.lastIndex) currentIndex++
                        }
                        accumulatedDrag = 0f
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        accumulatedDrag += dragAmount
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        val safeIndex = currentIndex.coerceIn(0, scheduledExerciseArray.lastIndex)

        // moves to the next exercise
        val nextExercise: String = if (safeIndex < arrayLength - 1) {
            scheduledExerciseArray[safeIndex + 1].exerciseName
        } else {
            "End"
        }

        // Holds the child content of the swipe screen
        SwipeScreenChild(
            scheduledExerciseArray[safeIndex],
            safeIndex,
            arrayLength,
            nextExercise,
            weightInput = weightInput,
            onWeightValueChange = { weightInput = it },
            repsInput = repsInput,
            onRepsValueChange = { repsInput = it },
            selectedWorkout = selectedWorkout,
            completedExercises = completedExercises
        )
    }
}
