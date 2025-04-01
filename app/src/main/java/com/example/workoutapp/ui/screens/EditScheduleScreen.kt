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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.workoutapp.R
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.components.editschedule.AddExercisesButton
import com.example.workoutapp.ui.components.editschedule.AddToCalendarButton
import com.example.workoutapp.ui.components.editschedule.SingleWorkoutScheduleEdit
import com.example.workoutapp.ui.components.editschedule.TimeBox
import com.example.workoutapp.ui.components.editschedule.WorkoutDay
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.SeparatorGrey
import java.util.Calendar



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScheduleScreen(navAddExercises: (Int) -> Unit ) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        SingleWorkoutScheduleEdit(navAddExercises)
        SingleWorkoutScheduleEdit(navAddExercises)

        AddNewWorkoutScheduleEdit()
        SaveWorkoutSchedule()
    }
}

@Composable
fun AddNewWorkoutScheduleEdit() {
    Button(
        onClick = { addNewRow() }
    ) {
        Text("Add New")
    }
}

fun addNewRow() {
    TODO("Not yet implemented")
}

@Composable
fun SaveWorkoutSchedule() {
    Button(
        onClick = { saveSchedule() }
    ) {
        Text("Save")
    }
}

fun saveSchedule() {
    TODO("Not yet implemented")
}







