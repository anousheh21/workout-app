package com.example.workoutapp.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Database
import com.example.workoutapp.data.DatabaseProvider
import com.example.workoutapp.data.ScheduledWorkout
import com.example.workoutapp.data.ScheduledWorkoutDao
import com.example.workoutapp.data.WorkoutDao
import java.lang.IllegalArgumentException

class WorkoutContentProvider: ContentProvider() {
    private lateinit var scheduledWorkoutDao: ScheduledWorkoutDao
    private lateinit var workoutDao: WorkoutDao

    override fun onCreate(): Boolean {
        // Get an instance of the workoutDAO, to use the Room database
        workoutDao = DatabaseProvider.getDatabase(context!!).workoutDao()
        // If provider was loaded successfully, return true
        return true
    }

    companion object {
        private const val SCHEDULED_WORKOUTS = 100
        private const val SCHEDULED_WORKOUT_ID = 101

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(WorkoutContract.AUTHORITY, WorkoutContract.ScheduledWorkouts.PATH_SCHEDULED_WORKOUTS, SCHEDULED_WORKOUTS)
            addURI(WorkoutContract.AUTHORITY, "${WorkoutContract.ScheduledWorkouts.PATH_SCHEDULED_WORKOUTS}/#", SCHEDULED_WORKOUT_ID)
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
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            SCHEDULED_WORKOUT_ID -> WorkoutContract.ScheduledWorkouts.CONTENT_ITEM_TYPE
            SCHEDULED_WORKOUTS -> WorkoutContract.ScheduledWorkouts.CONTENT_TYPE
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