package com.example.workoutapp.ui.components.workout

// SwipeScreenChild.kt shows the content for the current workout, this is what is being swiped through on the exercise swipe screen

import android.widget.Toast
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
import androidx.compose.ui.res.stringResource
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
import com.example.workoutapp.R
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
                    text = stringResource(R.string.weight_kg),
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
                    text = stringResource(R.string.reps),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(56.dp))


        // Filters for the current exercise
        var currentExercise = completedExercises.filter { it.plannedExerciseId == exercise.plannedExerciseId }

        // Sets variables
        val setsCompleted = currentExercise.size
        val setsPlanned = exercise.setNumber
        val canAddMoreSets = setsCompleted < setsPlanned

        // button to move to the next set
        NextSetButton(
            weightString = weightInput,
            repsString = repsInput,
            saveSet = { context, weightString: String, repsString: String ->
                val weight = weightString.toFloatOrNull()
                val reps = repsString.toIntOrNull()

                if (weight == null || reps == null) {
                    // Checks if the weight and reps are null (set to null if input is invalid, for example its a string) and a toast shows to tell the user this
                    Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
                    // If this is the case, returns before saving
                    return@NextSetButton
                }

                // Saves the exercise information to a variable
                val newExerciseStats = Exercise(
                    workoutId = selectedWorkout.workoutId,
                    plannedExerciseId = exercise.plannedExerciseId,
                    weight = weight,
                    reps = reps,
                    pb = false
                )

                // Adds the exercise to the database via the view model, then reloads so they display
                vm.insertExercise(context, newExerciseStats)
                vm.loadExercisesForWorkout(context, selectedWorkout.workoutId)

                // Clears input boxes to be used again
                onWeightValueChange("")
                onRepsValueChange("")
            },
            enabled = canAddMoreSets
        )

        Spacer(modifier = Modifier.height(37.dp))

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
                    // Shows what exercise is up next
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
            // Instructions for swiping
            text = stringResource(R.string.swipe_right_for_next_exercise_swipe_left_for_previous_exercise),
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