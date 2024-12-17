package ru.livetyping.zarina.feature.signup.ui.impl.impl.otp

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

internal data class OtpNavParams(val phone: PhoneNumber) {
    fun toNavEntry(): OtpNavEntry = OtpNavEntry(phone.value)
}
