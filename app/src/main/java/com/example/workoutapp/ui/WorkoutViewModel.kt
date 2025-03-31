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
        fun seedDummyData(context: Context) {
            val db = DatabaseProvider.getDatabase(context)
            val scheduledWorkoutDao = db.scheduledWorkoutDao()
            val workoutDao = db.workoutDao()

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    if (workoutDao.getAll().isEmpty()) {
                        val scheduledWorkout = ScheduledWorkout(
                            workoutName = "Push",
                            workoutDay = "Monday",
                            workoutTime = "09:00"
                        )

                        // Insert the workout above, and return the ID
                        val insertedPlanId =
                            scheduledWorkoutDao.insertAndReturnId(scheduledWorkout).toInt()

                        val workout = Workout(
                            workoutDate = "09/05/25",
                            workoutPlanId = insertedPlanId
                        )

                        workoutDao.insert(workout)
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