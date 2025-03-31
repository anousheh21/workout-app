package com.example.workoutapp.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.ui.WorkoutViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.workoutapp.data.DatabaseProvider
import com.example.workoutapp.data.Exercise
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.workoutapp.data.ExerciseWithName
import com.example.workoutapp.ui.theme.PrimaryText
import androidx.compose.ui.Alignment
import com.example.workoutapp.ui.theme.PrimaryColor

@Composable
fun ExerciseLogSingle(workoutId: Int) {
    val context = LocalContext.current
    val exercises = remember { mutableStateOf<List<ExerciseWithName>>(emptyList()) }

    LaunchedEffect(workoutId) {
        val db = DatabaseProvider.getDatabase(context)
        val exerciseDao = db.exerciseDao()
        exercises.value = exerciseDao.getExercisesWithNamesForWorkout(workoutId)
        val results = exerciseDao.getExercisesWithNamesForWorkout(workoutId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        ExerciseLogHeaders()
        // Spacer(modifier = Modifier.height(8.dp))

        for (exercise in exercises.value) {
            ExerciseRow(workoutId, exercise)
            Divider()
        }
    }
}

@Composable
fun ExerciseLogHeaders() {
    Row{
        Text("Exercise", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.width(20.dp))
        Text(" Weight/kg", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Reps", style = MaterialTheme.typography.titleMedium)
    }
}

 @Composable
fun ExerciseRow(workoutId: Int, exercise: ExerciseWithName) {
     val context = LocalContext.current
     val exercises = remember { mutableStateOf<List<ExerciseWithName>>(emptyList()) }

     LaunchedEffect(workoutId) {
         val db = DatabaseProvider.getDatabase(context)
         val exerciseDao = db.exerciseDao()
         exercises.value = exerciseDao.getExercisesWithNamesForWorkout(workoutId)

     }

     Row(modifier = Modifier.padding(vertical = 8.dp)) {
         Column {
             Text(text = exercise.exerciseName)
             Spacer(modifier = Modifier.height(8.dp))
             Text(text = "${exercise.muscleGroup}")
         }
         // Text(text = "${exercise.weight}kg x ${exercise.reps}")
         NumberBoxFloat(exercise.weight)
         NumberBoxInt(exercise.reps)

     }
}

@Composable
fun NumberBoxInt(number: Int) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PrimaryText)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}

@Composable
fun NumberBoxFloat(number: Float) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PrimaryText)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}