package com.example.workoutapp.ui.components

// WorkoutScheduleRow.kt shows a row for a scheduled workout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.data.ScheduledWorkoutWithExercises
import com.example.workoutapp.ui.theme.SecondPurple
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun WorkoutScheduleRow(
    workoutWithExercises: ScheduledWorkoutWithExercises,
    navAddExercises: (Int) -> Unit,
    addScheduledWorkoutToCalendar: (ScheduledWorkoutWithExercises) -> Unit,
    onLongPressDelete: (ScheduledWorkoutWithExercises) -> Unit)
{

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongPressDelete(workoutWithExercises)
                    }
                )
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column() {
            Text(
                text = workoutWithExercises.workoutName,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${workoutWithExercises.workoutDay}'s at ${workoutWithExercises.workoutTime}",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Add to Calendar",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = SecondPurple
                ),
                modifier = Modifier
                    .clickable {
                        addScheduledWorkoutToCalendar(workoutWithExercises)
                    }
            )
        }


        Text(
            text = "View Exercises",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = SecondPurple
            ),
            modifier = Modifier
                .clickable{
                    navAddExercises(workoutWithExercises.workoutPlanId)
                }
        )
    }
}