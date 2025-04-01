package com.example.workoutapp.ui.screens

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.workoutapp.R
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.components.editschedule.AddExercisesButton
import com.example.workoutapp.ui.components.editschedule.AddToCalendarButton
import com.example.workoutapp.ui.components.editschedule.SingleWorkoutScheduleEdit
import com.example.workoutapp.ui.components.editschedule.TimeBox
import com.example.workoutapp.ui.components.editschedule.WorkoutDay
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

    var nextWorkoutPlanId by remember { mutableStateOf(1) }
    val workoutIds = remember { mutableStateListOf<Int>() }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        workoutIds.forEach { id ->
            SingleWorkoutScheduleEdit(navAddExercises = { navAddExercises(id) })
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            AddNewWorkoutScheduleEdit(nextWorkoutPlanId) { id ->
                workoutIds.add(id)
                nextWorkoutPlanId++
            }
            Spacer(modifier = Modifier.height(27.dp))
            SaveWorkoutSchedule()
            Spacer(modifier = Modifier.height(75.dp))
        }

        SingleExerciseScheduleEdit()
    }
}

@Composable
fun SingleExerciseScheduleEdit() {
    Column() {
        Spacer(modifier = Modifier.height(19.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(23.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 26.dp)
        ) {
            ExerciseInput()
            MuscleGroupDropDown()
        }
        Spacer(modifier = Modifier.height(21.dp))
        Divider(color = SeparatorGrey, thickness = 1.dp)
    }
}

@Composable
fun ExerciseInput() {
    var exerciseNameInput by remember {mutableStateOf("") }

    TextField(
        value = exerciseNameInput,
        onValueChange = { exerciseNameInput = it },
        label = { Text("Exercise") },
        textStyle = TextStyle(color = DarkText, fontSize = 16.sp),
        shape = RoundedCornerShape(3.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = PrimaryText,
            unfocusedContainerColor = PrimaryText,
            disabledContainerColor = PrimaryText,
            focusedLabelColor = DarkText,
            unfocusedLabelColor = DarkText,
        ),
        modifier = Modifier
            .width(143.dp)
            //.padding(top = 10.dp, bottom = 11.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleGroupDropDown() {
    var expanded by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedGroup,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Muscle Group") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            textStyle = TextStyle(color = DarkText, fontSize = 16.sp),
            shape = RoundedCornerShape(3.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = PrimaryText,
                unfocusedContainerColor = PrimaryText,
                disabledContainerColor = PrimaryText,
                focusedLabelColor = DarkText,
                unfocusedLabelColor = DarkText,
            ),
            modifier = Modifier
                .menuAnchor()
                .width(208.dp)
                .padding(end = 26.dp)
        )
        
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            for (group in MuscleGroup.entries) {
                DropdownMenuItem(
                    text = { Text(group.toTitleCase()) },
                    onClick = {
                        selectedGroup = group.toTitleCase()
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AddNewWorkoutScheduleEdit(
    nextWorkoutPlanId: Int,
    onAddNewRow: (Int) -> Unit
) {
    Button(
        onClick = { onAddNewRow(nextWorkoutPlanId) },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = SecondPurple),
        contentPadding = PaddingValues(start = 35.dp, end = 35.dp, top = 12.dp, bottom = 13.dp),
        modifier = Modifier
            .width(150.dp)
    ) {
        Text(
            text = "Add New",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
fun SaveWorkoutSchedule() {
    Button(
        onClick = { saveSchedule() },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ThirdPurple),
        contentPadding = PaddingValues(start = 30.dp, end = 30.dp, top = 12.dp, bottom = 12.dp),
        modifier = Modifier
            .width(102.dp)
    ) {
        Text(
            text = "Save",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

fun saveSchedule() {
    TODO("Not yet implemented")
}







