package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.model

internal sealed interface PhoneChangingEvent {
    data object BackClicked : PhoneChangingEvent

    data object RequestPhoneChangeClicked : PhoneChangingEvent
}
