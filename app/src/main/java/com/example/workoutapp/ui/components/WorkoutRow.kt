package com.example.workoutapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.workoutapp.data.WorkoutDetails

@Composable
fun WorkoutRow(workout: WorkoutDetails, onClickWorkout: (WorkoutDetails) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickWorkout(workout) }
            .padding(start = 32.dp)
            .padding(top = 18.dp)
            .padding(bottom = 22.dp)
    ) {

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                    append(workout.workoutName)
                }

                append("   -   ")
                append(workout.workoutDate)
            }
        )
    }
}