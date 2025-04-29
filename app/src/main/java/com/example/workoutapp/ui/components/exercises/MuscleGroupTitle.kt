package com.example.workoutapp.ui.components.exercises

// MuscleGroupTitle.kt displays the muscle group as a title to be used in the exercises screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.ui.extensions.toTitleCase

@Composable
fun MuscleGroupTitle(muscleGroup: MuscleGroup) {
    Spacer(modifier = Modifier.height(36.dp))
    Text(
        text = muscleGroup.toTitleCase(),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier
            .padding(horizontal = 38.dp)
    )
    // Spacer(modifier = Modifier.height(5.dp))
}
