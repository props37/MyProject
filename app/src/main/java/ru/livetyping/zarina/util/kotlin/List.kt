package ru.livetyping.zarina.util.kotlin

fun <T> List<T>.loopingGet(index: Int): T? {
    return if (this.isEmpty()) {
        null
    } else {
        this.getOrNull(index % this.size)
    }
}
