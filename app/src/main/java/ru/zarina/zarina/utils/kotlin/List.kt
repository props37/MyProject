package ru.zarina.zarina.utils.kotlin

fun <T> List<T>.loopingGet(index: Int) = this[index % this.size]
