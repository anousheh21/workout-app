package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

data class dummyData(
    val workoutName: String,
    val workoutDate: String
)

@Composable
fun WorkoutsScreen() {
    // DELETE THIS FOR DEVELOPMENT ONLY
    val workoutsArray = mutableListOf(
        dummyData("Push", "21/03/25"),
        dummyData("Pull", "19/03/25"),
        dummyData("Legs", "18/03/25"),
        dummyData("Push", "16/03/25"),
        dummyData("Pull", "14/03/25")
    )

    WorkoutColumnList(workoutsArray)



}

@Composable
fun WorkoutColumnList(workouts: List<dummyData>) {
    LazyColumn {
        items(workouts) { workout ->
            WorkoutRow(workout)
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}

@Composable
fun WorkoutRow(workout: dummyData) {
    Row(
        modifier = Modifier
            .padding(start = 32.dp)
            .padding(top = 18.dp)
            .padding(bottom = 22.dp)
    ) {

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                    append(workout.workoutName)
                }

                append(" - ")
                append(workout.workoutDate)
            }
        )
    }
}