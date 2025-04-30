package com.example.workoutapp.ui.components.editschedule.singleWorkout

// ExerciseInput.kt contains the composable function that contains a text field which will be used to enter the name of an exercise to be added to the database

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.R
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryText

@Composable
fun ExerciseInput(
    exerciseNameInput: String,
    onValueChange: (String) -> Unit
) {

    // Text field that takes in the input for the exercise name the user wishes to add
    TextField(
        value = exerciseNameInput,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.exercise)) },
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