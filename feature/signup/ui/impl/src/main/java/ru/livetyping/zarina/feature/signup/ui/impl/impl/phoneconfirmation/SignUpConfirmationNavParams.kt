package ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

internal data class SignUpConfirmationNavParams(val phone: PhoneNumber) {
    fun toNavEntry(): SignUpConfirmationNavEntry = SignUpConfirmationNavEntry(phone.value)
}
