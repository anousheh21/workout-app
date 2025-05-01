package com.example.workoutapp.ui.components.workout

// CompletedExerciseTable.kt displays in the current workout screen, and shows the sets and reps and weights for the exercise being displayed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.workoutapp.R
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.PlannedExercise

@Composable
fun CompletedExerciseTable(
    exercises: List<Exercise>,
    plannedExercise: PlannedExercise,
) {

    Column {
        // Header row
        Row {
            Row {
                TableCell(stringResource(R.string.set), bold = true, modifier = Modifier.weight(1f))
                TableCell(stringResource(R.string.weight), bold = true, modifier = Modifier.weight(1f))
                TableCell(stringResource(R.string.reps), bold = true, modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Data rows
        for (i in 1..plannedExercise.setNumber) {
            Row {
                TableCell("$i", modifier = Modifier.weight(1f))
                if (i <= exercises.size) {
                    val exercise = exercises[i - 1]
                    TableCell("${exercise.weight}", modifier = Modifier.weight(1f))
                    TableCell("${exercise.reps}", modifier = Modifier.weight(1f))
                } else {
                    TableCell(" ", modifier = Modifier.weight(1f))
                    TableCell(" ", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}