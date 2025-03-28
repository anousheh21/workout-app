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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.workoutapp.ui.components.bottomNavItems
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf

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
            NavigationBar {
                val items = bottomNavItems()
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }
                items.forEachIndexed { index, item ->
                    NavigationBarItem(selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.route)
                        },
                        icon = {
                            Icon(
                                painter = item.icon,
                                contentDescription = item.name
                            )
                        })
                }
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