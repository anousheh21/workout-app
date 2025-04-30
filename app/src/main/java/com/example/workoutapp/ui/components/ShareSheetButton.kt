package com.example.workoutapp.ui.components

// ShareSheetButton.kt has a parameter passed to it that is a function that runs when the ShareSheetButton is pressed - this function allows the user to add a workout via the sharesheet

import android.content.Intent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.R
import com.example.workoutapp.data.ExerciseWithName
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.PrimaryText

@Composable
fun ShareSheetButton(
    workoutId: Int,
    exercises: List<ExerciseWithName>,
    shareWorkout: (Int, List<ExerciseWithName>) -> Intent
) {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = shareWorkout(workoutId, exercises)
            val intentChooser = Intent.createChooser(intent, "Share Workout")
            context.startActivity(intentChooser)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryColor,
            contentColor = PrimaryText
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = stringResource(R.string.share_workout),
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium

            )
        )
    }
}