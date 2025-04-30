package com.example.workoutapp.ui.screens

// NewWorkoutScreen.kt is the screen that allows the user to start a new workout
// It shows a list of scheduled workouts, and allows the user to select one to start

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.ScheduledWorkoutExercise
import com.example.workoutapp.data.ScheduledWorkoutWithExercises
import com.example.workoutapp.data.Workout
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.InstructionText
import com.example.workoutapp.ui.components.InstructionTitle
import com.example.workoutapp.ui.components.SelectWorkoutRow
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.SeparatorGrey
import com.example.workoutapp.ui.theme.ThirdPurple
import kotlinx.coroutines.launch

@Composable
fun NewWorkoutScreen(
    onClickStartWorkout: (Int) -> Unit
) {
    // Scroll state to allow the page to screel
    val scrollState = rememberScrollState()
    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Load scheduled workouts with their exercises from the database via the ViewModel
        vm.loadScheduledWorkoutsWithExercises(context)
    }

    // Set the loaded scheduled workouts to a variable via the ViewModel
    val schedWorkouts = vm.scheduledWorkoutsWithExercises

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        // If there are no scheduled workouts to display, display instructions that the user can follow to create a new scheduled workout
        if (schedWorkouts.isEmpty()) {
           Column(
               modifier = Modifier
                   .fillMaxSize()
                   .padding(16.dp),
               horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.Center
           ) {
               Spacer(modifier = Modifier.height(50.dp))
               InstructionTitle(text = "No Scheduled Workouts Available")
               InstructionText(text = "Schedule a workout in the schedule tab, then return here to start that workout")
           }
        } else {
            // Loop through each scheduled workout in the array to display it
            schedWorkouts.forEach { item ->
                SelectWorkoutRow(
                    schedWorkout = item,
                    // This function runs when the user selects a workout to start
                    startWorkout = {
                        scope.launch{

                            // Assign the workout plan ID of the selected workout
                            val workoutPlanId = item.workoutPlanId

                            // Calculate today's date
                            val calendar = java.util.Calendar.getInstance()
                            val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
                            val month = calendar.get(java.util.Calendar.MONTH) + 1
                            val year = calendar.get(java.util.Calendar.YEAR) % 100

                            // Assign the computed date to this variable and format it correctly for our use
                            val dateToday = String.format("%02d/%02d/%02d", day, month, year)

                            // Create a new Workout and assign it to a variable
                            val newWorkout = Workout(
                                workoutDate = dateToday,
                                workoutPlanId = workoutPlanId
                            )

                            // Add the workout just created to the database via the ViewModel
                            // Assign the ID of this workout to the variable
                            val newWorkoutId = vm.insertWorkoutAndReturnId(context, newWorkout)

                            // Navigate to the current workout screen so that the user can actually start the workout
                            // This is done by passing in the workout ID of the workout just created
                            onClickStartWorkout(newWorkoutId)
                        }

                    }
                )

                Divider(color = SeparatorGrey, thickness = 1.dp)
            }
        }
    }
}



