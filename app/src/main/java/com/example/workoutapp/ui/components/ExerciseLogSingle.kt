package com.example.workoutapp.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
                 style = MaterialTheme.typography.bodyMedium
             )
             Spacer(modifier = Modifier.height(7.dp))
             Text(
                 text = exercise.muscleGroup.toTitleCase(),
                 style = MaterialTheme.typography.bodySmall
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

fun Enum<*>.toTitleCase(): String {
    return name
        .lowercase()
        .split('_')
        .joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }
}

@Composable
fun NumberBox(number: Int) {
    Box(
        modifier = Modifier
            .width(41.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(PrimaryText)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            color = Color.Black
        )
    }
}
