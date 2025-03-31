package com.example.workoutapp.ui.components

import android.util.Log
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
import com.example.workoutapp.data.ExerciseWithName

@Composable
fun ExerciseLogSingle(workoutId: Int) {
    val context = LocalContext.current
    val exercises = remember { mutableStateOf<List<ExerciseWithName>>(emptyList()) }

    LaunchedEffect(workoutId) {
        val db = DatabaseProvider.getDatabase(context)
        val exerciseDao = db.exerciseDao()
        exercises.value = exerciseDao.getExercisesWithNamesForWorkout(workoutId)
        val results = exerciseDao.getExercisesWithNamesForWorkout(workoutId)

        Log.d("ExerciseDebug", "Loaded ${results.size} exercises for workoutId=$workoutId")
        results.forEach {
            Log.d("ExerciseDebug", "Exercise: ${it.exerciseName}, weight=${it.weight}, reps=${it.reps}, pb=${it.pb}")
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Exercises", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        for (exercise in exercises.value) {
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = exercise.exerciseName,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${exercise.weight}kg x ${exercise.reps}",
                    modifier = Modifier.weight(1f)
                )
            }
            Divider()
        }
    }
}