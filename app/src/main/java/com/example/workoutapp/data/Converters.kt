package com.example.workoutapp.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromMuscleGroup(value: MuscleGroup): String {
        return value.name
    }

    @TypeConverter
    fun toMuscleGroup(value: String): MuscleGroup {
        return MuscleGroup.valueOf(value)
    }
}