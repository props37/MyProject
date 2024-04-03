package ru.livetyping.zarina.utils.kotlin

fun Int.roundToMultipleOf(other: Int): Int {
    if (other == 0) return 0
    val modulo = this % other
    return when {
        modulo == 0 -> this
        modulo > other / 2 -> this - modulo + other
        else -> this - modulo
    }
}
