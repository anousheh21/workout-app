package com.example.workoutapp.ui.extensions

// ToTitleCase.kt converts enums to title case, making them easier to display in a user friendly and more readable way
fun Enum<*>.toTitleCase(): String {
    return name
        .lowercase()
        .split('_')
        .joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }
}