package com.example.workoutapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.components.TopBarBack
import com.example.workoutapp.ui.components.bottomNavItems
import com.example.workoutapp.ui.components.editschedule.AddNewWorkoutScheduleEdit
import com.example.workoutapp.ui.components.editschedule.SingleWorkoutScheduleEdit
import com.example.workoutapp.ui.components.editschedule.singleWorkout.SingleExerciseScheduleEdit
import com.example.workoutapp.ui.theme.BackgroundColor
import com.example.workoutapp.ui.theme.PrimaryColor
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.Purple40
import com.example.workoutapp.ui.theme.SeparatorGrey
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
    NewWorkout("newWorkout"),
    CurrentWorkout("currentWorkout/{workoutId}"),

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavScaffold(
    navController: NavHostController = rememberNavController(),
    viewModel: WorkoutViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // We’ll need context for loading from DB
    val context = LocalContext.current
    var showExerciseModal by remember { mutableStateOf(false) }
    var showWorkoutModal by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadScheduledWorkoutsWithExercises(context)
    }

    Scaffold(
        topBar = {
            // Which route are we on?
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            when (navBackStackEntry?.destination?.route) {
                // ---- TOP BAR for Workouts ----
                AppScreen.Workouts.route -> {
                    TopAppBar(
                        title = { Text("Workouts") },
                        actions = {
                            TextButton(onClick = { navController.navigate(AppScreen.NewWorkout.route) }) {
                                Text(
                                    text = "New Workout",
                                    style = TextStyle(
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryColor
                                    )
                                )
                            }
                        }
                    )
                }
                // ---- TOP BAR for PBs ----
                AppScreen.PBs.route -> {
                    TopAppBar(
                        title = { Text("Exercises") },
                        actions = {
                            AddNewWorkoutScheduleEdit(0) {
                                showExerciseModal = true
                            }
                        }
                    )
                }
                // ---- TOP BAR for Settings ----
                AppScreen.Settings.route -> {
                    TopAppBar(
                        title = { Text("Schedule") },
                        actions = {
                            AddNewWorkoutScheduleEdit(0) {
                                showWorkoutModal = true
                            }
                        }
                    )
                }

                AppScreen.CurrentWorkout.route -> {
                    val workoutIdParam = navBackStackEntry
                        ?.arguments
                        ?.getString("workoutId")
                        ?.toIntOrNull()

                    if (workoutIdParam != null) {
                        LaunchedEffect(workoutIdParam) {
                            viewModel.loadWorkoutById(context, workoutIdParam)
                        }

                        TopAppBar(
                            title = {
                                Text(text = viewModel.selectedWorkoutName.ifBlank { "Workout" })
                            },
                            navigationIcon = {
//                                IconButton(onClick = { navController.popBackStack() }) {
//                                    Icon(
//                                        imageVector = Icons.Default.ArrowBack,
//                                        contentDescription = "Back"
//                                    )
//                                }
                                TextButton(onClick = { navController.popBackStack() }) {
                                    Text("End Workout")
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
                            },
                            actions = {
                                IconButton( onClick ={
                                    viewModel.deleteWorkout(context, workoutIdParam)
                                    navController.popBackStack()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Workout",
                                        tint = Purple40
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
//                            Text(text = "$tempPlanId")
                            val workoutName = viewModel.scheduledWorkoutsWithExercises
                                .find { it.workoutPlanId == tempPlanId }
                                ?.workoutName

                            Text(text = workoutName ?: "Loading...")
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

                AppScreen.NewWorkout.route -> {
                    TopBarBack("Select Workout", navController)
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
                        icon = { Icon(
                            painter = item.icon,
                            contentDescription = item.name,
                            tint = PrimaryText
                        ) },
                        label = {
                            Text(
                                text = item.name,
                                fontWeight = if (selectedItemIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryText,
                            unselectedIconColor = PrimaryText,
                            selectedTextColor = PrimaryText,
                            unselectedTextColor = PrimaryText,
                            indicatorColor = BackgroundColor
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
//        if (showExerciseModal) {
//            Dialog(onDismissRequest = {
//                showExerciseModal = false
//                viewModel.loadPlannedExercises(context)
//            }) {
//                SingleExerciseScheduleEdit(onModalClose = { showExerciseModal = false })
//            }
//        }
        if (showExerciseModal) {
            Dialog(
                onDismissRequest = {
                    showExerciseModal = false
                    viewModel.loadPlannedExercises(context)
                }
            ) {
                SingleExerciseScheduleEdit(
                    onModalClose = {
                        showExerciseModal = false
                        viewModel.loadPlannedExercises(context)
                        viewModel.triggerRefresh()
                    }
                )
            }
        }

        if (showWorkoutModal) {
            Dialog(onDismissRequest = { showWorkoutModal = false }) {
                SingleWorkoutScheduleEdit(onModalClose = {
                    showWorkoutModal = false
                })
            }
        }

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
                EditScheduleScreen(
                    navAddExercises = { workoutPlanId: Int ->
                        navController.navigate("editScheduledExercises/$workoutPlanId")
                    }
                )
            }

            composable(route = AppScreen.NotificationSettings.route) {
                NotificationSettingsScreen()
            }

            composable(route = AppScreen.EditSchedule.route) {
                EditScheduleScreen(
                    navAddExercises = { workoutPlanId: Int ->
                        // Navigate with ID only
                        navController.navigate("editScheduledExercises/${workoutPlanId}")
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

            composable(route = AppScreen.NewWorkout.route) {
                NewWorkoutScreen(
                    onClickStartWorkout = { workoutId: Int ->
                        navController.navigate("currentWorkout/${workoutId}")
                    }
                )
            }

            composable(route = AppScreen.CurrentWorkout.route) { backStackEntry ->
                val workoutId = backStackEntry.arguments?.getString("workoutId")?.toIntOrNull()
                if (workoutId != null) {
                    // Show actual details screen
                    CurrentWorkoutScreen(workoutId = workoutId)
                } else {
                    // ID is missing or not an integer
                    Text("Invalid workout ID")
                }
            }
        }
    }
}