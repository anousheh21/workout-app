package com.example.workoutapp.ui.components

import android.content.Intent
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
import androidx.compose.material3.Button
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

        for (exercise in exercises.value) {
            ExerciseRow(workoutId, exercise)
            Divider()
        }

        Spacer(modifier = Modifier.height(50.dp))

        ShareSheetButton(
            workoutId = workoutId,
            exercises = exercises.value,
            shareWorkout = { workoutId, exercises ->
                return@ShareSheetButton Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "hi")
                }
            })
    }
}

@Composable
fun ShareSheetButton(
    workoutId: Int,
    exercises: List<ExerciseWithName>,
    shareWorkout: (Int, List<ExerciseWithName>) -> Intent
) {
    val context = LocalContext.current
    Button(onClick = {
        val intent = shareWorkout(workoutId, exercises)
        val intentChooser = Intent.createChooser(intent, "Share Workout")
        context.startActivity(intentChooser)
    }) {
        Text(
            text = "Share Workout"
        )
    }
}




