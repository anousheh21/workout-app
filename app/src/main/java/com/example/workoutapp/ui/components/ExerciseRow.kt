package com.example.workoutapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.data.DatabaseProvider
import com.example.workoutapp.data.ExerciseWithName
import com.example.workoutapp.ui.extensions.toTitleCase

@Composable
fun ExerciseRow(workoutId: Int, exercise: ExerciseWithName) {
    val context = LocalContext.current
    val exercises = remember { mutableStateOf<List<ExerciseWithName>>(emptyList()) }

    LaunchedEffect(workoutId) {
        val db = DatabaseProvider.getDatabase(context)
        val exerciseDao = db.exerciseDao()
        exercises.value = exerciseDao.getExercisesWithNamesForWorkout(workoutId)

    }
    // Spacer(modifier = Modifier.width(334.dp))
    Row(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .padding(start = 34.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = exercise.exerciseName,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = exercise.muscleGroup.toTitleCase(),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )
        }
        // Text(text = "${exercise.weight}kg x ${exercise.reps}")
        Spacer(modifier = Modifier.width(16.dp))
        NumberBox(number = exercise.weight.toInt())
        Spacer(modifier = Modifier.width(31.dp))
        NumberBox(number = exercise.reps)
        Spacer(modifier = Modifier.width(33.dp))

    }
}