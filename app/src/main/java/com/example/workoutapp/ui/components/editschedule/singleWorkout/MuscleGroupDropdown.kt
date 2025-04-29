package com.example.workoutapp.ui.components.editschedule.singleWorkout

// MuscleGroupDropDown.kt contains a composable which is used to create a drop down showing all the values that the MuscleGroup enum can hold

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.ui.extensions.toTitleCase
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleGroupDropDown(
    selectedGroup: MuscleGroup,
    onGroupSelected: (MuscleGroup) -> Unit
) {
    // Remembers whether the dropdown is showing or not
    var expanded by remember { mutableStateOf(false) }
    // var selectedGroup by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        // Text field set to read only to contain the selected value from the drop down
        TextField(
            value = selectedGroup.toTitleCase(),
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Muscle Group") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
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
                .menuAnchor()
                .fillMaxWidth()
                //.width(208.dp)
               // .padding(end = 26.dp)
        )

        // Drop down part that lists all the values that the muscle group enum can hold
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            for (group in MuscleGroup.entries) {
                DropdownMenuItem(
                    text = { Text(group.toTitleCase()) },
                    onClick = {
                        onGroupSelected(group)
                        expanded = false
                    }
                )
            }
        }
    }
}