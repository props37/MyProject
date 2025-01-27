package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.navigation.NavigationEntry

@Serializable
internal class PhoneChangeConfirmationNavEntry private constructor(
    private val phone: String,
) : NavigationEntry {
    fun getPhone(): PhoneNumber = PhoneNumber.create(phone)

    companion object {
        fun create(phone: PhoneNumber): PhoneChangeConfirmationNavEntry {
            return PhoneChangeConfirmationNavEntry(phone.value)
        }
    }
}
