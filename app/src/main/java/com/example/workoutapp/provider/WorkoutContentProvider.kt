package com.example.workoutapp.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
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
import java.lang.IllegalArgumentException

class WorkoutContentProvider: ContentProvider() {
    private lateinit var scheduledWorkoutDao: ScheduledWorkoutDao
    private lateinit var workoutDao: WorkoutDao
    private lateinit var plannedExerciseDao: PlannedExerciseDao
    private lateinit var scheduledWorkoutExerciseDao: ScheduledWorkoutExerciseDao
    private lateinit var exerciseDao: ExerciseDao

    override fun onCreate(): Boolean {
        // Get an instance of the workoutDAO, to use the Room database
        workoutDao = DatabaseProvider.getDatabase(context!!).workoutDao()
        scheduledWorkoutDao = DatabaseProvider.getDatabase(context!!).scheduledWorkoutDao()
        plannedExerciseDao = DatabaseProvider.getDatabase(context!!).plannedExerciseDao()
        scheduledWorkoutExerciseDao = DatabaseProvider.getDatabase(context!!).scheduledWorkoutExerciseDao()
        exerciseDao = DatabaseProvider.getDatabase(context!!).exerciseDao()
        // If provider was loaded successfully, return true
        return true
    }

    companion object {
        private const val SCHEDULED_WORKOUTS = 100
        private const val SCHEDULED_WORKOUT_ID = 101
        private const val PLANNED_EXERCISES = 102
        private const val PLANNED_EXERCISE_ID = 103
        private const val SCHEDULED_WORKOUT_EXERCISES = 104
        private const val SCHEDULED_WORKOUT_EXERCISE_ID = 105
        private const val WORKOUTS = 106
        private const val WORKOUT_ID = 107
        private const val EXERCISES = 108
        private const val EXERCISE_ID = 109

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(WorkoutContract.AUTHORITY, WorkoutContract.ScheduledWorkouts.PATH_SCHEDULED_WORKOUTS, SCHEDULED_WORKOUTS)
            addURI(WorkoutContract.AUTHORITY, "${WorkoutContract.ScheduledWorkouts.PATH_SCHEDULED_WORKOUTS}/#", SCHEDULED_WORKOUT_ID)
            addURI(WorkoutContract.AUTHORITY, WorkoutContract.PlannedExercises.PATH_PLANNED_EXERCISES, PLANNED_EXERCISES)
            addURI(WorkoutContract.AUTHORITY, "${WorkoutContract.PlannedExercises.PATH_PLANNED_EXERCISES}/#", PLANNED_EXERCISE_ID)
            addURI(WorkoutContract.AUTHORITY, WorkoutContract.ScheduledWorkoutExercises.PATH_SCHEDULED_WORKOUT_EXERCISES, SCHEDULED_WORKOUT_EXERCISES)
            addURI(WorkoutContract.AUTHORITY, "${WorkoutContract.ScheduledWorkoutExercises.PATH_SCHEDULED_WORKOUT_EXERCISES}/#", SCHEDULED_WORKOUT_EXERCISE_ID)
            addURI(WorkoutContract.AUTHORITY, WorkoutContract.Workouts.PATH_WORKOUTS, WORKOUTS)
            addURI(WorkoutContract.AUTHORITY, "${WorkoutContract.Workouts.PATH_WORKOUTS}/#", WORKOUT_ID)
            addURI(WorkoutContract.AUTHORITY, WorkoutContract.Exercises.PATH_EXERCISES, EXERCISES)
            addURI(WorkoutContract.AUTHORITY, "${WorkoutContract.Exercises.PATH_EXERCISES}/#", EXERCISE_ID)
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val match = uriMatcher.match(uri)
        return when (match) {
            SCHEDULED_WORKOUTS -> scheduledWorkoutDao.getAllScheduledWorkoutsCursor()
            SCHEDULED_WORKOUT_ID -> {
                val id = ContentUris.parseId(uri)
                scheduledWorkoutDao.getByIdCursor(id.toInt())
            }
            PLANNED_EXERCISES -> plannedExerciseDao.getAllPlannedExercises()
            PLANNED_EXERCISE_ID -> {
                val id = ContentUris.parseId(uri)
                plannedExerciseDao.getByIdCursor(id.toInt())
            }
            SCHEDULED_WORKOUT_EXERCISES -> scheduledWorkoutExerciseDao.getAllCursor()
            SCHEDULED_WORKOUT_EXERCISE_ID -> {
                val id = ContentUris.parseId(uri)
                scheduledWorkoutExerciseDao.getCursorById(id.toInt())
            }
            WORKOUTS -> workoutDao.getAllCursor()
            WORKOUT_ID -> {
                val id = ContentUris.parseId(uri)
                workoutDao.getWorkoutByIdCursor(id.toInt())
            }
            EXERCISES -> exerciseDao.getAllExercisesCursor()
            EXERCISE_ID -> {
                val id = ContentUris.parseId(uri)
                exerciseDao.getExerciseItemCursor(id.toInt())
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            SCHEDULED_WORKOUT_ID -> WorkoutContract.ScheduledWorkouts.CONTENT_ITEM_TYPE
            SCHEDULED_WORKOUTS -> WorkoutContract.ScheduledWorkouts.CONTENT_TYPE
            PLANNED_EXERCISE_ID -> WorkoutContract.PlannedExercises.CONTENT_ITEM_TYPE
            PLANNED_EXERCISES -> WorkoutContract.PlannedExercises.CONTENT_TYPE
            SCHEDULED_WORKOUT_EXERCISES -> WorkoutContract.ScheduledWorkoutExercises.CONTENT_ITEM_TYPE
            SCHEDULED_WORKOUT_EXERCISE_ID -> WorkoutContract.ScheduledWorkoutExercises.CONTENT_TYPE
            WORKOUTS -> WorkoutContract.Workouts.CONTENT_ITEM_TYPE
            WORKOUT_ID -> WorkoutContract.Workouts.CONTENT_TYPE
            EXERCISES -> WorkoutContract.Exercises.CONTENT_ITEM_TYPE
            EXERCISE_ID -> WorkoutContract.Exercises.CONTENT_TYPE
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val newId = when (uriMatcher.match(uri)) {
            SCHEDULED_WORKOUTS -> {
                val scheduledWorkout = ScheduledWorkout(
                    workoutName = values?.getAsString(WorkoutContract.ScheduledWorkouts.COLUMN_NAME) ?: "",
                    workoutDay = values?.getAsString(WorkoutContract.ScheduledWorkouts.COLUMN_DAY)  ?: "Monday",
                    workoutTime = values?.getAsString(WorkoutContract.ScheduledWorkouts.COLUMN_TIME) ?: "00:00"
                )
                scheduledWorkoutDao.insertForContProv(scheduledWorkout)
            }
            PLANNED_EXERCISES -> {
                val plannedExercise = PlannedExercise(
                    exerciseName = values?.getAsString(WorkoutContract.PlannedExercises.COLUMN_NAME) ?: "",
                    // muscleGroup = values?.getAsString(WorkoutContract.PlannedExercises.COLUMN_GROUP) ?: "",
                    muscleGroup = MuscleGroup.valueOf(values?.getAsString(WorkoutContract.PlannedExercises.COLUMN_GROUP) ?: "CHEST"),
                    setNumber = values?.getAsInteger(WorkoutContract.PlannedExercises.COLUMN_SET) ?: 3,
                )
                plannedExerciseDao.insertPlannedExercise(plannedExercise)
            }
            SCHEDULED_WORKOUT_EXERCISES -> {
                val scheduledWorkoutExercise = ScheduledWorkoutExercise(
                    workoutPlanId = values?.getAsInteger(WorkoutContract.ScheduledWorkoutExercises.COLUMN_WORKOUT_PLAN_ID) ?: 0,
                    plannedExerciseId = values?.getAsInteger(WorkoutContract.ScheduledWorkoutExercises.COLUMN_PLANNED_EXERCISE_ID) ?: 0
                )
                scheduledWorkoutExerciseDao.insertForContentProvider(scheduledWorkoutExercise)
            }
            WORKOUTS -> {
                val workout = Workout(
                    workoutDate = values?.getAsString(WorkoutContract.Workouts.COLUMN_DATE) ?: "",
                    workoutPlanId = values?.getAsInteger(WorkoutContract.Workouts.COLUMN_PLAN_ID) ?: 0
                )
                workoutDao.insertForContProv(workout)
            }
            EXERCISES -> {
                val exercise = Exercise(
                    workoutId = values?.getAsInteger(WorkoutContract.Exercises.COLUMN_WORKOUT_ID) ?: 0,
                    plannedExerciseId = values?.getAsInteger(WorkoutContract.Exercises.COLUMN_PLANNED_EXERCISE_ID) ?: 0,
                    weight = (values?.getAsFloat(WorkoutContract.Exercises.COLUMN_WEIGHT) ?: 0) as Float,
                    reps = values?.getAsInteger(WorkoutContract.Exercises.COLUMN_REPS) ?: 0,
                    pb = values?. getAsBoolean(WorkoutContract.Exercises.COLUMN_PB) ?: false
                )
                exerciseDao.insertExerciseContProv(exercise)
            }
            else -> throw IllegalArgumentException("Invalid URI for insert: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, newId)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val context = context ?: return 0
        val match = uriMatcher.match(uri)
        return when (match) {
            SCHEDULED_WORKOUT_ID -> {
                val scheduledWorkoutId = ContentUris.parseId(uri).toInt()
                // Retrieve the scheduled workout to be deleted
                val cursor = scheduledWorkoutDao.getByIdCursor(scheduledWorkoutId)
                if (cursor.moveToFirst()) {
                    val scheduledWorkout = ScheduledWorkout(
                        workoutPlanId = cursor.getInt(cursor.getColumnIndexOrThrow("workoutPlanId")),
                        workoutName = cursor.getString(cursor.getColumnIndexOrThrow("workoutName")),
                        workoutDay = cursor.getString(cursor.getColumnIndexOrThrow("workoutDay")),
                        workoutTime = cursor.getString(cursor.getColumnIndexOrThrow("workoutTime"))
                    )
                    cursor.close()
                    scheduledWorkoutDao.deleteForContProv(scheduledWorkout)
                }  else {
                    cursor.close()
                    0
                }
            }
            PLANNED_EXERCISE_ID -> {
                val plannedExerciseId = ContentUris.parseId(uri).toInt()
                val cursor = plannedExerciseDao.getByIdCursor(plannedExerciseId)
                if (cursor.moveToFirst()) {
                    val plannedExercise = PlannedExercise(
                        plannedExerciseId = cursor.getInt(cursor.getColumnIndexOrThrow("plannedExerciseId")),
                        exerciseName = cursor.getString(cursor.getColumnIndexOrThrow("exerciseName")),
                        // muscleGroup = cursor.getString(cursor.getColumnIndexOrThrow("muscleGroup")),
                        muscleGroup = MuscleGroup.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("muscleGroup"))),
                        setNumber = cursor.getInt(cursor.getColumnIndexOrThrow("setNumber"))
                    )
                    cursor.close()
                    plannedExerciseDao.deleteForContentProvider(plannedExercise)
                } else {
                    cursor.close()
                    0
                }
            }
            SCHEDULED_WORKOUT_EXERCISE_ID -> {
                val scheduledWorkoutExerciseId = ContentUris.parseId(uri).toInt()
                val cursor = scheduledWorkoutExerciseDao.getCursorById(scheduledWorkoutExerciseId)
                if (cursor.moveToFirst()) {
                    val scheduledWorkoutExercise = ScheduledWorkoutExercise(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        workoutPlanId = cursor.getInt(cursor.getColumnIndexOrThrow("workoutPlanId")),
                        plannedExerciseId = cursor.getInt(cursor.getColumnIndexOrThrow("plannedExerciseId"))
                    )
                    cursor.close()
                    scheduledWorkoutExerciseDao.deleteForContentProvider(scheduledWorkoutExercise)
                } else {
                    cursor.close()
                    0
                }
            }
            WORKOUT_ID -> {
                val workoutId = ContentUris.parseId(uri).toInt()
                val cursor = workoutDao.getWorkoutByIdCursor(workoutId)
                if (cursor.moveToFirst()) {
                    val workout = Workout(
                        workoutId = cursor.getInt(cursor.getColumnIndexOrThrow("workoutId")),
                        workoutDate = cursor.getString(cursor.getColumnIndexOrThrow("workoutDate")),
                        workoutPlanId = cursor.getInt(cursor.getColumnIndexOrThrow("workoutPlanId"))
                    )
                    cursor.close()
                    workoutDao.deleteForContProv(workout)
                } else {
                    cursor.close()
                    0
                }
            }
            EXERCISE_ID -> {
                val exerciseId = ContentUris.parseId(uri).toInt()
                val cursor = exerciseDao.getExerciseItemCursor(exerciseId)
                if (cursor.moveToFirst()) {
                    val exercise = Exercise(
                        exerciseId = cursor.getInt(cursor.getColumnIndexOrThrow("exerciseId")),
                        workoutId = cursor.getInt(cursor.getColumnIndexOrThrow("workoutId")),
                        plannedExerciseId = cursor.getInt(cursor.getColumnIndexOrThrow("plannedExerciseId")),
                        weight = cursor.getFloat(cursor.getColumnIndexOrThrow("weight")),
                        reps = cursor.getInt(cursor.getColumnIndexOrThrow("reps")),
                        pb = cursor.getInt(cursor.getColumnIndexOrThrow("pb")) != 0
                    )
                    cursor.close()
                    exerciseDao.deleteExerciseContProv(exercise)
                } else {
                    cursor.close()
                    0
                }
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        throw UnsupportedOperationException("Update operation is not supported")
    }

}