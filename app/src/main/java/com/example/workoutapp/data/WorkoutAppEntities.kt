package com.example.workoutapp.data

// WorkoutAppEntities.kt contains all the Entities to be used for the database

import androidx.room.*

// Entity for scheduled workouts
@Entity(tableName = "scheduledWorkouts")
data class ScheduledWorkout(
    @PrimaryKey(autoGenerate = true) val workoutPlanId: Int = 0,
    val workoutName: String,
    val workoutDay: String,
    val workoutTime: String
)

// Entity for scheduled workout exercises
@Entity(
    tableName = "scheduledWorkoutExercises",
    foreignKeys = [
        ForeignKey(
            entity = ScheduledWorkout::class,
            parentColumns = ["workoutPlanId"],
            childColumns = ["workoutPlanId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlannedExercise::class,
            parentColumns = ["plannedExerciseId"],
            childColumns = ["plannedExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ScheduledWorkoutExercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workoutPlanId: Int,
    val plannedExerciseId: Int
)


// Entity for planned exercises
@Entity(tableName = "plannedExercise")
data class PlannedExercise(
    @PrimaryKey(autoGenerate = true) val plannedExerciseId: Int = 0,
    val exerciseName: String,
    val muscleGroup: MuscleGroup,
    val setNumber: Int
)

// Entity for workouts
@Entity(
    tableName = "workouts",
    foreignKeys = [
        ForeignKey(
            entity = ScheduledWorkout::class,
            parentColumns = ["workoutPlanId"],
            childColumns = ["workoutPlanId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Workout(
    @PrimaryKey(autoGenerate = true) val workoutId: Int = 0,
    val workoutDate: String,
    val workoutPlanId: Int
)

// Entity for exercises
@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["workoutId"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlannedExercise::class,
            parentColumns = ["plannedExerciseId"],
            childColumns = ["plannedExerciseId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true) val exerciseId: Int = 0,
    val workoutId: Int,
    val plannedExerciseId: Int,
    val weight: Float,
    val reps: Int,
    val pb: Boolean,
    //val setNumber: Int
)

