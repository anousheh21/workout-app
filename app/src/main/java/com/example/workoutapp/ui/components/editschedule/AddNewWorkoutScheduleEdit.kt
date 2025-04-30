package com.example.workoutapp.ui.components.editschedule

// AddNewWorkoutScheduleEdit.kt contains a button that allows the user to add a new scheduled workout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.R
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.SecondPurple

@Composable
fun AddNewWorkoutScheduleEdit(
    id: Int,
    onAddNewRow: (Int) -> Unit
) {
    TextButton(
        onClick = { onAddNewRow(id) },
//        shape = RoundedCornerShape(10.dp),
//        colors = ButtonDefaults.buttonColors(containerColor = SecondPurple),
//        contentPadding = PaddingValues(start = 35.dp, end = 35.dp, top = 12.dp, bottom = 13.dp),
//        modifier = Modifier
//            .width(150.dp)
    ) {
        Text(
            text = stringResource(R.string.add_new),
            style = TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )
        )
    }
}