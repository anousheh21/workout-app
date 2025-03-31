package com.example.workoutapp.data

data class WorkoutDetails(
    val workoutId: Int,
    val workoutPlanId:Int,
    val workoutDate: String,
    val workoutName: String
)

data class ExerciseWithName(
    val exerciseId: Int,
    val workoutId: Int,
    val plannedExerciseId: Int,
    val exerciseName: String,
    val weight: Float,
    val reps: Int,
    val pb: Boolean
)