package com.example.workoutapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import com.example.workoutapp.ui.screens.AppScreen
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.workoutapp.R

data class BottomBarNavItem(
    val route: String,
    val name: String,
    val icon: Painter
)

@Composable
fun bottomNavBarItems(): List<BottomBarNavItem> {
    return listOf(
        BottomBarNavItem(AppScreen.Workouts.route, "Workouts", painterResource(R.drawable.dumbbell)),
        BottomBarNavItem(AppScreen.PBs.route, "PBs", painterResource(R.drawable.pbs)),
        BottomBarNavItem(AppScreen.Settings.route, "Settings", painterResource(R.drawable.settings))
    )
}
