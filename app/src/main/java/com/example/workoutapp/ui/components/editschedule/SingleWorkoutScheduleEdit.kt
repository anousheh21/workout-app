package com.example.workoutapp.ui.components.editschedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.extensions.toTitleCase
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.SeparatorGrey
import java.util.Calendar

@Composable
fun SingleWorkoutScheduleEdit(
    // navAddExercises: (Int) -> Unit,
    onModalClose: () -> Unit
) {
    var workoutNameInput by remember { mutableStateOf("") }
    val selectedExercises = remember { mutableStateListOf<PlannedExercise>() }
    var selectedDay by remember { mutableStateOf("") }

    // Time picker variables
    val currentTime = Calendar.getInstance()
    val initialHour = currentTime.get(Calendar.HOUR_OF_DAY)
    val initialMinute = currentTime.get(Calendar.MINUTE)

    Column (
        modifier = Modifier
            .padding(start = 17.dp, top = 30.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start

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
                initialHour,
                initialMinute,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row (
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 22.dp)
        ) {
//            AddExercisesButton(navAddExercises)
//            AddToCalendarButton()

            AddExercisesMultiSelect(
                selectedExercises = selectedExercises
            )
            SelectedExercisesList(
                selectedExercises = selectedExercises
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Divider(color = SeparatorGrey, thickness = 1.dp)

    }
}

@Composable
fun AddExercisesMultiSelect(
    selectedExercises: MutableList<PlannedExercise>,
    vm : WorkoutViewModel = viewModel()
) {
    // Read in list of planned exercises from database
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.loadPlannedExercises(context)
    }

    val plannedExercisesArray = vm.plannedExercisesArray


    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Button(onClick = {showDialog = true}) {
            Text("Open MultiSelect Dialog")
        }

//        SelectedExercisesList(
//            selectedExercises = selectedExercises
//        )

        if (showDialog) {
            ExerciseSelectDialog(
                plannedExercisesArray = plannedExercisesArray,
                selectedExercises = selectedExercises,
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun SelectedExercisesList(
    selectedExercises: List<PlannedExercise>
) {
    Text(
        text = "Selected: ${selectedExercises.joinToString {it.exerciseName}}",
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
fun ExerciseSelectDialog(
    plannedExercisesArray: List<PlannedExercise>,
    selectedExercises: MutableList<PlannedExercise>,
    onDismiss: () -> Unit
) {
    var muscleFilter by remember { mutableStateOf<MuscleGroup?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        },

        title = {Text("Select Items")},
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                var filterDropDownExpanded by remember { mutableStateOf(false) }

                Box{
                    OutlinedButton(onClick = {filterDropDownExpanded = true})  {
                        Text(muscleFilter?.name ?: "Filter By Type")
                    }

                    DropdownMenu(
                        expanded = filterDropDownExpanded,
                        onDismissRequest = { filterDropDownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All Types")},
                            onClick = {
                                muscleFilter = null
                                filterDropDownExpanded = false
                            }
                        )

                        MuscleGroup.entries.forEach { muscleGroupType ->
                            DropdownMenuItem(
                                text = {Text(muscleGroupType.toTitleCase())},
                                onClick = {
                                    muscleFilter = muscleGroupType
                                    filterDropDownExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))

                val filteredOptions = plannedExercisesArray.filter {
                    (muscleFilter == null || it.muscleGroup == muscleFilter)
                }

                Column(
                    modifier = Modifier
                        .heightIn(max = 300.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    filteredOptions.forEach { item ->
                        val selectionIndex = selectedExercises.indexOf(item).takeIf { it >= 0}
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (selectedExercises.contains(item)) {
                                        selectedExercises.remove(item)
                                    } else {
                                        selectedExercises.add(item)
                                    }
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = selectedExercises.contains(item),
                                onCheckedChange = {
                                    if (it) selectedExercises.add(item)
                                    else selectedExercises.remove(item)
                                }
                            )
                            Text(text = item.exerciseName)
                            Spacer(Modifier.weight(1f))
                            if (selectionIndex != null) {
                                Text("#${selectionIndex + 1}", style = MaterialTheme.typography.labelSmall)
                            }
                            Spacer(Modifier.weight(1f))
                            Text(text = item.muscleGroup.toTitleCase(), style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    if (filteredOptions.isEmpty()) {
                        Text(
                            "No Results Found",
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            }
        }
    )
}

