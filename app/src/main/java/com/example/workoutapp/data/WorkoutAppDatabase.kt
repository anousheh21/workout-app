package com.example.workoutapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        ScheduledWorkout::class,
        ScheduledWorkoutExercise::class,
        PlannedExercise::class,
        Workout::class,
        Exercise::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class WorkoutAppDatabase : RoomDatabase() {
    abstract fun scheduledWorkoutDao(): ScheduledWorkoutDao
    abstract fun scheduledWorkoutExerciseDao(): ScheduledWorkoutExerciseDao
    abstract fun plannedExerciseDao(): PlannedExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
}