package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.InstructionText
import com.example.workoutapp.ui.components.InstructionTitle
import com.example.workoutapp.ui.extensions.toTitleCase
import com.example.workoutapp.ui.theme.SeparatorGrey

@Composable
fun PBs() {
    val scrollState = rememberScrollState()
    //var showExerciseModal by remember { mutableStateOf(false) }

    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    val refreshKey = vm.refreshTrigger

    LaunchedEffect(refreshKey) {
        vm.loadPlannedExercises(context)
    }

    // val plannedExercisesArray = vm.plannedExercisesArray
    val plannedExercisesArray by vm::plannedExercisesArray

//    var nextExerciseId by remember { mutableStateOf(1) }
//    val exerciseIds = remember { mutableStateListOf<Int>() }

    if (plannedExercisesArray.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Spacer(modifier = Modifier.height(50.dp))
            InstructionTitle(text = "Add Exercises")
            InstructionText(text = "Click the \"Add New\" button to start adding exercises that can then be added to your workouts in the schedule tab")
        }
    } else {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
            // .padding(horizontal = 38.dp)
        ) {
            val groupedPlannedExercises = plannedExercisesArray.groupBy { it.muscleGroup }
            val sortedGroupedExercises = groupedPlannedExercises.toSortedMap()

            for((muscleGroup, exercisesInGroup) in sortedGroupedExercises) {
                MuscleGroupTitle(muscleGroup = muscleGroup)
                for (plannedExercise in exercisesInGroup) {
                    PlannedExerciseRow(exercise = plannedExercise)
                }
            }

            Spacer(modifier = Modifier.height(100.dp))

            // COULD MOVE THIS TO THE TOP BAR!!!
//        AddNewWorkoutScheduleEdit(0) {
//            showExerciseModal = true
//        }
        }
    }

//    if (showExerciseModal) {
//        Dialog(onDismissRequest = { showExerciseModal = false}) {
//            SingleExerciseScheduleEdit(onModalClose = { showExerciseModal = false })
//        }
//    }
}

@Composable
fun PlannedExerciseRow(exercise: PlannedExercise) {
    Spacer(modifier = Modifier.height(28.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
//            .padding(horizontal = 38.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = exercise.exerciseName,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .padding(horizontal = 38.dp)
        )

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
}

@Composable
fun MuscleGroupTitle(muscleGroup: MuscleGroup) {
    Spacer(modifier = Modifier.height(36.dp))
    Text(
        text = muscleGroup.toTitleCase(),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier
            .padding(horizontal = 38.dp)
    )
    // Spacer(modifier = Modifier.height(5.dp))
}


