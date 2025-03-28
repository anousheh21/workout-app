package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        }
    }
}

@Composable
fun WorkoutRow(workout: dummyData) {
    Row(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(workout.workoutName)
        Text(workout.workoutDate)
    }
}