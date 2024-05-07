package ru.livetyping.zarina.presentation.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ru.livetyping.zarina.util.kotlin.LocaleUtil
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun rememberFormattedLocalDate(
    localDate: LocalDate,
    formatterPattern: String,
    locale: Locale = LocaleUtil.RU,
): String = remember(localDate, formatterPattern, locale) {
    val formatter = DateTimeFormatter.ofPattern(formatterPattern, locale)
    localDate.format(formatter)
}
