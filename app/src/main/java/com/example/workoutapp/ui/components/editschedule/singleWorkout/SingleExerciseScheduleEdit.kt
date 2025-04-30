package com.example.workoutapp.ui.components.editschedule.singleWorkout

// SingleExerciseScheduleEdit.kt contains a composable that holds the input boxes that allow a user to add a new scheduled exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.R
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.theme.ThirdPurple
import kotlinx.coroutines.launch

@Composable
fun SingleExerciseScheduleEdit(
        vm: WorkoutViewModel = viewModel(),
        onModalClose: () -> Unit
    ) {
    // Variables to remember user input - these are passed down to the respective composable functions that allow the user to input their values
    var exerciseNameInput by remember { mutableStateOf("") }
    var setNumberInput by remember { mutableStateOf("") }
    var selectedMuscleGroup by remember { mutableStateOf(MuscleGroup.CHEST) }

    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(23.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExerciseInput(
                exerciseNameInput = exerciseNameInput,
                onValueChange = { exerciseNameInput = it }
            )
            ExerciseSetNumber(
                setNumberInput = setNumberInput,
                onValueChange = { setNumberInput = it }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        MuscleGroupDropDown(
            selectedGroup = selectedMuscleGroup,
            onGroupSelected = { selectedMuscleGroup = it }
        )
        Spacer(modifier = Modifier.height(35.dp))

        // Create a new planned exercise
        val newExercise = PlannedExercise(
            exerciseName = exerciseNameInput,
            muscleGroup =  selectedMuscleGroup,
            setNumber = setNumberInput.toIntOrNull() ?: 0
        )

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        // Saves the exercise using the ViewModel
        SaveExercise(
            newExercise = newExercise,
            saveExercise = {
                coroutineScope.launch {
                    vm.addNewPlannedExercise(context, newExercise)
                    vm.loadPlannedExercises(context)
                    onModalClose()
                }
            },
        )
    }
}


// Composable that holds a button that saves the scheduled exercise. The function that allows the exercise to be saved is passed as a parameter from SingleExerciseScheduleEdit.
@Composable
fun SaveExercise(newExercise: PlannedExercise, saveExercise: (PlannedExercise) -> Unit,) {
    Button(
        onClick = { saveExercise(newExercise) },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ThirdPurple),
        contentPadding = PaddingValues(start = 30.dp, end = 30.dp, top = 12.dp, bottom = 12.dp),
        modifier = Modifier
            .width(102.dp)
    ) {
        Text(
            text = stringResource(R.string.save),
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}




