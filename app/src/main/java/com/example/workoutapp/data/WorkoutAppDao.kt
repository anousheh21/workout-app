package com.example.workoutapp.data

import androidx.room.*
import org.jetbrains.annotations.Async.Schedule

@Dao
interface ScheduledWorkoutDao {
    @Insert
    suspend fun insert(scheduledWorkout: ScheduledWorkout)

    @Insert
    suspend fun insertAndReturnId(scheduledWorkout: ScheduledWorkout): Long

    @Query("SELECT * FROM scheduledWorkouts")
    suspend fun getAll(): List<ScheduledWorkout>

    @Query("SELECT * FROM scheduledWorkouts WHERE workoutPlanId = :id")
    suspend fun getById(id: Int): ScheduledWorkout

    @Query("DELETE FROM scheduledWorkouts")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(scheduledWorkout: ScheduledWorkout)
}

@Dao
interface ScheduledWorkoutExerciseDao {
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
interface WorkoutDao {
    @Insert
    suspend fun insert(workout: Workout)

    @Query("SELECT * FROM workouts")
    suspend fun getAll(): List<Workout>

    @Query("""
    SELECT workouts.workoutId, workouts.workoutPlanId, workouts.workoutDate, scheduledWorkouts.workoutName 
    FROM workouts
    INNER JOIN scheduledWorkouts ON workouts.workoutPlanId = scheduledWorkouts.workoutPlanId
""")
    suspend fun getWorkoutsWithDetails(): List<WorkoutDetails>

    @Query("SELECT * FROM workouts WHERE workoutId = :id")
    suspend fun getById(id: Int): Workout

    @Query("DELETE FROM workouts")
    suspend fun deleteAll()

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

