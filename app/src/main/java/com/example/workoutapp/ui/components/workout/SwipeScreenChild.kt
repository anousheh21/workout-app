package com.example.workoutapp.ui.components.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.data.Workout
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.extensions.toTitleCase
import com.example.workoutapp.ui.theme.SecondaryText

@Composable
fun SwipeScreenChild(
    exercise: PlannedExercise,
    index: Int,
    arraySize: Int,
    nextExercise: String,
    weightInput: String,
    onWeightValueChange: (String) -> Unit,
    repsInput: String,
    onRepsValueChange: (String) -> Unit,
    selectedWorkout: Workout,
    completedExercises: List<Exercise>,
    vm: WorkoutViewModel = viewModel()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(67.dp))
        Text(
            text = exercise.muscleGroup.toTitleCase(),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = exercise.exerciseName,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(55.dp))

        Row(

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CurrentExerciseStatsInput(
                    statsInput = weightInput,
                    onStatsValueChange = onWeightValueChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Weight/kg",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.width(43.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CurrentExerciseStatsInput(
                    statsInput = repsInput,
                    onStatsValueChange = onRepsValueChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Reps",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(56.dp))


        // Display all exercises, just as text for now
        var currentExercise = completedExercises.filter { it.plannedExerciseId == exercise.plannedExerciseId }

        val setsCompleted = currentExercise.size
        val setsPlanned = exercise.setNumber
        val canAddMoreSets = setsCompleted < setsPlanned


        NextSetButton(
            weightString = weightInput,
            repsString = repsInput,
            saveSet = { context, weightString: String, repsString: String ->
                val weight = weightString.toFloat()
                val reps = repsString.toInt()

                val newExerciseStats = Exercise(
                    workoutId = selectedWorkout.workoutId,
                    plannedExerciseId = exercise.plannedExerciseId,
                    weight = weight,
                    reps = reps,
                    pb = false
                )

                // Add the exercise to the database
                vm.insertExercise(context, newExerciseStats)

                // Load all exercises again to refresh the page
                vm.loadExercisesForWorkout(context, selectedWorkout.workoutId)

                // Clear input boxes
                onWeightValueChange("")
                onRepsValueChange("")
            },
            enabled = canAddMoreSets
        )

        Spacer(modifier = Modifier.height(37.dp))

        // Display all exercises, just as text for now
        currentExercise = completedExercises.filter { it.plannedExerciseId == exercise.plannedExerciseId }

        CompletedExerciseTable(exercises = currentExercise, plannedExercise = exercise)

        Spacer(modifier = Modifier.height(27.dp))

        Row() {
            Text(
                text = "Exercise: ${index + 1}/${arraySize}",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier = Modifier.width(110.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("Up Next: ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                        append(nextExercise)
                    }
                },
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(25.dp))


        Text(
            text = "Swipe right for next exercise, swipe left for previous exercise",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = SecondaryText,
                fontStyle = FontStyle.Italic
            )
        )

//        Spacer(modifier = Modifier.height(7.dp))
//
//        Text(
//            text = "Swipe down to see previous stats",
//            style = TextStyle(
//                fontSize = 12.sp,
//                fontWeight = FontWeight.Normal,
//                color = SecondaryText,
//                fontStyle = FontStyle.Italic
//            )
//        )

        Spacer(modifier = Modifier.height(26.dp))
    }
}