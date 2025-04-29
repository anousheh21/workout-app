package com.example.workoutapp.ui.components

// NumberBox.kt is the box on the page to display a completed workout - it displays the weight/reps for a particular exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.ui.theme.PrimaryText

@Composable
fun NumberBox(number: Int) {
    Box(
        modifier = Modifier
            .width(41.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(PrimaryText)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            color = Color.Black
        )
    }
}