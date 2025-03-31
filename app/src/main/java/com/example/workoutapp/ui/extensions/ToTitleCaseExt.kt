package com.example.workoutapp.ui.extensions

fun Enum<*>.toTitleCase(): String {
    return name
        .lowercase()
        .split('_')
        .joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }
}