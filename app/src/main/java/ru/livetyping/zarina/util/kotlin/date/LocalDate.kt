package ru.livetyping.zarina.util.kotlin.date

import java.time.LocalDate
import java.time.ZoneId

fun LocalDate.toMillis(): Long = this
    .atStartOfDay()
    .atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()
