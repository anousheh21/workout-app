package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.workoutapp.ui.components.editschedule.AddNewWorkoutScheduleEdit
import com.example.workoutapp.ui.components.editschedule.singleWorkout.SingleExerciseScheduleEdit
import com.example.workoutapp.ui.theme.ThirdPurple

@Composable
fun PBs() {
    val scrollState = rememberScrollState()

    var showExerciseModal by remember { mutableStateOf(false) }

    var nextExerciseId by remember { mutableStateOf(1) }
    val exerciseIds = remember { mutableStateListOf<Int>() }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            AddNewWorkoutScheduleEdit(nextExerciseId) {
                showExerciseModal = true
            }
            Spacer(modifier = Modifier.height(27.dp))
            SaveExercises()
            Spacer(modifier = Modifier.height(75.dp))
        }
    }

    if (showExerciseModal) {
        Dialog(onDismissRequest = { showExerciseModal = false}) {
            SingleExerciseScheduleEdit()
        }
    }
}

@Composable
fun SaveExercises() {
    Button(
        onClick = { saveExercises() },
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

fun saveExercises() {
    TODO("Not yet implemented")
}
