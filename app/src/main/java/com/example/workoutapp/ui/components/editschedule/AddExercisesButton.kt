package com.example.workoutapp.ui.components.editschedule

// AddExerciseButton.kt allows the user to add exercises - it is not used.

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.workoutapp.R
import com.example.workoutapp.ui.theme.PrimaryColor

@Composable
fun AddExercisesButton(navAddExercises: (Int) -> Unit ) {
    val tempWorkoutPlanId = 1
    Button(
        onClick = { navAddExercises(tempWorkoutPlanId) },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
        contentPadding = PaddingValues(start = 25.dp, top = 14.dp, bottom = 14.dp, end = 25.dp),
        modifier = Modifier
            .width(240.dp)

    ) {
        Row {
            Text(stringResource(R.string.add_exercises))
            Spacer(modifier = Modifier.width(70.dp))
            Icon(
                painter = painterResource(id = R.drawable.smallplus),
                contentDescription = "plus"
            )
        }
    }
}