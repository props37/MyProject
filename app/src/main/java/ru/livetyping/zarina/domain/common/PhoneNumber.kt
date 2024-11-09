package ru.livetyping.zarina.domain.common

import android.telephony.PhoneNumberUtils

@JvmInline
value class PhoneNumber private constructor(val value: String) {
    companion object {
        val ZARINA_SUPPORT: PhoneNumber
            get() = create("88007070666")

        fun create(phone: String): PhoneNumber {
            val normalizedPhone = PhoneNumberUtils.normalizeNumber(phone)
            return PhoneNumber(normalizedPhone)
        }
    }
}
