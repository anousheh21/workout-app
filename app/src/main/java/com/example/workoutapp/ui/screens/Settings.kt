package com.example.workoutapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workoutapp.ui.theme.SeparatorGrey


@Composable
fun Settings(

) {
    Column {
        SettingsRow("Edit Schedule")
        SettingsRow("Notifications")

    }
}

@Composable
fun SettingsRow(name: String) {
    Text(
        text = name,
        modifier = Modifier
            .padding(
                start = 32.dp,
                top = 19.dp,
                bottom = 21.dp
            )
    )

    Divider(color = SeparatorGrey, thickness = 1.dp)
}