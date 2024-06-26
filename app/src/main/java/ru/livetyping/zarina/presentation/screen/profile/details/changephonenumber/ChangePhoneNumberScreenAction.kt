package ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber

import ru.livetyping.zarina.domain.common.PhoneNumber

sealed class ChangePhoneNumberScreenAction {
    data object ScreenClosed : ChangePhoneNumberScreenAction()

    data class PhoneChangeRequested(val phone: PhoneNumber) : ChangePhoneNumberScreenAction()
}
