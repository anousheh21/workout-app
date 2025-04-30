package com.example.workoutapp.ui.screens

// PBs.kt is a screen that shows the scheduled exercises
// The user can navigate to it from the bottom nav bar

import android.content.Context
import android.graphics.Color
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.workoutapp.ui.components.exercises.MuscleGroupTitle
import com.example.workoutapp.ui.components.exercises.PlannedExerciseRow
import com.example.workoutapp.ui.extensions.toTitleCase
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.SeparatorGrey
import kotlinx.coroutines.launch

@Composable
fun PBs() {

    // Scroll state that allows the user to scroll through the screen
    val scrollState = rememberScrollState()
    //var showExerciseModal by remember { mutableStateOf(false) }

    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    val refreshKey = vm.refreshTrigger

    LaunchedEffect(refreshKey) {
        // Load all scheduled exercises from the database via the ViewModel
        vm.loadPlannedExercises(context)
    }

    // val plannedExercisesArray = vm.plannedExercisesArray
    val plannedExercisesArray by vm::plannedExercisesArray

//    var nextExerciseId by remember { mutableStateOf(1) }
//    val exerciseIds = remember { mutableStateListOf<Int>() }

    // If there are no scheduled exercises in the database, display instructions telling the user how to add them
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
            InstructionText(text = "Click the \"Add New\" button to start adding exercises that can then be added to your workouts in the schedule tab. To delete an exercise, press and hold.")
        }
    } else {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
            // .padding(horizontal = 38.dp)
        ) {
            // Group planned exercises by muscle group
            val groupedPlannedExercises = plannedExercisesArray.groupBy { it.muscleGroup }
            // Sort the planned exercises that have been grouped by muscle group (alphabetically by muscle group)
            val sortedGroupedExercises = groupedPlannedExercises.toSortedMap()

            for((muscleGroup, exercisesInGroup) in sortedGroupedExercises) {
                // For each muscle group, display that muscle group as a title
                MuscleGroupTitle(muscleGroup = muscleGroup)
                // Loop through all the exercises in that muscle group and display them
                for (plannedExercise in exercisesInGroup) {
                    PlannedExerciseRow(exercise = plannedExercise, vm = vm, context = context)
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




