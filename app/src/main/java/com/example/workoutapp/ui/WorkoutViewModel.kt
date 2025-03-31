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
import com.example.workoutapp.data.ExerciseDao
import com.example.workoutapp.data.PlannedExerciseDao
import com.example.workoutapp.data.ScheduledWorkout
import com.example.workoutapp.data.ScheduledWorkoutDao
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
                Log.d("WorkoutViewModel", "🧹 Cleared all workouts")
            }
        }
        fun seedDummyData(context: Context) {
            val db = DatabaseProvider.getDatabase(context)
            val scheduledWorkoutDao = db.scheduledWorkoutDao()
            val workoutDao = db.workoutDao()

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    if (workoutDao.getAll().isEmpty()) {

                        val scheduledWorkouts = listOf(
                            ScheduledWorkout(workoutName = "Push", workoutDay = "Monday", workoutTime = "09:00"),
                            ScheduledWorkout(workoutName = "Pull", workoutDay = "Wednesday", workoutTime = "10:00"),
                            ScheduledWorkout(workoutName = "Legs", workoutDay = "Friday", workoutTime = "08:30")
                        )

                        val workoutDates = listOf("09/05/25", "10/05/25", "11/05/25")

                        scheduledWorkouts.forEachIndexed { index, plan ->
                            val planId = scheduledWorkoutDao.insertAndReturnId(plan).toInt()

                            val workout = Workout(
                                workoutDate = workoutDates[index],
                                workoutPlanId = planId
                            )

                            workoutDao.insert(workout)
                            Log.d("WorkoutViewModel", "Inserted workout for ${plan.workoutName}")
                        }
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