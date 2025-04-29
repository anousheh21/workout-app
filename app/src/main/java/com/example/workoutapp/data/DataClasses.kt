package com.example.workoutapp.data

// DataClasses.kt contains data classes which combines different values that are not part of entities so that information can be displayed clearly on screen

// WorkoutDetails Data Class
data class WorkoutDetails(
    val workoutId: Int,
    val workoutPlanId:Int,
    val workoutDate: String,
    val workoutName: String
)

// ExerciseWithName Data Class
data class ExerciseWithName(
    val exerciseId: Int,
    val workoutId: Int,
    val plannedExerciseId: Int,
    val exerciseName: String,
    val muscleGroup: MuscleGroup,
    val weight: Float,
    val reps: Int,
    val pb: Boolean
)

// ScheduledWorkoutWithExercises Data Class
data class ScheduledWorkoutWithExercises(
    val workoutPlanId: Int,
    val workoutName: String,
    val workoutDay: String,
    val workoutTime: String,
    val workoutExercises: List<PlannedExercise>
)