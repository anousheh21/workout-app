package com.example.workoutapp.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import com.example.workoutapp.data.DatabaseProvider
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.ExerciseDao
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.data.PlannedExerciseDao
import com.example.workoutapp.data.ScheduledWorkout
import com.example.workoutapp.data.ScheduledWorkoutDao
import com.example.workoutapp.data.ScheduledWorkoutExercise
import com.example.workoutapp.data.ScheduledWorkoutExerciseDao
import com.example.workoutapp.data.Workout
import com.example.workoutapp.data.WorkoutDao
import com.example.workoutapp.data.WorkoutDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel() : ViewModel() {

        // DUMMY DATA FOR DEVELOPMENT
        fun clearAllWorkouts(context: Context) {
            val db = DatabaseProvider.getDatabase(context)
            val workoutDao = db.workoutDao()
            val scheduledWorkoutDao = db.scheduledWorkoutDao()

            viewModelScope.launch(Dispatchers.IO) {
                workoutDao.deleteAll()
                scheduledWorkoutDao.deleteAll()
                Log.d("WorkoutViewModel", "Cleared all workouts")
            }
        }

        fun seedDummyData(context: Context) {
            val db = DatabaseProvider.getDatabase(context)
            val scheduledWorkoutDao = db.scheduledWorkoutDao()
            val workoutDao = db.workoutDao()
            val plannedExerciseDao = db.plannedExerciseDao()
            val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()
            val exerciseDao = db.exerciseDao()

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    if (workoutDao.getAll().isEmpty()) {
                        val plannedExercisesByType = mapOf(
                            "Push" to listOf(
                                PlannedExercise(exerciseName = "Bench Press", muscleGroup = MuscleGroup.CHEST, setNumber = 3),
                                PlannedExercise(exerciseName = "Shoulder Press", muscleGroup = MuscleGroup.SHOULDERS, setNumber = 3)
                            ),
                            "Pull" to listOf(
                                PlannedExercise(exerciseName = "Deadlift", muscleGroup = MuscleGroup.BACK, setNumber = 3),
                                PlannedExercise(exerciseName = "Barbell Row", muscleGroup = MuscleGroup.BACK, setNumber = 3)
                            ),
                            "Legs" to listOf(
                                PlannedExercise(exerciseName = "Squats", muscleGroup = MuscleGroup.LEGS, setNumber = 4),
                                PlannedExercise(exerciseName = "Lunges", muscleGroup = MuscleGroup.LEGS, setNumber = 3)
                            )
                        )

                        val scheduledWorkouts = listOf(
                            ScheduledWorkout(workoutName = "Push", workoutDay = "Monday", workoutTime = "09:00"),
                            ScheduledWorkout(workoutName = "Pull", workoutDay = "Wednesday", workoutTime = "10:00"),
                            ScheduledWorkout(workoutName = "Legs", workoutDay = "Friday", workoutTime = "08:30")
                        )

                        val workoutDates = listOf("09/05/25", "10/05/25", "11/05/25")

                        scheduledWorkouts.forEachIndexed { index, plan ->
                            val planId = scheduledWorkoutDao.insertAndReturnId(plan).toInt()

                            val plannedExercises = plannedExercisesByType[plan.workoutName] ?: emptyList()
                            val plannedExerciseIds = plannedExercises.map {
                                plannedExerciseDao.insert(it).toInt()
                            }

                            plannedExerciseIds.forEach { plannedId ->
                                scheduledWorkoutExerciseDao.insert(
                                    ScheduledWorkoutExercise(
                                        workoutPlanId = planId,
                                        plannedExerciseId = plannedId
                                    )
                                )
                            }

                            val workout = Workout(
                                workoutDate = workoutDates[index],
                                workoutPlanId = planId
                            )
                            val workoutId = workoutDao.insert(workout).toInt()

                            plannedExerciseIds.forEach { plannedId ->
                                exerciseDao.insert(
                                    Exercise(
                                        workoutId = workoutId,
                                        plannedExerciseId = plannedId,
                                        weight = (50..100).random().toFloat(),
                                        reps = (5..12).random(),
                                        pb = listOf(true, false).random()
                                    )
                                )
                            }
                        }
                        Log.d("WorkoutViewModel", "Dummy data seeded successfully")
                    }
                } catch (e: Exception) {
                    Log.e("WorkoutViewModel", "Error seeding dummy data", e)
                }
            }
        }

        private val _workoutsArray = mutableStateOf<List<WorkoutDetails>>(emptyList())
        val workoutsArray: List<WorkoutDetails> get() = _workoutsArray.value

        fun loadWorkouts(context: Context) {
            val db = DatabaseProvider.getDatabase(context)
            val workoutDao = db.workoutDao()

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val data = workoutDao.getWorkoutsWithDetails()
                    _workoutsArray.value = data

                    Log.d("WorkoutViewModel", "Fetched ${data.size} workouts from DB")
                    data.forEach {
                        Log.d("WorkoutViewModel", "Workout: ${it.workoutName}, Date: ${it.workoutDate}")
                    }
                } catch (e: Exception) {
                    Log.e("WorkoutViewModel ","Error Loading workouts:", e)
                }
            }
        }
    fun debugExercisesFor(context: Context, workoutId: Int) {
        val db = DatabaseProvider.getDatabase(context)
        val exerciseDao = db.exerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
            // 1) Log all exercises in the database
            val all = exerciseDao.getAll()
            Log.d("ExerciseDebug", "---- All Exercises in DB: count=${all.size} ----")
            all.forEachIndexed { index, ex ->
                Log.d("ExerciseDebug", "[$index] exerciseId=${ex.exerciseId}, workoutId=${ex.workoutId}, " +
                        "plannedExerciseId=${ex.plannedExerciseId}, reps=${ex.reps}, weight=${ex.weight}, pb=${ex.pb}")
            }

            // 2) Log only the exercises for a given workoutId
            val exercisesForId = exerciseDao.getExercisesWithNamesForWorkout(workoutId)
            Log.d("ExerciseDebug", "---- Exercises for workoutId=$workoutId: count=${exercisesForId.size} ----")
            exercisesForId.forEachIndexed { index, ex ->
                Log.d("ExerciseDebug", "[$index] exerciseId=${ex.exerciseId}, workoutId=${ex.workoutId}, " +
                        "plannedExerciseId=${ex.plannedExerciseId}, name=${ex.exerciseName}, " +
                        "reps=${ex.reps}, weight=${ex.weight}, pb=${ex.pb}")
            }
        }
    }

    private val _selectedWorkout = mutableStateOf<Workout?>(null)
    val selectedWorkout: Workout? get() = _selectedWorkout.value

    var selectedWorkoutName by mutableStateOf("")

    fun loadWorkoutById(context: Context, workoutId: Int) {
        val db = DatabaseProvider.getDatabase(context)
        val workoutDao = db.workoutDao()
        val scheduledDao = db.scheduledWorkoutDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val workout = workoutDao.getById(workoutId)
                _selectedWorkout.value = workout

                // Get the workout name from scheduled workout
                val scheduledWorkout = scheduledDao.getById(workout.workoutPlanId)
                selectedWorkoutName = scheduledWorkout.workoutName
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error loading workout by ID", e)
            }
        }
    }
}