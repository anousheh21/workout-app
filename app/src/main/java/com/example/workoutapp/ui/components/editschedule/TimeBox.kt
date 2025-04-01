package com.example.workoutapp.ui.components.editschedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryText

@Composable
fun TimeBox(
    initialHour: Int,
    initialMinute: Int,

    ) {
    var showTimePicker by remember { mutableStateOf(false) }
    var workoutTime by remember { mutableStateOf(String.format("%02d:%02d", initialHour, initialMinute)) }

    Box(
        modifier = Modifier
            // .fillMaxWidth()
            // .padding(start = 21.dp, end = 20.dp, top = 10.dp, bottom = 11.dp)
            .height(56.dp)
            .width(86.dp)
            .clickable { showTimePicker = true }
            .background(PrimaryText, shape = RoundedCornerShape(3.dp))
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text =  workoutTime,
            style = TextStyle(fontSize = 16.sp, color = DarkText)
        )
    }

    if (showTimePicker) {
        Dialog(onDismissRequest = { showTimePicker = false }) {
            WorkoutTime(
                initialHour = initialHour,
                initialMinute = initialMinute,
                onConfirm = { hour, minute ->
                    workoutTime = String.format("%02d:%02d", hour, minute)
                    showTimePicker = false
                },
                onDismiss = {
                    showTimePicker = false
                }
            )
        }
    }
}