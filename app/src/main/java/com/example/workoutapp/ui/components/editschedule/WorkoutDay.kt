package com.example.workoutapp.ui.components.editschedule

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDay(
    selectedDay: String,
    onDaySelected: (String) -> Unit
) {
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    var expanded by remember { mutableStateOf(false) }
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
            textStyle = TextStyle(color = DarkText, fontSize = 16.sp),
            shape = RoundedCornerShape(3.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = PrimaryText,
                unfocusedContainerColor = PrimaryText,
                disabledContainerColor = PrimaryText,
                focusedLabelColor = DarkText,
                unfocusedLabelColor = DarkText,
            ),
            modifier = Modifier
                .menuAnchor()
                .width(210.dp)
                .padding(start = 22.dp, top = 10.dp, bottom = 11.dp)
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
                        onDaySelected(day)
                        expanded = false
                    }
                )
            }
        }
    }
}