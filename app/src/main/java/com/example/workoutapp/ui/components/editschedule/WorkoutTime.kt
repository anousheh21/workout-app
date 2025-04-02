package com.example.workoutapp.ui.components.editschedule

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTime(
    initialTime: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val parts = initialTime.split(":")
    val initialHour = parts.getOrNull(0)?.toIntOrNull() ?: 12
    val initialMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0

    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )

    Column {
        TimeInput(
            state = timePickerState
        )
        Button(onClick = { onDismiss() }) {
            Text("Cancel")
        }
        Button(onClick = {
            onConfirm(String.format("%02d:%02d", timePickerState.hour, timePickerState.minute))
            onDismiss()
        }) {
            Text("Confirm")
        }
    }
}