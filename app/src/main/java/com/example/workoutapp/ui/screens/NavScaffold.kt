package com.example.workoutapp.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier


// Enum to hold navigation routes
enum class AppScreen(val route: String) {
    Workouts("workouts"),
    WorkoutDetail("workoutDetail/{workoutName}"),
    PBs("pbs"),
    Settings("settings")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavScaffold(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Workout App") })
        },
        bottomBar = {
            BottomAppBar {
                
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Workouts.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.Workouts.route) {
                WorkoutsScreen()
            }

            composable(route = AppScreen.WorkoutDetail.route) {
                WorkoutDetailScreen()
            }

            composable(route = AppScreen.PBs.route) {
                PBs()
            }

            composable(route = AppScreen.Settings.route) {
                Settings()
            }
        }
    }
}