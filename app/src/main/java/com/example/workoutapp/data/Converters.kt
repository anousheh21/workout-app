package com.example.workoutapp.data

import androidx.room.TypeConverter

// This file contains type converters to turn enums to Strings and vice versa, to ensure their full compatibility

class Converters {

    // Type Converter to go from the MuscleGroup type created as an enum, to a String
    @TypeConverter
    fun fromMuscleGroup(value: MuscleGroup): String {
        return value.name
    }

    // Type Converter to turn a String into the MuscleGroup type (created as an enum)
    @TypeConverter
    fun toMuscleGroup(value: String): MuscleGroup {
        return MuscleGroup.valueOf(value)
    }
}