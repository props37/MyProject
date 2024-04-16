package ru.livetyping.zarina.data.common.remote.api.util

import java.time.LocalDate

fun parseRussianDate(date: String): LocalDate {
    val parts = date.split(' ')
    val day = parts[0]
    val month = getRussianMonthNumber(parts[1])
    val year = parts[2]
    return LocalDate.parse("$year-$month-$day")
}

fun getRussianMonthNumber(month: String): Int {
    val lowercase = month.lowercase()
    return when (lowercase) {
        "январь", "января" -> 1
        "февраль", "февраля" -> 2
        "март", "марта" -> 3
        "апрель", "апреля" -> 4
        "май", "мая" -> 5
        "июнь", "июня" -> 6
        "июль", "июля" -> 7
        "август", "августа" -> 8
        "сентябрь", "сентября" -> 9
        "октябрь", "октября" -> 10
        "ноябрь", "ноября" -> 11
        "декабрь", "декабря" -> 12
        else -> error("Unknown month $month")
    }
}
