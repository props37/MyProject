package ru.livetyping.zarina.util.kotlin

fun CharSequence.findSubstringBounds(substring: String, ignoreCase: Boolean = false): IntRange? {
    val start = indexOf(substring, ignoreCase = ignoreCase)
    return if (start != -1) {
        val end = start + substring.length
        start..end
    } else {
        null
    }
}
