package ru.livetyping.zarina.util.kotlin.date

import java.time.LocalDate
import java.time.ZoneId

fun LocalDate.toEpochMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long = this
    .atStartOfDay()
    .atZone(zoneId)
    .toInstant()
    .toEpochMilli()
