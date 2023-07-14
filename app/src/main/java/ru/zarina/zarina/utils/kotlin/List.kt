package ru.zarina.zarina.utils.kotlin

fun <T> List<T>.loopingGet(index: Int): T? {
    return if (isEmpty())
        null
    else
        this.getOrNull(index % this.size)
}
