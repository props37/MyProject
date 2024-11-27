package ru.livetyping.zarina.core.kotlinutil

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

public object LocalDateUtil {
    public fun fromMillis(millis: Long, zoneId: ZoneId = ZoneOffset.UTC): LocalDate {
        return Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDate()
    }
}
