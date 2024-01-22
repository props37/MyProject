package ru.zarina.zarina.util.kotlin

fun ClosedFloatingPointRange<Float>.valueAt(progress: Float): Float {
    return (start + (endInclusive - start) * progress).coerceIn(this)
}
