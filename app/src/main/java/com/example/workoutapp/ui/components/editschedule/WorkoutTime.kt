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
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
) {

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
        Button(onClick = {onConfirm(timePickerState.hour, timePickerState.minute) }) {
            Text("Confirm")
        }
    }
}