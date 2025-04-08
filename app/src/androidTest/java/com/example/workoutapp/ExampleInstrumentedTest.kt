package com.example.workoutapp

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workoutapp.data.MuscleGroup
import com.example.workoutapp.data.Workout
import com.example.workoutapp.provider.WorkoutContract

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.lang.IllegalArgumentException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(AndroidJUnit4::class)
//class ExampleInstrumentedTest {
//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.example.workoutapp", appContext.packageName)
//    }
//}

@RunWith(AndroidJUnit4::class)
class WorkoutContentProviderTest {

    private lateinit var context: Context
    private lateinit var resolver: ContentResolver

//    private val context = ApplicationProvider.getApplicationContext<Context>()
//    private val contentResolver = context.contentResolver
//
//    private val workoutUri = Uri.parse("content://com.example.workoutapp.provider/scheduledWorkouts")

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        resolver = context.contentResolver

        val values = ContentValues().apply {
            put(WorkoutContract.ScheduledWorkouts.COLUMN_NAME, "Initial Workout")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_DAY, "Monday")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_TIME, "09:00")
        }

        val plannedExerciseValues = ContentValues().apply {
            put(WorkoutContract.PlannedExercises.COLUMN_NAME, "Bench Press")
            put(WorkoutContract.PlannedExercises.COLUMN_GROUP, MuscleGroup.CHEST.toString())
            put(WorkoutContract.PlannedExercises.COLUMN_SET, 4)
        }

        val scheduledWorkoutExerciseValues = ContentValues().apply {
            put(WorkoutContract.ScheduledWorkoutExercises.COLUMN_WORKOUT_PLAN_ID, 1)
            put(WorkoutContract.ScheduledWorkoutExercises.COLUMN_PLANNED_EXERCISE_ID, 1)
        }

        val workoutValues = ContentValues().apply {
            put(WorkoutContract.Workouts.COLUMN_DATE, "01/05/25")
            put(WorkoutContract.Workouts.COLUMN_PLAN_ID, 1)
        }

        Log.d("setup", values.toString())
        val uri = resolver.insert(WorkoutContract.ScheduledWorkouts.CONTENT_URI, values)
        val plannedExerciseUri = resolver.insert(WorkoutContract.PlannedExercises.CONTENT_URI, plannedExerciseValues)
        val scheduledWorkoutExerciseUri = resolver.insert(WorkoutContract.ScheduledWorkoutExercises.CONTENT_URI, scheduledWorkoutExerciseValues)
        val workoutUri = resolver.insert(WorkoutContract.Workouts.CONTENT_URI, workoutValues)
    }

    // QUERY TESTS

    // Test to query the ScheduledWorkoutDao
    @Test
    fun testQueryAllScheduledWorkouts() {
        val cursor = resolver.query(WorkoutContract.ScheduledWorkouts.CONTENT_URI, null, null, null, null)
        assertNotNull(cursor)

        assertTrue("Cursor is empty", cursor!!.moveToFirst())

        do {
            val workoutPlanId = cursor.getInt(cursor.getColumnIndex(WorkoutContract.ScheduledWorkouts.COLUMN_ID))
            val workoutName = cursor.getString(cursor.getColumnIndex(WorkoutContract.ScheduledWorkouts.COLUMN_NAME))
            val workoutDay = cursor.getString(cursor.getColumnIndex(WorkoutContract.ScheduledWorkouts.COLUMN_DAY))
            val workoutTime = cursor.getString(cursor.getColumnIndex(WorkoutContract.ScheduledWorkouts.COLUMN_TIME))

            Log.d("testQueryAllScheduledWorkouts", "ID: $workoutPlanId, Name: $workoutName, Day: $workoutDay, Time: $workoutTime")

            assertTrue(workoutPlanId > 0)
            assertNotNull(workoutName)
            assertNotNull(workoutDay)
            assertNotNull(workoutTime)

        } while (cursor.moveToNext())

        cursor.close()
    }

    // Test to query the PlannedExerciseDao
    @Test
    fun testQueryAllPlannedExercises() {
        val cursor = resolver.query(WorkoutContract.PlannedExercises.CONTENT_URI, null, null, null, null,)
        assertNotNull(cursor)

        assertTrue("Cursor is empty", cursor!!.moveToFirst())

        do {
            val plannedExerciseId = cursor.getInt(cursor.getColumnIndex(WorkoutContract.PlannedExercises.COLUMN_ID))
            val exerciseName = cursor.getString(cursor.getColumnIndex(WorkoutContract.PlannedExercises.COLUMN_NAME))
            // val muscleGroup = cursor.getString(cursor.getColumnIndex(WorkoutContract.PlannedExercises.COLUMN_GROUP))
            val muscleGroup = MuscleGroup.valueOf(cursor.getString(cursor.getColumnIndex(WorkoutContract.PlannedExercises.COLUMN_GROUP)))
            val setNumber = cursor.getInt(cursor.getColumnIndex(WorkoutContract.PlannedExercises.COLUMN_SET))

            Log.d("testQueryAllPlannedExercises", "ID: $plannedExerciseId, Name: $exerciseName, Muscle Group: $muscleGroup, Set Number: $setNumber")

            assertTrue(plannedExerciseId > 0)
            assertNotNull(exerciseName)
            assertNotNull(muscleGroup)
            assertTrue(setNumber >= 0)
        } while (cursor.moveToNext())

        cursor.close()
    }

    // Test to query ScheduledWorkoutExerciseDao
    @Test
    fun testQueryAllScheduledWorkoutExercises() {
        val cursor = resolver.query(WorkoutContract.ScheduledWorkoutExercises.CONTENT_URI, null, null, null, null)
        assertNotNull(cursor)

        assertTrue("Cursor is empty", cursor!!.moveToFirst())

        do {
            val id = cursor.getInt(cursor.getColumnIndex(WorkoutContract.ScheduledWorkoutExercises.COLUMN_ID))
            val workoutPlanId = cursor.getInt(cursor.getColumnIndex(WorkoutContract.ScheduledWorkoutExercises.COLUMN_WORKOUT_PLAN_ID))
            val plannedExerciseId = cursor.getInt(cursor.getColumnIndex(WorkoutContract.ScheduledWorkoutExercises.COLUMN_PLANNED_EXERCISE_ID))

            Log.d("testQueryAllScheduledWorkoutExercises", "ID: $id, Workout Plan Id: $workoutPlanId, Planned Exercise Id: $plannedExerciseId")
            assertTrue(id > 0)
            assertTrue(workoutPlanId > 0)
            assertTrue(plannedExerciseId > 0)
        } while (cursor.moveToNext())

        cursor.close()
    }

    // Test to query WorkoutDao
    @Test
    fun testQueryAllWorkouts() {
        val cursor = resolver.query(WorkoutContract.Workouts.CONTENT_URI, null, null, null, null)
        assertNotNull(cursor)

        assertTrue("Cursor is empty", cursor!!.moveToFirst())

        do {
            val workoutId = cursor.getInt(cursor.getColumnIndex(WorkoutContract.Workouts.COLUMN_ID))
            val workoutDate = cursor.getString(cursor.getColumnIndex(WorkoutContract.Workouts.COLUMN_DATE))
            val workoutPlanId = cursor.getInt(cursor.getColumnIndex(WorkoutContract.Workouts.COLUMN_PLAN_ID))

            Log.d("testQueryAllWorkouts", "ID: $workoutId, WorkoutDate: $workoutDate, Workout Plan Id: $workoutPlanId")
            assertTrue(workoutId > 0)
            assertNotNull(workoutDate)
            assertTrue(workoutPlanId > 0)
        } while (cursor.moveToNext())

        cursor.close()
    }

    // INSERT TESTS

    @Test
    fun testInsertScheduledWorkout() {
        // Test inserting a scheduled workout
        val values = ContentValues().apply {
            put(WorkoutContract.ScheduledWorkouts.COLUMN_NAME, "Test Workout")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_DAY, "Wednesday")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_TIME, "06:30")
        }

        Log.d("testInsertScheduledWorkout", values.toString())
        val uri = resolver.insert(WorkoutContract.ScheduledWorkouts.CONTENT_URI, values)
        assertNotNull(uri)
        Log.d("testInsertScheduledWorkout", uri.toString())
        val scheduledWorkoutId = ContentUris.parseId(uri!!)
        Log.d("testInsertScheduledWorkout", scheduledWorkoutId.toString())
        assertTrue(scheduledWorkoutId > 0)
    }

    @Test
    fun testInsertPlannedExercise() {
        val values = ContentValues().apply {
            put(WorkoutContract.PlannedExercises.COLUMN_NAME, "Bicep Curls")
            put(WorkoutContract.PlannedExercises.COLUMN_GROUP, MuscleGroup.BICEPS.toString())
            put(WorkoutContract.PlannedExercises.COLUMN_SET, 3)
        }

        val uri = resolver.insert(WorkoutContract.PlannedExercises.CONTENT_URI, values)
        assertNotNull(uri)
        val plannedExerciseId = ContentUris.parseId(uri!!)
        assertTrue(plannedExerciseId > 0)
    }

    @Test
    fun testInsertScheduledWorkoutExercise() {
        // Insert ScheduledWorkout so we can use a foreign key
        val scheduledWorkoutValues = ContentValues().apply {
            put(WorkoutContract.ScheduledWorkouts.COLUMN_NAME, "Arms Workout")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_DAY, "Thursday")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_TIME, "12:30")
        }
        val scheduledWorkoutUri = resolver.insert(WorkoutContract.ScheduledWorkouts.CONTENT_URI, scheduledWorkoutValues)!!
        val scheduledWorkoutId = ContentUris.parseId(scheduledWorkoutUri).toInt()

        // Insert PlannedExercise so we can use the ID as a foreign key
        val plannedExerciseValues = ContentValues().apply {
            put(WorkoutContract.PlannedExercises.COLUMN_NAME, "Arnold Press")
            put(WorkoutContract.PlannedExercises.COLUMN_GROUP, MuscleGroup.SHOULDERS.toString())
            put(WorkoutContract.PlannedExercises.COLUMN_SET, 3)
        }
        val plannedExerciseUri = resolver.insert(WorkoutContract.PlannedExercises.CONTENT_URI, plannedExerciseValues)!!
        val plannedExerciseId = ContentUris.parseId(plannedExerciseUri).toInt()

        // Insert scheduledWorkoutExercise values using the values above, since this requires foreign keys
        val scheduledWorkoutExerciseValues = ContentValues().apply {
            put(WorkoutContract.ScheduledWorkoutExercises.COLUMN_WORKOUT_PLAN_ID, scheduledWorkoutId)
            put(WorkoutContract.ScheduledWorkoutExercises.COLUMN_PLANNED_EXERCISE_ID, plannedExerciseId)
        }
        val scheduledWorkoutExerciseUri = resolver.insert(WorkoutContract.ScheduledWorkoutExercises.CONTENT_URI, scheduledWorkoutExerciseValues)
        assertNotNull(scheduledWorkoutExerciseUri)
        val scheduledWorkoutExerciseId = ContentUris.parseId(scheduledWorkoutExerciseUri!!)
        assertTrue(scheduledWorkoutExerciseId > 0)
    }

    @Test
    fun testInsertWorkout() {
        // Insert ScheduledWorkout so we can use the ID as a foreign key
        val scheduledWorkoutValues = ContentValues().apply {
            put(WorkoutContract.ScheduledWorkouts.COLUMN_NAME, "Biceps Workout")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_DAY, "Monday")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_TIME, "12:45")
        }
        val scheduledWorkoutUri = resolver.insert(WorkoutContract.ScheduledWorkouts.CONTENT_URI, scheduledWorkoutValues)!!
        val scheduledWorkoutId = ContentUris.parseId(scheduledWorkoutUri).toInt()

        val workoutValues = ContentValues().apply {
            put(WorkoutContract.Workouts.COLUMN_DATE, "05/01/25")
            put(WorkoutContract.Workouts.COLUMN_PLAN_ID, scheduledWorkoutId)
        }
        val workoutUri = resolver.insert(WorkoutContract.Workouts.CONTENT_URI, workoutValues)
        assertNotNull(workoutUri)
        val workoutId = ContentUris.parseId(workoutUri!!)
        assertTrue(workoutId > 0)
    }

    // DELETE TESTS

    @Test
    fun testDeleteScheduledWorkout() {
        // Insert a scheduled workout first
        val uri = WorkoutContract.ScheduledWorkouts.CONTENT_URI
        val values = ContentValues().apply {
            put(WorkoutContract.ScheduledWorkouts.COLUMN_NAME, "Test Workout 2")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_DAY, "Tuesday")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_TIME, "10:45")
        }

        val insertUri = resolver.insert(uri, values)
        assertNotNull("Insert failed", insertUri)

        // Delete the scheduled workout
        val deleteCount = resolver.delete(insertUri!!, null, null)
        assertEquals("Delete failed", 1, deleteCount)

        val cursor = resolver.query(insertUri, null, null, null, null)
        assertNotNull(cursor)
        assertFalse("Scheduled workout should be deleted", cursor!!.moveToFirst())
        cursor.close()
    }

    @Test
    fun testDeletePlannedExercise() {
        val uri = WorkoutContract.PlannedExercises.CONTENT_URI
        val values = ContentValues().apply {
            put(WorkoutContract.PlannedExercises.COLUMN_NAME, "Lateral Raises")
            put(WorkoutContract.PlannedExercises.COLUMN_GROUP, MuscleGroup.SHOULDERS.toString())
            put(WorkoutContract.PlannedExercises.COLUMN_SET, 3)
        }

        val insertUri = resolver.insert(uri, values)
        assertNotNull("Insert failed", insertUri)

        // Delete the planned exercise
        val deleteCount = resolver.delete(insertUri!!, null, null)
        assertEquals("Delete failed", 1, deleteCount)

        val cursor = resolver.query(insertUri, null, null, null, null)
        assertNotNull(cursor)
        assertFalse("Scheduled workout should be deleted", cursor!!.moveToFirst())
        cursor.close()
    }

    @Test
    fun testDeleteScheduledWorkoutExercise() {
        // Insert ScheduledWorkout so we can use a foreign key
        val scheduledWorkoutValues = ContentValues().apply {
            put(WorkoutContract.ScheduledWorkouts.COLUMN_NAME, "Chest Workout")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_DAY, "Thursday")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_TIME, "12:00")
        }
        val scheduledWorkoutUri = resolver.insert(WorkoutContract.ScheduledWorkouts.CONTENT_URI, scheduledWorkoutValues)!!
        val scheduledWorkoutId = ContentUris.parseId(scheduledWorkoutUri).toInt()

        // Insert PlannedExercise so we can use the ID as a foreign key
        val plannedExerciseValues = ContentValues().apply {
            put(WorkoutContract.PlannedExercises.COLUMN_NAME, "Rear Deltoid Fly")
            put(WorkoutContract.PlannedExercises.COLUMN_GROUP, MuscleGroup.SHOULDERS.toString())
            put(WorkoutContract.PlannedExercises.COLUMN_SET, 3)
        }
        val plannedExerciseUri = resolver.insert(WorkoutContract.PlannedExercises.CONTENT_URI, plannedExerciseValues)!!
        val plannedExerciseId = ContentUris.parseId(plannedExerciseUri).toInt()

        val uri = WorkoutContract.ScheduledWorkoutExercises.CONTENT_URI
        // Insert scheduledWorkoutExercise values using the values above, since this requires foreign keys
        val scheduledWorkoutExerciseValues = ContentValues().apply {
            put(WorkoutContract.ScheduledWorkoutExercises.COLUMN_WORKOUT_PLAN_ID, scheduledWorkoutId)
            put(WorkoutContract.ScheduledWorkoutExercises.COLUMN_PLANNED_EXERCISE_ID, plannedExerciseId)
        }

        val insertUri = resolver.insert(uri, scheduledWorkoutExerciseValues)
        assertNotNull("Insert failed", insertUri)

        // Delete the scheduled workout exercise
        val deleteCount = resolver.delete(insertUri!!, null, null)
        assertEquals("Delete failed", 1, deleteCount)

        val cursor = resolver.query(insertUri, null, null, null, null)
        assertNotNull(cursor)
        assertFalse("Scheduled workout exercise should be deleted", cursor!!.moveToFirst())
        cursor.close()
    }

    @Test
    fun testDeleteWorkout() {
        // Insert ScheduledWorkout so we can use the ID as a foreign key
        val scheduledWorkoutValues = ContentValues().apply {
            put(WorkoutContract.ScheduledWorkouts.COLUMN_NAME, "Biceps Workout")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_DAY, "Monday")
            put(WorkoutContract.ScheduledWorkouts.COLUMN_TIME, "12:45")
        }
        val scheduledWorkoutUri = resolver.insert(WorkoutContract.ScheduledWorkouts.CONTENT_URI, scheduledWorkoutValues)!!
        val scheduledWorkoutId = ContentUris.parseId(scheduledWorkoutUri).toInt()

        val uri = WorkoutContract.Workouts.CONTENT_URI
        val workoutValues = ContentValues().apply {
            put(WorkoutContract.Workouts.COLUMN_DATE, "12/11/25")
            put(WorkoutContract.Workouts.COLUMN_PLAN_ID, scheduledWorkoutId)
        }

        val insertUri = resolver.insert(uri, workoutValues)
        assertNotNull("Insert failed", insertUri)

        // Delete the workout
        val deleteCount = resolver.delete(insertUri!!, null, null)
        assertEquals("Delete failed", 1, deleteCount)

        val cursor = resolver.query(insertUri, null, null, null, null)
        assertNotNull(cursor)
        assertFalse("Workout should be deleted", cursor!!.moveToFirst())
        cursor.close()

    }

    // MISCELLANEOUS TESTS

    @Test(expected = IllegalArgumentException::class)
    fun testQueryInvalidUri() {
        resolver.query(Uri.parse("content://com.example.workoutapp.provider/invalid"), null, null, null, null)
    }
}