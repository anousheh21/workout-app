package com.example.workoutapp.provider

import android.net.Uri

// Contract to define the structure of the database so that the content provider can understand it

object WorkoutContract {
    const val AUTHORITY = "com.example.workoutapp.provider"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")

    // Contract for Scheduled Workouts
    object ScheduledWorkouts {
        const val PATH_SCHEDULED_WORKOUTS = "scheduledWorkouts"
        val CONTENT_URI: Uri = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SCHEDULED_WORKOUTS)

        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_SCHEDULED_WORKOUTS"
        const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_SCHEDULED_WORKOUTS"

        const val COLUMN_ID = "workoutPlanId"
        const val COLUMN_NAME = "workoutName"
        const val COLUMN_DAY = "workoutDay"
        const val COLUMN_TIME = "workoutTime"
    }

    // Contract for Planned Exercises
    object PlannedExercises {
        const val PATH_PLANNED_EXERCISES = "plannedExercises"
        val CONTENT_URI: Uri =  Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLANNED_EXERCISES)

        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_PLANNED_EXERCISES"
        const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_PLANNED_EXERCISES"

        const val COLUMN_ID = "plannedExerciseId"
        const val COLUMN_NAME = "exerciseName"
        const val COLUMN_GROUP = "muscleGroup"
        const val COLUMN_SET = "setNumber"
    }

    // Contract for Scheduled Workout Exercises
    object ScheduledWorkoutExercises {
        const val PATH_SCHEDULED_WORKOUT_EXERCISES = "scheduledWorkoutExercises"
        val CONTENT_URI: Uri =  Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SCHEDULED_WORKOUT_EXERCISES)

        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_SCHEDULED_WORKOUT_EXERCISES"
        const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_SCHEDULED_WORKOUT_EXERCISES"

        const val COLUMN_ID = "id"
        const val COLUMN_WORKOUT_PLAN_ID = "workoutPlanId"
        const val COLUMN_PLANNED_EXERCISE_ID = "plannedExerciseId"
    }

    // Contract for Workouts
    object Workouts {
        const val PATH_WORKOUTS = "workouts"
        val CONTENT_URI: Uri =  Uri.withAppendedPath(BASE_CONTENT_URI, PATH_WORKOUTS)

        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_WORKOUTS"
        const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_WORKOUTS"

        const val COLUMN_ID = "workoutId"
        const val COLUMN_DATE = "workoutDate"
        const val COLUMN_PLAN_ID = "workoutPlanId"
    }

    // Contract for Exercises
    object Exercises {
        const val PATH_EXERCISES = "exercises"
        val CONTENT_URI: Uri =  Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EXERCISES)

        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_EXERCISES"
        const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_EXERCISES"

        const val COLUMN_ID =  "exerciseId"
        const val COLUMN_WORKOUT_ID = "workoutId"
        const val COLUMN_PLANNED_EXERCISE_ID = "plannedExerciseId"
        const val COLUMN_WEIGHT = "weight"
        const val COLUMN_REPS = "reps"
        const val COLUMN_PB = "pb"
    }


}