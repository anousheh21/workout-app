package com.example.workoutapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.TopBarBack
import com.example.workoutapp.ui.components.bottomNavItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class AppScreen(val route: String) {
    Workouts("workouts"),
    // We’ll only pass the workoutId here, not a name:
    WorkoutDetail("workoutDetail/{workoutId}"),
    PBs("pbs"),
    Settings("settings"),
    EditSchedule("editSchedule"),
    EditScheduledExercises("editScheduledExercises/{workoutPlanId}"),
    NotificationSettings("notificationSettings"),

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavScaffold(
    navController: NavHostController = rememberNavController(),
    viewModel: WorkoutViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // We’ll need context for loading from DB
    val context = LocalContext.current

    Scaffold(
        topBar = {
            // Which route are we on?
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            when (navBackStackEntry?.destination?.route) {
                // ---- TOP BAR for Workouts ----
                AppScreen.Workouts.route -> {
                    TopAppBar(
                        title = { Text("Workouts") }
                    )
                }
                // ---- TOP BAR for PBs ----
                AppScreen.PBs.route -> {
                    TopAppBar(
                        title = { Text("Exercises") }
                    )
                }
                // ---- TOP BAR for Settings ----
                AppScreen.Settings.route -> {
                    TopAppBar(
                        title = { Text("Settings") }
                    )
                }
                // ---- TOP BAR for WorkoutDetail ----
                AppScreen.WorkoutDetail.route -> {
                    // Grab workoutId from arguments
                    val workoutIdParam = navBackStackEntry
                        ?.arguments
                        ?.getString("workoutId")
                        ?.toIntOrNull()

                    // If valid ID, we load from DB to get the actual name
                    if (workoutIdParam != null) {
                        // Trigger a side-effect that sets viewModel.selectedWorkoutName
                        LaunchedEffect(workoutIdParam) {
                            viewModel.loadWorkoutById(context, workoutIdParam)
                        }

                        TopAppBar(
                            title = {
                                // Show the name from the viewModel
                                Text(text = viewModel.selectedWorkoutName.ifBlank { "Workout" })
                            },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                    } else {
                        // If there's no valid ID in the route
                        TopAppBar(
                            title = {
                                Text(text = "Invalid Workout")
                            },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                    }
                }

                AppScreen.EditScheduledExercises.route -> {
                    val tempPlanId = navBackStackEntry
                        ?.arguments
                        ?.getString("workoutPlanId")
                        ?.toIntOrNull()

                    TopAppBar(
                        title = {
                            Text(text = "$tempPlanId")
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }

                AppScreen.NotificationSettings.route -> {
                    TopBarBack("Notification Settings", navController)
                }

                AppScreen.EditSchedule.route -> {
                    TopBarBack("Edit Schedule", navController)
                }
            }
        },
        bottomBar = {
            NavigationBar {
                val items = bottomNavItems()
                var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = (selectedItemIndex == index),
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.route)
                        },
                        icon = { Icon(painter = item.icon, contentDescription = item.name) },
                        label = { Text(item.name) },
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
            // ---- COMPOSABLE for Workouts ----
            composable(route = AppScreen.Workouts.route) {
                WorkoutsScreen(
                    onClickWorkout = { workout: WorkoutDetails ->
                        // Navigate with ID only
                        navController.navigate("workoutDetail/${workout.workoutId}")
                    }
                )
            }

            // ---- COMPOSABLE for WorkoutDetail ----
            composable(route = AppScreen.WorkoutDetail.route) { backStackEntry ->
                val workoutId = backStackEntry.arguments?.getString("workoutId")?.toIntOrNull()
                if (workoutId != null) {
                    // Show actual details screen
                    WorkoutDetailScreen(workoutId = workoutId)
                } else {
                    // ID is missing or not an integer
                    Text("Invalid workout ID")
                }
            }

            // ---- COMPOSABLE for PBs ----
            composable(route = AppScreen.PBs.route) {
                PBs()
            }

            // ---- COMPOSABLE for Settings ----
            composable(route = AppScreen.Settings.route) {
                Settings(
                    navEditSchedule = { navController.navigate(AppScreen.EditSchedule.route) },
                    navNotificationSettings = { navController.navigate(AppScreen.NotificationSettings.route) }
                )
            }

            composable(route = AppScreen.NotificationSettings.route) {
                NotificationSettingsScreen()
            }

            composable(route = AppScreen.EditSchedule.route) {
                EditScheduleScreen(
                    navAddExercises = { tempWorkoutPlanId: Int ->
                        // Navigate with ID only
                        navController.navigate("editScheduledExercises/${tempWorkoutPlanId}")
                    }
                )
            }

            composable(route = AppScreen.EditScheduledExercises.route) { backStackEntry ->
                val workoutPlanId = backStackEntry.arguments?.getString("workoutPlanId")?.toIntOrNull()
                if (workoutPlanId != null) {
                    EditScheduledExercises(workoutPlanId = workoutPlanId)
                } else {
                    Text("Invalid workout plan ID")
                }
            }
        }
    }
}