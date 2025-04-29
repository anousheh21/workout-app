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
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.PlannedExercise
import com.example.workoutapp.data.ScheduledWorkout
import com.example.workoutapp.data.ScheduledWorkoutExercise
import com.example.workoutapp.data.ScheduledWorkoutWithExercises
import com.example.workoutapp.data.Workout
import com.example.workoutapp.data.WorkoutDetails
import com.example.workoutapp.ui.notifications.scheduleWorkoutNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class WorkoutViewModel() : ViewModel() {

    fun addNewPlannedExercise(context: Context, exercise: PlannedExercise) {
        val db = DatabaseProvider.getDatabase(context)
        val plannedExerciseDao = db.plannedExerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                plannedExerciseDao.insert(exercise)
                //val data = plannedExerciseDao.getAll()
               // _plannedExercisesArray.value = data
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error Inserting Exercises:", e)
            }
        }
    }

    var refreshTrigger by mutableStateOf(0)
        private set

    fun triggerRefresh() {
        refreshTrigger++
    }

//    private val _plannedExercisesArray = mutableStateOf<List<PlannedExercise>>(emptyList())
//    val plannedExercisesArray: List<PlannedExercise> get() = _plannedExercisesArray.value
//
//    fun loadPlannedExercises(context: Context) {
//        val db = DatabaseProvider.getDatabase(context)
//        val plannedExerciseDao = db.plannedExerciseDao()
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val data = plannedExerciseDao.getAll()
//                _plannedExercisesArray.value = data
//            } catch (e: Exception) {
//                Log.e("WorkoutViewModel", "Error Loading Planned Exercises", e)
//            }
//        }
//
//    }

private val _plannedExercisesArray = mutableStateOf<List<PlannedExercise>>(emptyList())
val plannedExercisesArray: List<PlannedExercise> get() = _plannedExercisesArray.value

fun loadPlannedExercises(context: Context) {
    val db = DatabaseProvider.getDatabase(context)
    val plannedExerciseDao = db.plannedExerciseDao()

    viewModelScope.launch(Dispatchers.IO) {
        // Collect from the Flow returned by plannedExerciseDao.getAll()
        plannedExerciseDao.getAll().collect { newList ->
            // Switch to Main thread to update UI state
            _plannedExercisesArray.value = newList
        }
    }
}

    fun deleteWorkout(context: Context, workoutId: Int) {
        viewModelScope.launch {
            val db = DatabaseProvider.getDatabase(context)
            db.exerciseDao().deleteExercisesForWorkout(workoutId)
            db.workoutDao().deleteWorkoutById(workoutId)

            // Reload updated workouts list
            loadWorkouts(context)
        }
    }

    fun insertExercise(
        context: Context,
        exercise: Exercise
    ) {
        val  db = DatabaseProvider.getDatabase(context)
        val exerciseDao = db.exerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                exerciseDao.insert(exercise)
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error Inserting New Exercise:", e)
            }
        }
    }

    private val _exercisesForWorkoutArray = mutableStateOf<List<Exercise>>(emptyList())
    val exercisesForWorkoutArray: List<Exercise> get() = _exercisesForWorkoutArray.value
    fun loadExercisesForWorkout(
        context: Context,
        workoutId: Int
    ) {
        val db = DatabaseProvider.getDatabase(context)
        val exerciseDao = db.exerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = exerciseDao.getExercisesForWorkout(workoutId)
                _exercisesForWorkoutArray.value = data
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error Retrieving Relevant Exercises:", e)
            }
        }
    }

    fun addExercisesToScheduledWorkout(
        context: Context,
        scheduledWorkoutExercises: List<ScheduledWorkoutExercise>
    ) {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                scheduledWorkoutExerciseDao.insertMultiple(scheduledWorkoutExercises)
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error Inserting Scheduled Workout:", e)
            }
        }
    }

    fun addNewScheduledWorkout(context: Context, workout: ScheduledWorkout) {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutDao = db.scheduledWorkoutDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                scheduledWorkoutDao.insert(workout)
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error Inserting Scheduled Workout:", e)
            }
        }
    }

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
                val insertedId = scheduledWorkoutDao.insertAndReturnId(workout).toInt()

                val scheduledWorkoutExercises = plannedExerciseIds.map { plannedId ->
                    ScheduledWorkoutExercise(
                        workoutPlanId = insertedId,
                        plannedExerciseId = plannedId
                    )
                }

                scheduledWorkoutExerciseDao.insertMultiple(scheduledWorkoutExercises)

                // SCHEDULE A NEW NOTIFICATION
                val workoutDay = workout.workoutDay
                val workoutTime = workout.workoutTime
                val workoutName = workout.workoutName


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

    suspend fun insertWorkoutAndReturnId(context: Context, workout: Workout): Int {
        val db = DatabaseProvider.getDatabase(context)
        val workoutDao = db.workoutDao()
        return workoutDao.insert(workout).toInt()
    }

//    private val _scheduledWorkoutsWithExercises = mutableStateOf<List<ScheduledWorkoutWithExercises>>(emptyList())
//    val scheduledWorkoutsWithExercises: List<ScheduledWorkoutWithExercises> get() = _scheduledWorkoutsWithExercises.value
//
//    fun loadScheduledWorkoutsWithExercises(context: Context) {
//        val db = DatabaseProvider.getDatabase(context)
//        val scheduledWorkoutDao = db.scheduledWorkoutDao()
//        val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val scheduledWorkouts = scheduledWorkoutDao.getAllScheduledWorkouts()
//                val data = scheduledWorkouts.map { scheduledWorkout ->
//                    val exercises = scheduledWorkoutExerciseDao.getExercisesForWorkout(scheduledWorkout.workoutPlanId)
//                    ScheduledWorkoutWithExercises(
//                        workoutPlanId = scheduledWorkout.workoutPlanId,
//                        workoutName = scheduledWorkout.workoutName,
//                        workoutDay = scheduledWorkout.workoutDay,
//                        workoutTime = scheduledWorkout.workoutTime,
//                        workoutExercises = exercises
//                    )
//                }
//                _scheduledWorkoutsWithExercises.value = data
//            } catch (e: Exception) {
//                Log.e("WorkoutViewModel", "Error Loading Scheduled workouts with exercises", e)
//            }
//        }
//    }

    fun deletePlannedExercise(exercise: PlannedExercise, context: Context) {
        val db = DatabaseProvider.getDatabase(context)
        val plannedExerciseDao = db.plannedExerciseDao()
        viewModelScope.launch(Dispatchers.IO){
            try {
                plannedExerciseDao.delete(exercise)
                loadPlannedExercises(context)
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error deleting planned exercise")
            }
        }
    }

    private val _scheduledWorkoutsWithExercises = mutableStateOf<List<ScheduledWorkoutWithExercises>>(emptyList())
    val scheduledWorkoutsWithExercises: List<ScheduledWorkoutWithExercises> get() = _scheduledWorkoutsWithExercises.value

    fun loadScheduledWorkoutsWithExercises(context: Context) {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutDao = db.scheduledWorkoutDao()
        val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                scheduledWorkoutDao.getAllScheduledWorkouts().collect { scheduledWorkouts ->
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
                    _scheduledWorkoutsWithExercises.value = data
                }
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error collecting scheduled workouts with exercises", e)
            }
        }
    }

    suspend fun isPlannedExerciseUsed(context: Context, exerciseId: Int): Boolean {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()
        return scheduledWorkoutExerciseDao.isPlannedExerciseUsed(exerciseId) > 0
    }

    suspend fun isScheduledWorkoutUse(context: Context, workoutPlanId: Int): Boolean {
        val db = DatabaseProvider.getDatabase(context)
        val workoutDao = db.workoutDao()
        return workoutDao.countWorkoutsByPlanId(workoutPlanId) > 0
    }


//    fun loadRelevantScheduledExerciseArray(context: Context, workoutPlanId: Int) {
//        thdnthd
//    }

    private val _scheduledWorkoutsArray = mutableStateOf<List<ScheduledWorkout>>(emptyList())
    val scheduledWorkoutsArray: List<ScheduledWorkout> get() = _scheduledWorkoutsArray.value

    fun loadScheduledWorkouts(context: Context) {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutDao = db.scheduledWorkoutDao()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = scheduledWorkoutDao.getAll()
                _scheduledWorkoutsArray.value = data
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "Error Retrieving Exercises:", e)
            }
        }
    }

    fun deleteScheduledWorkout(context: Context, schedWorkoutId: Int) {
        val db = DatabaseProvider.getDatabase(context)
        val scheduledWorkoutDao = db.scheduledWorkoutDao()
        val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()

        viewModelScope.launch(Dispatchers.IO) {
           try {
               scheduledWorkoutDao.delete(schedWorkoutId)
               loadScheduledWorkoutsWithExercises(context)
           } catch (e: Exception) {
               Log.e("WorkoutViewModel", "Error Deleting Scheduled Workout", e)
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

    // private val _exercisesForScheduledWorkout = mutableStateOf<

    // DUMMY DATA FOR DEVELOPMENT - CAN DELETE BEFORE SUBMISSION
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
//    fun seedDummyData(context: Context) {
//        val db = DatabaseProvider.getDatabase(context)
//        val scheduledWorkoutDao = db.scheduledWorkoutDao()
//        val workoutDao = db.workoutDao()
//        val plannedExerciseDao = db.plannedExerciseDao()
//        val scheduledWorkoutExerciseDao = db.scheduledWorkoutExerciseDao()
//        val exerciseDao = db.exerciseDao()
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                if (workoutDao.getAll().isEmpty()) {
//                    val plannedExercisesByType = mapOf(
//                        "Push" to listOf(
//                            PlannedExercise(exerciseName = "Bench Press", muscleGroup = MuscleGroup.CHEST, setNumber = 3),
//                            PlannedExercise(exerciseName = "Shoulder Press", muscleGroup = MuscleGroup.SHOULDERS, setNumber = 3)
//                        ),
//                        "Pull" to listOf(
//                            PlannedExercise(exerciseName = "Deadlift", muscleGroup = MuscleGroup.BACK, setNumber = 3),
//                            PlannedExercise(exerciseName = "Barbell Row", muscleGroup = MuscleGroup.BACK, setNumber = 3)
//                        ),
//                        "Legs" to listOf(
//                            PlannedExercise(exerciseName = "Squats", muscleGroup = MuscleGroup.LEGS, setNumber = 4),
//                            PlannedExercise(exerciseName = "Lunges", muscleGroup = MuscleGroup.LEGS, setNumber = 3)
//                        )
//                    )
//
//                    val scheduledWorkouts = listOf(
//                        ScheduledWorkout(workoutName = "Push", workoutDay = "Monday", workoutTime = "09:00"),
//                        ScheduledWorkout(workoutName = "Pull", workoutDay = "Wednesday", workoutTime = "10:00"),
//                        ScheduledWorkout(workoutName = "Legs", workoutDay = "Friday", workoutTime = "08:30")
//                    )
//
//                    val workoutDates = listOf("09/05/25", "10/05/25", "11/05/25")
//
//                    scheduledWorkouts.forEachIndexed { index, plan ->
//                        val planId = scheduledWorkoutDao.insertAndReturnId(plan).toInt()
//
//                        val plannedExercises = plannedExercisesByType[plan.workoutName] ?: emptyList()
//                        val plannedExerciseIds = plannedExercises.map {
//                            plannedExerciseDao.insert(it).toInt()
//                        }
//
//                        plannedExerciseIds.forEach { plannedId ->
//                            scheduledWorkoutExerciseDao.insert(
//                                ScheduledWorkoutExercise(
//                                    workoutPlanId = planId,
//                                    plannedExerciseId = plannedId
//                                )
//                            )
//                        }
//
//                        val workout = Workout(
//                            workoutDate = workoutDates[index],
//                            workoutPlanId = planId
//                        )
//                        val workoutId = workoutDao.insert(workout).toInt()

//                        plannedExerciseIds.forEach { plannedId ->
//                            exerciseDao.insert(
//                                Exercise(
//                                    workoutId = workoutId,
//                                    plannedExerciseId = plannedId,
//                                    weight = (50..100).random().toFloat(),
//                                    reps = (5..12).random(),
//                                    pb = listOf(true, false).random()
//                                )
//                            )
//                        }
                    }
//                    Log.d("WorkoutViewModel", "Dummy data seeded successfully")
//                }
//            } catch (e: Exception) {
//                Log.e("WorkoutViewModel", "Error seeding dummy data", e)
//            }
//        }
//    }
//    fun debugExercisesFor(context: Context, workoutId: Int) {
//        val db = DatabaseProvider.getDatabase(context)
//        val exerciseDao = db.exerciseDao()
//
//        viewModelScope.launch(Dispatchers.IO) {
//            // 1) Log all exercises in the database
//            val all = exerciseDao.getAll()
//            Log.d("ExerciseDebug", "---- All Exercises in DB: count=${all.size} ----")
//            all.forEachIndexed { index, ex ->
//                Log.d("ExerciseDebug", "[$index] exerciseId=${ex.exerciseId}, workoutId=${ex.workoutId}, " +
//                        "plannedExerciseId=${ex.plannedExerciseId}, reps=${ex.reps}, weight=${ex.weight}, pb=${ex.pb}")
//            }
//
//            // 2) Log only the exercises for a given workoutId
//            val exercisesForId = exerciseDao.getExercisesWithNamesForWorkout(workoutId)
//            Log.d("ExerciseDebug", "---- Exercises for workoutId=$workoutId: count=${exercisesForId.size} ----")
//            exercisesForId.forEachIndexed { index, ex ->
//                Log.d("ExerciseDebug", "[$index] exerciseId=${ex.exerciseId}, workoutId=${ex.workoutId}, " +
//                        "plannedExerciseId=${ex.plannedExerciseId}, name=${ex.exerciseName}, " +
//                        "reps=${ex.reps}, weight=${ex.weight}, pb=${ex.pb}")
//            }
//        }
//    }
//}