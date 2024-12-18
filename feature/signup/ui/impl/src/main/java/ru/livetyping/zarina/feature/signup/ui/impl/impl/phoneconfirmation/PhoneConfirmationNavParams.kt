package ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

internal data class PhoneConfirmationNavParams(val phone: PhoneNumber) {
    fun toNavEntry(): PhoneConfirmationNavEntry = PhoneConfirmationNavEntry(phone.value)
}
