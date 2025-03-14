package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.navigation.NavigationEntry

@Serializable
internal class SignInByPhoneConfirmationNavEntry private constructor(
    private val phone: String,
) : NavigationEntry {
    fun getPhone(): PhoneNumber = PhoneNumber.create(phone)

    companion object {
        fun create(phone: PhoneNumber): SignInByPhoneConfirmationNavEntry {
            return SignInByPhoneConfirmationNavEntry(phone.value)
        }
    }
}
