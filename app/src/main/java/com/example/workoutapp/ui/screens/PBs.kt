package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.workoutapp.ui.components.editschedule.AddNewWorkoutScheduleEdit
import com.example.workoutapp.ui.components.editschedule.singleWorkout.SingleExerciseScheduleEdit

@Composable
fun PBs() {
    val scrollState = rememberScrollState()

    var nextExerciseId by remember { mutableStateOf(1) }
    val exerciseIds = remember { mutableStateListOf<Int>() }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {

        exerciseIds.forEach { id ->
            SingleExerciseScheduleEdit()
        }

        AddNewWorkoutScheduleEdit(nextExerciseId) { id ->
            exerciseIds.add(id)
            nextExerciseId++
        }
    }
}