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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.workoutapp.data.Workout
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.SeparatorGrey
import com.example.workoutapp.ui.theme.ThirdPurple
import kotlinx.coroutines.launch

@Composable
fun NewWorkoutScreen(
    onClickStartWorkout: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
                    scope.launch{
                        // TODO: Implement this!

                        // Get the workout plan ID from the start button that was pressed
                        val workoutPlanId = item.workoutPlanId

                        // Get today's date
                        val calendar = java.util.Calendar.getInstance()
                        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
                        val month = calendar.get(java.util.Calendar.MONTH) + 1
                        val year = calendar.get(java.util.Calendar.YEAR) % 100

                        val dateToday = String.format("%02d/%02d/%02d", day, month, year)

                        // Create a new workout
                        val newWorkout = Workout(
                            workoutDate = dateToday,
                            workoutPlanId = workoutPlanId
                        )

                        // Add workout to the database, returning the workout ID
                        val newWorkoutId = vm.insertWorkoutAndReturnId(context, newWorkout)

                        // Navigate to the current workout page, and pass the workout just create to it
                        onClickStartWorkout(newWorkoutId)
                    }

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