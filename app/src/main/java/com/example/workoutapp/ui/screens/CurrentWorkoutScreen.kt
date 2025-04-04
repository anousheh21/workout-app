package com.example.workoutapp.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.data.Workout
import com.example.workoutapp.ui.WorkoutViewModel
import com.example.workoutapp.ui.extensions.toTitleCase
import com.example.workoutapp.ui.theme.DarkText
import com.example.workoutapp.ui.theme.PrimaryText
import com.example.workoutapp.ui.theme.ThirdPurple
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider

@Composable
fun CurrentWorkoutScreen(workoutId: Int) {
    // Load the workout, from the workout ID
    val vm: WorkoutViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.loadWorkoutById(context, workoutId)
        vm.loadScheduledWorkoutsWithExercises(context)

        // Load all relevant
        vm.loadExercisesForWorkout(context, workoutId)
    }

    val selectedWorkout = vm.selectedWorkout
    val selectedWorkoutName = vm.selectedWorkoutName

    val completedExercises = vm.exercisesForWorkoutArray

    // Get array of exercises associated with the workout plan that the workout is associated with
    val schedWorkoutWithExercises = vm.scheduledWorkoutsWithExercises
    val scheduledExerciseArray = schedWorkoutWithExercises
        .filter {
            it.workoutPlanId == (selectedWorkout?.workoutPlanId ?: -1)
        }
        .map { it.workoutExercises }

    val listToUse = scheduledExerciseArray.flatten()

    if (listToUse.isNotEmpty()) {
        if (selectedWorkout != null) {
            ExerciseSwipeScreen(listToUse, selectedWorkout, completedExercises)
        }
    } else {
        Text("No exercises found for this workout.")
    }

//    if (selectedWorkout != null) {
//        TestingDisplay(
//            workoutId,
//            selectedWorkoutName,
//            selectedWorkout,
//            scheduledExerciseArray
//        )
//    }

}

@Composable
fun SwipeScreenChild(
    exercise: PlannedExercise,
    index: Int,
    arraySize: Int,
    nextExercise: String,
    weightInput: String,
    onWeightValueChange: (String) -> Unit,
    repsInput: String,
    onRepsValueChange: (String) -> Unit,
    selectedWorkout: Workout,
    completedExercises: List<Exercise>,
    vm: WorkoutViewModel = viewModel()
) {
    Column() {
        Text(
            text = exercise.muscleGroup.toTitleCase(),
            textAlign = TextAlign.Center
        )
        Text(
            text = exercise.exerciseName,
            textAlign = TextAlign.Center
        )

        CurrentExerciseStatsInput(
            statsInput = weightInput,
            onStatsValueChange = onWeightValueChange
        )

        CurrentExerciseStatsInput(
            statsInput = repsInput,
            onStatsValueChange = onRepsValueChange
        )
        

        // Display all exercises, just as text for now
        var currentExercise = completedExercises.filter { it.plannedExerciseId == exercise.plannedExerciseId }

        val setsCompleted = currentExercise.size
        val setsPlanned = exercise.setNumber
        val canAddMoreSets = setsCompleted < setsPlanned


        NextSetButton(
            weightString = weightInput,
            repsString = repsInput,
            saveSet = { context, weightString: String, repsString: String ->
                val weight = weightString.toFloat()
                val reps = repsString.toInt()

                val newExerciseStats = Exercise(
                    workoutId = selectedWorkout.workoutId,
                    plannedExerciseId = exercise.plannedExerciseId,
                    weight = weight,
                    reps = reps,
                    pb = false
                )

                // Add the exercise to the database
                vm.insertExercise(context, newExerciseStats)

                // Load all exercises again to refresh the page
                vm.loadExercisesForWorkout(context, selectedWorkout.workoutId)

                // Clear input boxes
                onWeightValueChange("")
                onRepsValueChange("")
            },
           enabled = canAddMoreSets
        )

        // Display all exercises, just as text for now
        currentExercise = completedExercises.filter { it.plannedExerciseId == exercise.plannedExerciseId }




//        Column {
//            Text("Completed Sets:")
//
////            currentExercise.forEach { set ->
////                Text("• ${set.weight}kg x ${set.reps} reps${if (set.pb) " (PB!)" else ""}")
////            }
//
//
//        }

        CompletedExerciseTable(exercises = currentExercise, plannedExercise = exercise)

//        Text(text = "${exercise.setNumber}")

        Text(
            text = "Exercise: ${index + 1}/${arraySize}"
        )
        Text(
            text = "Next Exercise: $nextExercise",
        )
        Text(
            text = "Swipe right for next exercise, swipe left for previous exercise",
        )
        Text(
            text = "Swipe down to see previous stats"
        )
    }
}

@Composable
fun CompletedExerciseTable(
        exercises: List<Exercise>,
        plannedExercise: PlannedExercise,
    ) {

//    Column() {
//        Row() {
//            Text("Set")
//            Spacer(modifier = Modifier.width(20.dp))
//            Text("Weight")
//            Spacer(modifier = Modifier.width(20.dp))
//            Text("Reps")
//        }
//        Spacer(modifier = Modifier.height(20.dp))
//        for (i in 1..(plannedExercise.setNumber)) {
//            Row {
//                Text("${i}")
//
//                Spacer(modifier = Modifier.width(20.dp))
//
//                if (i <= exercises.size) {
//                    val exercise = exercises[i - 1]
//                    Text("${exercise.weight}")
//                    Spacer(modifier = Modifier.width(20.dp))
//                    Text("${exercise.reps}")
//                }
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//        }
//    }
    Column {
        // Header row
        Row {
            Row {
                TableCell("Set", bold = true, modifier = Modifier.weight(1f))
                TableCell("Weight", bold = true, modifier = Modifier.weight(1f))
                TableCell("Reps", bold = true, modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Data rows
        for (i in 1..plannedExercise.setNumber) {
            Row {
                TableCell("$i", modifier = Modifier.weight(1f))
                if (i <= exercises.size) {
                    val exercise = exercises[i - 1]
                    TableCell("${exercise.weight}", modifier = Modifier.weight(1f))
                    TableCell("${exercise.reps}", modifier = Modifier.weight(1f))
                } else {
                    TableCell(" ", modifier = Modifier.weight(1f))
                    TableCell(" ", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun TableCell(
    text: String,
    bold: Boolean = false,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .padding(4.dp),
        textAlign = TextAlign.Center,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
    )
}

//@Composable
//fun CompletedExerciseTable(exercises: List<Exercise>) {
//    val cellModifier = Modifier
//        .padding(2.dp)
//        .background(ThirdPurple)
//        .padding(vertical = 12.dp)
//
//    Column {
//        // Header Row: Set Numbers
//        Row {
//            Text(
//                text = "Set",
//                color = Color.White,
//                textAlign = TextAlign.Center,
//                modifier = cellModifier
//                    .weight(1f)
//            )
//            exercises.indices.forEach { i ->
//                Text(
//                    text = "${i + 1}",
//                    color = Color.White,
//                    textAlign = TextAlign.Center,
//                    modifier = cellModifier
//                        .weight(1f)
//                )
//            }
//        }
//
//        // Weight Row
//        Row {
//            Text(
//                text = "Weight/kg",
//                color = Color.White,
//                textAlign = TextAlign.Center,
//                modifier = cellModifier
//                    .weight(1f)
//            )
//            exercises.forEach {
//                Text(
//                    text = "${it.weight}",
//                    color = Color.White,
//                    textAlign = TextAlign.Center,
//                    modifier = cellModifier
//                        .weight(1f)
//                )
//            }
//        }
//
//        // Reps Row
//        Row {
//            Text(
//                text = "Reps",
//                color = Color.White,
//                textAlign = TextAlign.Center,
//                modifier = cellModifier
//                    .weight(1f)
//            )
//            exercises.forEach {
//                Text(
//                    text = "${it.reps}",
//                    color = Color.White,
//                    textAlign = TextAlign.Center,
//                    modifier = cellModifier
//                        .weight(1f)
//                )
//            }
//        }
//    }
//}

@Composable
fun CurrentExerciseStatsInput(
    statsInput: String,
    onStatsValueChange: (String) -> Unit
) {
    TextField(
        value = statsInput,
        onValueChange = onStatsValueChange,
        textStyle = TextStyle(color = DarkText, fontSize = 16.sp),
        shape = RoundedCornerShape(3.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = PrimaryText,
            unfocusedContainerColor = PrimaryText,
            disabledContainerColor = PrimaryText,
            focusedLabelColor = DarkText,
            unfocusedLabelColor = DarkText,
        ),
    )
}

@Composable
fun NextSetButton(
    weightString: String,
    repsString: String,
    saveSet: (Context, String, String) -> Unit,
    enabled: Boolean = true

    ) {
    val context = LocalContext.current
    Button(
        onClick = { saveSet(context, weightString, repsString) },
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ThirdPurple),
        contentPadding = PaddingValues(start = 27.dp, end = 27.dp, top = 10.dp, bottom = 10.dp),
        modifier = Modifier
            .width(119.dp)
    ) {
        Text(
            text = "Next Set",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
fun ExerciseSwipeScreen(
    scheduledExerciseArray: List<PlannedExercise>,
    selectedWorkout: Workout,
    completedExercises: List<Exercise>
) {
    var currentIndex by remember { mutableStateOf(0) }
    var accumulatedDrag by remember { mutableStateOf(0f) }
    val arrayLength = scheduledExerciseArray.size

    var weightInput by remember { mutableStateOf("") }
    var repsInput by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (accumulatedDrag > 100) {
                            if (currentIndex > 0) currentIndex--
                        } else if (accumulatedDrag < -100) {
                            if (currentIndex < scheduledExerciseArray.lastIndex) currentIndex++
                        }
                        accumulatedDrag = 0f
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        accumulatedDrag += dragAmount
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        val safeIndex = currentIndex.coerceIn(0, scheduledExerciseArray.lastIndex)

        val nextExercise: String = if (safeIndex < arrayLength - 1) {
            scheduledExerciseArray[safeIndex + 1].exerciseName
        } else {
            "End"
        }

        SwipeScreenChild(
            scheduledExerciseArray[safeIndex],
            safeIndex,
            arrayLength,
            nextExercise,
            weightInput = weightInput,
            onWeightValueChange = { weightInput = it },
            repsInput = repsInput,
            onRepsValueChange = { repsInput = it },
            selectedWorkout = selectedWorkout,
            completedExercises = completedExercises
        )
    }
}

@Composable
fun TestingDisplay(
    workoutId: Int,
    selectedWorkoutName: String,
    selectedWorkout: Workout,
    scheduledExerciseArray: List<List<PlannedExercise>>
    ) {

    Column() {
        Text("$workoutId")
        Text(selectedWorkoutName)
        Text("$selectedWorkout")
        Text("${selectedWorkout.workoutPlanId}")
        Text("$scheduledExerciseArray")
    }
}