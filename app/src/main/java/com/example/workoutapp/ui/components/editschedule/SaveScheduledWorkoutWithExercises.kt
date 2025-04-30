package com.example.workoutapp.ui.components.editschedule

// SaveScheduledWorkoutWithExercises.kt contains a button that receives a parameter for a function that will save the scheduled workout alongside the scheduled execrises for that workout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.R
import com.example.workoutapp.data.ScheduledWorkout
import com.example.workoutapp.ui.theme.ThirdPurple

@Composable
fun SaveScheduledWorkoutWithExercises(
    newScheduledWorkout: ScheduledWorkout,
    saveScheduledWorkoutWithExercises: (ScheduledWorkout) -> Unit,

    ) {
    Button(
        onClick = { saveScheduledWorkoutWithExercises(newScheduledWorkout) },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ThirdPurple),
        contentPadding = PaddingValues(start = 30.dp, end = 30.dp, top = 12.dp, bottom = 12.dp),
        modifier = Modifier
            .width(102.dp)
        //.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.save_2),
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}