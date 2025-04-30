package com.example.workoutapp.ui

// WorkoutViewModel is the ViewModel used for this app, to act as a bridge between the database and the UI
// It contains functions to allow the screens to make use of the database

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import com.example.workoutapp.data.DatabaseProvider
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.data.ScheduledWorkout
import com.example.workoutapp.data.ScheduledWorkoutExercise
import com.example.workoutapp.data.ScheduledWorkoutWithExercises
import com.example.workoutapp.data.Workout
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.notifications.scheduleWorkoutNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class WorkoutViewModel() : ViewModel() {

    // Function to add a new scheduled exercise to the database
    fun addNewPlannedExercise(context: Context, exercise: PlannedExercise) {
        val db = DatabaseProvider.getDatabase(context)
        val plannedExerciseDao = db.plannedExerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Insert the planned exerise
                plannedExerciseDao.insert(exercise)
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error Inserting Exercises:", e)
            }
        }
    }

    // Variable which triggers a screen refresh
    var refreshTrigger by mutableStateOf(0)
        private set

    // Function to refresh the UI when the refresh trigger is incremented
    fun triggerRefresh() {
        refreshTrigger++
    }

// Variables to hold an array of planned exercises
private val _plannedExercisesArray = mutableStateOf<List<PlannedExercise>>(emptyList())
val plannedExercisesArray: List<PlannedExercise> get() = _plannedExercisesArray.value

// Function to load the planned exercises
fun loadPlannedExercises(context: Context) {
    val db = DatabaseProvider.getDatabase(context)
    val plannedExerciseDao = db.plannedExerciseDao()

    viewModelScope.launch(Dispatchers.IO) {
        // Collect all planned exercises (use collect as a Flow is being used here)
        plannedExerciseDao.getAll().collect { newList ->
            _plannedExercisesArray.value = newList
        }
    }
}

    // Function to delete a workout with a specific workout ID
    fun deleteWorkout(context: Context, workoutId: Int) {
        viewModelScope.launch {
            val db = DatabaseProvider.getDatabase(context)
            db.exerciseDao().deleteExercisesForWorkout(workoutId)
            db.workoutDao().deleteWorkoutById(workoutId)

            // Once the workout has been deleted, reload the workouts to refresh the UI
            loadWorkouts(context)
        }
    }

    // Function to insert a new exercise
    fun insertExercise(
        context: Context,
        exercise: Exercise
    ) {
        val  db = DatabaseProvider.getDatabase(context)
        val exerciseDao = db.exerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Insert the exercise into the database
                exerciseDao.insert(exercise)
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error Inserting New Exercise:", e)
            }
        }
    }

    // Variables to hold an array of exercises
    private val _exercisesForWorkoutArray = mutableStateOf<List<Exercise>>(emptyList())
    val exercisesForWorkoutArray: List<Exercise> get() = _exercisesForWorkoutArray.value

    // Function to load exercises for a specific workout
    fun loadExercisesForWorkout(
        context: Context,
        workoutId: Int
    ) {
        val db = DatabaseProvider.getDatabase(context)
        val exerciseDao = db.exerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Get exercises for the workout that had its ID passed as a parameter
                val data = exerciseDao.getExercisesForWorkout(workoutId)
                // Assign the exercises just retrieved to the variable
                _exercisesForWorkoutArray.value = data
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error Retrieving Relevant Exercises:", e)
            }
        }
    }

    // Function ta add exercises to a scheduled workout
    fun addExercisesToScheduledWorkout(
        context: Context,
        scheduledWorkoutExercises: List<ScheduledWorkoutExercise>
    ) {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Insert all relevant exercises into the database
                scheduledWorkoutExerciseDao.insertMultiple(scheduledWorkoutExercises)
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error Inserting Scheduled Workout:", e)
            }
        }
    }

    // Function to add a new scheduled workout
    fun addNewScheduledWorkout(context: Context, workout: ScheduledWorkout) {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutDao = db.scheduledWorkoutDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Insert the scheduled workout into the database
                scheduledWorkoutDao.insert(workout)
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error Inserting Scheduled Workout:", e)
            }
        }
    }

    // Function to add a new scheduled workout and return its ID
    fun addNewScheduledWorkoutReturnId(
        context: Context,
        workout: ScheduledWorkout,
        plannedExerciseIds: List<Int>
    ) {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutDao = db.scheduledWorkoutDao()
        val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Insert workout and assign the returned ID to this variable
                val insertedId = scheduledWorkoutDao.insertAndReturnId(workout).toInt()

                val scheduledWorkoutExercises = plannedExerciseIds.map { plannedId ->
                    ScheduledWorkoutExercise(
                        workoutPlanId = insertedId,
                        plannedExerciseId = plannedId
                    )
                }

                scheduledWorkoutExerciseDao.insertMultiple(scheduledWorkoutExercises)

                // Schedule a new notification
                val workoutDay = workout.workoutDay
                val workoutTime = workout.workoutTime
                val workoutName = workout.workoutName


                // May the day to a calendar day variable
                val dayInt = when (workoutDay.lowercase()) {
                    "monday" -> Calendar.MONDAY
                    "tuesday" -> Calendar.TUESDAY
                    "wednesday" -> Calendar.WEDNESDAY
                    "thursday" -> Calendar.THURSDAY
                    "friday" -> Calendar.FRIDAY
                    "saturday" -> Calendar.SATURDAY
                    "sunday" -> Calendar.SUNDAY
                    else -> Calendar.MONDAY
                }


                scheduleWorkoutNotification(context, dayInt, workoutTime, workoutName, workoutDay)

            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error inserting scheduled workout with exercises", e)
            }
        }
    }

    // Function to insert a workout and return its ID
    suspend fun insertWorkoutAndReturnId(context: Context, workout: Workout): Int {
        val db = DatabaseProvider.getDatabase(context)
        val workoutDao = db.workoutDao()
        return workoutDao.insert(workout).toInt()
    }

    // Function to delete a planned exercise
    fun deletePlannedExercise(exercise: PlannedExercise, context: Context) {
        val db = DatabaseProvider.getDatabase(context)
        val plannedExerciseDao = db.plannedExerciseDao()
        viewModelScope.launch(Dispatchers.IO){
            try {
                // Delete the exercise from the database
                plannedExerciseDao.delete(exercise)
                // Reload the planned exercises so that the UI updates
                loadPlannedExercises(context)
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error deleting planned exercise")
            }
        }
    }

    // Variables to hold an array of scheduled workouts with exercises
    private val _scheduledWorkoutsWithExercises = mutableStateOf<List<ScheduledWorkoutWithExercises>>(emptyList())
    val scheduledWorkoutsWithExercises: List<ScheduledWorkoutWithExercises> get() = _scheduledWorkoutsWithExercises.value

    // Function to load scheduled workouts alongside their exercises
    fun loadScheduledWorkoutsWithExercises(context: Context) {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutDao = db.scheduledWorkoutDao()
        val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Collect all scheduled workouts (collect as we are using a Flow)
                scheduledWorkoutDao.getAllScheduledWorkouts().collectLatest { scheduledWorkouts ->
                    val data = scheduledWorkouts.map { scheduledWorkout ->
                        val exercises = scheduledWorkoutExerciseDao.getExercisesForWorkout(scheduledWorkout.workoutPlanId)
                        ScheduledWorkoutWithExercises(
                            workoutPlanId = scheduledWorkout.workoutPlanId,
                            workoutName = scheduledWorkout.workoutName,
                            workoutDay = scheduledWorkout.workoutDay,
                            workoutTime = scheduledWorkout.workoutTime,
                            workoutExercises = exercises
                        )
                    }
                    // Assign to the private variable
                    _scheduledWorkoutsWithExercises.value = data
                }
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error collecting scheduled workouts with exercises", e)
            }
        }
    }

    // Function to check if a planned exercise is used in a scheduled workout
    suspend fun isPlannedExerciseUsed(context: Context, exerciseId: Int): Boolean {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()
        return scheduledWorkoutExerciseDao.isPlannedExerciseUsed(exerciseId) > 0
    }

    // Function to check if a scheduled workout has been carried out
    suspend fun isScheduledWorkoutUse(context: Context, workoutPlanId: Int): Boolean {
        val db = DatabaseProvider.getDatabase(context)
        val workoutDao = db.workoutDao()
        return workoutDao.countWorkoutsByPlanId(workoutPlanId) > 0
    }


    // Variables to hold an array of scheduled workouts
    private val _scheduledWorkoutsArray = mutableStateOf<List<ScheduledWorkout>>(emptyList())
    val scheduledWorkoutsArray: List<ScheduledWorkout> get() = _scheduledWorkoutsArray.value

    // Function to load scheduled workouts
    fun loadScheduledWorkouts(context: Context) {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutDao = db.scheduledWorkoutDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Get all scheduled workouts from the database
                val data = scheduledWorkoutDao.getAll()
                _scheduledWorkoutsArray.value = data
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error Retrieving Exercises:", e)
            }
        }
    }

    // Function to delete a scheduled workout
    fun deleteScheduledWorkout(context: Context, schedWorkoutId: Int) {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutDao = db.scheduledWorkoutDao()
        val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
           try {
               // Delete the scheduled workout  from the database
               scheduledWorkoutDao.delete(schedWorkoutId)
               // Reload the scheduled workouts with their exercises to refresh the UI
               loadScheduledWorkoutsWithExercises(context)
           } catch (e: Exception) {
               Log.e("WorkoutViewModel", "Error Deleting Scheduled Workout", e)
           }
        }
    }

    // Variables to hold an array of workouts with their details
    private val _workoutsArray = mutableStateOf<List<WorkoutDetails>>(emptyList())
    val workoutsArray: List<WorkoutDetails> get() = _workoutsArray.value

    fun loadWorkouts(context: Context) {
        val db = DatabaseProvider.getDatabase(context)
        val workoutDao = db.workoutDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Load all workouts with their details
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

    // Variables to hold a selected workout
    private val _selectedWorkout = mutableStateOf<Workout?>(null)
    val selectedWorkout: Workout? get() = _selectedWorkout.value

    var selectedWorkoutName by mutableStateOf("")

    // Function to load a workout by its ID
    fun loadWorkoutById(context: Context, workoutId: Int) {
        val db = DatabaseProvider.getDatabase(context)
        val workoutDao = db.workoutDao()
        val scheduledDao = db.scheduledWorkoutDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Load the workout
                val workout = workoutDao.getById(workoutId)
                // Assign the loaded workout to the private variable
                _selectedWorkout.value = workout

                // Get the workout name from scheduled workout
                val scheduledWorkout = scheduledDao.getById(workout.workoutPlanId)
                selectedWorkoutName = scheduledWorkout.workoutName
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error loading workout by ID", e)
            }
        }
    }

    // private val _exercisesForScheduledWorkout = mutableStateOf<

    // Function to delete all workouts and all scheduled workouts
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
}