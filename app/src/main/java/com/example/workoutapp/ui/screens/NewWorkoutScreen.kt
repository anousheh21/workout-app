package com.example.workoutapp.ui.screens

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.ScheduledWorkoutExercise
import com.example.workoutapp.data.ScheduledWorkoutWithExercises
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.SeparatorGrey
import com.example.workoutapp.ui.theme.ThirdPurple

@Composable
fun NewWorkoutScreen() {
    val scrollState = rememberScrollState()
    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.loadScheduledWorkoutsWithExercises(context)
    }

    val schedWorkouts = vm.scheduledWorkoutsWithExercises

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        schedWorkouts.forEach { item ->
            SelectWorkoutRow(
                schedWorkout = item,
                startWorkout = {
                    // TODO: Implement this!
                }
            )

            Divider(color = SeparatorGrey, thickness = 1.dp)
        }
    }
}

@Composable
fun SelectWorkoutRow(
        schedWorkout: ScheduledWorkoutWithExercises,
        startWorkout: () -> Unit,
    ) {
    Row {
        Text(text = schedWorkout.workoutName)
        StartWorkoutButton(startWorkout)
    }
}

@Composable
fun StartWorkoutButton(
    startWorkout: () -> Unit
) {
    Button(
        onClick = { startWorkout() },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        modifier = Modifier
            .width(65.dp)
    ) {
        Text(
            text = "Start",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}