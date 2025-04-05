package com.example.workoutapp.ui.screens

import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.R
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.ScheduledWorkoutExercise
import com.example.workoutapp.data.ScheduledWorkoutWithExercises
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.editschedule.AddExercisesButton
import com.example.workoutapp.ui.components.editschedule.AddNewWorkoutScheduleEdit
import com.example.workoutapp.ui.components.editschedule.AddToCalendarButton
import com.example.workoutapp.ui.components.editschedule.SingleWorkoutScheduleEdit
import com.example.workoutapp.ui.components.editschedule.TimeBox
import com.example.workoutapp.ui.components.editschedule.WorkoutDay
import com.example.workoutapp.ui.components.editschedule.addWorkoutToCalendar
import com.example.workoutapp.ui.components.editschedule.singleWorkout.SingleExerciseScheduleEdit
import com.example.workoutapp.ui.extensions.toTitleCase
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.SecondPurple
import com.example.workoutapp.ui.theme.SeparatorGrey
import com.example.workoutapp.ui.theme.ThirdPurple
import java.util.Calendar



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScheduleScreen(navAddExercises: (Int) -> Unit ) {
    val scrollState = rememberScrollState()
    var showWorkoutModal by remember { mutableStateOf(false) }

    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.loadScheduledWorkoutsWithExercises(context)
    }

    val scheduledWorkoutWithExercises = vm.scheduledWorkoutsWithExercises

    Column(
        modifier = Modifier
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        scheduledWorkoutWithExercises.forEach { item ->
            Spacer(modifier = Modifier.height(19.dp))
            WorkoutScheduleRow(
                workoutWithExercises = item,
                navAddExercises,
                addScheduledWorkoutToCalendar = { workout ->
                    val calendar = Calendar.getInstance()
                    val splitTime = workout.workoutTime.split(":")
                    val workoutHour = splitTime.getOrNull(0)?.toIntOrNull() ?: 9
                    val workoutMinute = splitTime.getOrNull(1)?.toIntOrNull() ?: 0
                    var byDay = "SA"

                    if (workout.workoutDay == "Monday") {
                        calendar.set(Calendar.DAY_OF_WEEK, 2)
                        byDay = "MO"
                    } else if (workout.workoutDay == "Tuesday") {
                        calendar.set(Calendar.DAY_OF_WEEK, 3)
                        byDay = "TU"
                    } else if (workout.workoutDay == "Wednesday") {
                        calendar.set(Calendar.DAY_OF_WEEK, 4)
                        byDay = "WE"
                    } else if (workout.workoutDay == "Thursday") {
                        calendar.set(Calendar.DAY_OF_WEEK, 5)
                        byDay = "TH"
                    } else if (workout.workoutDay == "Friday") {
                        calendar.set(Calendar.DAY_OF_WEEK, 6)
                        byDay = "FR"
                    } else if (workout.workoutDay == "Saturday") {
                        calendar.set(Calendar.DAY_OF_WEEK, 7)
                        byDay = "SA"
                    } else if (workout.workoutDay == "Sunday") {
                        calendar.set(Calendar.DAY_OF_WEEK, 1)
                        byDay = "SU"
                    }

                    calendar.set(Calendar.HOUR_OF_DAY, workoutHour)
                    calendar.set(Calendar.MINUTE, workoutMinute)

                    val intent = Intent(Intent.ACTION_INSERT).apply {
                        data = android.provider.CalendarContract.Events.CONTENT_URI
                        putExtra(android.provider.CalendarContract.Events.TITLE, workout.workoutName)
//                        putExtra(android.provider.CalendarContract.Events.EVENT_LOCATION, "Gym")
                        putExtra(android.provider.CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.timeInMillis)
                       // putExtra(android.provider.CalendarContract.EXTRA_EVENT_END_TIME, calendar.timeInMillis + 60 * 60 * 1000)
                        // putExtra(android.provider.CalendarContract.Events.DESCRIPTION, "Workout with ${workout.workoutExercises.size} exercises.")
                        putExtra(CalendarContract.Events.RRULE, "FREQ=WEEKLY;BYDAY=${byDay}")
                    }

                    context.startActivity(intent)

                }
            )
            Spacer(modifier = Modifier.height(19.dp))

            Divider(
                color = SeparatorGrey,
                thickness = 1.dp,
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        AddNewWorkoutScheduleEdit(0) {
            showWorkoutModal = true
        }
    }

    if (showWorkoutModal) {
        Dialog(onDismissRequest = { showWorkoutModal = false }) {
            SingleWorkoutScheduleEdit(onModalClose = {showWorkoutModal = false})
        }
    }
}

@Composable
fun WorkoutScheduleRow(
        workoutWithExercises: ScheduledWorkoutWithExercises,
        navAddExercises: (Int) -> Unit,
        addScheduledWorkoutToCalendar: (ScheduledWorkoutWithExercises) -> Unit
    ) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column() {
            Text(
                text = workoutWithExercises.workoutName,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${workoutWithExercises.workoutDay}'s at ${workoutWithExercises.workoutTime}",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Add to Calendar",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = SecondPurple
                ),
                modifier = Modifier
                    .clickable {
                        addScheduledWorkoutToCalendar(workoutWithExercises)
                    }
            )
        }


        Text(
            text = "View Exercises",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = SecondPurple
            ),
            modifier = Modifier
                .clickable{
                    navAddExercises(workoutWithExercises.workoutPlanId)
                }
        )
    }
}















