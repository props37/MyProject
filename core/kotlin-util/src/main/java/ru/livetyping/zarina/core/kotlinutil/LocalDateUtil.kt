package ru.livetyping.zarina.core.kotlinutil

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

public object LocalDateUtil {
    public fun fromEpochMillis(millis: Long, zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
        return Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDate()
    }
}
