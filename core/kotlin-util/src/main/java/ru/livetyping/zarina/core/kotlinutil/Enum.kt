package ru.livetyping.zarina.core.kotlinutil

public inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? {
    return try {
        enumValueOf<T>(name)
    } catch (_: Exception) {
        null
    }
}
