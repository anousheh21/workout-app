package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.editschedule.AddNewWorkoutScheduleEdit
import com.example.workoutapp.ui.components.editschedule.singleWorkout.SingleExerciseScheduleEdit
import com.example.workoutapp.ui.extensions.toTitleCase
import com.example.workoutapp.ui.theme.ThirdPurple

@Composable
fun PBs() {
    val scrollState = rememberScrollState()
    var showExerciseModal by remember { mutableStateOf(false) }

    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.loadPlannedExercises(context)
    }

    val plannedExercisesArray = vm.plannedExercisesArray

//    var nextExerciseId by remember { mutableStateOf(1) }
//    val exerciseIds = remember { mutableStateListOf<Int>() }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            AddNewWorkoutScheduleEdit(0) {
                showExerciseModal = true
            }
            Spacer(modifier = Modifier.height(20.dp))

            val groupedPlannedExercises = plannedExercisesArray.groupBy { it.muscleGroup }
            val sortedGroupedExercises = groupedPlannedExercises.toSortedMap()

            for((muscleGroup, exercisesInGroup) in sortedGroupedExercises) {
                Text(muscleGroup.toTitleCase())
                for (plannedExercise in exercisesInGroup) {
                    Text(plannedExercise.exerciseName)
                }
            }

//            plannedExercisesArray.forEach { exercise ->
//                Text(
//                    text = "${exercise.exerciseName} (${exercise.setNumber} sets) - ${exercise.muscleGroup.name}",
//                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal)
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//            }

            Spacer(modifier = Modifier.height(75.dp))
        }
    }

    if (showExerciseModal) {
        Dialog(onDismissRequest = { showExerciseModal = false}) {
            SingleExerciseScheduleEdit(onModalClose = { showExerciseModal = false })
        }
    }
}

@Composable
fun PlannedExerciseRow(exercise: PlannedExercise) {

}

@Composable
fun MuscleGroupTitle(muscleGroup: MuscleGroup) {
    
}


