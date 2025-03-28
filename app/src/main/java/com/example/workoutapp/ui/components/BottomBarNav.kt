package com.example.workoutapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import com.example.workoutapp.ui.screens.AppScreen
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.workoutapp.R

data class BottomNavigationItem(
    val route: String,
    val name: String,
    val icon: Painter
)

@Composable
fun bottomNavItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(AppScreen.Workouts.route, "Workouts", painterResource(R.drawable.dumbbell)),
        BottomNavigationItem(AppScreen.PBs.route, "PBs", painterResource(R.drawable.pbs)),
        BottomNavigationItem(AppScreen.Settings.route, "Settings", painterResource(R.drawable.settings))
    )
}
