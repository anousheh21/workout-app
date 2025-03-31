package com.example.workoutapp.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// DELETE THESE DATA CLASSES, AS THEY ARE FOR DEVELOPMENT ONLY!!!
//data class dummyData(
//    val workoutName: String,
//    val workoutDate: String
//)

class WorkoutViewModel(
//    private val scheduledWorkoutDao: ScheduledWorkoutDao,
//    private val scheduledWorkoutExerciseDao: ScheduledWorkoutExerciseDao,
//    private  val plannedExerciseDao: PlannedExerciseDao,
//    private val workoutDao: WorkoutDao,
//    private val exerciseDao: ExerciseDao,
    ) : ViewModel() {

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

        private val _workoutsArray = mutableStateOf<List<Workout>>(emptyList())
        val workoutsArray: List<Workout> get() = _workoutsArray.value

        fun loadWorkouts(context: Context) {
            val db = DatabaseProvider.getDatabase(context)
            val workoutDao = db.workoutDao()

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val data = workoutDao.getAll()
                    _workoutsArray.value = data
                } catch (e: Exception) {
                    Log.e("WorkoutViewModel ","Error Loading workouts:", e)
                }
            }
        }



//    private val _workoutsArray = mutableListOf(
//        dummyData("Push", "21/03/25"),
//        dummyData("Pull", "19/03/25"),
//        dummyData("Legs", "18/03/25"),
//        dummyData("Push", "16/03/25"),
//        dummyData("Pull", "14/03/25")
//    )
//
//    val workoutsArray: List<dummyData> get() = _workoutsArray
}