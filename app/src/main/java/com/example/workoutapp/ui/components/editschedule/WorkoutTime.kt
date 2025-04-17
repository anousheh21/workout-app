package com.example.workoutapp.ui.components.editschedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.workoutapp.ui.theme.BackgroundColor
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.SecondaryText

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

    val timeInputColors: TimePickerColors = TimePickerDefaults.colors(
            timeSelectorUnselectedContainerColor = PrimaryText,
            timeSelectorSelectedContainerColor   = Color.White,
            timeSelectorUnselectedContentColor   = DarkText,
            timeSelectorSelectedContentColor     = DarkText
    )

    val selectionColors = TextSelectionColors(
        handleColor      = BackgroundColor,
        backgroundColor  = BackgroundColor.copy(alpha = .4f)
    )

    val patchedScheme   = MaterialTheme.colorScheme.copy(
        primary  = SecondaryText,
        outline  = SecondaryText
    )

    Column {
        CompositionLocalProvider(
            LocalTextSelectionColors provides selectionColors,
            LocalTextStyle           provides TextStyle(color = DarkText)
        ) {
            MaterialTheme(colorScheme = patchedScheme) {
                TimeInput(
                    state  = timePickerState,
                    colors = timeInputColors
                )
            }
        }


        Button(onClick = {
            onConfirm(String.format("%02d:%02d", timePickerState.hour, timePickerState.minute))
            onDismiss()
        }) {
            Text("Confirm")
        }
        Button(onClick = { onDismiss() }) {
            Text("Cancel")
        }
    }
}