package com.example.workoutapp.ui.screens

import android.text.style.TabStopSpan.Standard
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.workoutapp.ui.components.bottomNavItems
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.navigation.compose.currentBackStackEntryAsState
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// Enum to hold navigation routes
enum class AppScreen(val route: String) {
    Workouts("workouts"),
    WorkoutDetail("workoutDetail/{workout}"),
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
            // Variables to track the screen that is currently visible, so that the top bar can be changed for the different screens
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            when (navBackStackEntry?.destination?.route) {
                 AppScreen.Workouts.route -> {
                     TopAppBar(
                         title = {
                             Text("Workouts")
                         }
                     )
                 }
                AppScreen.PBs.route -> {
                    TopAppBar(
                        title = {
                            Text("PBs")
                        }
                    )
                }

                AppScreen.Settings.route -> {
                    TopAppBar(
                        title = {
                            Text("Settings")
                        }
                    )
                }

                AppScreen.WorkoutDetail.route -> {
                    val workoutName = navBackStackEntry?.arguments?.getString("workout")?.let {
                        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    } ?: "Workout"

                    TopAppBar(
                        title = {
                            Text(text = workoutName)
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack()}) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
             }
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
                        },
                        label = {
                            Text(item.name)
                        },
                        alwaysShowLabel = true
                    )
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
                WorkoutsScreen(
                    onClickWorkout = { workout ->
                        val encodedWorkout = URLEncoder.encode( workout, StandardCharsets.UTF_8.toString())
                        navController.navigate("workoutDetail/$encodedWorkout")
                    }
                )
            }

            composable(route = AppScreen.WorkoutDetail.route) { backStackEntry ->
                val workout = backStackEntry.arguments?.getString("workout")?.let {
                    URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                } ?: "No Data"
                WorkoutDetailScreen(workout)
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