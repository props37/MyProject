package ru.livetyping.zarina.util.kotlin.date

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Locale

object LocalDateUtil {
    fun fromMillis(millis: Long, zoneId: ZoneId = ZoneOffset.UTC): LocalDate {
        return Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDate()
    }

    fun parseRussianDate(date: String): LocalDate {
        val parts = date.split(' ')
        val day = parts[0]
        val month = getRussianMonthNumber(parts[1])
        val year = parts[2]
        Locale("ru")
        return LocalDate.parse("$year-$month-$day")
    }

    private fun getRussianMonthNumber(month: String): String {
        val lowercase = month.lowercase()
        return when (lowercase) {
            "январь", "января" -> "01"
            "февраль", "февраля" -> "02"
            "март", "марта" -> "03"
            "апрель", "апреля" -> "04"
            "май", "мая" -> "05"
            "июнь", "июня" -> "06"
            "июль", "июля" -> "07"
            "август", "августа" -> "08"
            "сентябрь", "сентября" -> "09"
            "октябрь", "октября" -> "10"
            "ноябрь", "ноября" -> "11"
            "декабрь", "декабря" -> "12"
            else -> error("Unknown month $month")
        }
    }
}
