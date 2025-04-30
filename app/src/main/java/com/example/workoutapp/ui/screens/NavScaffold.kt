package com.example.workoutapp.ui.screens

// NavScaffold.kt contains the scaffolding for the app as well as the main navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
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
    val context = LocalContext.current

    // Variables to remember the state of whether the exercise and workout modals are showing or not
    var showExerciseModal by remember { mutableStateOf(false) }
    var showWorkoutModal by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Load scheduled workouts with exercises from the database via the ViewModel
        viewModel.loadScheduledWorkoutsWithExercises(context)
    }

    Scaffold(
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            // Checks which app screen is being shown, and displays the corresponding top bar
            when (navBackStackEntry?.destination?.route) {

                // Top bar for the WorkoutsScreen
                AppScreen.Workouts.route -> {
                    TopAppBar(
                        title = { Text("Workouts") },
                        actions = {
                            // Button to navigate to the NewWorkoutScreen, so that a user can start a workout from this page
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

                // Top bar for the scheduled exercises screen
                AppScreen.PBs.route -> {
                    TopAppBar(
                        title = { Text("Exercises") },
                        actions = {
                            AddNewWorkoutScheduleEdit(0) {
                                // Shows the modal that allows the user to add a new scheduled exercise
                                showExerciseModal = true
                            }
                        }
                    )
                }

                // Top bar for the scheduled workouts screen
                AppScreen.Settings.route -> {
                    TopAppBar(
                        title = { Text("Schedule") },
                        actions = {
                            AddNewWorkoutScheduleEdit(0) {
                                // Shows the modal that allows the user to add a new scheduled workout
                                showWorkoutModal = true
                            }
                        }
                    )
                }

                // Top bar for the current workout screen
                AppScreen.CurrentWorkout.route -> {

                    // We need the workout ID to display the workout name in the top bar, so we get the workout ID here
                    val workoutIdParam = navBackStackEntry
                        ?.arguments
                        ?.getString("workoutId")
                        ?.toIntOrNull()

                    // Load in the workout corresponding to the workout ID, if the workout ID exists
                    if (workoutIdParam != null) {
                        LaunchedEffect(workoutIdParam) {
                            viewModel.loadWorkoutById(context, workoutIdParam)
                        }

                        TopAppBar(
                            title = {
                                // Display the workout name corresponding to the workout ID above
                                Text(text = viewModel.selectedWorkoutName.ifBlank { "Workout" },
                                    modifier = Modifier.padding(start = 30.dp))
                            },
                            actions = {
                                // When the button is clicked, go back two screens (back to WorkoutScreen)
                                // This button ends the current workout
                                TextButton(onClick = {
                                    navController.popBackStack()
                                    navController.popBackStack()
                                }) {
                                    Text(
                                        text  = "End Workout",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            color = PrimaryColor,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(end = 10.dp)
                                    )
                                }
                            }
                        )
                    } else {
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

                // Top bar for WorkoutDetailScreen
                AppScreen.WorkoutDetail.route -> {

                    // Assign the workout ID parameter
                    val workoutIdParam = navBackStackEntry
                        ?.arguments
                        ?.getString("workoutId")
                        ?.toIntOrNull()

                    // If the workout ID was valid, load the corresponding workout from tha database via the ViewModel
                    if (workoutIdParam != null) {
                        LaunchedEffect(workoutIdParam) {
                            viewModel.loadWorkoutById(context, workoutIdParam)
                        }

                        TopAppBar(
                            title = {
                                // Display the name of the workout that corresponds to the workout ID that was assigned above
                                Text(text = viewModel.selectedWorkoutName.ifBlank { "Workout" })
                            },
                            // Back button to navigate to the previous screen
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            // Button to delete a workout
                            actions = {
                                IconButton( onClick ={
                                    viewModel.deleteWorkout(context, workoutIdParam)
                                    // When the workout is deleted, move to the previous screen (this will be WorkoutsScreen)
                                    navController.popBackStack()
                                },
                                    modifier = Modifier.padding(end = 35.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Workout",
                                        tint = PrimaryColor,
                                        modifier = Modifier
                                            .size(28.dp)
                                    )
                                }
                            }
                        )
                    } else {
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

                // Top bar for EditScheduledExercises screen
                AppScreen.EditScheduledExercises.route -> {

                    // Assign the workout plan ID to this variable, making it null if it is not a valid Int
                    val tempPlanId = navBackStackEntry
                        ?.arguments
                        ?.getString("workoutPlanId")
                        ?.toIntOrNull()

                    TopAppBar(
                        title = {
//                            Text(text = "$tempPlanId")
                            // Find the workout with the tempPlanId, and load it from the  database via the ViewModel
                            val workoutName = viewModel.scheduledWorkoutsWithExercises
                                .find { it.workoutPlanId == tempPlanId }
                                ?.workoutName

                            Text(text = workoutName ?: "Loading...")
                        },
                        // Back button
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

                // Top bar for NotificationSettingsScreen (this is not used in the app)
                AppScreen.NotificationSettings.route -> {
                    TopBarBack("Notification Settings", navController)
                }

                // Top bar for EditScheduleScreen
                AppScreen.EditSchedule.route -> {
                    TopBarBack("Edit Schedule", navController)
                }

                // Top bar for NewWorkoutScreen
                AppScreen.NewWorkout.route -> {
                    TopBarBack("Select Workout", navController)
                }
            }
        },
        bottomBar = {
            // The bottom bar is used to navigate between three screens: WorkoutsScreen, EditScheduleScreen, and PBs (the schedule exercises screen)
            NavigationBar {
                val items = bottomNavItems()

                // State to remember which bottom bar item has been selected
                var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

                // For each item for the bottom bar, display it, and handle its navigation
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = (selectedItemIndex == index),
                        onClick = {
                            selectedItemIndex = index
                            // Navigate to the corresponding screen
                            navController.navigate(item.route)
                        },
                        // Show the icon for that screen in the bottom bar
                        icon = { Icon(
                            painter = item.icon,
                            contentDescription = item.name,
                            tint = PrimaryText
                        ) },
                        // Provide a label for clarity
                        label = {
                            Text(
                                text = item.name,
                                fontWeight = if (selectedItemIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        alwaysShowLabel = true,
                        // Set colours of the bottom bar
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

        // If showExerciseModal is true, display the dialog
        if (showExerciseModal) {
            Dialog(
                onDismissRequest = {
                    showExerciseModal = false
                    // Refreshes the page so that any changes made via the modal reflect on the screen once the modal has been dismissed
                    viewModel.loadPlannedExercises(context)
                }
            ) {
                // The dialog allows the user to add a new scheduled exercise
                SingleExerciseScheduleEdit(
                    onModalClose = {
                        showExerciseModal = false
                        viewModel.loadPlannedExercises(context)
                        viewModel.triggerRefresh()
                    }
                )
            }
        }

        // If showWorkoutModal is true, display the dialog that allows the user to add a new scheduled workout to the database
        if (showWorkoutModal) {
            Dialog(onDismissRequest = { showWorkoutModal = false }) {
                SingleWorkoutScheduleEdit(onModalClose = {
                    showWorkoutModal = false
                })
            }
        }

        // NavHost code to define navigation
        NavHost(
            navController = navController,
            startDestination = AppScreen.Workouts.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Composable for the Workouts route
            composable(route = AppScreen.Workouts.route) {
                // Display WorkoutsScreen
                WorkoutsScreen(
                    // Pass in a function as a parameter that navigates to the WorkoutDetailScreen
                    onClickWorkout = { workout: WorkoutDetails ->
                        navController.navigate("workoutDetail/${workout.workoutId}")
                    }
                )
            }

            // Composable for the workout detail screen
            composable(route = AppScreen.WorkoutDetail.route) { backStackEntry ->
                // Load the relevant workout ID
                val workoutId = backStackEntry.arguments?.getString("workoutId")?.toIntOrNull()
                // If a valid workout ID exists, show the workout detail screen for that workout
                if (workoutId != null) {
                    WorkoutDetailScreen(workoutId = workoutId)
                } else {
                    Text("Invalid workout ID")
                }
            }

            // Composable for the scheduled exercises screen
            composable(route = AppScreen.PBs.route) {
                // Displays the scheduled exercises screen
                PBs()
            }

            // Composable for the scheduled workouts screen
            composable(route = AppScreen.Settings.route) {
                // Displays the scheduled workouts screen
                EditScheduleScreen(
                    // Passes a function as a parameter that allows the user to navigate to the workout's scheduled exercises screen
                    navAddExercises = { workoutPlanId: Int ->
                        navController.navigate("editScheduledExercises/$workoutPlanId")
                    }
                )
            }

            // Composable for the NotificationSettings route - this is not used in the app
            composable(route = AppScreen.NotificationSettings.route) {
                NotificationSettingsScreen()
            }

            // Composable for the EditSchedule route
            composable(route = AppScreen.EditSchedule.route) {
                // Display the EditScheduleScreen
                EditScheduleScreen(
                    // Passes a function as a parameter to navigate to the corresponding scheduled exercises screen
                    navAddExercises = { workoutPlanId: Int ->
                        navController.navigate("editScheduledExercises/${workoutPlanId}")
                    }
                )
            }

            // Composable for the EditScheduledExercises route
            composable(route = AppScreen.EditScheduledExercises.route) { backStackEntry ->
                // Get the relevant workout plan ID
                val workoutPlanId = backStackEntry.arguments?.getString("workoutPlanId")?.toIntOrNull()
                // If a valid workout plan ID exists, display the edit scheduled exercises screen for that workout
                if (workoutPlanId != null) {
                    EditScheduledExercises(workoutPlanId = workoutPlanId)
                } else {
                    Text("Invalid workout plan ID")
                }
            }

            // Composable for the NewWorkout route
            composable(route = AppScreen.NewWorkout.route) {
                // Display NewWorkoutScreen
                NewWorkoutScreen(
                    // Passes a function as a parameter that navigates to the current workout screen with the relevant workout ID
                    onClickStartWorkout = { workoutId: Int ->
                        navController.navigate("currentWorkout/${workoutId}")
                    }
                )
            }

            // Composable for the CurrentWorkout route
            composable(route = AppScreen.CurrentWorkout.route) { backStackEntry ->
                // Get the relevant workout ID
                val workoutId = backStackEntry.arguments?.getString("workoutId")?.toIntOrNull()
                // If the workout ID is valid, display the current workout screen with that workout ID
                if (workoutId != null) {
                    CurrentWorkoutScreen(workoutId = workoutId)
                } else {
                    Text("Invalid workout ID")
                }
            }
        }
    }
}