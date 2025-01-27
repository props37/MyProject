package ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.navigation.NavigationEntry

@Serializable
internal class SignUpConfirmationNavEntry private constructor(
    private val phone: String,
) : NavigationEntry {
    fun getPhone(): PhoneNumber = PhoneNumber.create(phone)

    companion object {
        fun create(phone: PhoneNumber): SignUpConfirmationNavEntry {
            return SignUpConfirmationNavEntry(phone.value)
        }
    }
}
