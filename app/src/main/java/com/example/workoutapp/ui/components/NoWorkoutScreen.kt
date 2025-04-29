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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoWorkoutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        InstructionTitle(text = "How to Use This App")
        InstructionText(text = "1. Navigate to the exercises tab, and add all exercises that you might carry out")
        InstructionText(text = "2. Navigate to the schedule tab, and schedule in your workouts, adding the relevant exercises")
        InstructionText(text = "3. Return to the workouts page, and click \"New Workout\" to start a new workout")
    }
}

