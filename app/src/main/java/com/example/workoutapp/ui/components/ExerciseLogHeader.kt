package com.example.workoutapp.ui.components

// ExerciseLogHeader.kt contains the heading for the exercise log

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ExerciseLogHeader(
    text: String
) {
    Text(
        text = text,
        style = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
    )
}