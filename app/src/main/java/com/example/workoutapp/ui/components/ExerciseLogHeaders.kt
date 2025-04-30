package com.example.workoutapp.ui.components

// ExerciseLogHeaders.kt formats the header section of the exercise log, with the exercise log headers together

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.R
import com.example.workoutapp.data.Exercise

@Composable
fun ExerciseLogHeaders() {
    Row{
        Spacer(modifier = Modifier.width(35.dp))
        ExerciseLogHeader(text = stringResource(R.string.exercise))
        Spacer(modifier = Modifier.width(130.dp))
        ExerciseLogHeader(text = stringResource(R.string.weight_kg))
        Spacer(modifier = Modifier.width(25.dp))
        ExerciseLogHeader(text = stringResource(R.string.reps))
    }
}

