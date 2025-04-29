package com.example.workoutapp.ui.components.editschedule

// AddExercisesMultiSelect.kt shows a dialog that allows users to add scheduled exercises to a scheduled workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.theme.ThirdPurple

@Composable
fun AddExercisesMultiSelect(
    selectedExercises: MutableList<PlannedExercise>,
    vm : WorkoutViewModel = viewModel()
) {
    // Read in list of planned exercises from database
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.loadPlannedExercises(context)
    }

    val plannedExercisesArray = vm.plannedExercisesArray

    // State to remember if the dialog is showing or not
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Button that shows the dialog to add the exercises
        Button(
            onClick = {showDialog = true},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ThirdPurple),
            //contentPadding = PaddingValues(start = 30.dp, end = 30.dp, top = 12.dp, bottom = 12.dp),
            modifier = Modifier
                .width(150.dp)
        ) {
            Text("Add Exercises")
        }

        // If showDialog is true, show the dialog that allows the user to add the exercises
        if (showDialog) {
            ExerciseSelectDialog(
                plannedExercisesArray = plannedExercisesArray,
                selectedExercises = selectedExercises,
                onDismiss = { showDialog = false }
            )
        }
    }
}