package com.example.workoutapp.ui.components

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.workoutapp.data.ExerciseWithName
import com.example.workoutapp.ui.theme.PrimaryText
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.workoutapp.data.Workout
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.SeparatorGrey

@Composable
fun ExerciseLogSingle(workoutId: Int) {
    val context = LocalContext.current
    val exercises = remember { mutableStateOf<List<ExerciseWithName>>(emptyList()) }
    val workout = remember { mutableStateOf<WorkoutDetails?>(null) }

    LaunchedEffect(workoutId) {
        val db = DatabaseProvider.getDatabase(context)
        val exerciseDao = db.exerciseDao()
        exercises.value = exerciseDao.getExercisesWithNamesForWorkout(workoutId)
        val results = exerciseDao.getExercisesWithNamesForWorkout(workoutId)
        val workoutDao = db.workoutDao()
        workout.value = workoutDao.getWorkoutDetailsById(workoutId)

        // GET WORKOUT INFO
    }

    Column(modifier = Modifier.padding(16.dp)) {
        ExerciseLogHeaders()
        Spacer(modifier = Modifier.height(15.dp))
       // Divider()
//        for (exercise in exercises.value) {
//            ExerciseRow(workoutId, exercise)
//            Divider()
//        }

        for ((index, exercise) in exercises.value.withIndex()) {
            val previous = exercises.value.getOrNull(index - 1)
            if (index == 0 || exercise.exerciseName != previous?.exerciseName) {
                Divider(
                    thickness = 1.dp,
                    color = SeparatorGrey
                )
            }
            ExerciseRow(workoutId, exercise)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
//            Spacer(modifier = Modifier.width(30.dp))
            ShareSheetButton(
                workoutId = workoutId,
                exercises = exercises.value,
                shareWorkout = { workoutId, exercises ->
                    val workoutName = workout.value?.workoutName ?: "Workout Name"
                    val workoutDate = workout.value?.workoutDate ?: "Date"

                    val exerciseDetails = exercises.joinToString("\n") { e ->
                        "${e.exerciseName}: ${e.weight} kg for ${e.reps} reps"
                    }

                    val shareText = """
                    $workoutName
                    Date: $workoutDate
                    
                    EXERCISES COMPLETED:
                    $exerciseDetails
                """.trimIndent()

                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                })
        }
    }
}






