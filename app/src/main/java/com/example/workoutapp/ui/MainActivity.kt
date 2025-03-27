package com.example.workoutapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.workoutapp.ui.screens.NavScaffold
import com.example.workoutapp.ui.theme.WorkoutAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutAppTheme {
                NavScaffold()

            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkoutAppTheme {
//        Greeting("Android")
    }
}