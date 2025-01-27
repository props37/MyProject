package ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.navigation.NavigationEntry

@Serializable
internal class PhoneConfirmationNavEntry private constructor(
    private val phone: String,
) : NavigationEntry {
    fun getPhone(): PhoneNumber = PhoneNumber.create(phone)

    companion object {
        fun create(phone: PhoneNumber): PhoneConfirmationNavEntry {
            return PhoneConfirmationNavEntry(phone.value)
        }
    }
}
