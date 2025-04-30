package com.example.workoutapp.ui.components.editschedule

// WorkoutTime.kt allows the user to select the time they want to do their workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.workoutapp.R
import com.example.workoutapp.ui.theme.BackgroundColor
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.SecondPurple
import com.example.workoutapp.ui.theme.SecondaryText
import com.example.workoutapp.ui.theme.ThirdPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTime(
    initialTime: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Workout time takes in a string of the initial time, and here we split it into parts for hour and minute
    val parts = initialTime.split(":")
    val initialHour = parts.getOrNull(0)?.toIntOrNull() ?: 12
    val initialMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0

    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )

    // Define the colours of the time picker
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


        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    // Confirms the time that the user has specified, and formats it correctly for our use
                    onConfirm(String.format("%02d:%02d", timePickerState.hour, timePickerState.minute))
                    onDismiss()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ThirdPurple)
            ) {
                Text(stringResource(R.string.confirm))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = { onDismiss() },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SecondPurple)
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    }
}