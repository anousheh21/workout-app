package com.example.workoutapp.ui.components.editschedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.ui.extensions.toTitleCase
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.SecondaryText

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
                Text(
                    text = "Save",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = PrimaryColor,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },

        title = { Text("Select Items") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                var filterDropDownExpanded by remember { mutableStateOf(false) }

                Box{
                    OutlinedButton(onClick = {filterDropDownExpanded = true})  {
                        Text(
                            text = muscleFilter?.toTitleCase() ?: "Filter By Type",
                            style = TextStyle(
                                color = PrimaryText
                            )
                        )
                    }

                    DropdownMenu(
                        expanded = filterDropDownExpanded,
                        onDismissRequest = { filterDropDownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All Types") },
                            onClick = {
                                muscleFilter = null
                                filterDropDownExpanded = false
                            }
                        )

                        MuscleGroup.entries.forEach { muscleGroupType ->
                            DropdownMenuItem(
                                text = { Text(muscleGroupType.toTitleCase()) },
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
                            Text(
                                text = item.exerciseName,
                                style = TextStyle(
                                    color = PrimaryText
                                )
                            )
                            Spacer(Modifier.weight(1f))
                            if (selectionIndex != null) {
                                Text(
                                    text = "#${selectionIndex + 1}",
//                                    //style = MaterialTheme.typography.labelSmall
                                    style = TextStyle(
                                        color = SecondaryText
                                    )
                                    // style = MaterialTheme.typography.labelSmall.copy(color = SecondaryText)
                                )
                            }
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = item.muscleGroup.toTitleCase(),
                                style = MaterialTheme.typography.labelSmall.copy(color = PrimaryText)
                            )
                        }
                    }

                    if (filteredOptions.isEmpty()) {
                        Text(
                            text = "No Results Found",
                            style = TextStyle(
                                color = PrimaryText,
                                fontSize = 16.sp
                            ),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            }
        }
    )
}