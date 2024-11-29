package ru.livetyping.zarina.core.kotlinutil

import java.time.LocalDate
import java.time.ZoneId

public fun LocalDate.toEpochMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long = this
    .atStartOfDay()
    .atZone(zoneId)
    .toInstant()
    .toEpochMilli()
