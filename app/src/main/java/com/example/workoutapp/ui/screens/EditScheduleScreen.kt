package com.example.workoutapp.ui.screens

// EditScheduleScreen.kt is a screen that allows the user to view their scheduled workouts

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.R
import com.example.workoutapp.data.DatabaseProvider
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.ScheduledWorkoutExercise
import com.example.workoutapp.data.ScheduledWorkoutWithExercises
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.InstructionText
import com.example.workoutapp.ui.components.InstructionTitle
import com.example.workoutapp.ui.components.WorkoutScheduleRow
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
import kotlinx.coroutines.launch
import java.util.Calendar



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScheduleScreen(navAddExercises: (Int) -> Unit ) {
    // Variable to remember scroll state, to allow the screen to scroll
    val scrollState = rememberScrollState()
    var showWorkoutModal by remember { mutableStateOf(false) }

    // State variables to allow for the deletion of scheduled workouts
    var showDeleteModal by remember { mutableStateOf(false) }
    var schedWorkoutToDelete by remember { mutableStateOf<ScheduledWorkoutWithExercises?>(null) }

    var ableToDeleteCheck by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // ViewModel variables
    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // Load scheduled workouts with their exercises from the database via the ViewModel
        vm.loadScheduledWorkoutsWithExercises(context)
    }

    // Save data from the database from this ViewModel variable
    val scheduledWorkoutWithExercises = vm.scheduledWorkoutsWithExercises

    // Show instructions if there are no scheduled workouts to display
    if (scheduledWorkoutWithExercises.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(45.dp))
            InstructionTitle(text = stringResource(R.string.plan_your_workouts))
            InstructionText(text = stringResource(R.string.click_add_new_to_add_a_new_scheduled_workout_to_delete_a_scheduled_workout_press_and_hold))
        }
    } else {
        // If there are scheduled workouts to display, display them in a WorkoutScheduleRow
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

                        // Set the calendar day of the week
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

                        // Set the workout hour and minute to the calendar
                        calendar.set(Calendar.HOUR_OF_DAY, workoutHour)
                        calendar.set(Calendar.MINUTE, workoutMinute)

                        // Set intent for the calendar
                        val intent = Intent(Intent.ACTION_INSERT).apply {
                            data = android.provider.CalendarContract.Events.CONTENT_URI
                            putExtra(android.provider.CalendarContract.Events.TITLE, workout.workoutName)
                            putExtra(android.provider.CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.timeInMillis)
                            putExtra(CalendarContract.Events.RRULE, "FREQ=WEEKLY;BYDAY=${byDay}")
                        }

                        // Send intent to the outside app (to the calendar app)
                        context.startActivity(intent)

                    },
                    // On a long press, show a modal to delete the workout
                    onLongPressDelete = { workoutToDelete ->
                        schedWorkoutToDelete = workoutToDelete
                        showDeleteModal = true
                    }
                )
                Spacer(modifier = Modifier.height(19.dp))

                Divider(
                    color = SeparatorGrey,
                    thickness = 1.dp,
                )
            }
//        Spacer(modifier = Modifier.height(50.dp))
//        AddNewWorkoutScheduleEdit(0) {
//            showWorkoutModal = true
//        }
        }
    }

//    if (showWorkoutModal) {
//        Dialog(onDismissRequest = { showWorkoutModal = false }) {
//            SingleWorkoutScheduleEdit(onModalClose = {showWorkoutModal = false})
//        }
//    }


    // Used to display a dialog to delete a scheduled workout
    if (showDeleteModal && schedWorkoutToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteModal = false
                schedWorkoutToDelete = null
                ableToDeleteCheck = null
           },
            title = { Text(text = stringResource(R.string.delete_scheduled_workout))},
            text = {
                Text(text = ableToDeleteCheck ?: "Are you sure you want to delete '${schedWorkoutToDelete!!.workoutName}'?",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = PrimaryText,

                        ))
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch{
                        val db = DatabaseProvider.getDatabase(context)
                        val scheduledWorkoutDao = db.scheduledWorkoutDao()
                        val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()
                        val useCheck = vm.isScheduledWorkoutUse(context, schedWorkoutToDelete!!.workoutPlanId)
                        // Checks if the scheduled workout has been used (so carried out as a workout)
                        if (useCheck) {
                            // If it's been used, the user cannot delete it
                            ableToDeleteCheck = "This workout cannot be deleted as it has been carried out"
                        } else {
                            // If the scheduled workout has not been used, delete it from the database
                            schedWorkoutToDelete?.let { delWorkout ->
                                //vm.deleteScheduledWorkout(context, delWorkout.workoutPlanId)
                                scheduledWorkoutExerciseDao.deleteExercisesForScheduledWorkout(delWorkout.workoutPlanId)
                                scheduledWorkoutDao.delete(delWorkout.workoutPlanId)

                                vm.loadScheduledWorkoutsWithExercises(context)
                            }
                            // Hide the modal and reset the variables
                            showDeleteModal = false
                            schedWorkoutToDelete = null
                            ableToDeleteCheck = null
                        }
                    }
                }) {
                    Text(
                        text = stringResource(R.string.delete),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    )
                }

//                Button(onClick = {
//                    schedWorkoutToDelete?.let { delWorkout ->
//                        vm.deleteScheduledWorkout(context, delWorkout.workoutPlanId)
//                        vm.loadScheduledWorkoutsWithExercises(context)
//                    }
//                    showDeleteModal = false
//                    schedWorkoutToDelete = null
//                }) {
//                    Text("Delete")
//                }
            },
            dismissButton = {
                TextButton(onClick = {
                    // Dismiss the modal and dreset the variables
                    showDeleteModal = false
                    schedWorkoutToDelete = null
                    ableToDeleteCheck = null
                }) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryColor
                        )
                    )
                }
//                Button(onClick = {
//                    showDeleteModal = false
//                    schedWorkoutToDelete = null
//                }) {
//                    Text("Cancel")
//                }
            }
        )
    }
}

















