package com.example.workoutapp.ui.components.editschedule

// SingleWorkoutScheduleEdit.kt holds the input boxes required for the user to be able to schedule a workout, and add the relevant information

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.data.ScheduledWorkout
import com.example.workoutapp.data.ScheduledWorkoutExercise
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.extensions.toTitleCase
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.SecondaryText
import com.example.workoutapp.ui.theme.SeparatorGrey
import com.example.workoutapp.ui.theme.ThirdPurple
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun SingleWorkoutScheduleEdit(
    // navAddExercises: (Int) -> Unit,
    vm: WorkoutViewModel = viewModel(),
    onModalClose: () -> Unit
) {
    // State to remember user input
    var workoutNameInput by remember { mutableStateOf("") }
    val selectedExercises = remember { mutableStateListOf<PlannedExercise>() }
    var selectedDay by remember { mutableStateOf("") }
    var workoutTime by remember { mutableStateOf("00:00") }

    Column (
        modifier = Modifier
            //.padding(start = 0.dp, top = 30.dp)
            .fillMaxWidth(),
        //horizontalAlignment = Alignment.Start

    ) {

        WorkoutNameInputField(
            workoutNameInput = workoutNameInput,
            onChangeValue = { workoutNameInput = it }
        )

        Row (
            horizontalArrangement = Arrangement.spacedBy(23.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            WorkoutDay(
                selectedDay = selectedDay,
                onDaySelected = { selectedDay = it }
            )
            TimeBox(
                workoutTime = workoutTime,
                onTimeChange = { workoutTime = it }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row (
            //horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                //.padding(start = 22.dp)
        ) {
//            AddExercisesButton(navAddExercises)
//            AddToCalendarButton()

            AddExercisesMultiSelect(
                selectedExercises = selectedExercises
            )
//            SelectedExercisesList(
//                selectedExercises = selectedExercises
//            )
        }

        Row (
            //horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
            //.padding(start = 22.dp)
        ) {
//            AddExercisesButton(navAddExercises)
//            AddToCalendarButton()


            SelectedExercisesList(
                selectedExercises = selectedExercises
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Take the users input and create a new ScheduledWorkout variable from it
        val newScheduledWorkout = ScheduledWorkout(
            workoutName = workoutNameInput,
            workoutDay = selectedDay,
            workoutTime = workoutTime
        )


        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Save the above ScheduledWorkout variable via functions in the ViewModel
            SaveScheduledWorkoutWithExercises(
                newScheduledWorkout = newScheduledWorkout,
                saveScheduledWorkoutWithExercises = {
                    val plannedExerciseIds = selectedExercises.map { it.plannedExerciseId }

                    vm.addNewScheduledWorkoutReturnId(
                        context = context,
                        workout = newScheduledWorkout,
                        plannedExerciseIds = plannedExerciseIds
                    )

//                vm.loadPlannedExercises(context)

                    onModalClose()
                }
            )
        }

        //Divider(color = SeparatorGrey, thickness = 1.dp)

    }
}









