package com.example.workoutapp.data

import android.content.Context
import androidx.room.Room

//  DatabaseProvider.kt builds instantiates the Workout database

object DatabaseProvider {
    @Volatile
    private var INSTANCE: WorkoutAppDatabase? = null

    fun getDatabase(context: Context): WorkoutAppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                WorkoutAppDatabase::class.java,
                "workout_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}