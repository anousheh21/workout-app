package com.example.workoutapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseLogHeaders() {
    Row{
        Spacer(modifier = Modifier.width(34.dp))
        Text("Exercise", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.width(97.dp))
        Text(" Weight/kg", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.width(16.dp))
        Text("Reps", style = MaterialTheme.typography.titleMedium)
    }
}