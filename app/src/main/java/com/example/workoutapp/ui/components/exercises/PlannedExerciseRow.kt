package com.example.workoutapp.ui.components.exercises

// PlannedExerciseRow.kt shows a row of a planned exercise in the scheduled exercises screen

import android.content.Context
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.R
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.SeparatorGrey
import kotlinx.coroutines.launch

@Composable
fun PlannedExerciseRow(exercise: PlannedExercise, vm: WorkoutViewModel, context: Context) {
    // Variables used for deleting a row
    var showDeleteModal by remember { mutableStateOf(false) }
    var ableToDeleteCheck by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    //val vm: WorkoutViewModel = viewModel()
    Spacer(modifier = Modifier.height(28.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                // Tap gesture to detect a long press - a long press pops up a dialog that allows the user to delete the exercise
                detectTapGestures(
                    onLongPress = {
                        showDeleteModal = true
                    }
                )
            },
//            .padding(horizontal = 38.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Displays the name of the exercise
        Text(
            text = exercise.exerciseName,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .padding(horizontal = 38.dp)
        )

        // Displays the number of sets to do for that exercise
        Text(
            text = "${exercise.setNumber} Sets",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .padding(horizontal = 38.dp)
        )
    }
    Spacer(modifier = Modifier.height(28.dp))
    Divider(
        color = SeparatorGrey,
        thickness = 1.dp,
    )

    // If showDeleteModal is true, show a dialog that allows the user to delete that exercise
    if (showDeleteModal) {
        //val context = LocalContext.current
        AlertDialog(
            onDismissRequest = { showDeleteModal = false },
            confirmButton = {
                TextButton(onClick = {
//                    vm.deletePlannedExercise(exercise, context)
//                    showDeleteModal = false

                    scope.launch {
                        // Checks if the scheduled exercise has been added to a scheduled workout. If it has, the user cannot delete it.
                        val useCheck = vm.isPlannedExerciseUsed(context, exercise.plannedExerciseId)
                        if (useCheck) {
                            ableToDeleteCheck = "This exercise cannot be deleted as it is being used as part of a scheduled workout"
                        } else {
                            // If the scheduled exercise isn't being used, then delete it, and stop showing the modal
                            vm.deletePlannedExercise(exercise, context)
                            showDeleteModal = false
                        }
                    }
                }) {
                    Text(
                        text = stringResource(R.string.delete),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteModal = false }) {
                    Text(
//                        text = "Cancel",
                        text = stringResource(R.string.cancel),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryColor
                        )
                    )
                }
            },
            title = { Text(stringResource(R.string.delete_exercise)) },
            text = { Text(
                text = ableToDeleteCheck ?: "Are you sure you want to delete '${exercise.exerciseName}'?",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = PrimaryText,

                    )
            ) }
        )
    }
}
