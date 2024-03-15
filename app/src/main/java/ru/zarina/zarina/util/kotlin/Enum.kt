package ru.zarina.zarina.util.kotlin

inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? {
    return try {
        enumValueOf<T>(name)
    } catch (_: Exception) {
        null
    }
}
