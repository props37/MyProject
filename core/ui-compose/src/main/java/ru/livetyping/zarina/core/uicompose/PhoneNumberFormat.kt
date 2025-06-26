package ru.livetyping.zarina.core.uicompose

import android.telephony.PhoneNumberUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ru.livetyping.zarina.core.kotlinutil.LocaleUtil
import ru.livetyping.zarina.core.uicommon.formatPhoneNumber
import kotlin.text.Typography.nbsp

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

@Composable
public fun rememberSimpleFormattedPhoneNumber(
    phoneNumber: String,
    defaultCountryIso: String = LocaleUtil.RU.country,
): String {
    val context = LocalContext.current
    return remember(phoneNumber, context, defaultCountryIso) {
        var formatted = PhoneNumberUtils.formatNumber(phoneNumber, defaultCountryIso)
        formatted = formatted.replace(' ', nbsp)
        formatted
    }
}
