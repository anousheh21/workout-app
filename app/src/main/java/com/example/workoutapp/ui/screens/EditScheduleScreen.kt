package com.example.workoutapp.ui.screens

import android.util.Log
import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.workoutapp.R
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.PrimaryText
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScheduleScreen(navAddExercises: (Int) -> Unit ) {
    var workoutNameInput by remember { mutableStateOf("") }

    // Time picker variables
    val currentTime = Calendar.getInstance()
    val initialHour = currentTime.get(Calendar.HOUR_OF_DAY)
    val initialMinute = currentTime.get(Calendar.MINUTE)

    Column {
        TextField(
            value = workoutNameInput,
            onValueChange = { workoutNameInput = it },
            label = { Text("Workout Name") },
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
                .fillMaxWidth()
                .padding(start = 22.dp, top = 10.dp, bottom = 11.dp)
        )

        Row (
            horizontalArrangement = Arrangement.spacedBy(23.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            WorkoutDay()
            TimeBox(
                initialHour,
                initialMinute,
            )
        }

        Row (
            horizontalArrangement = Arrangement.spacedBy(17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AddExercisesButton(navAddExercises)
            AddToCalendarButton()
        }


    }
}

@Composable
fun AddExercisesButton(navAddExercises: (Int) -> Unit ) {
    val tempWorkoutPlanId = 1
    Button(
        onClick = { navAddExercises(tempWorkoutPlanId) },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
        contentPadding = PaddingValues(start = 25.dp, top = 14.dp, bottom = 14.dp, end = 25.dp)
    ) {
        Row {
            Text("Add Exercises")
            Spacer(modifier = Modifier.width(82.dp))
            Icon(
                painter = painterResource(id = R.drawable.smallplus),
                contentDescription = "plus"
            )
        }
    }
}

@Composable
fun AddToCalendarButton() {
    Button(
        onClick = { addWorkoutToCalendar() },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
        contentPadding = PaddingValues(10.dp),
        modifier = Modifier
            .size(47.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = "Calendar",
        )
    }
}

fun addWorkoutToCalendar() {
    Log.d("PLACEHOLDER", "This will add the workout to the calendar")
}

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
                        selectedDay = day
                        expanded = false
                    }
                )
            }
        }
    }
}