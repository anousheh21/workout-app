package com.example.workoutapp.ui.components

// NoWorkoutScreen.kt shows as the first thing the user sees when they log into the app for the first time
// It explains how to use the app, before they have added any data to it

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.R

@Composable
fun NoWorkoutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        InstructionTitle(text = stringResource(R.string.how_to_use_this_app))
        InstructionText(text = stringResource(R.string._1_navigate_to_the_exercises_tab_and_add_all_exercises_that_you_might_carry_out))
        InstructionText(text = stringResource(R.string._2_navigate_to_the_schedule_tab_and_schedule_in_your_workouts_adding_the_relevant_exercises))
        InstructionText(text = stringResource(R.string._3_return_to_the_workouts_page_and_click_new_workout_to_start_a_new_workout))
    }
}

