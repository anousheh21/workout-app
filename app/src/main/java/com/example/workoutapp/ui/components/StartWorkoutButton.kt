package com.example.workoutapp.ui.components

// StartWorkoutButton.kt allows the user to start a workout - uses tha startWorkout parameter function to do so

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
import com.example.workoutapp.ui.theme.PrimaryColor

@Composable
fun StartWorkoutButton(
    startWorkout: () -> Unit
) {
    Button(
        onClick = { startWorkout() },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 5.dp),
        modifier = Modifier
            .width(65.dp)
    ) {
        Text(
            text = stringResource(R.string.start),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}