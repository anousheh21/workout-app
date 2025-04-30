package com.example.workoutapp.ui.screens

// Settings.kt is not used in the final app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workoutapp.ui.theme.SeparatorGrey


@Composable
fun Settings(
    navEditSchedule: () -> Unit,
    navNotificationSettings: () -> Unit,
) {
    Column {
        SettingsRow("Edit Schedule", navEditSchedule)
        SettingsRow("Notifications", navNotificationSettings)

    }
}

@Composable
fun SettingsRow(
        name: String,
        onClickSetting: () -> Unit,
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickSetting() }
    ) {
        Text(
            text = name,
            modifier = Modifier
                .padding(
                    start = 32.dp,
                    top = 19.dp,
                    bottom = 21.dp
                )
        )
    }

    Divider(color = SeparatorGrey, thickness = 1.dp)
}