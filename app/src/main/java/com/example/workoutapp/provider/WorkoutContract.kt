package com.example.workoutapp.provider

import android.net.Uri

object WorkoutContract {
    const val AUTHORITY = "com.example.workoutapp.provider"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")

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
}