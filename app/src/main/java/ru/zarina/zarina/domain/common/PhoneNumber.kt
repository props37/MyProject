package ru.zarina.zarina.domain.common

import android.telephony.PhoneNumberUtils

@JvmInline
value class PhoneNumber private constructor(val value: String) {
    companion object {
        fun create(phone: String): PhoneNumber {
            val normalizedPhone = PhoneNumberUtils.normalizeNumber(phone)
            return PhoneNumber(normalizedPhone)
        }
    }
}
