package com.example.workoutapp.ui.components.editschedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.SeparatorGrey
import java.util.Calendar

@Composable
fun SingleWorkoutScheduleEdit(navAddExercises: (Int) -> Unit) {
    var workoutNameInput by remember { mutableStateOf("") }

    // Time picker variables
    val currentTime = Calendar.getInstance()
    val initialHour = currentTime.get(Calendar.HOUR_OF_DAY)
    val initialMinute = currentTime.get(Calendar.MINUTE)

    Column (
        modifier = Modifier
            .padding(start = 17.dp, top = 30.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start

    ) {
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
                .width(319.dp)
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

        Spacer(modifier = Modifier.height(10.dp))

        Row (
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 22.dp)
        ) {
            AddExercisesButton(navAddExercises)
            AddToCalendarButton()
        }

        Spacer(modifier = Modifier.height(30.dp))
        Divider(color = SeparatorGrey, thickness = 1.dp)

    }
}