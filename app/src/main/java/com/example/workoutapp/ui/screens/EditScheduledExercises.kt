package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.workoutapp.ui.components.editschedule.singleWorkout.SingleExerciseScheduleEdit

@Composable
fun EditScheduledExercises(workoutPlanId: Int) {
    val scrollState = rememberScrollState() 

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {

    }
}