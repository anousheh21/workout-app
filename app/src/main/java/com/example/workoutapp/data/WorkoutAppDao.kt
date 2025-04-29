package com.example.workoutapp.data

// WorkoutAppDao.kt is the DAO to be used for the Workout Database. For each of the 'tables' in the room database, the queries used by the ViewModel are below

import android.database.Cursor
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.Async.Schedule

// DAO for Scheduled Workouts
@Dao
interface ScheduledWorkoutDao {

    // Insert a scheduled workout into the database
    @Insert
    suspend fun insert(scheduledWorkout: ScheduledWorkout)


    // Insert a scheduled workout into the database and return the ID of that workout
    @Insert
    suspend fun insertAndReturnId(scheduledWorkout: ScheduledWorkout): Long

    // Get a list of all scheduled workouts in the database
    @Query("SELECT * FROM scheduledWorkouts")
    suspend fun getAll(): List<ScheduledWorkout>


    // Get a list of all scheduled workouts in the database as a Flow type
    @Query("SELECT * FROM scheduledWorkouts")
    fun getAllScheduledWorkouts(): Flow<List<ScheduledWorkout>>


    // Counts the number of scheduled workouts with a particular workout plan ID
    @Query("SELECT COUNT(*) FROM scheduledWorkouts WHERE workoutPlanId = :workoutPlanId")
    suspend fun isScheduledWorkoutUsed(workoutPlanId: Int): Int

    // Gets a particular scheduled workout by its ID
    @Query("SELECT * FROM scheduledWorkouts WHERE workoutPlanId = :id")
    suspend fun getById(id: Int): ScheduledWorkout

    // Deletes all scheduled workouts from the database
    @Query("DELETE FROM scheduledWorkouts")
    suspend fun deleteAll()

    // Deletes a particular scheduled workout from the database
    @Delete
    suspend fun deleteSched(scheduledWorkout: ScheduledWorkout)

    // Deletes a particular scheduled workotu from the database, by its ID
    @Query("DELETE FROM scheduledWorkouts WHERE workoutPlanId = :scheduledWorkoutId")
    suspend fun delete(scheduledWorkoutId: Int)

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

// DOE for Scheduled Workout Exercises
@Dao
interface ScheduledWorkoutExerciseDao {

    // Inserts a scheduled workout exercise into the database
    @Insert
    suspend fun insert(exercise: ScheduledWorkoutExercise)

    // Inserts a list of scheduled workout exercises into the database
    @Insert
    suspend fun insertMultiple(exercises: List<ScheduledWorkoutExercise>)

    // Gets a list of exercises with a particular workout plan ID
    @Query("SELECT * FROM scheduledWorkoutExercises WHERE workoutPlanId = :planId")
    suspend fun getExercisesForPlan(planId: Int): List<ScheduledWorkoutExercise>

    // Deletes all exercises for a particular scheduled workout
    @Query("DELETE FROM scheduledWorkoutExercises WHERE workoutPlanId = :workoutPlanId")
    suspend fun deleteExercisesForScheduledWorkout(workoutPlanId: Int)

    // Counts the number of scheduled workout exercises for a particular workout plan
    @Query("SELECT COUNT(*) FROM scheduledWorkoutExercises WHERE plannedExerciseId = :exerciseId")
    suspend fun isPlannedExerciseUsed(exerciseId: Int): Int

    // Gets a list of planned exercises for a workout with a specific plan ID
    @Query("""
        SELECT p.* FROM plannedExercise p
        INNER JOIN scheduledWorkoutExercises swe ON p.plannedExerciseId = swe.plannedExerciseId
        WHERE swe.workoutPlanId = :planId
    """)
    suspend fun getExercisesForWorkout(planId: Int): List<PlannedExercise>

    // Deletes a particular scheduled workout exercise from the database
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

// DAO for Planned Exercises
@Dao
interface PlannedExerciseDao {

    // Inserts a planned exercise into the database and returns its ID
    @Insert
    suspend fun insert(exercise: PlannedExercise): Long

    // Insert a list of planned exercises into the database
    @Insert
    suspend fun insertMultiple(exercises: List<PlannedExercise>)

//    @Query("SELECT * FROM plannedExercise")
//    suspend fun getAll(): List<PlannedExercise>

    // Gets a list of all planned exercises as a Flow
    @Query("SELECT * FROM PlannedExercise")
    fun getAll(): Flow<List<PlannedExercise>>

    // Deletes a particular planned exercise from the database
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

// DAO for Workouts
@Dao
interface WorkoutDao {

    // Insets a workout into the database and returns the ID of that workout
    @Insert
    suspend fun insert(workout: Workout): Long

    // Gets a list of all workouts
    @Query("SELECT * FROM workouts")
    suspend fun getAll(): List<Workout>

    // Counts the number of workouts with a specific workout plan ID
    @Query("SELECT COUNT(*) FROM workouts WHERE workoutPlanId = :workoutPlanId")
    suspend fun countWorkoutsByPlanId(workoutPlanId: Int): Int

    // Gets a list of workouts with the details of that workout, including some from the scheduled workout related to the workout
    @Query("""
    SELECT workouts.workoutId, workouts.workoutPlanId, workouts.workoutDate, scheduledWorkouts.workoutName 
    FROM workouts
    INNER JOIN scheduledWorkouts ON workouts.workoutPlanId = scheduledWorkouts.workoutPlanId
""")
    suspend fun getWorkoutsWithDetails(): List<WorkoutDetails>

    // Gets the details of a particular workout
    @Query("""
    SELECT workouts.workoutId, workouts.workoutPlanId, workouts.workoutDate, scheduledWorkouts.workoutName 
    FROM workouts
    INNER JOIN scheduledWorkouts ON workouts.workoutPlanId = scheduledWorkouts.workoutPlanId
    WHERE workouts.workoutId = :id
""")
    suspend fun getWorkoutDetailsById(id: Int): WorkoutDetails

    // Select a workout by its ID
    @Query("SELECT * FROM workouts WHERE workoutId = :id")
    suspend fun getById(id: Int): Workout

    // Deletes all workouts in the database
    @Query("DELETE FROM workouts")
    suspend fun deleteAll()

    // Deletes a particular workout from the database
    @Query("DELETE FROM workouts WHERE workoutId = :workoutId")
    suspend fun deleteWorkoutById(workoutId: Int)

    // Deletes a particular workout from the database
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

// DAO for Exercises
@Dao
interface ExerciseDao {

    // Insert an exercise into the database
    @Insert
    suspend fun insert(exercise: Exercise)

    // Gets a list of all exercises in the database
    @Query("SELECT * FROM exercises")
    suspend fun getAll(): List<Exercise>

    // Gets a list of all exercises for a specific workout
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

    // Deletes al exercises for a particular workout
    @Query("DELETE FROM exercises WHERE workoutId = :workoutId")
    suspend fun deleteExercisesForWorkout(workoutId: Int)

    // Deletes a particular exercise
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
