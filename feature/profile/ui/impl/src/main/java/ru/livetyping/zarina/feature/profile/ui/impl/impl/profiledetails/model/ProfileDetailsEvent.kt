package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model

internal sealed interface ProfileDetailsEvent {
    data class BirthDateEpochMillisChanged(val millis: Long?) : ProfileDetailsEvent

    data object PhoneClicked : ProfileDetailsEvent

    data object EmailClicked : ProfileDetailsEvent

    data object ChangePasswordClicked : ProfileDetailsEvent

    data class ReceiveEmailsChanged(val receiveEmails: Boolean) : ProfileDetailsEvent

    data class ReceiveSmsChanged(val receiveSms: Boolean) : ProfileDetailsEvent

    data object SignOutClicked : ProfileDetailsEvent

    data object DeleteAccountClicked : ProfileDetailsEvent

    data object ErrorRefreshClicked : ProfileDetailsEvent
}
