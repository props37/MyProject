package ru.zarina.zarina.util.kotlin

fun CharSequence.findSubstringBounds(substring: String): IntRange? {
    val start = indexOf(substring)
    return if (start != -1) {
        val end = start + substring.length
        start..end
    } else {
        null
    }
}
