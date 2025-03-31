//package com.example.workoutapp.ui.screens
//
//import android.text.style.TabStopSpan.Standard
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.ui.Modifier
//import com.example.workoutapp.ui.components.bottomNavItems
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.navigation.compose.currentBackStackEntryAsState
//import java.net.URLDecoder
//import java.net.URLEncoder
//import java.nio.charset.StandardCharsets
//
//// Enum to hold navigation routes
//enum class AppScreen(val route: String) {
//    Workouts("workouts"),
//    WorkoutDetail("workoutDetail/{workoutId}"),
//    PBs("pbs"),
//    Settings("settings")
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun NavScaffold(
//    navController: NavHostController = rememberNavController()
//) {
//    Scaffold(
//        topBar = {
//            // Variables to track the screen that is currently visible, so that the top bar can be changed for the different screens
//            val navBackStackEntry by navController.currentBackStackEntryAsState()
//
//            when (navBackStackEntry?.destination?.route) {
//                 AppScreen.Workouts.route -> {
//                     TopAppBar(
//                         title = {
//                             Text("Workouts")
//                         }
//                     )
//                 }
//                AppScreen.PBs.route -> {
//                    TopAppBar(
//                        title = {
//                            Text("PBs")
//                        }
//                    )
//                }
//
//                AppScreen.Settings.route -> {
//                    TopAppBar(
//                        title = {
//                            Text("Settings")
//                        }
//                    )
//                }
//
//                AppScreen.WorkoutDetail.route -> {
//                    val workoutName = navBackStackEntry?.arguments?.getString("workoutId")?.let {
//                        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
//                    } ?: "Workout"
//
//                    TopAppBar(
//                        title = {
//                            Text(text = workoutName)
//                        },
//                        navigationIcon = {
//                            IconButton(onClick = { navController.popBackStack()}) {
//                                Icon(
//                                    imageVector = Icons.Default.ArrowBack,
//                                    contentDescription = "Back"
//                                )
//                            }
//                        }
//                    )
//                }
//             }
//        },
//        bottomBar = {
//            NavigationBar {
//                val items = bottomNavItems()
//                var selectedItemIndex by rememberSaveable {
//                    mutableIntStateOf(0)
//                }
//                items.forEachIndexed { index, item ->
//                    NavigationBarItem(selected = selectedItemIndex == index,
//                        onClick = {
//                            selectedItemIndex = index
//                            navController.navigate(item.route)
//                        },
//                        icon = {
//                            Icon(
//                                painter = item.icon,
//                                contentDescription = item.name
//                            )
//                        },
//                        label = {
//                            Text(item.name)
//                        },
//                        alwaysShowLabel = true
//                    )
//                }
//            }
//        }
//    ) { innerPadding ->
//        NavHost(
//            navController = navController,
//            startDestination = AppScreen.Workouts.route,
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            composable(route = AppScreen.Workouts.route) {
//                WorkoutsScreen(
//                    onClickWorkout = { workout ->
//                        // val encodedWorkout = URLEncoder.encode( workout.workoutId.toString(), StandardCharsets.UTF_8.toString())
//                        navController.navigate("workoutDetail/${workout.workoutId}")
//                    }
//                )
//            }
//
//            composable(route = AppScreen.WorkoutDetail.route) { backStackEntry ->
////                val workout = backStackEntry.arguments?.getString("workoutId")?.let {
////                    URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
////                } ?: "No Data"
////                WorkoutDetailScreen(workout)
//
//                val workoutId = backStackEntry.arguments?.getString("workoutId")?.toIntOrNull()
//
//                if (workoutId != null) {
//                    WorkoutDetailScreen(workoutId = workoutId)
//                } else {
//                    Text("Invalid workout ID")
//                }
//            }
//
//            composable(route = AppScreen.PBs.route) {
//                PBs()
//            }
//
//            composable(route = AppScreen.Settings.route) {
//                Settings()
//            }
//        }
//    }
//}

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
import com.example.workoutapp.ui.components.bottomNavItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class AppScreen(val route: String) {
    Workouts("workouts"),
    // We’ll only pass the workoutId here, not a name:
    WorkoutDetail("workoutDetail/{workoutId}"),
    PBs("pbs"),
    Settings("settings")
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
                        title = { Text("PBs") }
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
                Settings()
            }
        }
    }
}