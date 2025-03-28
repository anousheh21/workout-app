package com.example.workoutapp.data

import androidx.room.*

@Entity(tableName = "scheduledWorkouts")
data class ScheduledWorkoutsEntity(
    @PrimaryKey(autoGenerate = true) val workoutPlanId: Int = 0,
    val workoutName: String,
    val workoutDay: String,
    val workoutTime: String
)

@Entity(
    tableName = "scheduledWorkoutsExercises",
    foreignKeys = [
        ForeignKey(
            entity = ScheduledWorkoutsEntity::class,
            parentColumns = ["workoutPlanId"],
            childColumns = ["workoutPlanId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlannedExercisesEntity::class,
            parentColumns = ["plannedExerciseId"],
            childColumns = ["plannedExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ScheduledWorkoutExercisesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workoutPlanId: Int,
    val plannedExerciseId: Int
)

@Entity(tableName = "plannedExercise")
data class PlannedExercisesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val exerciseName: String,
    val muscleGroup: MuscleGroup,
    val setNumber: Int
)

@Entity(
    tableName = "workouts",
    foreignKeys = [
        ForeignKey(
            entity = ScheduledWorkoutsEntity::class,
            parentColumns = ["workoutPlanId"],
            childColumns = ["workoutPlanId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class WorkoutsEntity(
    @PrimaryKey(autoGenerate = true) val workoutId: Int = 0,
    val workoutDate: String,
    val workoutPlanId: Int
)

@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutsEntity::class,
            parentColumns = ["workoutId"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlannedExercisesEntity::class,
            parentColumns = ["plannedExerciseId"],
            childColumns = ["plannedExeriseId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class ExercisesEntity(
    @PrimaryKey(autoGenerate = true) val exerciseId: Int = 0,
    val workoutId: Int,
    val plannedExerciseId: Int,
    val weight: Float,
    val reps: Int,
    val pb: Boolean
)

