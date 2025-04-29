package com.example.workoutapp.ui.components

// InstructionText.kt is a composable that will show the text that explains how to use a screen of the app - it is displayed before the user has added anything to the database from a screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InstructionText(
    text: String
) {
    Spacer(modifier = Modifier.height(15.dp))
    Text(
        text = text,
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 22.sp
        ),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )

}