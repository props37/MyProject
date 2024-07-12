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

fun CharSequence.removePrefix(predicate: (Char) -> Boolean): CharSequence {
    if (this.isEmpty() || !predicate(this.first())) return this

    var prefixEndIndex = 1
    for (index in prefixEndIndex until this.length) {
        if (!predicate(this[index])) break
        prefixEndIndex = index
    }
    return StringBuilder(this).removeRange(0, prefixEndIndex)
}
