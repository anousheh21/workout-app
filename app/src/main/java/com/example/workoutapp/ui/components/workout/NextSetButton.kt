package com.example.workoutapp.ui.components.workout

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.ui.theme.SecondPurple

@Composable
fun NextSetButton(
    weightString: String,
    repsString: String,
    saveSet: (Context, String, String) -> Unit,
    enabled: Boolean = true

) {
    val context = LocalContext.current
    Button(
        onClick = { saveSet(context, weightString, repsString) },
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = SecondPurple),
        contentPadding = PaddingValues(start = 27.dp, end = 27.dp, top = 10.dp, bottom = 10.dp),
        modifier = Modifier
            .width(119.dp)
    ) {
        Text(
            text = "Next Set",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}