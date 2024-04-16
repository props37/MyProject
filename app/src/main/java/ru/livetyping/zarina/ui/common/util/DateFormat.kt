package ru.livetyping.zarina.ui.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ru.livetyping.zarina.util.platform.locale
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun rememberFormattedLocalDate(
    localDate: LocalDate,
    formatterPattern: String,
    locale: Locale = LocalContext.current.locale,
): String = remember(localDate, formatterPattern, locale) {
    val formatter = DateTimeFormatter.ofPattern(formatterPattern, locale)
    localDate.format(formatter)
}
