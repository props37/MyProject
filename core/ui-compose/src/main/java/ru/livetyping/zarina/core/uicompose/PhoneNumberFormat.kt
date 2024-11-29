package ru.livetyping.zarina.core.uicompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ru.livetyping.zarina.core.kotlinutil.LocaleUtil
import ru.livetyping.zarina.core.uicommon.formatPhoneNumber

@Composable
public fun rememberFormattedPhoneNumber(
    phoneNumber: String,
    defaultCountryIso: String = LocaleUtil.RU.country,
): String? {
    val context = LocalContext.current
    return remember(phoneNumber, context, defaultCountryIso) {
        formatPhoneNumber(phoneNumber, context, defaultCountryIso)
    }
}
