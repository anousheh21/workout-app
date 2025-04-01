package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.workoutapp.ui.theme.DarkText

@Composable
fun EditScheduleScreen() {
    var workoutNameInput by remember { mutableStateOf("") }

        Column() {
            TextField(
                value = workoutNameInput,
                onValueChange = { workoutNameInput = it },
                label = { Text("Workout Name") },
                textStyle = TextStyle(color = DarkText)
            )

            Row {
                WorkoutDay()
            }
        }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDay() {
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    var expanded by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf("") }
    var submittedDay by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedDay,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Workout Day") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            textStyle = TextStyle(color = DarkText),
            modifier = Modifier
                .menuAnchor()
                // .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            daysOfWeek.forEach { day ->
                DropdownMenuItem(
                    text = { Text(day) },
                    onClick = {
                        selectedDay = day
                        expanded = false
                    }
                )
            }
        }
    }
}