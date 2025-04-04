package com.example.workoutapp.ui.components.workout

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryText

@Composable
fun CurrentExerciseStatsInput(
    statsInput: String,
    onStatsValueChange: (String) -> Unit
) {
    TextField(
        value = statsInput,
        onValueChange = onStatsValueChange,
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
            .width(61.dp)
            .height(60.dp)
    )
}