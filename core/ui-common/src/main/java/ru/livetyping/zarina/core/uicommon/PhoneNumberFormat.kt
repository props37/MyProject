package ru.livetyping.zarina.core.uicommon

import android.content.Context
import android.telephony.PhoneNumberUtils
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import ru.livetyping.zarina.core.kotlinutil.LocaleUtil
import kotlin.text.Typography.nbsp

public fun formatPhoneNumber(
    phoneNumber: String,
    context: Context,
    defaultCountryIso: String = LocaleUtil.RU.country,
): String? {
    // Format
    val phoneNumberUtil = PhoneNumberUtilProvider.provide(context)
    val phoneNumberUtilNumber = phoneNumberUtil.parse(phoneNumber, defaultCountryIso)
    var formatted =
        phoneNumberUtil.format(phoneNumberUtilNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
            ?: return null

    // Add visual formatting
    formatted = PhoneNumberUtils.formatNumber(formatted, defaultCountryIso)
    formatted = formatted.replace(' ', nbsp)
    return formatted
}
