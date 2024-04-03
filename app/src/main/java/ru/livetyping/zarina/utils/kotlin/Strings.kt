package ru.livetyping.zarina.utils.kotlin

import java.util.Locale

fun String.capitalize(): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
