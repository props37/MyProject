package ru.livetyping.zarina.presentation.common.util

import android.content.Context
import android.telephony.PhoneNumberUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import ru.livetyping.zarina.presentation.common.phone.PhoneNumberUtilProvider
import ru.livetyping.zarina.util.kotlin.LocaleUtil
import kotlin.text.Typography.nbsp

@Composable
fun rememberFormattedPhoneNumber(
    phoneNumber: String,
    defaultCountryIso: String = LocaleUtil.RU.country,
    useNonBreakingSpaces: Boolean = true,
): String? {
    val context = LocalContext.current
    return remember(phoneNumber, context, defaultCountryIso, useNonBreakingSpaces) {
        formatPhoneNumber(phoneNumber, context, defaultCountryIso, useNonBreakingSpaces)
    }
}

fun formatPhoneNumber(
    phoneNumber: String,
    context: Context,
    defaultCountryIso: String = LocaleUtil.RU.country,
    useNonBreakingSpaces: Boolean = true,
): String? {
    // Format
    val phoneNumberUtil = PhoneNumberUtilProvider.provide(context)
    val phoneNumberUtilNumber = phoneNumberUtil.parse(phoneNumber, defaultCountryIso)
    var formatted =
        phoneNumberUtil.format(phoneNumberUtilNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
            ?: return null
    // Add visual formatting
    formatted = PhoneNumberUtils.formatNumber(formatted, defaultCountryIso)
    if (useNonBreakingSpaces) {
        formatted = formatted.replace(' ', nbsp)
    }
    return formatted
}
