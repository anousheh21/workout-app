package com.example.workoutapp.data

import android.database.Cursor
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.Async.Schedule

@Dao
interface ScheduledWorkoutDao {
    @Insert
    suspend fun insert(scheduledWorkout: ScheduledWorkout)

    @Insert
    suspend fun insertAndReturnId(scheduledWorkout: ScheduledWorkout): Long

    @Query("SELECT * FROM scheduledWorkouts")
    suspend fun getAll(): List<ScheduledWorkout>

    @Query("SELECT * FROM scheduledWorkouts")
    fun getAllScheduledWorkouts(): Flow<List<ScheduledWorkout>>

    @Query("SELECT * FROM scheduledWorkouts WHERE workoutPlanId = :id")
    suspend fun getById(id: Int): ScheduledWorkout

    @Query("DELETE FROM scheduledWorkouts")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(scheduledWorkout: ScheduledWorkout)

    // For the content provider
    @Query("SELECT * FROM scheduledWorkouts")
    fun getAllScheduledWorkoutsCursor(): Cursor

    @Query("SELECT * FROM scheduledWorkouts WHERE workoutPlanId = :id")
    fun getByIdCursor(id: Int): Cursor

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertForContProv(scheduledWorkout: ScheduledWorkout): Long

    @Delete
    fun deleteForContProv(scheduledWorkout: ScheduledWorkout): Int

}

@Dao
interface ScheduledWorkoutExerciseDao {
    @Insert
    suspend fun insert(exercise: ScheduledWorkoutExercise)

    @Insert
    suspend fun insertMultiple(exercises: List<ScheduledWorkoutExercise>)

    @Query("SELECT * FROM scheduledWorkoutExercises WHERE workoutPlanId = :planId")
    suspend fun getExercisesForPlan(planId: Int): List<ScheduledWorkoutExercise>

    @Query("""
        SELECT p.* FROM plannedExercise p
        INNER JOIN scheduledWorkoutExercises swe ON p.plannedExerciseId = swe.plannedExerciseId
        WHERE swe.workoutPlanId = :planId
    """)
    suspend fun getExercisesForWorkout(planId: Int): List<PlannedExercise>

    @Delete
    suspend fun delete(exercise: ScheduledWorkoutExercise)

    // For the ContentProvider
    @Query("SELECT * FROM scheduledWorkoutExercises")
    fun getAllCursor(): Cursor

    @Query("SELECT * FROM scheduledWorkoutExercises WHERE workoutPlanId = :planId")
    fun getCursorById(planId: Int): Cursor

    @Insert
    fun insertForContentProvider(scheduledWorkoutExercise: ScheduledWorkoutExercise): Long

    @Delete
    fun deleteForContentProvider(scheduledWorkoutExercise: ScheduledWorkoutExercise): Int

}

@Dao
interface PlannedExerciseDao {
    @Insert
    suspend fun insert(exercise: PlannedExercise): Long

    @Insert
    suspend fun insertMultiple(exercises: List<PlannedExercise>)

//    @Query("SELECT * FROM plannedExercise")
//    suspend fun getAll(): List<PlannedExercise>

    @Query("SELECT * FROM PlannedExercise")
    fun getAll(): Flow<List<PlannedExercise>>

    @Delete
    suspend fun delete(exercise: PlannedExercise)

    // For the ContentProvider
    @Query("SELECT * FROM plannedExercise")
    fun getAllPlannedExercises(): Cursor

    @Insert
    fun insertPlannedExercise(plannedExercise: PlannedExercise): Long

    @Delete
    fun deleteForContentProvider(plannedExercise: PlannedExercise): Int

    @Query("SELECT * FROM plannedExercise WHERE plannedExerciseId = :id")
    fun getByIdCursor(id: Int): Cursor
}

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(workout: Workout): Long

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

    @Query("DELETE FROM workouts WHERE workoutId = :workoutId")
    suspend fun deleteWorkoutById(workoutId: Int)

    @Delete
    suspend fun delete(workout: Workout)

    // For the Content Provider
    @Query("SELECT * FROM workouts WHERE workoutId = :id")
    fun getWorkoutByIdCursor(id: Int): Cursor

    @Query("SELECT * FROM workouts")
    fun getAllCursor(): Cursor

    @Insert
    fun insertForContProv(workout: Workout): Long

    @Delete
    fun deleteForContProv(workout: Workout): Int


}

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insert(exercise: Exercise)

    @Query("SELECT * FROM exercises")
    suspend fun getAll(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId")
    suspend fun getExercisesForWorkout(workoutId: Int): List<Exercise>

    // Debug query to log joined exercises
    @Query("""
    SELECT e.exerciseId, e.workoutId, e.plannedExerciseId, p.exerciseName, p.muscleGroup, e.weight, e.reps, e.pb
    FROM exercises e
    INNER JOIN plannedExercise p ON e.plannedExerciseId = p.plannedExerciseId
    WHERE e.workoutId = :workoutId
""")
    suspend fun getExercisesWithNamesForWorkout(workoutId: Int): List<ExerciseWithName>

    @Query("DELETE FROM exercises WHERE workoutId = :workoutId")
    suspend fun deleteExercisesForWorkout(workoutId: Int)

    @Delete
    suspend fun delete(exercise: Exercise)

    // For the Content Provider
    @Insert
    fun insertExerciseContProv(exercise: Exercise): Long

    @Delete
    fun deleteExerciseContProv(exercise: Exercise): Int

    @Query("SELECT * FROM exercises")
    fun getAllExercisesCursor(): Cursor

    @Query("SELECT * FROM exercises WHERE exerciseId = :exerciseId")
    fun getExerciseItemCursor(exerciseId: Int): Cursor
}
