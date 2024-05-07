package ru.livetyping.zarina.presentation.common.util

import android.telephony.PhoneNumberUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ru.livetyping.zarina.util.platform.locale
import java.util.Locale
import kotlin.text.Typography.nbsp

@Composable
fun rememberFormattedPhoneNumber(
    phoneNumber: String,
    useNonBreakingSpaces: Boolean = true,
): String? {
    val context = LocalContext.current
    return remember(phoneNumber, useNonBreakingSpaces, context) {
        formatPhoneNumber(phoneNumber, useNonBreakingSpaces, context.locale.country)
    }
}

fun formatPhoneNumber(
    phoneNumber: String,
    useNonBreakingSpaces: Boolean = false,
    defaultCountryIso: String = Locale.getDefault().country,
): String? {
    var formatted = PhoneNumberUtils.formatNumber(phoneNumber, defaultCountryIso)
    if (useNonBreakingSpaces) {
        formatted = formatted.replace(' ', nbsp)
    }
    return formatted
}
