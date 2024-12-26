package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

internal data class PhoneChangeConfirmationNavParams(val phone: PhoneNumber) {
    fun toNavEntry(): PhoneChangeConfirmationNavEntry {
        return PhoneChangeConfirmationNavEntry(phone.value)
    }
}
