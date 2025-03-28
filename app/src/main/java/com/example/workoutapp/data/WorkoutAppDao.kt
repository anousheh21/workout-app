package com.example.workoutapp.data

import androidx.room.*

@Dao
interface ScheduledWorkoutDao {
    @Insert
    suspend fun insert(scheduledWorkout: ScheduledWorkout)

    @Query("SELECT * FROM scheduledWorkouts")
    suspend fun getAll(): List<ScheduledWorkout>

    @Delete
    suspend fun delete(scheduledWorkout: ScheduledWorkout)
}

@Dao
interface ScheduleWorkoutExerciseDao {
    @Insert
    suspend fun insert(exercise: ScheduledWorkoutExercise)

    @Insert
    suspend fun insertMultiple(exercises: List<ScheduledWorkoutExercise>)

    @Query("SELECT * FROM scheduledWorkoutExercises WHERE workoutPlanId = :planId")
    suspend fun getExercisesForPlan(planId: Int): List<ScheduledWorkoutExercise>

    @Delete
    suspend fun delete(exercise: ScheduledWorkoutExercise)
}

@Dao
interface PlannedExerciseDao {
    @Insert
    suspend fun insert(exercise: PlannedExercise)

    @Insert
    suspend fun insertMultiple(exercises: List<PlannedExercise>)

    @Query("SELECT * FROM plannedExercise")
    suspend fun getAll(): List<PlannedExercise>

    @Delete
    suspend fun delete(exercise: PlannedExercise)
}

@Dao
interface WorkoutsDao {
    @Insert
    suspend fun insert(workout: Workout)

    @Query("SELECT * FROM workouts")
    suspend fun getAll(): List<Workout>

    @Delete
    suspend fun delete(workout: Workout)
}

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insert(exercise: Exercise)

    @Query("SELECT * FROM exercises")
    suspend fun getAll(): List<Exercise>

    @Delete
    suspend fun delete(exercise: Exercise)
}

